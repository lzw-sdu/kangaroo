package com.faceunity.app_ptag.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * Created on 2018/7/24 17:03.


 * 一个快速开发的通用 Adapter，用于数据量比较少的场景。（未对 findViewById 优化）
 */
open class FastAdapter<T>(var list: MutableList<T>, val layoutResource: Int, val bind: (holder: ViewHolder, bean : T, position: Int) -> Unit) : RecyclerView.Adapter<FastAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(layoutResource, parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        bind(holder, list[position], position)
    }

    fun updateList(list: MutableList<T>) {
        this.list = list
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}