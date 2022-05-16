package com.faceunity.app_ptag.ui.edit.parser

import com.faceunity.app_ptag.ui.edit.entity.control.*
import com.faceunity.editor_ptag.data_center.FuDevDataCenter
import com.faceunity.editor_ptag.data_center.entity.FuResourceLoadMode
import com.faceunity.support.entity.FUAEMasterCategory
import com.faceunity.support.entity.FUAEMinorCategory
import com.faceunity.support.entity.FUAEModel
import com.faceunity.support.entity.FUAESubCategory
import com.faceunity.toolbox.utils.FURandomUtils

/**
 * 将 [FUAEModel] 解析为 [ControlModel]
 */
class ControlModelParser {

    fun parseControlModel(aeModel: FUAEModel): ControlModel {
        val controlModel = ControlModel(mutableListOf())
        with(aeModel) {
            masterCategories?.forEach {
                controlModel.masterList.add(buildMasterCategoryModel(it))
            }
        }
        return controlModel
    }

    private fun buildMasterCategoryModel(fuMasterCategory: FUAEMasterCategory): MasterCategoryModel {
        val masterCategoryModel = MasterCategoryModel(
            id = "MasterCategory-" + FURandomUtils.randomID(),
            key = fuMasterCategory.key,
            name = fuMasterCategory.name ?: "",
            iconPath = FUImage.createFromModel(fuMasterCategory.compat?.localIcon),
            selectIconPath = FUImage.createFromModel(fuMasterCategory.compat?.localSelectIcon),
            minorList = mutableListOf()
        )
        with(fuMasterCategory) {
            masterCategoryModel.minorList.addAll(buildMinorCategoryModel(minorCategories))
        }

        return masterCategoryModel
    }

    private fun buildMinorCategoryModel(fuMinorCategories: List<FUAEMinorCategory>?): List<MinorCategoryModel> {
        val minorList = mutableListOf<MinorCategoryModel>()
        fuMinorCategories?.forEach { fuMinorCategory: FUAEMinorCategory ->
            fuMinorCategory.subCategories?.forEach { fuSubCategory: FUAESubCategory ->
                val minorCategoryModel = MinorCategoryModel(
                    id = "MinorCategory-" + FURandomUtils.randomID(),
                    key = fuSubCategory.key,
                    name = fuSubCategory.name ?: "",
                    iconPath = FUImage.createFromModel(fuSubCategory.compat?.localIcon),
                    selectIconPath = FUImage.createFromModel(fuSubCategory.compat?.localSelectIcon),
                    fuSelectedItem = fuSubCategory.selectedItem,
                    fuSelectedColorItem = fuSubCategory.selectedColor,
                    fuMinorCategory = fuMinorCategory,
                    fuSubCategory = fuSubCategory,
                    subList = mutableListOf()
                )
                minorCategoryModel.subList.addAll(buildSubCategoryModel(fuSubCategory))
                minorList.add(minorCategoryModel)
            }
        }

        return minorList
    }

    private fun buildSubCategoryModel(fuSubCategories: FUAESubCategory): List<SubCategoryModel> {
        val subList = mutableListOf<SubCategoryModel>()
        fuSubCategories.items?.forEach { item ->
            val subCategoryModel = SubCategoryNormalModel(
                id = "SubCategory-" + FURandomUtils.randomID(),
                key = item.bundlePath ?: "",
                iconPath = when {
                                FuDevDataCenter.resourceLoadMode == FuResourceLoadMode.Cloud && item.compat?.iconUrl != null -> {
                                    item.compat?.iconUrl.let { FUImage.createFromModel(it) }
                                }
                                (FuDevDataCenter.resourceLoadMode == FuResourceLoadMode.Local || FuDevDataCenter.resourceLoadMode == FuResourceLoadMode.CustomCloud) &&item.compat?.localIcon != null -> {
                                    item.compat?.localIcon.let { FUImage.createFromModel(it) }
                                }
                                else -> FUImage.createNull()
                                },
                fuItem = item
            )
            subList.add(subCategoryModel)
        }
        fuSubCategories.colorCategories?.forEach { group ->
            group.colors?.forEach { item ->
                val subCategoryColorModel = SubCategoryColorModel(
                    id = "SubCategoryColor-" + FURandomUtils.randomID(),
                    key = item.key,
                    color = item.color,
                    groupKey = group.key,
                    fuColorItem = item
                )
                subList.add(subCategoryColorModel)
            }
        }

        return subList
    }

    companion object {

        private val parser =  ControlModelParser()

        fun buildControlModel(aeModel: FUAEModel): ControlModel {
            return parser.parseControlModel(aeModel)
        }
    }
}