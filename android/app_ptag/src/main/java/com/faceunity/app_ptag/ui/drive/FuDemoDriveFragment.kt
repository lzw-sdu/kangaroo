package com.faceunity.app_ptag.ui.drive

import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.faceunity.app_ptag.FuDevInitializeWrapper
import com.faceunity.app_ptag.R
import com.faceunity.app_ptag.databinding.FuDemoDriveFragmentBinding
import com.faceunity.app_ptag.databinding.FuDriveSettingBottomsheetBinding
import com.faceunity.app_ptag.sta.util.keyboard.KeyboardHeightObserver
import com.faceunity.app_ptag.sta.util.keyboard.KeyboardHeightProvider
import com.faceunity.app_ptag.sta.util.keyboard.KeyboardUtils
import com.faceunity.app_ptag.ui.build_avatar.renderer.FuDemoCameraRenderer
import com.faceunity.app_ptag.ui.drive.entity.BodyFollowMode
import com.faceunity.app_ptag.ui.drive.entity.BodyTrackMode
import com.faceunity.app_ptag.ui.drive.entity.DrivePage
import com.faceunity.app_ptag.view_model.FuAvatarManagerViewModel
import com.faceunity.app_ptag.view_model.FuDriveViewModel
import com.faceunity.core.camera.entity.FUCameraConfig
import com.faceunity.core.camera.enumeration.FUCameraTypeEnum
import com.faceunity.core.entity.*
import com.faceunity.core.renderer.entity.FUDrawFrameMatrix
import com.faceunity.core.renderer.infe.OnGLRendererListener
import com.faceunity.editor_ptag.store.DevAIRepository
import com.faceunity.editor_ptag.store.DevAvatarManagerRepository
import com.faceunity.editor_ptag.store.DevDrawRepository
import com.faceunity.editor_ptag.util.gone
import com.faceunity.editor_ptag.util.invisible
import com.faceunity.editor_ptag.util.visible
import com.google.android.material.bottomsheet.BottomSheetDialog

/**
 * 驱动页面
 * 此处为了 UI 状态，所有开关经过了一层 [FuDemoDriveViewModel] 中转。也可根据实际需求直接调用 [FuDriveViewModel] 中的方法
 */
class FuDemoDriveFragment : Fragment(), KeyboardHeightObserver {

    companion object {
        fun newInstance() = FuDemoDriveFragment()
    }

    private lateinit var binding: FuDemoDriveFragmentBinding
    private lateinit var settingBinding: FuDriveSettingBottomsheetBinding

    private lateinit var viewModel: FuDemoDriveViewModel
    protected lateinit var fuDriveViewModel: FuDriveViewModel
    private lateinit var fuAvatarManagerViewModel: FuAvatarManagerViewModel

    private lateinit var settingDialog: BottomSheetDialog

    private lateinit var mKeyboardHeightProvider: KeyboardHeightProvider

    private var mKeyboardHeight: Int = 0


    private val ptaRenderer by lazy { FuDemoCameraRenderer(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FuDemoDriveFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FuDemoDriveViewModel::class.java)
        fuDriveViewModel = ViewModelProvider(this).get(FuDriveViewModel::class.java)
        fuAvatarManagerViewModel = ViewModelProvider(this).get(FuAvatarManagerViewModel::class.java)

        initView()
        initSettingDialog()
        initRenderer()
        inject()
        injectSetting()


    }

    private fun initView() {
        binding.glTextureView.apply {
            isOpaque = false
            setOnTouchListener { _, event ->
                ptaRenderer.onTouchEvent(
                    event.x.toInt(),
                    event.y.toInt(),
                    event.action
                ) //为了可以拖拽预览相机窗口
                mKeyboardHeight == 0
            }

            setOnClickListener {
                if (mKeyboardHeight > 0) {
                    KeyboardUtils.hideSoftInput(activity)
                    playAnim(binding.frameInput, 0)
                }
            }

        }



        binding.backBtn.apply {
            setOnClickListener {
                Navigation.findNavController(it).popBackStack() //返回上一个页面
            }
        }
        binding.settingBtn.apply {
            setOnClickListener {
                settingDialog.show()
            }

        }
        binding.switchCameraToward.apply {
            setOnClickListener {
                ptaRenderer.switchCamera() //切换相机
            }
        }

        binding.arMode.apply {
            setOnClickListener {
                viewModel.checkoutArPage() //切换AR模式，完成后会通知 [drivePageLiveData]
            }
        }
        binding.arMode.gone()
        binding.arModeText.apply {
            setOnClickListener {
                viewModel.checkoutArPage() //切换AR模式，完成后会通知 [drivePageLiveData]
            }
        }
        binding.arModeText.gone()
        binding.trackMode.apply {
            setOnClickListener {
                viewModel.checkoutTrackPage() //切换跟踪模式，完成后会通知 [drivePageLiveData]
            }
        }

        binding.trackModeText.apply {
            setOnClickListener {
                viewModel.checkoutTrackPage() //切换跟踪模式，完成后会通知 [drivePageLiveData]
            }
        }
        binding.textMode.apply {
            setOnClickListener {
                viewModel.checkoutTextPage() //切换文本驱动模式，完成后会通知 [drivePageLiveData]
            }
        }
        binding.textModeText.apply {
            setOnClickListener {
                viewModel.checkoutTextPage() //切换文本驱动模式，完成后会通知 [drivePageLiveData]
            }
        }
        binding.textMode.gone()
        binding.textModeText.gone()
        binding.btnSend.apply {
            setOnClickListener {
                binding.etInput.text.clear()
                KeyboardUtils.hideSoftInput(activity)
            }
        }
        binding.btnSend.gone()


        mKeyboardHeightProvider = KeyboardHeightProvider(activity)
        mKeyboardHeightProvider?.setKeyboardHeightObserver(this)
        binding.etInput.apply {
            setOnClickListener {
                KeyboardUtils.showSoftInput(activity, binding.etInput)
            }
            post {
                mKeyboardHeightProvider?.start()
            }
            addTextChangedListener {
                doOnTextChanged { text, start, before, count -> if (TextUtils.isEmpty(text)) binding.btnSend.gone() else binding.btnSend.visible() }
            }
        }

    }

    private fun initSettingDialog() {
        settingBinding =
            FuDriveSettingBottomsheetBinding.inflate(LayoutInflater.from(requireContext())).apply {

            }
        settingDialog = BottomSheetDialog(requireContext()).apply {
            setContentView(settingBinding.root)
            dismissWithAnimation = true
        }

        settingBinding.switchFaceTrackBtn.apply {
            setOnClickListener {
                viewModel.switchFaceTrack() //切换人脸跟踪开关
            }
        }

        settingBinding.bodyTrackLayout.apply {
            settingBinding.bodyTrackText.text = viewModel.bodyTrackModeLiveData.value?.showText()
            val popupMenu = PopupMenu(requireContext(), settingBinding.bodyTrackText, Gravity.END)
            val list = BodyTrackMode.values().map { it.showText() }
            list.forEach {
                popupMenu.menu.add(it)
            }
            popupMenu.setOnMenuItemClickListener {
                when (list.indexOf(it.title)) {
                    0 -> {
                        viewModel.requestSetBodyTrackFull() //开启人体全身跟踪，完成后会通知 [bodyTrackModeLiveData]
                    }
                    1 -> {
                        viewModel.requestSetBodyTrackHalf() //开启人体半身跟踪，完成后会通知 [bodyTrackModeLiveData]
                    }
                    2 -> {
                        viewModel.requestSetBodyTrackClose() //关闭人体跟踪，完成后会通知 [bodyTrackModeLiveData]
                    }
                }
                true
            }

            setOnClickListener {
                popupMenu.show()
            }
        }

        settingBinding.bodyFollowModeLayout.apply {
            settingBinding.bodyFollowModeText.text =
                viewModel.bodyFollowModeLiveData.value?.showText()
            val popupMenu =
                PopupMenu(requireContext(), settingBinding.bodyFollowModeText, Gravity.END)
            val list = BodyFollowMode.values().map { it.showText() }
            list.forEach {
                popupMenu.menu.add(it)
            }
            popupMenu.setOnMenuItemClickListener {
                when (list.indexOf(it.title)) {
                    0 -> {
                        viewModel.requestSetBodyFollowModeFix() //切换人体跟踪模式 Fix，完成后会通知 [bodyFollowModeLiveData]
                    }
                    1 -> {
                        viewModel.requestSetBodyFollowModeStage() //切换人体跟踪模式 Stage，完成后会通知 [bodyFollowModeLiveData]
                    }
                    2 -> {
                        viewModel.requestSetBodyFollowModeAlign() //切换人体跟踪模式 Align，完成后会通知 [bodyFollowModeLiveData]
                    }
                }
                true
            }

            setOnClickListener {
                popupMenu.show()
            }
        }
    }

    override fun onKeyboardHeightChanged(height: Int, orientation: Int) {
        if (height > 0) {
            playAnim(binding.frameInput, -height)
            binding.etInput.maxLines = 3
        } else {
            playAnim(binding.frameInput, 0)
            binding.etInput.maxLines = 1
        }
        mKeyboardHeight = height
    }

    private fun playAnim(view: View, translation: Int) {
        view.animate().translationY(translation.toFloat())
            .setDuration(100)
            .start()
    }

    private fun initRenderer() {
        ptaRenderer.apply {
            bindCameraConfig(FUCameraConfig().apply {
                cameraType = FUCameraTypeEnum.CAMERA2
                cameraFPS = 30
            })


            bindGLTextureView(binding.glTextureView).

            bindListener(object : OnGLRendererListener {
                override fun onSurfaceCreated() {
                    FuDevInitializeWrapper.initRenderKit()
                    fuAvatarManagerViewModel.autoInitDefaultAvatar(false)
                    viewModel.init()

                    DevAvatarManagerRepository.getCurrentAvatarInfo()?.avatar?.let { avatar ->
                        avatar.animation.getAnimations().forEach {
                            if (!it.path.contains("huxi")) {
                                avatar.animation.removeAnimation(it)
                            }
                        }
                    }
                }


                override fun onSurfaceChanged(width: Int, height: Int) {
                    ptaRenderer.surfaceChanged(width, height) //渲染依据实际分辨率设置
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

    private fun inject() {
        viewModel.drivePageLiveData.observe(viewLifecycleOwner) {
            when (it) { //根据不同的页面，对 Renderer 采用不同的渲染状态
                DrivePage.Ar -> {
                    ptaRenderer.drawSmallViewport(false)
                    fuDriveViewModel.openArMode() //进入到 AR 页面时直接开启 AR 模式
                    onOpenArMode()


                    binding.settingBtn.gone()
//                    binding.switchCameraToward.visible()
                    binding.frameInput.gone()

                    KeyboardUtils.hideSoftInput(activity)
                    binding.arMode.setImageResource(R.drawable.icon_drive_face_sel)
                    binding.trackMode.setImageResource(R.drawable.icon_drive_body_nor)
                    binding.textMode.setImageResource(R.drawable.icon_drive_text_nor)
                }
                DrivePage.Track -> {
                    onCloseArMode()
//                    binding.backBtn.gone()
                    binding.btnSend.gone()
                    binding.etInput.gone()
                    binding.switchCameraToward.gone()
                    binding.settingBtn.gone()
                    binding.arMode.gone()
                    binding.arModeText.gone()
                    binding.frameInput.gone()
                    binding.trackMode.gone()
                    binding.trackModeText.gone()
                    binding.textMode.gone()
                    binding.textModeText.gone()
                    binding.switchCameraToward.invisible()
                    binding.settingBtn.invisible()
                    ptaRenderer.switchCamera()

                    ptaRenderer.drawSmallViewport(true)

                    viewModel.notifyBodyTrackModeLiveData()

                  /*  binding.settingBtn.visible()
                    binding.switchCameraToward.visible()*/
                    binding.frameInput.gone()

                    KeyboardUtils.hideSoftInput(activity)
                    binding.arMode.setImageResource(R.drawable.icon_drive_face_nor)
                    binding.trackMode.setImageResource(R.drawable.icon_drive_body_sel)
                    binding.textMode.setImageResource(R.drawable.icon_drive_text_nor)

                    DevDrawRepository.getScene().setBackgroundBundle(null)
                    DevDrawRepository.getScene()
                        .setBackgroundColor(FUColorRGBData(0.0, 0.0, 0.0, 0.0))
                    DevDrawRepository.getScene().businessSupport.setEnableGroundReflection(false)
                }
                DrivePage.Text -> {
                    onCloseArMode()
                    ptaRenderer.drawSmallViewport(false)
                    fuDriveViewModel.closeBodyTrack()

                    binding.settingBtn.gone()
                    binding.switchCameraToward.gone()
                    binding.frameInput.visible()
                    binding.arMode.setImageResource(R.drawable.icon_drive_face_nor)
                    binding.trackMode.setImageResource(R.drawable.icon_drive_body_nor)
                    binding.textMode.setImageResource(R.drawable.icon_drive_text_sel)
                }
                else -> {}
            }

        }
    }

    private fun injectSetting() {
        viewModel.isFaceTrackLiveData.observe(viewLifecycleOwner) {
            if (it) {
                fuDriveViewModel.openFaceTrack() //开启人脸跟踪
                settingBinding.switchFaceTrackBtn.setImageResource(R.drawable.btn_switch_on)
            } else {
                fuDriveViewModel.closeFaceTrack() //关闭人脸跟踪
                settingBinding.switchFaceTrackBtn.setImageResource(R.drawable.btn_switch_off)
            }
        }
        viewModel.bodyTrackModeLiveData.observe(viewLifecycleOwner) {
            when (it) {
                BodyTrackMode.Full -> {
                    fuDriveViewModel.openBodyFullTrack() //开启全身跟踪
                }
                BodyTrackMode.Half -> {
                    fuDriveViewModel.openBodyHalfTrack() //开启半身跟踪
                }
                BodyTrackMode.Close -> {
                    fuDriveViewModel.closeBodyTrack() //关闭跟踪
                }
                else -> {}
            }
            settingBinding.bodyTrackText.text = it.showText()

        }
        viewModel.bodyFollowModeLiveData.observe(viewLifecycleOwner) {
            when (it) {
                BodyFollowMode.Fix -> {
                    fuDriveViewModel.setBodyFollowModeFix() //设置人体跟踪模式为 Fix
                }
                BodyFollowMode.Stage -> {
                    fuDriveViewModel.setBodyFollowModeStage() //设置人体跟踪模式为 Stage
                }
                BodyFollowMode.Align -> {
                    fuDriveViewModel.setBodyFollowModeAlight() //设置人体跟踪模式为 Align
                }
                else -> {}
            }
            settingBinding.bodyFollowModeText.text = it.showText()

        }
    }

    private fun onOpenArMode() {

        fuDriveViewModel.closeBodyTrack()

        //添加一个比心的手势检测
        DevAIRepository.openHandTrack()
        DevDrawRepository.getScene()
            .setBackgroundBundle(FUBundleData("temp/fg_danshoubixin.bundle"))
    }

    private fun onCloseArMode() {
        fuDriveViewModel.closeArMode() //进入其他页面时关闭 AR 模式
        DevAIRepository.closeHandTrack()
        DevDrawRepository.getScene().setBackgroundBundle(null)
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
        mKeyboardHeightProvider?.close()
        mKeyboardHeightProvider?.setKeyboardHeightObserver(null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        ptaRenderer.release()
        FuDevInitializeWrapper.releaseRenderKit()
    }

}