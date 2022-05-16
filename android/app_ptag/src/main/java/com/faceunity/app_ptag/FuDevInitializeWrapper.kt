package com.faceunity.app_ptag

import android.content.Context
import com.faceunity.app_ptag.client_cloud.ClientCloudControlImpl
import com.faceunity.app_ptag.client_cloud.ClientCloudRepository
import com.faceunity.app_ptag.client_cloud.IClientCloudControl
import com.faceunity.app_ptag.compat.*
import com.faceunity.app_ptag.data_center.FuCloudResourceLoader
import com.faceunity.app_ptag.data_center.FuCloudResourcePath
import com.faceunity.app_ptag.util.DefaultAvatarHelper
import com.faceunity.core.avatar.model.Scene
import com.faceunity.core.entity.FUBundleData
import com.faceunity.core.entity.FUColorRGBData
import com.faceunity.core.faceunity.FURenderKit
import com.faceunity.core.faceunity.FURenderManager
import com.faceunity.core.faceunity.FUSceneKit
import com.faceunity.editor_ptag.data_center.FuDevDataCenter
import com.faceunity.editor_ptag.data_center.entity.FuResourceLoadMode
import com.faceunity.editor_ptag.store.DevDrawRepository
import com.faceunity.editor_ptag.util.FuLog
import com.faceunity.fupta.cloud_download.CloudRepository
import com.faceunity.fupta.cloud_download.CloudResourceManager
import com.faceunity.fupta.cloud_download.entity.CloudConfig
import com.faceunity.fupta.cloud_download.entity.CloudMode
import com.faceunity.toolbox.utils.FULogger

/**
 * SDK 所有初始化相关的代码聚合
 */
object FuDevInitializeWrapper {
    var clientCloudControlImpl: IClientCloudControl? = null
    var defaultAvatarHelper: DefaultAvatarHelper? = null

    val defaultCloudConfig by lazy {
        CloudConfig(
            domain = "https://apigateway.faceunity.com",
            apiKey = "7pMuFupEyFASgLsRhzsHvm", //联系相芯相关人员获取
            apiSecret = "rRMf9fMo5aArPB4PDc3m9X", //联系相芯相关人员获取
            projectId = "c01bdf3a-5592-4e58-8603-7af1003c8de7", //联系相芯相关人员获取
            mode = CloudMode.Release
        )
    }

    fun initSDK(context: Context) {
        initCompat(context)
        initCloudControl(context)
        initAppAssets(context, true)

        FURenderManager.setCoreDebug(FULogger.LogLevel.WARN) //图形算法相关日志
        FURenderManager.setKitDebug(FULogger.LogLevel.DEBUG) //客户端核心库相关日志
        FURenderManager.registerFURender(getAuthPack())
        FUSceneKit.getInstance().bindControlBundle(FUBundleData(FuDevDataCenter.resourcePath.sdkControllerCppBundle()))

        initScene()
        initBusiness(context)
    }

    fun getAuthPack(): ByteArray {
        return authpack.A() //联系相芯相关人员获取
    }

    /**
     * 使用相芯服务器下载资源的方式运行 Demo
     */
    private fun initCloudResourceConfig(context: Context) {
        FuDevDataCenter.resourceLoadMode = FuResourceLoadMode.Cloud
        FuDevDataCenter.resourceLoader = FuCloudResourceLoader(context)
        FuDevDataCenter.resourcePath = FuCloudResourcePath(context)
    }


    /**
     * 一些第三方依赖相关的。必须提供对应实现。
     */
    private fun initCompat(context: Context){
        FuDevDataCenter.initResourceParser(GsonResourceParserImpl()) //初始化 JSON 解析器。必要。如果使用其他 JSON 解析器可实现自己的接口。
    }

    /**
     * @param isCloud 为 true 则客户端的配置资源（即 Demo 中"app_ptag/src/main/assets/AppAssets"路径下的内容）走云端下发。
     */
    private fun initAppAssets(context: Context, isCloud: Boolean = true) {
        if (isCloud) {
            clientCloudControlImpl = ClientCloudControlImpl(
                repository = ClientCloudRepository(
                    fuLog = FuLog,
                    request = OkHttpNetRequest().apply {
                        fuLog = FuLog
                    },
                    domain = "https://fu-ptag-assets.oss-cn-hangzhou.aliyuncs.com",
                    mode = defaultCloudConfig.mode.text
                ),
                fuJsonParser = GsonJsonParser(),
                fuStorageField = SPStorageFieldImpl(context, "CloudConfig"),
                zip = FuZipImpl()
            )
            FuDevDataCenter.resourcePath.appAssets = context.getExternalFilesDir(null)?.path + "/download/" + "AppAssets"
        } else {
            FuDevDataCenter.resourcePath.appAssets = "AppAssets"
        }
    }

    /**
     * 设置云端解析相关接口。
     * 如果不使用相芯提供的云服务。则需将 FuDevDataCenter.resourceLoadMode 设为 Local 或者 CustomCloud。并实现对应的自定义 FuDevDataCenter.resourceLoader、FuDevDataCenter.resourcePath。
     */
    private fun initCloudControl(context: Context) {
        initCloudResourceConfig(context)

        val cloudRepository = CloudRepository(
            fuLog = FuLog,
            request = OkHttpNetRequest().apply {
                fuLog = FuLog
            },
            cloudConfig = defaultCloudConfig
        )
        val cloudResourceManager = CloudResourceManager(
            checkInterface = CloudCheckInterface(),
            saveInterface = CloudSaveInterface(context),
            fuLog = FuLog
        )
        val jsonParser = GsonJsonParser()
        FuDevDataCenter.initCloudControl(
            cloudRepository,
            cloudResourceManager,
            jsonParser,
            FuDownloadHelperImpl()
        )
        FuDevDataCenter.initCloudPTAControl(
            repository = cloudRepository,
            jsonParser = jsonParser
        )
    }

    /**
     * 一些业务功能。根据实际情况选择。
     */
    private fun initBusiness(context: Context) {
        defaultAvatarHelper = DefaultAvatarHelper(SPStorageFieldImpl(context, "DefaultAvatarSet"))
    }

    fun initRenderKit() {
        FURenderKit.getInstance().apply {
            setMultiSamples(4) //抗锯齿
        }
    }

    /**
     * 统一对 Scene 做一些业务形预处理
     */
    fun initScene(sceneBackgroundColor: Int? = null) {
        DevDrawRepository.setSceneEvent(object : DevDrawRepository.SceneEvent {
            override fun onSceneCreated(scene: Scene) {
                scene.setEnableShadow(false) // 开启阴影
                scene.businessSupport.setEnableGroundReflection(false) // 开启地面反射
                sceneBackgroundColor?.let { scene.setBackgroundColor(FUColorRGBData(it))  }
            }
        })
    }

    fun releaseRenderKit() {
        FURenderKit.getInstance().release()
    }
}