package com.faceunity.app_ptag.ui.edit.weight.control

import com.faceunity.support.entity.FUAEColorItem
import com.faceunity.support.entity.FUAEItem
import com.faceunity.support.entity.FUAEMinorCategory

/**
 * 用于外部的 [AvatarControlView] 业务事件监听
 */
interface AvatarControlListener {
    /**
     * 当切换二级菜单
     */
    fun onMinorSelect(item: FUAEMinorCategory)

    /**
     * 当普通条目被选择
     */
    fun onNormalItemClick(item: FUAEItem)

    /**
     * 当颜色条目被选择
     */
    fun onColorItemClick(item: FUAEColorItem)

    fun onFacepupClick(groupKey: String, fileId: String?)

    /**
     * 当历史-回退
     */
    fun onHistoryBackClick()

    /**
     * 当历史-前进
     */
    fun onHistoryForwardClick()


    fun onHistoryResetClick()
}