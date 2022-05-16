package com.faceunity.app_ptag.ui.edit.entity.control

import com.faceunity.support.entity.FUAEColorItem

/**
 * Created on 2021/7/23 0023 14:12.


 */
data class SubCategoryColorModel(
    val id: String,
    val key: String,
    val color: Int,
    private val groupKey: String,
    private val fuColorItem: FUAEColorItem
) : SubCategoryModel(Type.Color) {
    override fun id() = fuColorItem.color.toString()

    fun useFUColorItem() = fuColorItem
}
