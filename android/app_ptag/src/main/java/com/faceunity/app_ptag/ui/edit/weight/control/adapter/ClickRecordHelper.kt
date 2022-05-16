package com.faceunity.app_ptag.ui.edit.weight.control.adapter

import androidx.recyclerview.widget.RecyclerView

/**
 * 点击事件辅助类。为需要记录点击状态的 Adapter 提供一个默认的实现。
 * note：该做法是为了避免“实现一个大而全的 BaseAdapter，而增加了各模块复杂度”的情况。
 */
class ClickRecordHelper: ClickRecordInterface {

    private var lastClickPosition = -1
    private var nowClickPosition = -1

    override fun getNowSelectPosition(): Int {
        return nowClickPosition
    }

    override fun getLastSelectPosition(): Int {
        return lastClickPosition
    }

    override fun recordClick(position: Int) {
        lastClickPosition = nowClickPosition
        nowClickPosition = position
    }

    override fun getDefaultPosition(needClick: Boolean): Int {
        if (nowClickPosition != -1) return nowClickPosition
        if (needClick) {
            nowClickPosition = 0
        }
        return 0
    }

    override fun resetClickRecord() {
        lastClickPosition = -1
        nowClickPosition = -1
    }

    override fun notifyClickItemChanged(adapter: RecyclerView.Adapter<*>) {
        adapter.notifyItemChanged(lastClickPosition)
        adapter.notifyItemChanged(nowClickPosition)
    }
}