package com.faceunity.app_ptag.ui.preview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.faceunity.app_ptag.FuDevInitializeWrapper
import com.faceunity.app_ptag.R
import com.faceunity.app_ptag.databinding.FuDemoPreviewFragmentBinding
import com.faceunity.app_ptag.databinding.FuEditSettingBottomsheetBinding
import com.faceunity.app_ptag.weight.avatar_manager.AvatarManagerDialog
import com.faceunity.app_ptag.weight.avatar_manager.entity.FuAvatarContainer
import com.faceunity.app_ptag.weight.avatar_manager.entity.FuAvatarWrapper
import com.faceunity.app_ptag.weight.avatar_manager.parser.FuAvatarContainerParser
import com.faceunity.core.camera.entity.FUCameraConfig
import com.faceunity.core.entity.FURenderInputData
import com.faceunity.core.entity.FURenderOutputData
import com.faceunity.core.renderer.entity.FUDrawFrameMatrix
import com.faceunity.core.renderer.impl.FUCustomRenderer
import com.faceunity.core.renderer.infe.OnGLRendererListener
import com.faceunity.editor_ptag.store.DevAvatarManagerRepository
import com.faceunity.app_ptag.view_model.FuAvatarManagerViewModel
import com.faceunity.app_ptag.view_model.FuPreviewViewModel
import com.faceunity.pta.pta_core.widget.TouchGLTextureView
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlin.math.absoluteValue

class FuDemoPreviewFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance() = FuDemoPreviewFragment()
    }

    private lateinit var binding: FuDemoPreviewFragmentBinding
    private lateinit var settingBinding: FuEditSettingBottomsheetBinding

    private lateinit var viewModel: FuDemoPreviewViewModel
    private lateinit var fuPreviewViewModel: FuPreviewViewModel
    private lateinit var fuAvatarManagerViewModel: FuAvatarManagerViewModel

    private lateinit var settingDialog: BottomSheetDialog
    private lateinit var avatarManagerDialog: AvatarManagerDialog
    private val avatarWrapper = FuAvatarContainer(mutableListOf(), mutableListOf())

    private val ptaRenderer = FUCustomRenderer()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FuDemoPreviewFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FuDemoPreviewViewModel::class.java)
        fuPreviewViewModel = ViewModelProvider(this).get(FuPreviewViewModel::class.java)
        fuAvatarManagerViewModel = ViewModelProvider(this).get(FuAvatarManagerViewModel::class.java)

        initView()
        initSettingDialog()
        initAvatarManagerDialog()
        initRenderer()
        injectSetting()

        fuAvatarManagerViewModel.autoInitDefaultAvatar()
    }

    private fun initView() {
        binding.glTextureView.apply {
            isOpaque = false
            if (this is TouchGLTextureView) {
                setTouchListener(object : TouchGLTextureView.OnTouchListener {
                    private var lastDeltaX = 0f

                    override fun onScale(scale: Float) {
                        if (viewModel.lockScaleLiveData.value == false) { //视角锁定
                            fuPreviewViewModel.scaleAvatar(scale)
                        }
                    }

                    override fun onMove(deltaX: Float, deltaY: Float) {
                        lastDeltaX = deltaX
                        fuPreviewViewModel.rotateAvatar(deltaX)
                        fuPreviewViewModel.cancelRollAvatar()
                        if (viewModel.lockScaleLiveData.value == false) { //视角锁定
                            fuPreviewViewModel.moveVerticalAvatar(-deltaY) //Android 与 OpenGL 坐标系上下相反，故为负
                        }
                    }

                    override fun onClick() {

                    }

                    override fun onUp() {
                        if (lastDeltaX.absoluteValue > 0.001) { //如果手势离开屏幕时高于一定的速度,则触发惯性滚动
                            fuPreviewViewModel.rollAvatar(lastDeltaX)
                        }
                    }
                })
            }

        }

        binding.settingBtn.apply {
            setOnClickListener {
                settingDialog.show()
            }
        }

        binding.managerAvatarBtn.setOnClickListener {
            avatarManagerDialog.show()
        }

    }

    private fun initSettingDialog() {
        settingBinding = FuEditSettingBottomsheetBinding.inflate(LayoutInflater.from(requireContext())).apply {
            lockScaleBtn.setOnClickListener {
                viewModel.switchLockScaleStatus() //切换视角锁定状态
            }
        }
        settingDialog = BottomSheetDialog(requireContext()).apply {
            setContentView(settingBinding.root)
            dismissWithAnimation = true
//            findViewById<View>(R.id.design_bottom_sheet)?.let {
//                BottomSheetBehavior.from(it).setPeekHeight(FUDensityUtils.dp2px(requireContext(), 216f)) //设置预览高度，与 UI 强相关
//            }
        }
    }

    private fun initAvatarManagerDialog() {
        avatarManagerDialog = AvatarManagerDialog(requireContext(),
            onItemClick = { item: FuAvatarWrapper ->
                fuAvatarManagerViewModel.autoSwitchAvatar(item.id) //加载形象。如果已准备好则直接加载，否则从云端下载。
            },
            onItemDelete = { item: FuAvatarWrapper ->
                fuAvatarManagerViewModel.autoRemoveAvatar(item.id) //删除形象
            }
        )
        fuAvatarManagerViewModel.avatarCollectionLiveData.observe(viewLifecycleOwner) {
            val fuAvatarContainerParser = FuAvatarContainerParser()
            val wrapperList = DevAvatarManagerRepository.mapAvatar {
                fuAvatarContainerParser.parserAvatarInfoToFuAvatarWrapper(it)
            }
            avatarWrapper.avatarList.apply {
                clear()
                addAll(wrapperList)
            }
            avatarManagerDialog.syncAvatarContainer(avatarWrapper) //同步最新的形象容器至 UI
        }
        fuAvatarManagerViewModel.avatarSelectLiveData.observe(viewLifecycleOwner) {
            avatarWrapper.selectId.apply {
                clear()
                add(it)
            }
            avatarManagerDialog.syncAvatarContainer(avatarWrapper) //同步最新的形象容器至 UI
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

    private fun injectSetting() {
        viewModel.lockScaleLiveData.observe(viewLifecycleOwner) {
            settingBinding.lockScaleBtn.setImageResource(
                if (it) {
                    R.drawable.btn_switch_on
                } else {
                    R.drawable.btn_switch_off
                }
            )
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        ptaRenderer.release()
        FuDevInitializeWrapper.releaseRenderKit()
    }

}