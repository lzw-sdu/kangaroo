package com.faceunity.app_ptag.weight.avatar_manager.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.faceunity.app_ptag.databinding.ItemAvatarWrapperBinding
import com.faceunity.app_ptag.weight.avatar_manager.entity.FuAvatarWrapper
import com.faceunity.app_ptag.weight.avatar_manager.entity.FuAvatarWrapperDiffCallback
import com.faceunity.editor_ptag.util.visibleOrGone
import com.faceunity.toolbox.media.FUMediaUtils

/**
 * Created on 2021/12/13 0013 20:14.


 */
class AvatarManagerAdapter(
    private val listener: Listener,
    var selectId: MutableList<String> = mutableListOf()
) : ListAdapter<FuAvatarWrapper, AvatarManagerAdapter.ViewHolder>(FuAvatarWrapperDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemAvatarWrapperBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: ItemAvatarWrapperBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FuAvatarWrapper) {
            val isSelect = selectId.contains(item.id)

            if (item.icon != null) {
                FUMediaUtils.loadBitmap(binding.root.context, item.icon).let {
                    binding.icon.setImageBitmap(it)
                }
            } else {
                binding.name.text = item.id
            }

            binding.selectBg.visibleOrGone(isSelect)

            binding.root.setOnClickListener {
                listener.onClick(item)
            }
        }
    }

    interface Listener {
        fun onClick(item: FuAvatarWrapper)
    }
}