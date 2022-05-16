package com.faceunity.app_ptag.ui.edit

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.faceunity.app_ptag.FuDevInitializeWrapper
import com.faceunity.app_ptag.databinding.FuEditFragmentBinding
import com.faceunity.app_ptag.ui.edit.entity.facepup.CustomFacepupIcon
import com.faceunity.app_ptag.ui.edit.translator.CustomFacepupModelTranslator
import com.faceunity.app_ptag.ui.edit.weight.control.AvatarControlListener
import com.faceunity.app_ptag.ui.edit.weight.facepup.FacepupControlView
import com.faceunity.app_ptag.util.ToastUtils
import com.faceunity.app_ptag.weight.DownloadingDialog
import com.faceunity.app_ptag.weight.FuDemoRetryDialog
import com.faceunity.core.camera.entity.FUCameraConfig
import com.faceunity.core.entity.FUAnimationBundleData
import com.faceunity.core.entity.FURenderInputData
import com.faceunity.core.entity.FURenderOutputData
import com.faceunity.core.renderer.entity.FUDrawFrameMatrix
import com.faceunity.core.renderer.impl.FUCustomRenderer
import com.faceunity.core.renderer.infe.OnGLRendererListener
import com.faceunity.editor_ptag.data.DownloadStatus
import com.faceunity.editor_ptag.data_center.FuDevDataCenter
import com.faceunity.editor_ptag.store.DevDrawRepository
import com.faceunity.editor_ptag.util.gone
import com.faceunity.editor_ptag.util.visible
import com.faceunity.app_ptag.view_model.FuAvatarManagerViewModel
import com.faceunity.app_ptag.view_model.FuEditViewModel
import com.faceunity.app_ptag.view_model.FuPreviewViewModel
import com.faceunity.app_ptag.view_model.entity.FuRequestErrorInfo
import com.faceunity.editor_ptag.store.DevAvatarManagerRepository
import com.faceunity.fupta.cloud_download.CloudResourceManager
import com.faceunity.fupta.facepup.FacePupManager
import com.faceunity.fupta.facepup.FacePupParser
import com.faceunity.fupta.facepup.entity.tier.FacepupSlider
import com.faceunity.pta.pta_core.widget.TouchGLTextureView
import com.faceunity.support.entity.FUAEColorItem
import com.faceunity.support.entity.FUAEItem
import com.faceunity.support.entity.FUAEMinorCategory
import kotlin.concurrent.thread

class FuDemoEditFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance() = FuDemoEditFragment()
    }

    private var _binding: FuEditFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<FuDemoEditViewModel>()
    private lateinit var fuPreviewViewModel: FuPreviewViewModel
    private lateinit var fuAvatarManagerViewModel: FuAvatarManagerViewModel
    private lateinit var fuEditViewModel: FuEditViewModel

    private val downloadingDialog: DownloadingDialog by lazy {
        DownloadingDialog(requireContext())
    }

    private val ptaRenderer = FUCustomRenderer()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FuEditFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showSaveDialog()
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        fuPreviewViewModel = ViewModelProvider(this).get(FuPreviewViewModel::class.java)
        fuAvatarManagerViewModel = ViewModelProvider(this).get(FuAvatarManagerViewModel::class.java)
        fuEditViewModel = ViewModelProvider(this).get(FuEditViewModel::class.java)

        initView()
        initRenderer()
        initFacepup()
        injectEditModel()
        injectDownload()

        fuAvatarManagerViewModel.autoInitDefaultAvatar(false) //自动加载默认的 Avatar
        fuEditViewModel.requestEditorData() //初始化编辑数据，后在 [controlModelLiveData] 响应
        viewModel.initFacepup()
    }

    private fun initView() {
        binding.glTextureView.apply {
            isOpaque = false
            if (this is TouchGLTextureView) {
                setTouchListener(object : TouchGLTextureView.OnTouchListener {

                    override fun onScale(scale: Float) {

                    }

                    override fun onMove(deltaX: Float, deltaY: Float) {
                        fuPreviewViewModel.rotateAvatar(deltaX)
                    }

                    override fun onClick() {

                    }

                    override fun onUp() {

                    }
                })
            }
        }
        binding.backBtn.setOnClickListener {
            showSaveDialog()
        }
        binding.avatarControlView.visibleControl()
    }


    private fun injectDownload() {
        fuAvatarManagerViewModel.cloudAvatarPrepareLiveData.observe(viewLifecycleOwner) { //形象所需的资源下载完成后，显示该形象
            fuAvatarManagerViewModel.switchAvatarById(it)
        }
        fuAvatarManagerViewModel.avatarSelectLiveData.observe(viewLifecycleOwner) {
            fuEditViewModel.syncAvatarEditStatus()
        }
        fuAvatarManagerViewModel.requestErrorInfoLiveData.observe(viewLifecycleOwner) { //网络请求异常
            val type = it.type
            when (type) {
                is FuRequestErrorInfo.Type.Retry -> {
                    FuDemoRetryDialog(requireContext(), type.tip).apply {
                        callback = object : FuDemoRetryDialog.Callback {
                            override fun onCancel() {
                                dismiss()
                            }

                            override fun onFinish() { //网络错误下的重试按钮要执行的操作
                                fuAvatarManagerViewModel.autoInitDefaultAvatar(false)
                                dismiss()
                            }
                        }
                        create()
                        show()
                    }
                }
                is FuRequestErrorInfo.Type.Error -> {
                    ToastUtils.showFailureToast(requireContext(), "遇到异常：${type.tip}。/n请稍后重试")
                }
            }
        }
        fuEditViewModel.downloadInfoLiveData.observe(viewLifecycleOwner) {
            val (fileId, status) = it
            when (status) {
                is DownloadStatus.Start -> {
                    binding.avatarControlView.controlStyleRecord {
                        it.notifyDownloadStart(fileId)
                    }
                }
                is DownloadStatus.Progress -> {

                }
                is DownloadStatus.Finish -> {
                    FuDevDataCenter.getCloudResourceManager()?.checkResourceStatus(fileId).also { //刷新资源状态
                        if (it != CloudResourceManager.CheckStatus.Success) {
                            //TODO 异常处理
                        }
                    }
                    it.groupFileIdList?.forEach {
                        if (it == fileId) return@forEach
                        FuDevDataCenter.getCloudResourceManager()?.checkResourceStatus(it).also { //刷新资源状态
                            if (it != CloudResourceManager.CheckStatus.Success) {
                                //TODO 异常处理
                            }
                        }
                    }
                    binding.avatarControlView.controlStyleRecord {
                        it.notifyDownloadSuccess(fileId)
                    }
                }
                is DownloadStatus.Error -> {
                    binding.avatarControlView.controlStyleRecord {
                        it.notifyDownloadError(fileId)
                    }
                    it.retryItem?.let { item ->
                        FuDemoRetryDialog(requireContext(), status.errorMsg).apply {
                            callback = object : FuDemoRetryDialog.Callback {
                                override fun onCancel() {
                                    dismiss()
                                }

                                override fun onFinish() { //网络错误下的重试按钮要执行的操作
                                    fuEditViewModel.loadCloudItem(item)
                                    dismiss()
                                }
                            }
                            create()
                            show()
                        }
                    }

                }
            }
        }

        fuAvatarManagerViewModel.downloadStatusLiveData.observe(viewLifecycleOwner) { //网络下载状态
            val status = it.downloadStatus
            when (status) {
                is DownloadStatus.Start -> {
                    downloadingDialog.show()
                }
                is DownloadStatus.Progress -> {
                    downloadingDialog.updateText("下载 ${status.downloadCount}/${status.totalCount}")
                }
                is DownloadStatus.Error -> {
                    FuDemoRetryDialog(requireContext(), "下载异常：${status.errorMsg}").apply {
                        callback = object : FuDemoRetryDialog.Callback {
                            override fun onCancel() {
                                dismiss()
                            }

                            override fun onFinish() { //网络错误下的重试按钮要执行的操作
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
                    ptaRenderer.setEmptyTextureConfig(width, height) //渲染依据实际分辨率设置
                }

                override fun onRenderBefore(inputData: FURenderInputData) {

                }

                override fun onRenderAfter(
                    outputData: FURenderOutputData,
                    drawMatrix: FUDrawFrameMatrix
                ) {
                }

                override fun onDrawFrameAfter() {

                }

                override fun onSurfaceDestroy() {
                    FuDevInitializeWrapper.releaseRenderKit()
                }
            })
        }
    }

    private fun initFacepup() {
        binding.facepupControlView.apply {

            setControlListener(object : FacepupControlView.ControlListener {
                override fun onSeekBarScroll(slider: FacepupSlider, scale: Float) {
                    fuEditViewModel.setFacepupTierSeekBarItem(getFileId(), getGroupKey(), slider, scale)
                }

                override fun onResetClick() {
                    fuEditViewModel.resetFacepupByFileId(getFileId())
                }

                override fun onStart() {
                    binding.facepupControlView.visible()
                    binding.avatarControlView.gone()
                    binding.backBtn.gone()
                    tempSwitchCamera("cam_ptag_banshen.bundle")
                }

                override fun onFinish() {
                    binding.facepupControlView.gone()
                    binding.avatarControlView.visible()
                    binding.backBtn.visible()
                    tempSwitchCamera("ani_ptag_cam.bundle")
                }

                override fun onModeSwitch(mode: Int) {
                    showFacepupDialog(getGroupKey(), getFileId(), mode)
                }


                //临时的切换相机功能
                private fun tempSwitchCamera(bundleName: String) {
                    DevDrawRepository.getScene()?.apply {
                        cameraAnimation.setAnimation(FUAnimationBundleData("temp/$bundleName"))
                        cameraAnimationGraph.setAnimationGraphParam("DefaultStateBlendTime", 0f)
                    }
                }
            })
        }
    }

    private fun injectEditModel() {
        fuEditViewModel.controlModelLiveData.observe(viewLifecycleOwner) {
            binding.avatarControlView.bindData(it) //将编辑组件的控制模型绑定到 Avatar 控制组件
            binding.avatarControlView.controlListener = controlListener
        }
        fuEditViewModel.filterGroupLiveData.observe(viewLifecycleOwner) {
            binding.avatarControlView.filterGenderList(it) //同步刷新 UI 的筛选状态
        }
        fuEditViewModel.selectItemLiveData.observe(viewLifecycleOwner) {
            binding.avatarControlView.notifySelectItem(it) //通知最新选中的 Item，交由 UI 暂存
        }
        fuEditViewModel.currentAvatarLiveData.observe(viewLifecycleOwner) {
            binding.facepupControlView.syncGender(it.gender())
        }
    }


    private val controlListener = object : AvatarControlListener {
        private var currentFacepupGroupKey: String? = null

        override fun onMinorSelect(item: FUAEMinorCategory) {
            fuEditViewModel.clickMinor(item)
            if (fuEditViewModel.isHasFacepup(item.key)) {
                binding.avatarControlView.visibleFacepupBtn()
                currentFacepupGroupKey = item.key
            } else {
                binding.avatarControlView.goneFacepupBtn()
                currentFacepupGroupKey = null
            }
        }

        override fun onNormalItemClick(item: FUAEItem) {
            val fileId = item.fuEditBundleItem?.fileID
            if (currentFacepupGroupKey != null && fileId != null && !fuEditViewModel.isHasFacepupPackInfo(fileId)) { //当点击道具时重置该组的捏脸信息。除非是当前道具。
                fuEditViewModel.resetFacepupByGroupKey(currentFacepupGroupKey!!)
            }
            fuEditViewModel.autoSetItem(item)
        }

        override fun onColorItemClick(item: FUAEColorItem) {
            fuEditViewModel.autoSetColorItem(item)
        }

        override fun onFacepupClick(groupKey: String, fileId: String?) {
            showFacepupDialog(groupKey, fileId, null)
        }

        override fun onHistoryBackClick() {
            fuEditViewModel.historyBack()
        }

        override fun onHistoryForwardClick() {
            fuEditViewModel.historyForward()
        }

        override fun onHistoryResetClick() {
            fuEditViewModel.historyReset()
        }
    }

    private fun showFacepupDialog(groupKey: String, fileId: String?, mode: Int?) {
        if (!fuEditViewModel.isHasFacepup(groupKey)) {
            ToastUtils.showFailureToast(requireContext(), "没有找到对应的捏脸数据")
            return
        }
        if (fileId == null) {
            ToastUtils.showFailureToast(requireContext(), "没有找到选中的道具")
            return
        }
        val customGroup = viewModel.buildCustomGroup(groupKey, mode ?: viewModel.facepupMode)
        binding.facepupControlView.syncGroupInfo(customGroup, fileId)
        binding.facepupControlView.syncFacepupMode(viewModel.facepupMode)
    }

    private fun showSaveDialog() {
        //TODO 一、区分是否是默认形象；二、在退出的时候对编辑的 Avatar 的 Avatar 恢复（不需要维护，拿一个独立的形象。）
        fuAvatarManagerViewModel.saveCurrentAvatar()
        findNavController().popBackStack()
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        ptaRenderer.release()
        FuDevInitializeWrapper.releaseRenderKit()
    }

}