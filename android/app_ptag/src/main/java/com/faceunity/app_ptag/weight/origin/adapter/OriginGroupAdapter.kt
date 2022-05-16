package com.faceunity.fupta.facepup.weight.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.faceunity.app_ptag.databinding.ItemOriginGroupBinding
import com.faceunity.fupta.facepup.entity.FacePupGroup
import com.faceunity.fupta.facepup.weight.FacePupControlEventListener

/**
 * Created on 2021/11/18 0018 14:31.


 */
internal class OriginGroupAdapter(
    private val controlEventListener: FacePupControlEventListener
) : ListAdapter<FacePupGroup, OriginGroupAdapter.OriginGroupViewHolder>(FacePupGroupDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OriginGroupViewHolder {
        return OriginGroupViewHolder(
            ItemOriginGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            controlEventListener
        )
    }


    override fun onBindViewHolder(holder: OriginGroupViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class OriginGroupViewHolder(
        private val binding: ItemOriginGroupBinding,
        private val controlEventListener: FacePupControlEventListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FacePupGroup) {
            binding.name.text = item.groupKey
            binding.root.setOnClickListener {
                controlEventListener.onGroupItemClick(item)
            }
        }
    }

    object FacePupGroupDiffCallback: DiffUtil.ItemCallback<FacePupGroup>() {
        override fun areItemsTheSame(oldItem: FacePupGroup, newItem: FacePupGroup): Boolean {
            return newItem.groupKey == oldItem.groupKey
        }

        override fun areContentsTheSame(oldItem: FacePupGroup, newItem: FacePupGroup): Boolean {
            return newItem.groupKey == oldItem.groupKey
        }

    }
}