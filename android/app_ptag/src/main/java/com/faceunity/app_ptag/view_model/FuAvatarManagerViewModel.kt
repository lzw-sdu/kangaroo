package com.faceunity.app_ptag.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.faceunity.app_ptag.BuildConfig
import com.faceunity.app_ptag.FuDevInitializeWrapper
import com.faceunity.app_ptag.client_cloud.entity.UpdateAppAssetsCallback
import com.faceunity.app_ptag.util.CheckResourceHelper
import com.faceunity.app_ptag.util.SingleLiveEvent
import com.faceunity.editor_ptag.cache.FuCacheResource
import com.faceunity.editor_ptag.business.cloud.CloudException
import com.faceunity.editor_ptag.business.cloud.ICloudControl
import com.faceunity.editor_ptag.data.DownloadStatus
import com.faceunity.editor_ptag.data_center.FuDevDataCenter
import com.faceunity.editor_ptag.data_center.entity.FuResourceLoadMode
import com.faceunity.editor_ptag.store.DevAvatarManagerRepository
import com.faceunity.editor_ptag.store.DevDrawRepository
import com.faceunity.editor_ptag.store.DevSceneManagerRepository
import com.faceunity.editor_ptag.util.FuLog
import com.faceunity.editor_ptag.util.safeRun
import com.faceunity.editor_ptag.business.cloud.entity.FuLoadAvatarInfo
import com.faceunity.app_ptag.view_model.entity.FuRequestErrorInfo
import com.faceunity.app_ptag.view_model.entity.FuUploadAvatarState
import com.faceunity.editor_ptag.business.cloud.callback.*
import com.faceunity.editor_ptag.parser.DevAvatarTranslator
import com.faceunity.fupta.avatar_data.entity.resource.ProjectResource
import com.faceunity.pta.pta_core.model.AvatarInfo
import com.faceunity.toolbox.utils.FUDateUtils
import java.io.File
import java.lang.Exception

/**
 * Avatar 管理的辅助 ViewModel。
 */
class FuAvatarManagerViewModel : ViewModel() {

    private val cloudControl: ICloudControl
        get() = FuDevDataCenter.cloudControl!!

    private val _avatarCollectionLiveData = SingleLiveEvent<Collection<AvatarInfo>>()
    val avatarCollectionLiveData: LiveData<Collection<AvatarInfo>> get() = _avatarCollectionLiveData

    private val _avatarSelectLiveData = SingleLiveEvent<String>()
    val avatarSelectLiveData: LiveData<String> get() = _avatarSelectLiveData

    private val _isSaveAvatarSuccessLiveData = SingleLiveEvent<Boolean>()
    val isSaveAvatarSuccessLiveData: LiveData<Boolean> get() = _isSaveAvatarSuccessLiveData

    private val _uploadAvatarStateLiveData = SingleLiveEvent<FuUploadAvatarState>()
    val uploadAvatarStateLiveData: LiveData<FuUploadAvatarState> get() = _uploadAvatarStateLiveData

    private val _downloadStatusLiveData = SingleLiveEvent<FuLoadAvatarInfo>()
    val downloadStatusLiveData: LiveData<FuLoadAvatarInfo> get() = _downloadStatusLiveData

    private val _requestErrorInfoLiveData = SingleLiveEvent<FuRequestErrorInfo>()
    val requestErrorInfoLiveData: LiveData<FuRequestErrorInfo> get() = _requestErrorInfoLiveData
    
    private val _cloudAvatarPrepareLiveData = SingleLiveEvent<String>()
    val cloudAvatarPrepareLiveData: LiveData<String> get() = _cloudAvatarPrepareLiveData

    //region 便携调用

    /**
     * 先加载本地数据，如果本地已存在则直接显示。再一并加载网络请求
     * @param isAutoDraw 是否自动渲染。true 则下载完成后会自动渲染， false 则会下载完成后通知 [cloudAvatarPrepareLiveData]
     */
    fun autoInitDefaultAvatar(
        isAutoDraw: Boolean = true
    ) {
        val loadAvatarFun = loadAvatarFun@ {
            val loadAvatarId = getDefaultLoadAvatarId()
            if (loadAvatarId == null) {
                FuLog.error("autoInitDefaultAvatar 找不到可以加载的 Avatar")
                return@loadAvatarFun
            }
            loadCloudAvatarById(loadAvatarId, isAutoDraw)
        }
        when(FuDevDataCenter.resourceLoadMode) {
            FuResourceLoadMode.Cloud -> {
                if (checkBaseResourceReady()) {
                    if (DevAvatarManagerRepository.getCurrentAvatarInfo() == null) {    //如果当前没有 Avatar 形象，则加载第一个
                        DevAvatarManagerRepository.switchFirstAvatarAndGet()
                    }
                    safeRun {
                        initDefaultAvatar()
                    }
                    initCloudAvatar(loadAvatarFun)
                } else {
                    initCloudAvatar(loadAvatarFun)
                }
            }
            FuResourceLoadMode.Local, FuResourceLoadMode.CustomCloud -> {
                initDefaultAvatar()
            }
        }

    }

    fun autoSwitchAvatar(avatarId: String) {
        when(FuDevDataCenter.resourceLoadMode) {
            FuResourceLoadMode.Cloud -> {
                loadCloudAvatarById(avatarId)
            }
            FuResourceLoadMode.Local, FuResourceLoadMode.CustomCloud -> {
                switchAvatarById(avatarId)
            }
        }
        
    }

    fun autoRemoveAvatar(avatarId: String) {
        removeAvatarById(avatarId)
    }

    fun autoSaveCurrentAvatar() {
        saveCurrentAvatar()
    }

    //endregion 便携调用

    //region 形象管理

    /**
     * 加载形象列表，完成后会通知 [fuAvatarContainerLiveData]
     */
    fun loadAvatarContainer() {
        DevAvatarManagerRepository.loadAvatar()
        syncAvatarContainer()
    }

    private fun loadAvatarContainerNotRepeat() {
        if (DevAvatarManagerRepository.getAvatarListSize() == 0) {
            loadAvatarContainer()
        }
        syncAvatarContainer()
    }

    /**
     * 通过 [avatarId] 找到符合条件的 AvatarInfo 后，将其渲染。需要所有资源已准备好。
     * 如果不确定资源是否准备好，可调用 CheckResourceHelper.checkAvatarAndScene(avatarInfo, sceneInfo) 检查。
     * @param isCheckIntegrity 是否检查资源准备的完整性。true 则会进行 Log 提示，需要下载则不再渲染；false 则不检查直接渲染，遇到资源不全则可能显示异常。
     */
    @JvmOverloads
    fun switchAvatarById(avatarId: String, isCheckIntegrity: Boolean = true) {
        loadAvatarContainerNotRepeat()
        val avatarInfo = DevAvatarManagerRepository.switchAvatarAndGet(avatarId)
        if (avatarInfo == null) {
            FuLog.error("switchAvatarById $avatarId not found.")
            return
        }
        DevSceneManagerRepository.initSceneConfigNotRepeat()
        val sceneInfo = DevSceneManagerRepository.filterGenderDefaultSceneFirst(avatarInfo.gender())
        if (sceneInfo == null) {
            FuLog.error("符合条件的 SceneInfo 找不到，无法加载 Scene 与 Avatar")
            return
        }

        if (isCheckIntegrity) {
            val result = CheckResourceHelper.checkAvatarAndScene(avatarInfo, sceneInfo)
            FuLog.info("switchAvatarById $avatarId check result: $result")
            if (result.needDownloadFileIdList.isNotEmpty()) {
                FuLog.error("${result.needDownloadFileIdList} 需要下载，无法加载 Avatar $avatarId")
                return
            }
            if (result.needUpdateFileIdList.isNotEmpty()) {
                FuLog.warn("${result.needUpdateFileIdList} 需要更新")
            }
        }

        DevDrawRepository.loadSceneAndAvatar(sceneInfo, avatarInfo)

        notifySwitchAvatar(avatarId)
    }

    /**
     * 移除 [avatarId] Avatar
     */
    fun removeAvatarById(avatarId: String) {
        val avatarInfo = DevAvatarManagerRepository.getAvatarInfo(avatarId) ?: return
        val currentAvatarInfo = DevAvatarManagerRepository.getCurrentAvatarInfo()
        DevAvatarManagerRepository.deleteAvatar(avatarInfo)
        if (currentAvatarInfo == avatarInfo) { //如果选中当前被删除的 Avatar，则选中上一个
            val switchAvatarId = DevAvatarManagerRepository.mapAvatar { it.id }.lastOrNull()
            if (switchAvatarId == null) {
                syncAvatarContainer()
                return
            }
            switchAvatarById(switchAvatarId)
        }
        syncAvatarContainer()

    }

    //endregion 形象管理

    /**
     * 得到当前应该加载的 AvatarId，需要对应 Avatar 文件夹已存在 avatar.json。
     */
    fun getDefaultLoadAvatarId(): String? {
        var currentAvatarId = DevAvatarManagerRepository.getCurrentAvatarId()
        if (currentAvatarId == null) {
            loadAvatarContainerNotRepeat()
            currentAvatarId = DevAvatarManagerRepository.getFirstAvatarId()
        }
        if (currentAvatarId == null) {
            val originAvatarList = DevAvatarManagerRepository.loadOriginAvatarList()
            currentAvatarId = originAvatarList.firstOrNull { it.avatarJson != null }?.id
        }
        return currentAvatarId
    }


    /**
     * 加载当前选择的 Avatar 形象（默认为第一个）和其对应 Scene。
     * 需要所有资源已准备好
     */
    fun initDefaultAvatar() {
        FuCacheResource.init(FuDevDataCenter.resourceParser,  FuDevDataCenter.resourcePath, FuDevDataCenter.resourceLoader)

        val defaultAvatarId = getDefaultLoadAvatarId()
        if (defaultAvatarId == null) {
            FuLog.error("找不到 $defaultAvatarId Avatar 数据，无法加载")
            return
        }
        switchAvatarById(defaultAvatarId)

    }

    //region 云端下载



    /**
     * 检查几个必要资源是否已准备好。
     * @return true：必要资源已准备好，可以正确执行渲染。
     */
    private fun checkBaseResourceReady() : Boolean {
        FuCacheResource.init(FuDevDataCenter.resourceParser,  FuDevDataCenter.resourcePath, FuDevDataCenter.resourceLoader)
        if (FuCacheResource.itemListText == null || FuCacheResource.controllerConfigBundle == null) return false
        val existAvatarList = DevAvatarManagerRepository.loadOriginAvatarList().filter { it.avatarJson != null }
        if (existAvatarList.size == 0) return false
        return true
    }

    /**
     * 初始化云端所需的信息。
     * 失败则会通知 [requestErrorInfoLiveData]
     * @param onSuccess 当初始化成功时的回调。通常在里面加载某个 Avatar。
     */
    fun initCloudAvatar(
        onSuccess: (() -> Unit)? = null,
    ) {
        if (FuDevDataCenter.fastCheckBlock()) {
            FuLog.error("当前 FuResourceLoadMode 非 Cloud，无法执行此操作")
            return
        }
        fun loadCloud() {
            FuCacheResource.init(FuDevDataCenter.resourceParser,  FuDevDataCenter.resourcePath, FuDevDataCenter.resourceLoader)
            cloudControl.initCloud(callback = object : InitCloudCallback {
                override fun onSuccess(projectResource: ProjectResource) {
                    checkRepeatedDefaultAvatar(projectResource)

                    cloudControl.loadAvatarList(avatarIdList = projectResource.avatar_list.map { it.avatar_id }, callback = object : LoadAvatarListCallback {
                        override fun onSuccess(avatarList: List<String>) {
                            onSuccess?.invoke()
                        }

                        override fun onFailed(failedAvatarIdList: List<String>, failedExceptionList: List<CloudException>) {
                            notifyCloudException(failedExceptionList.first())
                        }
                    })
                }

                override fun onFailed(cloudException: CloudException) {
                    notifyCloudException(cloudException)
                }
            })
        }
        if (FuDevInitializeWrapper.clientCloudControlImpl == null) {
            loadCloud()
            return
        }
        FuDevInitializeWrapper.clientCloudControlImpl!!.updateAppAssets(
            "1.2.3",
            FuDevDataCenter.resourcePath.appAssets.let { File(it) },
            callback = object : UpdateAppAssetsCallback {
                override fun onSuccess() {
                    FuLog.info("App资源下载完成")
                    loadCloud()
                }

                override fun onFastReturn() {
                    FuLog.info("已存在，跳过")
                    loadCloud()
                }

                override fun onCloudFailed(cloudException: CloudException) {
                    FuLog.error(cloudException.toString())
                    notifyCloudException(cloudException)
                }

                override fun onFailed(exception: Exception) {
                    FuLog.error("updateAppAssets", exception)
                    _requestErrorInfoLiveData.postValue(FuRequestErrorInfo(FuRequestErrorInfo.Type.Error(exception.toString())))

                }
            })
    }

    /**
     * 检查是否需要下载需要的资源，是则自动
     * @param isAutoDraw 是否自动渲染。true 则下载完成后会自动渲染， false 则会下载完成后通知 [cloudAvatarPrepareLiveData]
     */
    fun loadCloudAvatarById(
        avatarId: String,
        isAutoDraw: Boolean = true
    ) {
        if (FuDevDataCenter.fastCheckBlock()) {
            FuLog.error("当前 FuResourceLoadMode 非 Cloud，无法执行此操作")
            return
        }
        cloudControl.downloadAvatarResource(avatarId = avatarId, callback = object : DownloadAvatarResourceCallback {
            override fun onSuccess(avatarId: String) {
                if (isAutoDraw) {
                    switchAvatarById(avatarId)
                } else {
                    _cloudAvatarPrepareLiveData.postValue(avatarId)
                }
            }

            override fun onDownloading(downloadStatus: DownloadStatus) {
                _downloadStatusLiveData.postValue(FuLoadAvatarInfo(avatarId, downloadStatus))
            }

            override fun onOperationFailed(message: String) {
                FuLog.error(message)
            }

            override fun onCloudFailed(cloudException: CloudException) {
                notifyCloudException(cloudException)
            }
        })
    }


    //endregion 云端下载


    //region 业务功能

    /**
     * @param isCopy 是否以创建一个新的 Avatar 的方式保存。
     */
    fun saveCurrentAvatar() {
        val isCopy: Boolean = false
        val avatarInfo = DevAvatarManagerRepository.getCurrentAvatarInfo()
        if (avatarInfo == null) {
            FuLog.warn("当前 AvatarInfo 为空")
            _isSaveAvatarSuccessLiveData.postValue(false)
            return
        }
        val saveAvatarInfo = if(isCopy) {
            val avatarId = FUDateUtils.getDateTimeString()
            val avatarJsonContent = avatarInfo.avatar.getAvatarJson(avatarId)
            if (avatarJsonContent != null && FuCacheResource.controllerConfigBundle != null) {
                DevAvatarManagerRepository.writeAvatarFile(avatarId, avatarJsonContent, avatarInfo.avatarLogo)
                DevAvatarManagerRepository.createAvatarInfo(avatarId, DevAvatarTranslator(), FuCacheResource.controllerConfigBundle!!)
            } else {
                null
            }
            } else {
            avatarInfo
        }
        if (saveAvatarInfo == null) {
            FuLog.warn("复制 Avatar 失败")
            _isSaveAvatarSuccessLiveData.postValue(false)
            return
        }
        if (isCopy) {
            DevAvatarManagerRepository.addAvatarAndSwitch(saveAvatarInfo)
        }
        val isSuccess = DevAvatarManagerRepository.saveAvatarInfo(saveAvatarInfo, saveAvatarInfo.id)
        _isSaveAvatarSuccessLiveData.postValue(isSuccess)
    }

    fun uploadCurrentAvatar() {
        if (FuDevDataCenter.fastCheckBlock()) {
            FuLog.error("当前 FuResourceLoadMode 非 Cloud，无法执行此操作")
            return
        }
        val avatarInfo = DevAvatarManagerRepository.getCurrentAvatarInfo()
        if (avatarInfo == null) {
            _uploadAvatarStateLiveData.postValue(FuUploadAvatarState.Error("当前 AvatarInfo 为空"))
            return
        }
        val isSaveSuccess = DevAvatarManagerRepository.saveAvatarInfo(avatarInfo, avatarInfo.id)
        if (!isSaveSuccess) {
            _uploadAvatarStateLiveData.postValue(FuUploadAvatarState.Error("当前 AvatarInfo 保存失败"))
            return
        }
        val content = avatarInfo.avatar.getAvatarJson(avatarInfo.id) ?: ""
        cloudControl.uploadAvatar(content = content, callback = object : UploadAvatarCallback {
            override fun onSuccess(avatarId: String) {
                avatarInfo.customSavePath(File(File(avatarInfo.avatarDir!!).parent, avatarId).path)
                _uploadAvatarStateLiveData.postValue(FuUploadAvatarState.Success(avatarId))
            }

            override fun onCloudFailed(cloudException: CloudException) {
                notifyCloudException(cloudException)
            }
        })
    }

    private fun checkRepeatedDefaultAvatar(projectResource: ProjectResource) {
        val defaultAvatarHelper = FuDevInitializeWrapper.defaultAvatarHelper
        if (defaultAvatarHelper == null) {
            return
        }
        defaultAvatarHelper.updateProjectJson(projectResource) {
            it.forEach {
                autoRemoveAvatar(it)
            }
            true
        }
    }

    //endregion 业务功能


    //region LiveData 通知

    private fun notifyCloudException(cloudException: CloudException) {
        if (FuDevDataCenter.fastCheckBlock()) {
            FuLog.error("当前 FuResourceLoadMode 非 Cloud，无法执行此操作")
            return
        }
        FuLog.error("CloudResDownloadManager.onError: ${cloudException}")
        when(cloudException) {
            is CloudException.TokenException -> {
                cloudControl.initCloud()
                _requestErrorInfoLiveData.postValue(FuRequestErrorInfo(FuRequestErrorInfo.Type.Retry("Token 过期，请重试")))
            }
            is CloudException.NetworkException -> {
                _requestErrorInfoLiveData.postValue(FuRequestErrorInfo(FuRequestErrorInfo.Type.Retry(cloudException.desc + cloudException.exception?.message)))
            }
            is CloudException.FormatException -> {
                _requestErrorInfoLiveData.postValue(FuRequestErrorInfo(FuRequestErrorInfo.Type.Error(cloudException.desc ?: cloudException.exception?.message ?: "")))
            }
            else -> {
                _requestErrorInfoLiveData.postValue(FuRequestErrorInfo(FuRequestErrorInfo.Type.Error(cloudException.toString())))
            }
        }
    }

    /**
     * 通知 Avatar 切换时的 LiveData
     */
    private fun notifySwitchAvatar(avatarId: String) {
        _avatarSelectLiveData.postValue(avatarId)
        val selectIdList = mutableListOf<String>()
        DevAvatarManagerRepository.getCurrentAvatarId()?.let {
            selectIdList.add(it)
        }
    }


    /**
     * 构建 AvatarList 的容器，用于页面渲染
     */
    private fun syncAvatarContainer() {
        _avatarCollectionLiveData.postValue(DevAvatarManagerRepository.mapAvatar {it})
        _avatarSelectLiveData.postValue(DevAvatarManagerRepository.getCurrentAvatarId())
    }

    //endregion LiveData 通知


}