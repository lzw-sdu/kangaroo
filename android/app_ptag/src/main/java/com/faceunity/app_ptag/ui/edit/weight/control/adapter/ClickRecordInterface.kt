package com.faceunity.app_ptag.ui.edit.weight.control.adapter

import androidx.recyclerview.widget.RecyclerView

/**
 * Created on 2021/7/26 0026 17:59.


 */
interface ClickRecordInterface {

    fun getNowSelectPosition() : Int

    fun getLastSelectPosition() : Int

    fun recordClickSafe(position: Int?) {
        position?.let {
            recordClick(it)
        }
    }

    fun recordClick(position: Int)

    fun getDefaultPosition(needClick: Boolean = false): Int

    fun resetClickRecord()

    fun notifyClickItemChanged(adapter: RecyclerView.Adapter<*>)
}