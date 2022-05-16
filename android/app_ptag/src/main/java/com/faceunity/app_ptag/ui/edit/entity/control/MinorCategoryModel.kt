package com.faceunity.app_ptag.ui.edit.entity.control

import com.faceunity.support.entity.FUAEColorItem
import com.faceunity.support.entity.FUAEItem
import com.faceunity.support.entity.FUAEMinorCategory
import com.faceunity.support.entity.FUAESubCategory

/**
 * Created on 2021/7/23 0023 10:59.


 */
data class MinorCategoryModel(
    val id: String,
    val key: String,
    val name: String,
    val iconPath: FUImage,
    val selectIconPath: FUImage,
    private var fuSelectedItem: FUAEItem?,
    private var fuSelectedColorItem: FUAEColorItem?,
    private val fuMinorCategory: FUAEMinorCategory,
    private val fuSubCategory: FUAESubCategory,
    val subList: MutableList<SubCategoryModel>
) {

    fun useFUMinorCategory() = fuMinorCategory

    fun setSelectedItem(subCategoryModel: SubCategoryModel) {
        when (subCategoryModel) {
            is SubCategoryNormalModel -> {
                fuSelectedItem = subCategoryModel.useFUItem()
                fuSubCategory.selectedItem = fuSelectedItem
            }
            is SubCategoryColorModel -> {
                fuSelectedColorItem = subCategoryModel.useFUColorItem()
                fuSubCategory.selectedColor = fuSelectedColorItem
            }
        }
    }

    fun getSelectedItem(): SubCategoryModel? {
        if (subList.size == 0) return null
        fuSelectedItem = fuSubCategory.selectedItem
        fuSelectedColorItem = fuSubCategory.selectedColor
        if (fuSelectedItem != null) {
            val filterList =
                subList.filter { it is SubCategoryNormalModel && it.useFUItem() == fuSelectedItem }
            return filterList.firstOrNull()
        }
        if (fuSelectedColorItem != null) {
            val filterList =
                subList.filter { it is SubCategoryColorModel && it.useFUColorItem() == fuSelectedColorItem }
            return filterList.firstOrNull()
        }

        return null
    }
}