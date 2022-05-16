package com.faceunity.app_ptag.ui.edit_custom

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.faceunity.app_ptag.ui.edit_custom.entity.CustomControlModel
import com.faceunity.app_ptag.ui.edit_custom.entity.StoreInfo
import com.faceunity.app_ptag.ui.edit_custom.entity.UserBuyHistoryInfo
import com.faceunity.editor_ptag.cache.FuCacheResource
import com.faceunity.editor_ptag.data_center.FuDevDataCenter
import com.faceunity.app_ptag.ui.edit.parser.EditConfigParser
import com.faceunity.editor_ptag.store.DevAvatarManagerRepository
import com.faceunity.editor_ptag.store.DevDrawRepository
import com.faceunity.editor_ptag.store.DevEditRepository
import com.faceunity.editor_ptag.store.DevSceneManagerRepository
import com.faceunity.editor_ptag.util.FuLog
import com.faceunity.fupta.avatar_data.entity.resource.*
import com.faceunity.pta.pta_core.repository.EditRepository
import com.faceunity.support.entity.FUAEModel

class FuDemoEditCustomViewModel : ViewModel() {

    private val _controlCustomModelLiveData = MutableLiveData<CustomControlModel>()
    val controlCustomModelLiveData: LiveData<CustomControlModel> get() = _controlCustomModelLiveData

    /**
     * 临时代码，不推荐参考
     */
    fun loadDefaultAvatar() {
        FuCacheResource.init(FuDevDataCenter.resourceParser,  FuDevDataCenter.resourcePath, FuDevDataCenter.resourceLoader)
        if (DevAvatarManagerRepository.getAvatarListSize() == 0) {
            DevAvatarManagerRepository.loadAvatar()
        }
        val avatarInfo = DevAvatarManagerRepository.switchFirstAvatarAndGet() ?: return
        DevSceneManagerRepository.initSceneConfigNotRepeat()
        val sceneInfo = DevSceneManagerRepository.filterGenderDefaultSceneFirst(avatarInfo.gender())
        if (sceneInfo == null) {
            FuLog.error("符合条件的 SceneInfo 找不到，无法加载 Scene 与 Avatar")
            return
        }
        DevDrawRepository.loadSceneAndAvatar(sceneInfo, avatarInfo.avatar)

    }

    fun requestData() {
        val storeInfo = testLoadStoreInfo()
        val userBuyHistoryInfo = testLoadUserBuyHistoryInfo()
        buildCustomControlModel(storeInfo, userBuyHistoryInfo)
    }

    private fun testLoadStoreInfo(): StoreInfo {
        return StoreInfo(listOf(
            StoreInfo.Goods("1", "GAssets/AsiaMale/hair/hair001.bundle", 10),
            StoreInfo.Goods("2", "GAssets/AsiaMale/hair/hair002.bundle", 20),
            StoreInfo.Goods("3", "GAssets/AsiaMale/hair/hair004.bundle", 10),
            StoreInfo.Goods("4", "GAssets/AsiaMale/hair/hair009.bundle", 15),
        ))
    }

    private fun testLoadUserBuyHistoryInfo(): UserBuyHistoryInfo {
        return UserBuyHistoryInfo(listOf(
            UserBuyHistoryInfo.Item("1", "GAssets/AsiaMale/hair/hair002.bundle", "付费", ""),
            UserBuyHistoryInfo.Item("2", "GAssets/AsiaMale/hair/hair009.bundle", "签到解锁", "")
        ))
    }

    private fun buildCustomControlModel(storeInfo: StoreInfo, userBuyHistoryInfo: UserBuyHistoryInfo) {
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
        val cloneFUAvatarEditModel = DevEditRepository.buildEditModel(builder) as FUAEModel
        val customControlModel =
            CustomControlModelParser().parseCustomControlModel(cloneFUAvatarEditModel, storeInfo, userBuyHistoryInfo)

        _controlCustomModelLiveData.postValue(customControlModel)
    }

    fun clickItem(item: CustomControlModel.Item) {
        val avatar = DevAvatarManagerRepository.getCurrentAvatarInfo()?.avatar ?: return
        val fileIDList = item.fuaeItem?.fuEditBundleItem?.getFullFileIDList() ?: return
        DevEditRepository.setItemList(avatar, fileIDList)
    }
}