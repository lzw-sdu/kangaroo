package com.faceunity.app_ptag.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.faceunity.editor_ptag.business.cloud.CloudException
import com.faceunity.editor_ptag.business.cloud.ICloudControl
import com.faceunity.editor_ptag.business.pta.IPhotoToAvatarControl
import com.faceunity.editor_ptag.business.pta.callback.InitPTAResourceCallback
import com.faceunity.editor_ptag.business.pta.callback.RequestPhotoToAvatarCallback
import com.faceunity.editor_ptag.business.pta.config.InitPTAResourceParams
import com.faceunity.editor_ptag.business.pta.config.RequestPhotoToAvatarParams
import com.faceunity.editor_ptag.data_center.FuDevDataCenter
import com.faceunity.editor_ptag.store.DevAvatarManagerRepository
import com.faceunity.editor_ptag.util.FuLog
import com.faceunity.pta.pta_core.model.AvatarInfo
import java.io.File

/**
 * 照片生成形象的 ViewModel
 */
class FuBuildAvatarViewModel : ViewModel() {

    private val ptaControl: IPhotoToAvatarControl
        get() = FuDevDataCenter.ptaControl!!

    private var cachePhotoPath: String? = null
    private val _photoPathLiveData = MutableLiveData<String>()
    val photoPathLiveData: LiveData<String> get() = _photoPathLiveData

    private val _isInitPTASuccessLiveData = MutableLiveData<Boolean>()
    val isInitPTASuccessLiveData: LiveData<Boolean> get() = _isInitPTASuccessLiveData

    private val _isBuildSuccessLiveData = MutableLiveData<Boolean>()
    val isBuildSuccessLiveData: LiveData<Boolean> get() = _isBuildSuccessLiveData

    /**
     * 初始化 PTA 需要的资源。资源路径由 [FuDevDataCenter.resourcePath] 配置，需要其中的 [ossBuildAvatarInfoList()] 与 [binBuildAvatarFileList()]
     * 初始化信息会通知 [isInitPTASuccessLiveData]。
     */
    fun initPTAResource(auth: ByteArray) {
        ptaControl.initPTAResource(
            params = InitPTAResourceParams(authPack = auth),
            callback = object : InitPTAResourceCallback {
                override fun onInitPTAResource(isSuccess: Boolean, message: String?) {
                    _isInitPTASuccessLiveData.postValue(isSuccess)
                    if (!isSuccess) {
                        FuLog.error("初始化 Photo To Avatar 异常")
                    } else {
                        FuLog.info("初始化 Photo To Avatar 成功 ")
                    }
                }
            }
        )
    }

    @Deprecated("请使用 initPTAResource")
    fun loadPTAResource(auth: ByteArray) {
        initPTAResource(auth)
    }

    /**
     * 配置形象生成需要使用的图片文件
     * 成功后会通知 [photoPathLiveData]
     */
    fun cachePhoto(photoFile: File) {
        cachePhotoPath = photoFile.path
        _photoPathLiveData.postValue(cachePhotoPath!!)
    }

    /**
     * 清除在 [cachePhoto] 中配置的图片文件
     */
    fun clearCachePhoto() {
        cachePhotoPath = null
    }

    /**
     * 使用 [cachePhotoPath] 配置的图片，和传入的配置信息 [isMale] 进行照片生成形象。
     */
    fun requestBuildAvatar(isMale: Boolean) {
        ptaControl.requestPhotoToAvatar(
            params = RequestPhotoToAvatarParams(
                gender = if (isMale) RequestPhotoToAvatarParams.Gender.Male else RequestPhotoToAvatarParams.Gender.Female,
                photoFile = File(cachePhotoPath!!),
            ),
            callback = object : RequestPhotoToAvatarCallback {
                override fun onSuccess(avatarJson: String) {
                    FuLog.info(avatarJson)
                }

                override fun onSave(avatarInfo: AvatarInfo) {
                    DevAvatarManagerRepository.addAvatarAndSwitch(avatarInfo)
                    _isBuildSuccessLiveData.postValue(true)
                }

                override fun onOperationFailed(message: String) {
                    FuLog.error(message)
                    _isBuildSuccessLiveData.postValue(false)
                }

                override fun onCloudFailed(cloudException: CloudException) {
                    FuLog.error(cloudException.toString())
                    _isBuildSuccessLiveData.postValue(false)
                }

                override fun onFailed(ex: Exception) {
                    FuLog.error("Build Avatar error", ex)
                    _isBuildSuccessLiveData.postValue(false)
                }
            }
        )

    }

    /**
     * 释放形象生成库的资源
     */
    fun releasePTAResource() {
        ptaControl.releasePTAResource()
    }
}