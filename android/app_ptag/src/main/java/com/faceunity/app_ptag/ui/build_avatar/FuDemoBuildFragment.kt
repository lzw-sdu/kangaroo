package com.faceunity.app_ptag.ui.build_avatar

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.faceunity.app_ptag.FuDevInitializeWrapper
import com.faceunity.app_ptag.R
import com.faceunity.app_ptag.databinding.FuDemoBuildFragmentBinding
import com.faceunity.app_ptag.ui.build_avatar.renderer.FuDemoCameraRenderer
import com.faceunity.app_ptag.ui.build_avatar.widget.FuDemoBuildFinishDialog
import com.faceunity.app_ptag.ui.build_avatar.widget.FuDemoPTALoadingDialog
import com.faceunity.app_ptag.util.ToastUtils
import com.faceunity.core.camera.entity.FUCameraConfig
import com.faceunity.core.camera.enumeration.FUCameraTypeEnum
import com.faceunity.core.entity.FURenderInputData
import com.faceunity.core.entity.FURenderOutputData
import com.faceunity.core.media.photo.FUPhotoRecordHelper
import com.faceunity.core.renderer.entity.FUDrawFrameMatrix
import com.faceunity.core.renderer.infe.OnGLRendererListener
import com.faceunity.editor_ptag.util.gone
import com.faceunity.editor_ptag.util.visible
import com.faceunity.app_ptag.view_model.FuBuildAvatarViewModel
import com.faceunity.app_ptag.view_model.FuDriveViewModel
import com.faceunity.toolbox.file.FUFileUtils
import com.faceunity.toolbox.media.FUMediaUtils
import com.faceunity.toolbox.utils.FUGLUtils
import java.io.File

class FuDemoBuildFragment : Fragment() {

    companion object {
        fun newInstance() = FuDemoBuildFragment()
        const val IMAGE_REQUEST_CODE = 0x102
    }

    private lateinit var binding: FuDemoBuildFragmentBinding

    private lateinit var viewModel: FuDemoBuildViewModel
    private lateinit var fuBuildAvatarViewModel: FuBuildAvatarViewModel
    private val fuDriveViewModel by viewModels<FuDriveViewModel>()

    private val ptaRenderer by lazy { FuDemoCameraRenderer(requireContext()) }

    private val loadingDialog by lazy {
        FuDemoPTALoadingDialog(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FuDemoBuildFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FuDemoBuildViewModel::class.java)
        fuBuildAvatarViewModel = ViewModelProvider(this).get(FuBuildAvatarViewModel::class.java)

        initView()
        initSelectGenderLayout()
        initRenderer()
        inject()

        fuBuildAvatarViewModel.initPTAResource(FuDevInitializeWrapper.getAuthPack())
        fuDriveViewModel.openFaceTrack()
    }

    private fun initView() {
        binding.takePhotoBack.setOnClickListener {
            Navigation.findNavController(it).popBackStack() //关闭页面
        }
        binding.takePhotoChangeCamera.setOnClickListener {
            ptaRenderer.switchCamera() //切换摄像头
        }
        binding.takePhotoSelect.setOnClickListener {
            openSelectPhotoPage() //打开选择图片页面，完成后会调用 [onActivityResult]
        }
        binding.takePhotoBtn.setOnClickListener {
            if (viewModel.isCheckFacePassLiveData.value == false) {
                ToastUtils.showFailureToast(requireContext(), "未识别到人脸")
                return@setOnClickListener
            }
            takeSnapSotToPTA() //用 PTARenderer 拍照，成功后会调用 [onSaveSnapShotSuccess]
        }
    }

    private fun initSelectGenderLayout() {
        binding.createDialogBack.setOnClickListener {
            fuBuildAvatarViewModel.clearCachePhoto() //清空之前选择的图片。只是清空状态，并不会删除文件。
            binding.selectGenderLayout.gone()
        }
        binding.createDialogLeft.setOnClickListener {
            fuBuildAvatarViewModel.requestBuildAvatar(true) //申请生成男性 Avatar，需提前调用 [cachePhoto] 设置图片
            loadingDialog.show()
        }
        binding.createDialogRight.setOnClickListener {
            fuBuildAvatarViewModel.requestBuildAvatar(false) //申请生成女性 Avatar，需提前调用 [cachePhoto] 设置图片
            loadingDialog.show()
        }
    }

    private fun initRenderer() {
        ptaRenderer.apply {
            bindCameraConfig(FUCameraConfig().apply {
                cameraType = FUCameraTypeEnum.CAMERA2
            })
            bindGLTextureView(binding.glTextureView)
            bindListener(object : OnGLRendererListener {
                override fun onSurfaceCreated() {
                    FuDevInitializeWrapper.initRenderKit()
                }

                override fun onSurfaceChanged(width: Int, height: Int) {
                    ptaRenderer.surfaceChanged(width, height) //渲染依据实际分辨率设置
                }

                override fun onRenderBefore(inputData: FURenderInputData) {
                    viewModel.onRenderPrepare(inputData, ptaRenderer.getFuCamera().getCameraByte()!!)
                }

                override fun onRenderAfter(
                    outputData: FURenderOutputData,
                    drawMatrix: FUDrawFrameMatrix
                ) {
                    if (isNeedTakePic) {
                        val texture = outputData.texture
                        if (texture != null) {
                            val recordData  = FUPhotoRecordHelper.RecordData(texture.texId, drawMatrix.texMatrix, FUGLUtils.IDENTITY_MATRIX, texture.width, texture.height)
                            photoRecordHelper.sendRecordingData(recordData)
                        } else {
                            onSaveSnapShotError("outputData texture is null")
                        }
                        isNeedTakePic = false
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

    private fun inject() {
        fuBuildAvatarViewModel.photoPathLiveData.observe(viewLifecycleOwner) {
            val bitmap = BitmapFactory.decodeFile(it)
            binding.takePhotoPic.setImageBitmap(bitmap)
            binding.selectGenderLayout.visible() //显示后续步骤 UI
        }
        fuBuildAvatarViewModel.isBuildSuccessLiveData.observe(viewLifecycleOwner) {
            loadingDialog.dismiss()
            if (it) { //形象生成成功
                FuDemoBuildFinishDialog(requireContext()).apply {
                    callback = object : FuDemoBuildFinishDialog.Callback {
                        override fun onCancel() {
                            findNavController().popBackStack()
                            dismiss()
                        }

                        override fun onFinish() {
                            findNavController().popBackStack()
                            findNavController().navigate(R.id.editFragment)
                            dismiss()
                        }
                    }
                    create()
                    show()
                }
            } else { //形象生成失败
                ToastUtils.showFailureToast(requireContext(), "生成失败，请重新拍照尝试")
            }

        }

        viewModel.tipMsgLiveData.observe(viewLifecycleOwner) {
            binding.takePhotoPoint.text = it
        }
    }

    private val photoRecordHelper by lazy {
        val helper= FUPhotoRecordHelper()
        helper.bindListener(object : FUPhotoRecordHelper.OnPhotoRecordingListener{
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


    private fun takeSnapSotToPTA() {
        isNeedTakePic = true
    }

    private fun onSaveSnapShotSuccess(bitmap: Bitmap) {
        val cacheImageFile = File(requireContext().getExternalCacheDir(), "cacheImage${System.currentTimeMillis()}.png")
        FUMediaUtils.addBitmapToExternal(cacheImageFile.path, bitmap, false)
        fuBuildAvatarViewModel.cachePhoto(cacheImageFile) //将用于形象生成的图片文件传递给 FUBuildAvatarViewModel，之后根据 photoPathLiveData 执行后续逻辑
    }

    private fun onSaveSnapShotError(errorMsg: String) {
        ToastUtils.showFailureToast(requireContext(), errorMsg)
    }

    private fun openSelectPhotoPage() {
        val intent2 = Intent()
        intent2.addCategory(Intent.CATEGORY_OPENABLE)
        intent2.type = "image/*"
        if (Build.VERSION.SDK_INT < 19) {
            intent2.action = Intent.ACTION_GET_CONTENT
        } else {
            intent2.action = Intent.ACTION_OPEN_DOCUMENT
        }
        startActivityForResult(
            intent2,
            IMAGE_REQUEST_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val filePath = FUFileUtils.getAbsolutePathByUri(requireContext(), data.data!!)
            if (filePath == null) {
                ToastUtils.showFailureToast(requireContext(), "所选图片文件不存在。")
                return
            }
            val file = File(filePath)
            if (file.exists()) {
                fuBuildAvatarViewModel.cachePhoto(file) //将用于形象生成的图片文件传递给 FUBuildAvatarViewModel，之后根据 photoPathLiveData 执行后续逻辑
            } else {
                ToastUtils.showFailureToast(requireContext(), "所选图片文件不存在。")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        ptaRenderer.resumeRender()
    }

    override fun onPause() {
        super.onPause()
        ptaRenderer.pauseRender()
    }

    override fun onDestroy() {
        super.onDestroy()
        fuBuildAvatarViewModel.releasePTAResource() //释放资源
        fuDriveViewModel.closeFaceTrack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        ptaRenderer.release()
        FuDevInitializeWrapper.releaseRenderKit()
    }

}