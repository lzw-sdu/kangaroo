package com.faceunity.app_ptag.ui.edit.weight.control.adapter.diff

import androidx.recyclerview.widget.DiffUtil
import com.faceunity.app_ptag.ui.edit.entity.control.MasterCategoryModel

/**
 *
 */
object MasterCategoryDiffCallback : DiffUtil.ItemCallback<MasterCategoryModel>() {
    override fun areItemsTheSame(
        oldItem: MasterCategoryModel,
        newItem: MasterCategoryModel
    ): Boolean {
        return newItem.key == oldItem.key
    }

    override fun areContentsTheSame(
        oldItem: MasterCategoryModel,
        newItem: MasterCategoryModel
    ): Boolean {
        return newItem.key == oldItem.key
    }

}