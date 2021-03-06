package com.faceunity.app_ptag.ui.function_demo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.faceunity.app_ptag.FuDevInitializeWrapper
import com.faceunity.app_ptag.databinding.FunctionDemoFragmentBinding
import com.faceunity.core.avatar.business.FrameActionExecutor
import com.faceunity.core.camera.entity.FUCameraConfig
import com.faceunity.core.entity.FUAnimationBundleData
import com.faceunity.core.entity.FUBundleData
import com.faceunity.core.entity.FURenderInputData
import com.faceunity.core.entity.FURenderOutputData
import com.faceunity.core.faceunity.FUSceneKit
import com.faceunity.core.listener.OnExecuteListener
import com.faceunity.core.renderer.entity.FUDrawFrameMatrix
import com.faceunity.core.renderer.impl.FUCustomRenderer
import com.faceunity.core.renderer.infe.OnGLRendererListener
import com.faceunity.editor_ptag.data_center.FuDevDataCenter
import com.faceunity.editor_ptag.store.DevAvatarManagerRepository
import com.faceunity.editor_ptag.store.DevDrawRepository
import com.faceunity.editor_ptag.util.FuLog
import com.faceunity.editor_ptag.util.visible
import com.faceunity.fupta.facepup.FacePupManager
import com.faceunity.fupta.facepup.entity.FacePupParam
import com.faceunity.fupta.facepup.entity.FacePupTierWrapper
import com.faceunity.fupta.facepup.weight.FacePupControlListener
import com.faceunity.fupta.facepup.weight.FacePupControlTierListener

class FunctionDemoFragment : Fragment() {

    companion object {
        fun newInstance() = FunctionDemoFragment()
    }

    private lateinit var viewModel: FunctionDemoViewModel

    private lateinit var binding: FunctionDemoFragmentBinding

    private val ptaRenderer = FUCustomRenderer()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FunctionDemoFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FunctionDemoViewModel::class.java)

        initView()
        initRenderer()
    }

    //????????????????????????????????????????????????????????? Activity???Fragment ??????????????????
    private fun initView() {
        binding.removeCamera.setOnClickListener {
            val sceneInfo = DevDrawRepository.getCurrentSceneInfo() ?: return@setOnClickListener
            DevDrawRepository.getScene().cameraAnimation.setAnimation(null)
        }
        //???????????????????????????????????????????????????????????????????????????????????? [setCameraByDataCenter] ?????????
        binding.setCamera.setOnClickListener {
            val sceneInfo = DevDrawRepository.getCurrentSceneInfo() ?: return@setOnClickListener
            val fullPath = "OSSAssets/GAssets/camera/scene/ani_ptag_cam.bundle"
            DevDrawRepository.getScene().cameraAnimation.setAnimation(FUAnimationBundleData(fullPath))
        }
        binding.setCameraByDataCenter.setOnClickListener {
            val sceneInfo = DevDrawRepository.getCurrentSceneInfo() ?: return@setOnClickListener
            val fileID = "GAssets/camera/scene/ani_ptag_cam.bundle"
            val path = FuDevDataCenter.resourcePath.ossCustom(fileID)
            //??????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            DevDrawRepository.getScene().cameraAnimation.setAnimation(FUAnimationBundleData(path))
        }
        binding.removeAnimation.setOnClickListener {
            val avatarInfo = DevAvatarManagerRepository.getCurrentAvatarInfo() ?: return@setOnClickListener
            avatarInfo.avatar.animation.removeAllAnimations()
        }
        //???????????????????????????????????????????????? scene_list.json ????????????????????????
        binding.setAnimation.setOnClickListener {
            val avatarInfo = DevAvatarManagerRepository.getCurrentAvatarInfo() ?: return@setOnClickListener
            val fullPath = "OSSAssets/GAssets/animation/AsiaMale/ani_ptag_amale_sikao.bundle"
            val animation = FUAnimationBundleData(fullPath, "think")
            avatarInfo.avatar.animation.apply {
                addAnimation(animation)
                playAnimation(animation, true)
            }
        }
        binding.setAnimationBySceneConfig.setOnClickListener {
            val avatarInfo = DevAvatarManagerRepository.getCurrentAvatarInfo() ?: return@setOnClickListener
            val sceneInfo = DevDrawRepository.getCurrentSceneInfo() ?: return@setOnClickListener
            val animationRes = sceneInfo.getAllAnimation().randomOrNull() ?: return@setOnClickListener
            val path = FuDevDataCenter.resourcePath.ossCustom(animationRes.path)
            val animation = FUAnimationBundleData(path, animationRes.name ?: "random")
            avatarInfo.avatar.animation.apply {
                addAnimation(animation)
                playAnimation(animation, true)
            }
        }
        binding.setBackground.setOnClickListener {
            val sceneInfo = DevDrawRepository.getCurrentSceneInfo() ?: return@setOnClickListener
            val fullPath = "OSSAssets/GAssets/2dsprite/background/bg_yuanlin.bundle"
            DevDrawRepository.getScene().setBackgroundBundle(FUBundleData(fullPath))
        }
        binding.setBackgroundByExecutor.setOnClickListener {
            val sceneInfo = DevDrawRepository.getCurrentSceneInfo() ?: return@setOnClickListener
            val fullPath = "OSSAssets/GAssets/2dsprite/background/bg_yuanlin.bundle"
            val executor = FrameActionExecutor()
            executor.setSceneBackgroundBundle(DevDrawRepository.getScene(), FUBundleData(fullPath))
            FUSceneKit.getInstance().executeFrameAction(executor, object : OnExecuteListener {
                override fun onCompleted() {
                }
            })
        }
        binding.openOriginFacepupView.setOnClickListener {
            val meshPointsString = FuDevDataCenter.fastLoadString { appFacepupPoints() }
            val meshConfigString = FuDevDataCenter.fastLoadString { appFacepupConfig() }
            val meshLanguageString = FuDevDataCenter.fastLoadString { appFacepupLanguage() }
            val facePupContainer = FacePupManager.loadFacePupContainer(meshPointsString, meshConfigString, meshLanguageString)
            binding.facePupOriginControlView.apply {
                visible()
                updateContainer(facePupContainer)
                setControlListener(object : FacePupControlListener {
                    override fun onSeekBarScroll(item: FacePupParam, scale: Float) {
                        DevAvatarManagerRepository.getCurrentAvatarInfo()?.avatar?.let {
                            it.deformation.setDeformation(item.key, scale)
                        }
                    }
                })
            }
        }
        binding.openTierFacepupView.setOnClickListener {
            val meshPointsString = FuDevDataCenter.fastLoadString { appFacepupPoints() }
            val meshConfigString = FuDevDataCenter.fastLoadString { appFacepupConfig() }
            val meshLanguageString = FuDevDataCenter.fastLoadString { appFacepupLanguage() }
            val facePupContainer = FacePupManager.loadFacePupContainer(meshPointsString, meshConfigString, meshLanguageString)
            val tierWrapper = FacePupManager.buildTierWrapper(facePupContainer)
            binding.facePupTierControlView.apply {
                visible()
                updateContainer(tierWrapper)
                setControlListener(object : FacePupControlTierListener {
                    override fun onSeekBarScroll(item: FacePupTierWrapper.SeekBarItem, scale: Float) {
                        val (fv, sv) = FacePupManager.scaleToFacePupParamValue(scale)
                        DevAvatarManagerRepository.getCurrentAvatarInfo()?.avatar?.let {
                            item.controlItem.controlFirst {
                                it.deformation.setDeformation(this, fv)
                            }
                            item.controlItem.controlSecond {
                                it.deformation.setDeformation(this, sv)
                            }
                        }
                    }

                    override fun onResetClick() {
                        DevAvatarManagerRepository.getCurrentAvatarInfo()?.avatar?.let { avatar ->
                            avatar.deformation.getDeformationCache().forEach {
                                avatar.deformation.setDeformation(it.key, 0f)
                            }
                        }
                    }
                })
            }
        }
        binding.setBundleThenSave.setOnClickListener {
            val avatarInfo = DevAvatarManagerRepository.getCurrentAvatarInfo() ?: return@setOnClickListener
            val testPath = "GAssets/AsiaFemale/cloth/lower_inner/shorts/shorts005.bundle" //????????????????????????????????????
            val path = FuDevDataCenter.resourcePath.ossCustom(testPath)
            avatarInfo.avatar.getComponents().firstOrNull { it.path.contains("shorts")  }
                ?.let { it1 -> avatarInfo.avatar.removeComponent(it1) } //???????????????????????????????????????
            avatarInfo.avatar.addComponent(FUBundleData(path, fileId = testPath))
            val avatarJson = avatarInfo.avatar.getAvatarJson("test1") //?????????????????? json ????????????????????? shorts005 ?????????????????????
            FuLog.info(avatarJson.toString())
            FUSceneKit.getInstance().executeGLAction({ //??????????????? json ????????????????????????????????????????????????????????? GL ?????????
                val avatarJson = avatarInfo.avatar.getAvatarJson("test2")
                FuLog.info(avatarJson.toString())
            })

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
                    viewModel.initDefaultAvatar()
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
                }

                override fun onDrawFrameAfter() {

                }

                override fun onSurfaceDestroy() {
                    FuDevInitializeWrapper.releaseRenderKit()
                }
            })
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