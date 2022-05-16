package com.faceunity.app_ptag.ui.edit.entity.control

import com.faceunity.support.entity.FUAEItem

/**
 * Created on 2021/7/23 0023 14:12.


 */
data class SubCategoryNormalModel(
    val id: String,
    val key: String,
    val iconPath: FUImage,
    private val fuItem: FUAEItem
) : SubCategoryModel(Type.Normal) {
    override fun id() = key

    override fun isReset(): Boolean {
        return false
    }

    fun hasIcon(): Boolean {
        return !iconPath.isNull()
    }

    fun useFUItem() = fuItem

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SubCategoryNormalModel) return false
        if (!super.equals(other)) return false

        if (key != other.key) return false
        if (fuItem != other.fuItem) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + key.hashCode()
        result = 31 * result + fuItem.hashCode()
        return result
    }


}
