package com.faceunity.app_ptag.ui.edit.entity.control

/**
 * Created on 2021/7/23 0023 11:00.
 */
abstract class SubCategoryModel(
    val type: Type
) {

    abstract fun id(): String

    open fun isReset(): Boolean = false

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SubCategoryModel) return false

        if (type != other.type) return false

        return true
    }

    override fun hashCode(): Int {
        return type.hashCode()
    }


    enum class Type {
        Normal,
        Color
    }
}