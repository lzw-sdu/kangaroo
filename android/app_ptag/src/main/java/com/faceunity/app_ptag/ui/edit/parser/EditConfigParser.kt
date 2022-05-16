package com.faceunity.app_ptag.ui.edit.parser

import com.faceunity.fupta.avatar_data.entity.resource.*
import com.faceunity.support.entity.*
import com.faceunity.support.entity.clean.FuEditBundleItem
import com.faceunity.support.entity.compat.FuEditCompat
import com.faceunity.toolbox.utils.FURandomUtils

/**
 * Created on 2021/12/14 0014 20:51.
 */
class EditConfigParser(
    private val tempResourcePathTran: TempResourcePathTran
) {

    fun buildFUAEModel(
        editConfigResource: EditConfigResource,
        editCustomConfigResource: EditCustomConfigResource,
        editItemListResource: EditItemListResource,
        editCustomItemListResource: EditCustomItemListResource,
        editColorListResource: EditColorListResource,
        itemListResource: ItemListResource
    ) : FUAEModel {
        val aeModel = FUAEModel()
        editCustomConfigResource.getTree().forEach { masterKey ->
            val fuaeMasterCategory = buildMasterCategory(
                masterKey,
                editConfigResource.source.map.get(masterKey),
                editCustomConfigResource.source.map.get(masterKey)
            )
            aeModel.masterCategories.add(fuaeMasterCategory)

            editCustomConfigResource.getMenu(masterKey).forEach { minorKey ->
                val fuaeMinorCategory = buildMinorCategory(
                    minorKey,
                    editConfigResource.source.map.get(minorKey),
                    editCustomConfigResource.source.map.get(minorKey)
                )
                fuaeMasterCategory.minorCategories.add(fuaeMinorCategory)

                val minorList = editCustomConfigResource.getMenu(minorKey)
                if (minorList.isEmpty()) {
                    val subKey = minorKey
                    val fuaeSubCategory = buildSubCategory(
                        subKey,
                        editConfigResource.source.map.get(subKey),
                        editCustomConfigResource.source.map.get(subKey)
                    )
                    fuaeMinorCategory.subCategories.add(fuaeSubCategory)

                    putSubCategoryData(
                        fuaeSubCategory,
                        editItemListResource.map.get(subKey) ?: emptyList(),
                        editCustomItemListResource.map.get(subKey) ?: emptyList(),
                        editColorListResource.map[subKey] ?: emptyList(),
                        editConfigResource.color_keys.map[subKey] ?: emptyList(),
                        itemListResource
                    )
                } else {
                    minorList.forEach { subKey ->
                        val fuaeSubCategory = buildSubCategory(
                            subKey,
                            editConfigResource.source.map.get(subKey),
                            editCustomConfigResource.source.map.get(subKey)
                        )
                        fuaeMinorCategory.subCategories.add(fuaeSubCategory)

                        putSubCategoryData(
                            fuaeSubCategory,
                            editItemListResource.map.get(subKey) ?: emptyList(),
                            editCustomItemListResource.map.get(subKey) ?: emptyList(),
                            editColorListResource.map[subKey] ?: emptyList(),
                            editConfigResource.color_keys.map[subKey] ?: emptyList(),
                            itemListResource
                        )
                    }
                }

            }
        }

        return aeModel
    }

    private fun buildMasterCategory(masterKey: String, item: EditConfigResource.Source.SourceItem?, customItem: EditCustomConfigResource.Source.SourceItem?) : FUAEMasterCategory {
        val fuaeMasterCategory = FUAEMasterCategory(masterKey)
        fuaeMasterCategory.params.putAll(parseCustomParams(customItem))
        fuaeMasterCategory.compat = parseEditConfigCompat(item)
        return fuaeMasterCategory
    }

    private fun buildMinorCategory(minorKey: String, item: EditConfigResource.Source.SourceItem?, customItem: EditCustomConfigResource.Source.SourceItem?) : FUAEMinorCategory {
        val fuaeMinorCategory = FUAEMinorCategory(minorKey)
        fuaeMinorCategory.params.putAll(parseCustomParams(customItem))
        fuaeMinorCategory.compat = parseEditConfigCompat(item)
        return fuaeMinorCategory
    }

    private fun buildSubCategory(subKey: String, item: EditConfigResource.Source.SourceItem?, customItem: EditCustomConfigResource.Source.SourceItem?) : FUAESubCategory {
        val fuaeSubCategory = FUAESubCategory(subKey)
        fuaeSubCategory.params.putAll(parseCustomParams(customItem))
        fuaeSubCategory.compat = parseEditConfigCompat(item)
        return fuaeSubCategory
    }

    private fun putSubCategoryData(
        fuaeSubCategory: FUAESubCategory,
        editItemList: List<EditItemListResource.Item>,
        customItemList: List<EditCustomItemListResource.Item>,
        colorItemList: List<EditColorListResource.Color>,
        colorKeyList: List<String>,
        itemListResource: ItemListResource
    ) {
        val fuItemList = mutableListOf<FUAEItem>()
        editItemList.forEachIndexed { index, item ->
            val customItem = customItemList.getOrNull(index) ?: return@forEachIndexed
            val itemResource = itemListResource.list.firstOrNull { it.path == item.path } ?: return@forEachIndexed
            val fuaeItem = editItemToFullAEItem(item, customItem, itemResource)
            fuItemList.add(fuaeItem)
        }
        fuaeSubCategory.items.addAll(fuItemList)

        colorKeyList.forEach { colorKey ->
            val fuColorItemList = colorItemList.map { colorItemToFUAEColorItem(colorKey, it) }
            val fuaeColorCategory = FUAEColorCategory(colorKey)
            fuaeColorCategory.colors.addAll(fuColorItemList)
            fuaeSubCategory.colorCategories.add(fuaeColorCategory)
        }

    }

    private fun parseCustomParams(sourceItem: EditCustomConfigResource.Source.SourceItem?): Map<String, Any?> {
        val params = mutableMapOf<String, Any?>()
        sourceItem?.map?.forEach { (key, value) ->
            params[key] = value
        }

        return params
    }

    private fun parseEditConfigCompat(sourceItem: EditConfigResource.Source.SourceItem?): FuEditCompat? {
        if (sourceItem == null) return null
        return FuEditCompat(
            localIcon = tempResourcePathTran.appCustom(sourceItem.icon.replace("fu_asset://", "")),
            localSelectIcon = tempResourcePathTran.appCustom(sourceItem.selected_icon.replace("fu_asset://", ""))
        )
    }

    private fun editItemToFullAEItem(item: EditItemListResource.Item, customItem: EditCustomItemListResource.Item, itemResource: ItemListResource.Item) : FUAEItem {
        val type = when {
            item.path_list != null -> {
                "group"
            }
            item.path != null -> {
                "single"
            }
            else -> {
                ""
            }
        }
        val key = when(type) {
            "single" -> {
                item.path!!
            }
            "group" -> {
                item.path_list!!.joinToString(",")
            }
            else -> {
                FURandomUtils.randomID()
            }
        }
        val aeItem = FUAEItem(key)
        customItem.map?.forEach { (key, value) ->
            aeItem.params[key] = value
        }
        val fuEditBundleItem = when(type) {
            "single" -> {
                FuEditBundleItem(item.path!!.let { it })
            }
            "group" -> {
                FuEditBundleItem(item.path_list!!.map { it })
            }
            else -> {
                null
            }
        }
        aeItem.fuEditBundleItem = fuEditBundleItem
        val localIcon = item.path?.let { tempResourcePathTran.ossCustom(it).replace(".bundle", ".png") }
        val iconUrl = itemResource.icon_url.takeIf { it.isNotBlank() }
        aeItem.compat = FuEditCompat(localIcon = localIcon, iconUrl = iconUrl)
        return aeItem
    }

    private fun colorItemToFUAEColorItem(key: String, item: EditColorListResource.Color): FUAEColorItem {
        val aeColorItem = FUAEColorItem(key, item.r, item.g, item.b, item.intensity ?: 1.0f)

        return aeColorItem
    }

    interface TempResourcePathTran {

        fun appCustom(path: String): String

        fun ossCustom(path: String): String
    }
}