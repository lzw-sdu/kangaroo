package com.faceunity.app_ptag.ui.edit.weight.control.adapter.diff

import androidx.recyclerview.widget.DiffUtil
import com.faceunity.app_ptag.ui.edit.entity.control.MinorCategoryModel

/**
 *
 */
object MinorCategoryDiffCallback : DiffUtil.ItemCallback<MinorCategoryModel>() {
    override fun areItemsTheSame(
        oldItem: MinorCategoryModel,
        newItem: MinorCategoryModel
    ): Boolean {
        return newItem.key == oldItem.key
    }

    override fun areContentsTheSame(
        oldItem: MinorCategoryModel,
        newItem: MinorCategoryModel
    ): Boolean {
        return newItem.key == oldItem.key
    }

}