package com.faceunity.app_ptag.ui.edit.weight.control.adapter.diff

import androidx.recyclerview.widget.DiffUtil
import com.faceunity.app_ptag.ui.edit.entity.control.SubCategoryModel

/**
 *
 */
object SubCategoryDiffCallback: DiffUtil.ItemCallback<SubCategoryModel>() {
    override fun areItemsTheSame(oldItem: SubCategoryModel, newItem: SubCategoryModel): Boolean {
        if (newItem.type != oldItem.type) return false
        return newItem.id() == oldItem.id()
    }

    override fun areContentsTheSame(oldItem: SubCategoryModel, newItem: SubCategoryModel): Boolean {
        if (newItem.type != oldItem.type) return false
        return newItem == oldItem
    }

}