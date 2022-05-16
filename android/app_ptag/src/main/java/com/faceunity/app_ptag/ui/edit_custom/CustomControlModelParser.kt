package com.faceunity.app_ptag.ui.edit_custom

import com.faceunity.app_ptag.ui.edit_custom.entity.CustomControlModel
import com.faceunity.app_ptag.ui.edit_custom.entity.StoreInfo
import com.faceunity.app_ptag.ui.edit_custom.entity.UserBuyHistoryInfo
import com.faceunity.support.entity.FUAEItem
import com.faceunity.support.entity.FUAEModel

/**
 *
 */
class CustomControlModelParser {

    fun parseCustomControlModel(aeModel: FUAEModel, storeInfo: StoreInfo, userBuyHistoryInfo: UserBuyHistoryInfo): CustomControlModel {
        val mainMenuList = mutableListOf<CustomControlModel.MainMenu>()
        aeModel.masterCategories.forEach { aeMaster ->
            val minorMenuList = mutableListOf<CustomControlModel.MinorMenu>()
            aeMaster.minorCategories.forEach { aeMinor ->
                aeMinor.subCategories.forEach {
                    val itemList = mutableListOf<CustomControlModel.Item>()
                    it.items.forEach { aeItem ->
                        val item = buildItem(aeItem)
                        item.isLock = isLockItem(aeItem.params["fileID"].toString(), storeInfo, userBuyHistoryInfo)
                        itemList.add(item)
                    }
                    minorMenuList.add(CustomControlModel.MinorMenu(aeMinor.iconPath, itemList))
                }
            }
            mainMenuList.add(CustomControlModel.MainMenu(aeMaster.iconPath, minorMenuList))
        }

        return CustomControlModel(mainMenuList)
    }

    private fun buildItem(aeItem: FUAEItem) : CustomControlModel.Item {
        return CustomControlModel.Item(
            aeItem,
            aeItem.params["icon"].toString(),
            false,
            false
        )
    }

    private fun isLockItem(fileID: String, storeInfo: StoreInfo, userBuyHistoryInfo: UserBuyHistoryInfo) : Boolean {
        var isLock = false
        storeInfo.list.map { it.fuFileID }.forEach {
            if (fileID == it) {
                isLock = true
                return@forEach
            }
        }
        if (!isLock) return isLock
        userBuyHistoryInfo.list.map { it.fuFileID }.forEach {
            if (fileID == it) {
                isLock = false
                return@forEach
            }
        }
        return isLock
    }
}