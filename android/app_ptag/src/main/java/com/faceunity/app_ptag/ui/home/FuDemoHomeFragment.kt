package com.faceunity.app_ptag.ui.home

import android.content.ClipData
import android.content.ClipboardManager
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.faceunity.app_ptag.FuDevInitializeWrapper
import com.faceunity.app_ptag.R
import com.faceunity.app_ptag.databinding.FuDemoHomeFragmentBinding
import com.faceunity.app_ptag.ui.drive.entity.BodyTrackMode
import com.faceunity.app_ptag.ui.home.widget.FuDemoCopySuccessDialog
import com.faceunity.app_ptag.ui.home.widget.FuDemoShareDialog
import com.faceunity.app_ptag.util.ToastUtils
import com.faceunity.app_ptag.weight.DownloadingDialog
import com.faceunity.app_ptag.weight.FuDemoRetryDialog
import com.faceunity.app_ptag.weight.avatar_manager.AvatarManagerDialog
import com.faceunity.app_ptag.weight.avatar_manager.entity.FuAvatarContainer
import com.faceunity.app_ptag.weight.avatar_manager.entity.FuAvatarWrapper
import com.faceunity.app_ptag.weight.avatar_manager.parser.FuAvatarContainerParser
import com.faceunity.core.camera.entity.FUCameraConfig
import com.faceunity.core.entity.FURenderInputData
import com.faceunity.core.entity.FURenderOutputData
import com.faceunity.core.media.photo.FUPhotoRecordHelper
import com.faceunity.core.renderer.entity.FUDrawFrameMatrix
import com.faceunity.core.renderer.impl.FUCustomRenderer
import com.faceunity.core.renderer.infe.OnGLRendererListener
import com.faceunity.editor_ptag.data.DownloadStatus
import com.faceunity.editor_ptag.data_center.FuDevDataCenter
import com.faceunity.editor_ptag.store.DevAvatarManagerRepository
import com.faceunity.editor_ptag.util.visible
import com.faceunity.app_ptag.view_model.FuAvatarManagerViewModel
import com.faceunity.app_ptag.view_model.FuPreviewViewModel
import com.faceunity.app_ptag.view_model.entity.FuRequestErrorInfo
import com.faceunity.app_ptag.view_model.entity.FuUploadAvatarState
import com.faceunity.core.avatar.model.Avatar
import com.faceunity.core.entity.FUColorRGBData
import com.faceunity.editor_ptag.store.DevDrawRepository
import com.faceunity.editor_ptag.util.FuLog
import com.faceunity.editor_ptag.util.gone
import com.faceunity.fupta.facepup.FacePupManager
import com.faceunity.fupta.facepup.entity.FacePupParam
import com.faceunity.fupta.facepup.weight.FacePupControlListener
import com.faceunity.pta.pta_core.widget.TouchView
import com.faceunity.toolbox.utils.FUGLUtils
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.absoluteValue


/**
 * ??????
 */
class FuDemoHomeFragment : Fragment() {

    private var _binding: FuDemoHomeFragmentBinding? = null
    private val binding get() = _binding!!

    private val fuAvatarManagerViewModel by viewModels<FuAvatarManagerViewModel>()
    private val fuPreviewViewModel by viewModels<FuPreviewViewModel>()

    private val avatarWrapper = FuAvatarContainer(mutableListOf(), mutableListOf())
    private lateinit var avatarManagerDialog: AvatarManagerDialog
    private val downloadingDialog: DownloadingDialog by lazy {
        DownloadingDialog(requireContext())
    }

    private val ptaRenderer = FUCustomRenderer()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FuDemoHomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        initRenderer()
        initAvatarManagerDialog()
        inject()
        injectDownload()
        setAvatarLoadListener()

        fuAvatarManagerViewModel.autoInitDefaultAvatar(false)
    }

    private fun initView() {
        binding.glTextureView.apply {
            isOpaque = false
        }
        binding.touchView.apply {
            setTouchListener(object : TouchView.OnTouchListener {
                private var lastDeltaX = 0f

                override fun onScale(scale: Float) {
//                    fuPreviewViewModel.scaleAvatar(scale)
                }

                override fun onMove(deltaX: Float, deltaY: Float) {
                    lastDeltaX = deltaX
                    fuPreviewViewModel.rotateAvatar(deltaX)
                    fuPreviewViewModel.cancelRollAvatar()
//                    fuPreviewViewModel.moveVerticalAvatar(-deltaY) //Android ??? OpenGL ?????????????????????????????????
                }

                override fun onClick() {
                }

                override fun onUp() {
                    if (lastDeltaX.absoluteValue > 0.001) { //????????????????????????????????????????????????,?????????????????????
                        fuPreviewViewModel.rollAvatar(lastDeltaX)
                    }
                }
            })
        }
        binding.managerAvatarBtn.setOnClickListener {
            avatarManagerDialog.show()
        }
        binding.managerAvatarBtn.gone()
        binding.editAvatarBtn.setOnClickListener {
            findNavController().navigate(R.id.editFragment)
        }
        binding.editAvatarBtn.gone()
        binding.buildAvatarBtn.setOnClickListener {
            findNavController().navigate(R.id.buildFragment)
        }
        binding.buildAvatarBtn.gone()
        binding.driveBtn.setOnClickListener {
            findNavController().navigate(R.id.driveFragment)
        }
        binding.savePhotoBtn.setOnClickListener {
            saveSnapShotToFile()
        }
        binding.savePhotoBtn.gone()

    }

    private fun initRenderer() {
        ptaRenderer.apply {
            bindCameraConfig(FUCameraConfig())
            setDefaultRenderType(FUCustomRenderer.RendererTypeEnum.EMPTY_TEXTURE)
            bindGLTextureView(binding.glTextureView)
            bindListener(object : OnGLRendererListener {
                override fun onSurfaceCreated() {
                    FuDevInitializeWrapper.initRenderKit()
                }

                override fun onSurfaceChanged(width: Int, height: Int) {
                    ptaRenderer.setEmptyTextureConfig(width, height) //?????????????????????????????????
                }

                override fun onRenderBefore(inputData: FURenderInputData) {

                }

                override fun onRenderAfter(
                    outputData: FURenderOutputData,
                    drawMatrix: FUDrawFrameMatrix
                ) {
                    if (isNeedTakePic) { //?????????????????????????????????
                        val texture = outputData.texture
                        if (texture != null) {
                            val recordData = FUPhotoRecordHelper.RecordData(texture.texId, drawMatrix.texMatrix, FUGLUtils.IDENTITY_MATRIX, texture.width, texture.height)
                            recordData.isAlpha = true
                            photoRecordHelper.sendRecordingData(recordData)
                        } else {
                            onSaveSnapShotError("outputData texture is null")
                        }
                        isNeedTakePic = false
                    }
                    if (isAvatarExecuteCompleted) {
                        onAvatarShow()
                        isAvatarExecuteCompleted = false
                    }
                }

                override fun onDrawFrameAfter() {

                }

                override fun onSurfaceDestroy() {
                    FuDevInitializeWrapper.releaseRenderKit()
                }
            })
        }
    }


    private fun initAvatarManagerDialog() {
        avatarManagerDialog = AvatarManagerDialog(
            requireContext(),
            onItemClick = { item: FuAvatarWrapper ->
                fuAvatarManagerViewModel.autoSwitchAvatar(item.id) //???????????????????????????????????????????????????????????????????????????
            },
            onItemDelete = { item: FuAvatarWrapper ->
                fuAvatarManagerViewModel.autoRemoveAvatar(item.id) //????????????
            }
        )
        avatarManagerDialog.create()

        fuAvatarManagerViewModel.avatarCollectionLiveData.observe(viewLifecycleOwner) {
            val fuAvatarContainerParser = FuAvatarContainerParser()
            val wrapperList = DevAvatarManagerRepository.mapAvatar {
                fuAvatarContainerParser.parserAvatarInfoToFuAvatarWrapper(it)
            }
            avatarWrapper.avatarList.apply {
                clear()
                addAll(wrapperList)
            }
            avatarManagerDialog.syncAvatarContainer(avatarWrapper) //?????????????????????????????? UI
        }
        fuAvatarManagerViewModel.avatarSelectLiveData.observe(viewLifecycleOwner) {
            avatarWrapper.selectId.apply {
                clear()
                it?.let { add(it) }
            }
            avatarManagerDialog.syncAvatarContainer(avatarWrapper) //?????????????????????????????? UI
        }
    }

    private fun inject() {
        fuAvatarManagerViewModel.cloudAvatarPrepareLiveData.observe(viewLifecycleOwner) { //??????????????????????????????????????????????????????
            fuAvatarManagerViewModel.switchAvatarById(it)
        }
        fuAvatarManagerViewModel.uploadAvatarStateLiveData.observe(viewLifecycleOwner) {
            when(it) {
                is FuUploadAvatarState.Success -> {
//                    ToastUtils.showSuccessToast(requireContext(), it.avatarId)
                    FuDemoCopySuccessDialog(requireContext(), it.avatarId).show()
                    fun copy(text: String) {
                        val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val textCd = ClipData.newPlainText("text", text)
                        clipboard.setPrimaryClip(textCd)
                    }
                    copy(it.avatarId)
//                    fuAvatarManagerViewModel.downloadAvatar(it.avatarId)

                }
                is FuUploadAvatarState.Error -> {
                    ToastUtils.showFailureToast(requireContext(), it.errorInfo)
                }
            }
        }
    }

    private fun injectDownload() {
        fuAvatarManagerViewModel.requestErrorInfoLiveData.observe(viewLifecycleOwner) { //??????????????????
            val type = it.type
            when(type) {
                is FuRequestErrorInfo.Type.Retry -> {
                    FuDemoRetryDialog(requireContext(), type.tip).apply {
                        callback = object : FuDemoRetryDialog.Callback {
                            override fun onCancel() {
                                dismiss()
                            }

                            override fun onFinish() { //????????????????????????????????????????????????
                                fuAvatarManagerViewModel.autoInitDefaultAvatar(false)
                                dismiss()
                            }
                        }
                        create()
                        show()
                    }
                }
                is FuRequestErrorInfo.Type.Error -> {
                    ToastUtils.showFailureToast(requireContext(), "???????????????${type.tip}???/n???????????????")
                }
            }
        }
        fuAvatarManagerViewModel.downloadStatusLiveData.observe(viewLifecycleOwner) { //??????????????????
            val status = it.downloadStatus
            when(status) {
                is DownloadStatus.Start -> {
                    downloadingDialog.show()
                }
                is DownloadStatus.Progress -> {
                    downloadingDialog.updateText("?????? ${status.downloadCount}/${status.totalCount}")
                }
                is DownloadStatus.Error -> {
                    FuDemoRetryDialog(requireContext(), "???????????????${status.errorMsg}").apply {
                        callback = object : FuDemoRetryDialog.Callback {
                            override fun onCancel() {
                                dismiss()
                            }

                            override fun onFinish() { //????????????????????????????????????????????????
                                fuAvatarManagerViewModel.autoSwitchAvatar(it.avatarId)
                                dismiss()
                            }
                        }
                        create()
                        show()
                    }
                    downloadingDialog.dismiss()
                }
                is DownloadStatus.Finish -> {
                    downloadingDialog.dismiss()
                }
            }
        }
    }

    private var isAvatarExecuteCompleted = false

    private fun setAvatarLoadListener() {
        DevDrawRepository.setAvatarEvent(object : DevDrawRepository.AvatarEvent {
            override fun onAvatarLoaded(avatar: Avatar) {
                isAvatarExecuteCompleted = true
            }
        })
    }

    private fun onAvatarShow() {
        FuLog.info("Avatar show on screen.")
    }

    //region ??????

    private val photoRecordHelper by lazy {
        val helper = FUPhotoRecordHelper()
        helper.bindListener(object : FUPhotoRecordHelper.OnPhotoRecordingListener {
            override fun onRecordSuccess(bitmap: Bitmap?, tag: String) {
                if (bitmap != null) {
                    onSaveSnapShotSuccess(bitmap)
                } else {
                    onSaveSnapShotError("record bitmap is null")
                }
            }
        })
        helper
    }

    @Volatile
    private var isNeedTakePic = false

    /**
     * ????????????????????????????????? [onSaveSnapShotSuccess]????????????????????? [onSaveSnapShotError]
     */
    private fun saveSnapShotToFile() {
        isNeedTakePic = true
    }


    private fun onSaveSnapShotSuccess(bitmap: Bitmap) {



        requireActivity().runOnUiThread {
            FuDemoShareDialog(requireContext(), bitmap, object : FuDemoShareDialog.OnClickListener {
                override fun onSaveClick() {
                    saveBitmapToFile(bitmap)
                }

                override fun onCopyClick() {
                    fuAvatarManagerViewModel.uploadCurrentAvatar()
                }
            }).show()
        }
    }

    private fun onSaveSnapShotError(errorMsg: String) {
        ToastUtils.showFailureToast(requireContext(), errorMsg)
    }

    /**
     * ????????????????????????????????? "Pictures/PTAG/" ?????????
     */
    private fun saveBitmapToFile(bitmap: Bitmap) {
        val fileName = "ptag-${
            SimpleDateFormat("yyyyMMdd-HHmmss", Locale.getDefault()).format(
                GregorianCalendar().time
            )
        }.png"

        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DESCRIPTION, "This is a Avatar")
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.TITLE, fileName)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/PTAG/")
            }
        }
        val uri = requireContext().contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            values
        )
        val openOutputStream = requireContext().contentResolver.openOutputStream(uri!!)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, openOutputStream)
        openOutputStream?.close()
        requireActivity().runOnUiThread {
            ToastUtils.showSuccessToast(requireContext(), "???????????????????????????")
        }
    }

    //endregion ??????

    override fun onResume() {
        super.onResume()
        ptaRenderer.resumeRender()
    }

    override fun onPause() {
        super.onPause()
        ptaRenderer.pauseRender()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        ptaRenderer.release()
        FuDevInitializeWrapper.releaseRenderKit()
    }
}