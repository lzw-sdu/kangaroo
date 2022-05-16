package com.faceunity.app_ptag.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.faceunity.core.avatar.model.Avatar
import com.faceunity.core.avatar.model.Scene
import com.faceunity.editor_ptag.business.cloud.CloudException
import com.faceunity.editor_ptag.business.cloud.ICloudControl
import com.faceunity.editor_ptag.business.cloud.callback.DownloadItemResourceCallback
import com.faceunity.editor_ptag.data.DownloadStatus
import com.faceunity.app_ptag.ui.edit.entity.GenderProFilter
import com.faceunity.app_ptag.ui.edit.entity.StyleProFilter
import com.faceunity.app_ptag.ui.edit.entity.control.ControlModel
import com.faceunity.app_ptag.ui.edit.parser.ControlModelParser
import com.faceunity.editor_ptag.data_center.FuDevDataCenter
import com.faceunity.editor_ptag.data_center.FuEditHistoryManager
import com.faceunity.editor_ptag.data_center.entity.FuResourceLoadMode
import com.faceunity.app_ptag.ui.edit.parser.EditConfigParser
import com.faceunity.editor_ptag.store.DevAvatarManagerRepository
import com.faceunity.editor_ptag.store.DevDrawRepository
import com.faceunity.editor_ptag.store.DevEditRepository
import com.faceunity.editor_ptag.util.FuLog
import com.faceunity.editor_ptag.view_model.entity.FuSingleDownloadInfo
import com.faceunity.fupta.avatar_data.entity.resource.*
import com.faceunity.fupta.facepup.entity.FacePupTierWrapper
import com.faceunity.fupta.facepup.entity.tier.FacepupSlider
import com.faceunity.pta.pta_core.model.AvatarInfo
import com.faceunity.pta.pta_core.repository.EditRepository
import com.faceunity.support.entity.FUAEColorItem
import com.faceunity.support.entity.FUAEItem
import com.faceunity.support.entity.FUAEMinorCategory
import com.faceunity.support.entity.FUAEModel
import com.faceunity.support.filter.AssociateFilter
import com.faceunity.support.filter.FilterGroup
import com.faceunity.support.filter.GenderFilter
import com.faceunity.support.filter.StyleFilter

/**
 * 编辑组件的快速开发 ViewModel
 */
class FuEditViewModel: ViewModel() {

    private val cloudControl: ICloudControl
        get() = FuDevDataCenter.cloudControl!!

    private val _controlModelLiveData = MutableLiveData<ControlModel>()
    val controlModelLiveData: LiveData<ControlModel> get() = _controlModelLiveData

    private val _selectItemLiveData = MutableLiveData<List<String>>()
    val selectItemLiveData: LiveData<List<String>> get() = _selectItemLiveData

    private val filterGroup = FilterGroup()
    private val _filterGroupLiveData = MutableLiveData<FilterGroup>()
    val filterGroupLiveData: LiveData<FilterGroup> get() = _filterGroupLiveData

    private val _downloadInfoLiveData = MutableLiveData<FuSingleDownloadInfo>()
    val downloadInfoLiveData: LiveData<FuSingleDownloadInfo> get() = _downloadInfoLiveData

    private val _currentAvatarLiveData = MutableLiveData<AvatarInfo>()
    val currentAvatarLiveData: LiveData<AvatarInfo> get() = _currentAvatarLiveData

    private var defaultAvatar: Avatar? = null

    private var controlModel: ControlModel? = null



    //region ControlModel 相关（由 DevEditRepository 移出）

    /**
     * 初始化并构造一个 [FUAEModel]，然后需要通过 [getCloneFUAvatarEditModel] 获取。
     */
    fun initFUAvatarEditModel(builder: EditRepository.FUAvatarEditModelBuilder) : FUAEModel{
        return DevEditRepository.buildEditModel(builder) as FUAEModel
    }

    fun initControlModel() {
        val builder = object : EditRepository.FUAvatarEditModelBuilder {
            override fun buildEditModel(
                editConfig: EditConfigResource,
                editCustomConfig: EditCustomConfigResource,
                editItemList: EditItemListResource,
                editCustomItemList: EditCustomItemListResource,
                editColorList: EditColorListResource,
                itemList: ItemListResource
            ): FUAEModel {
                val editConfigParser = EditConfigParser(object : EditConfigParser.TempResourcePathTran {
                    override fun appCustom(path: String): String {
                        return FuDevDataCenter.resourcePath.appCustom(path)
                    }

                    override fun ossCustom(path: String): String {
                        return FuDevDataCenter.resourcePath.ossCustom(path)
                    }
                })
                val cacheFUAEModel = editConfigParser.buildFUAEModel(editConfig, editCustomConfig, editItemList, editCustomItemList, editColorList, itemList)
                return cacheFUAEModel
            }
        }
        val fuaeModel = initFUAvatarEditModel(builder)
        controlModel = ControlModelParser.buildControlModel(fuaeModel)
    }


    /**
     * 获得一个 [FUAEModel],需要提前调用 [initFUAvatarEditModel] 方法。
     */
    @Deprecated("please use initFUAvatarEditModel() and parse result.")
    fun getCloneFUAvatarEditModel(): FUAEModel {
        throw UnsupportedOperationException("please use initFUAvatarEditModel() and parse result.")
    }

    @Deprecated("use updateColor(avatar: Avatar, fuColorItem: FUAEColorItem)")
    fun updateColor(scene: Scene, fuColorItem: FUAEColorItem) {
        val avatar = scene.getAvatars().firstOrNull()
        if (avatar == null) {
            FuLog.error("$scene has not Avatar,return")
            return
        }
        updateColor(avatar, fuColorItem)
    }

    /**
     * 使用 [fuColorItem] 更新 [avatar] 的颜色
     */
    fun updateColor(avatar: Avatar, fuColorItem: FUAEColorItem) {
        val buildFUColorRGBData = fuColorItem.buildFUColorRGBData()
        DevEditRepository.setColor(avatar, fuColorItem.key, buildFUColorRGBData)
    }

    @Deprecated("use updateItem(avatar: Avatar, fuItem: FUAEItem)")
    fun updateItem(scene: Scene, fuItem: FUAEItem) {
        updateItemList(scene, listOf(fuItem))
    }

    @Deprecated("use updateItem(avatar: Avatar, fuItem: List<FUAEItem>)")
    fun updateItemList(scene: Scene, fuItem: List<FUAEItem>) {
        val avatar = scene.getAvatars().firstOrNull()
        if (avatar == null) {
            FuLog.error("$scene has not Avatar,return")
            return
        }
        updateItem(avatar, fuItem)
    }

    /**
     * 使用 [fuItem] 更新 [avatar] 的资源
     */
    fun updateItem(avatar: Avatar, fuItem: FUAEItem) {
        updateItem(avatar, listOf(fuItem))
    }

    /**
     * 使用多个 [fuItem] 同时更新 [avatar] 的资源
     */
    fun updateItem(avatar: Avatar, fuItem: List<FUAEItem>) {
        val fuEditBundleItemList = fuItem.map { it.fuEditBundleItem }.filterNotNull()
        val fileIDList = mutableListOf<String>()
        fuEditBundleItemList.forEach {
            fileIDList.addAll(it.getFullFileIDList())
        }
        DevEditRepository.setItemList(avatar, fileIDList)
    }


    //endregion ControlModel 相关



    //region 便携调用

    /**
     * 点击 Bundle Item 后触发对应渲染事件
     */
    fun autoSetItem(fuaeItem: FUAEItem) {
        when(FuDevDataCenter.resourceLoadMode) {
            FuResourceLoadMode.Local, FuResourceLoadMode.CustomCloud -> {
                setItem(fuaeItem)
            }
            FuResourceLoadMode.Cloud -> {
                loadCloudItem(fuaeItem)
            }
        }
    }

    /**
     * 点击 Color Item 后触发对应渲染事件
     */
    fun autoSetColorItem(fuColorItem: FUAEColorItem) {
        val avatar = loadControlAvatar() ?: return
        updateColor(avatar, fuColorItem)
        pushHistory()
    }

    //endregion 编写调用

    fun loadControlAvatarInfo(): AvatarInfo? {
        return DevAvatarManagerRepository.getCurrentAvatarInfo()
    }

    fun loadControlAvatar(): Avatar? {
        return loadControlAvatarInfo()?.avatar
    }

    /**
     * 申请加载 [ControlModel] （编辑组件的业务数据结构）信息。
     * 完成后会通过 [controlModelLiveData] 通知。
     */
    fun requestEditorData() {
        DevEditRepository.initEditNeedModel()
        initControlModel()
//        syncAvatarEditStatus()
        notifyControlModelLiveData()
    }

    fun syncAvatarEditStatus() {
        val avatarInfo = loadControlAvatarInfo()
        if (avatarInfo == null) {
            return
        }
        defaultAvatar = avatarInfo.avatar.clone()
        DevEditRepository.syncFacepupInfo(avatarInfo, DevEditRepository.getFacepupContainer())
        DevEditRepository.setControlAvatar(avatarInfo.id)
        _currentAvatarLiveData.postValue(avatarInfo!!)
        historyReset()
        updateFilterGroupByAvatarId(avatarInfo.id)
        notifySelectItem()

    }

    private fun notifyControlModelLiveData() {
        controlModel?.let {
            _controlModelLiveData.postValue(it)
        }
    }

    /**
     * 根据传入的 [avatarId] 刷新编辑页对应的菜单。
     * 如男性角色显示男性菜单。
     */
    fun updateFilterGroupByAvatarId(avatarId: String) {
        val avatarInfo = DevAvatarManagerRepository.getAvatarInfo(avatarId)
        if (avatarInfo == null) {
            return
        }
        if (avatarInfo != DevAvatarManagerRepository.getCurrentAvatarInfo()) {
            return
        }
        val isMale = avatarInfo.gender() == "male"
        updateFilterGroupByMale(isMale)
    }

    /**
     * 传入性别信息，同步菜单栏的性别筛选。
     * 如果不设置，则会在例如头发的菜单栏里，同时显示男性头发与女性头发。
     */
    @Deprecated("use updateFilterGroupByAvatarId")
    fun updateFilterGroupByMale(isMale: Boolean) {
        if (isMale) {
            filterGroup.replaceSameTagRule(GenderProFilter(GenderFilter.GenderFilterKey.JustMale, GenderFilter.GenderFilterKey.All))
            filterGroup.replaceSameTagRule(AssociateFilter(AssociateFilter.AssociateFilterKey.Other))
        } else {
            filterGroup.replaceSameTagRule(GenderProFilter(GenderFilter.GenderFilterKey.JustFemale, GenderFilter.GenderFilterKey.All))
            filterGroup.replaceSameTagRule(AssociateFilter(AssociateFilter.AssociateFilterKey.AssociateShirt, AssociateFilter.AssociateFilterKey.Other))
        }
        notifyFilterGroupLiveData()
    }

    @Deprecated("use updateFilterGroupByAvatarId")
    fun updateFilterGroupByAsia(isAsia: Boolean) {
        if (isAsia) {
            filterGroup.replaceSameTagRule(StyleProFilter(StyleFilter.StyleFilterKey.JustAsia, StyleFilter.StyleFilterKey.All))
        } else {
            filterGroup.replaceSameTagRule(StyleProFilter(StyleFilter.StyleFilterKey.JustEurope, StyleFilter.StyleFilterKey.All))
        }
        notifyFilterGroupLiveData()
    }


    private fun notifyFilterGroupLiveData() {
        _filterGroupLiveData.postValue(filterGroup)
    }

    fun loadCloudItem(fuaeItem: FUAEItem) {
        if (FuDevDataCenter.fastCheckBlock()) {
            FuLog.error("当前 FuResourceLoadMode 非 Cloud，无法执行此操作")
            return
        }
        val fileID = fuaeItem.fuEditBundleItem?.fileID ?: return
        val avatar = loadControlAvatar() ?: return
        cloudControl.downloadItemResource(avatar = avatar, fileId = fileID, callback = object : DownloadItemResourceCallback {
            override fun onSuccess(fileId: String, downloadFileIdList: List<String>) {
                setItem(fuaeItem)
                _downloadInfoLiveData.postValue(FuSingleDownloadInfo(fileID, DownloadStatus.Finish(), groupFileIdList = downloadFileIdList))
            }

            override fun onDownloading(downloadStatus: DownloadStatus) {
                when(downloadStatus) {
                    is DownloadStatus.Error -> {
                        _downloadInfoLiveData.postValue(FuSingleDownloadInfo(fileID, downloadStatus, retryItem = fuaeItem))
                    }
                    is DownloadStatus.Finish -> {
                        //在 onSuccess 中处理了。
                    }
                    else -> {
                        _downloadInfoLiveData.postValue(FuSingleDownloadInfo(fileID, downloadStatus))
                    }
                }
            }

            override fun onOperationFailed(message: String) {
                FuLog.error(message)
            }

            override fun onCloudFailed(cloudException: CloudException) {
                when(cloudException) {
                    is CloudException.TokenException -> {
                        cloudControl.initCloud()
                        _downloadInfoLiveData.postValue(FuSingleDownloadInfo(fileID, DownloadStatus.Error("Token 过期，请重试"), fuaeItem))
                    }
                    is CloudException.NetworkException -> {
                        _downloadInfoLiveData.postValue(FuSingleDownloadInfo(fileID, DownloadStatus.Error(cloudException.desc + cloudException.exception?.message), fuaeItem))
                    }
                    is CloudException.FormatException -> {
                        _downloadInfoLiveData.postValue(FuSingleDownloadInfo(fileID, DownloadStatus.Error(cloudException.exception?.message ?: ""), fuaeItem))
                    }
                    else -> {
                        _downloadInfoLiveData.postValue(FuSingleDownloadInfo(fileID, DownloadStatus.Error(cloudException.toString()), fuaeItem))
                    }
                }
            }
        })

    }

    fun setItem(fuaeItem: FUAEItem) {
        val avatar = loadControlAvatar() ?: return
        updateItem(avatar, fuaeItem)
        pushHistory()
        notifySelectItem()
    }

    /**
     * 点击 Bundle Item 后触发对应渲染事件
     */
    @Deprecated("use autoSetItem")
    fun clickItem(fuaeItem: FUAEItem) {
        autoSetItem(fuaeItem)
    }

    /**
     * 点击 Color Item 后触发对应渲染事件
     */
    @Deprecated("use autoSetColorItem")
    fun clickColorItem(fuColorItem: FUAEColorItem) {
        autoSetColorItem(fuColorItem)
    }

    fun clickMinor(item: FUAEMinorCategory) {
        notifySelectItem()
    }

    //region 历史功能

    fun historyReset() {
        val avatar = defaultAvatar?.clone() ?: return
        playAvatarAnimation(avatar)
        FuEditHistoryManager.reset(avatar)
        DevDrawRepository.replaceCurrentAvatarByCompare(avatar)
        syncAvatarToCurrentAvatarInfo(avatar)
        notifySelectItem()
    }

    fun pushHistory() {
        val avatar = loadControlAvatar() ?: return
        FuEditHistoryManager.pushHistory(avatar)
    }

    fun historyBack() {
        val avatar = FuEditHistoryManager.pullBackHistory() ?: return
        playAvatarAnimation(avatar)
        DevDrawRepository.replaceCurrentAvatarByCompare(avatar)
        syncAvatarToCurrentAvatarInfo(avatar)
        notifySelectItem()
    }

    fun historyForward() {
        val avatar = FuEditHistoryManager.pullForwardHistory() ?: return
        playAvatarAnimation(avatar)
        DevDrawRepository.replaceCurrentAvatarByCompare(avatar)
        syncAvatarToCurrentAvatarInfo(avatar)
        notifySelectItem()
    }

    /**
     * 当切换 Avatar 后，需要手动播放需要的动画
     */
    private fun playAvatarAnimation(avatar: Avatar) {
        avatar.animation.apply {
            val huxiAnim = getAnimations().firstOrNull { it.path.contains("huxi") } ?: return
            playAnimation(huxiAnim)
        }
    }

    private fun syncAvatarToCurrentAvatarInfo(avatar: Avatar) {
        val avatarInfo = DevAvatarManagerRepository.getCurrentAvatarInfo() ?: return
        avatarInfo.avatar = avatar
        _currentAvatarLiveData.postValue(avatarInfo!!)
    }

    //endregion 历史功能

    //region 捏脸功能

    /**
     * 该菜单栏是否支持捏脸
     */
    fun isHasFacepup(minorKey: String): Boolean {
        return minorKey in DevEditRepository.getFacepupContainer().getAllGroupKey()
    }


    /**
     * 将传入的捏脸值解析并设置到 Avatar 中
     */
    fun setFacepupTierSeekBarItem(fileId: String, groupKey: String, slider: FacepupSlider, seekBarValue: Float) {
        val avatar = DevAvatarManagerRepository.getCurrentAvatarInfo()?.avatar ?: return
        val facepupMap = DevEditRepository.parseFacepupTierSeekBarItem(slider, seekBarValue)
        DevEditRepository.putFacepupInfo(fileId, groupKey, facepupMap)
        facepupMap.forEach {
            avatar.deformation.setDeformation(it.key, it.value)
        }
    }

    /**
     * 重置该模型的捏脸信息
     */
    fun resetFacepupByFileId(fileId: String) {
        val avatar = DevAvatarManagerRepository.getCurrentAvatarInfo()?.avatar ?: return
        val facepupPackInfo = DevEditRepository.getFacepupPackInfo(fileId)
        facepupPackInfo?.map?.forEach {
            avatar.deformation.setDeformation(it.key, 0f)
        }
        DevEditRepository.resetFacepupInfo(fileId)
    }

    /**
     * 重置该类别的捏脸信息
     */
    fun resetFacepupByGroupKey(groupKey: String) {
        val avatar = DevAvatarManagerRepository.getCurrentAvatarInfo()?.avatar ?: return
        val resetKey = DevEditRepository.getFacepupContainer().getAllFacePupKey(groupKey)
        resetKey.forEach {
            avatar.deformation.setDeformation(it, 0f)
        }
        DevEditRepository.resetGroupFacepupInfo(groupKey)
    }

    /**
     * 该道具是否含有捏脸信息
     */
    fun isHasFacepupPackInfo(fileId: String): Boolean {
        val facepupPackInfo = DevEditRepository.getFacepupPackInfo(fileId)
        if (facepupPackInfo == null) {
            return false
        }
        if (facepupPackInfo.map.size == 0){
            return false
        }
        return true
    }

    //endregion 捏脸功能


    private fun notifySelectItem() {
        _selectItemLiveData.postValue(getSelectItem())
    }

    private fun getSelectItem() : List<String> {
        val avatarInfo = loadControlAvatarInfo() ?: return emptyList()
        return avatarInfo.getUsedBundleFileID()
    }
}