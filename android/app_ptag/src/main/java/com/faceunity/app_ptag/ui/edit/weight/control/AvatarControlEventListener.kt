package com.faceunity.app_ptag.ui.edit.weight.control

import com.faceunity.app_ptag.ui.edit.entity.control.MasterCategoryModel
import com.faceunity.app_ptag.ui.edit.entity.control.MinorCategoryModel
import com.faceunity.app_ptag.ui.edit.entity.control.SubCategoryModel

/**
 * 仅用于 [AvatarControlView] 内部的事件响应
 */
internal interface AvatarControlEventListener {
    fun onMasterClick(item: MasterCategoryModel, position: Int)
    fun onMinorClick(item: MinorCategoryModel, position: Int)
    fun onSubClick(item: SubCategoryModel, position: Int)
}