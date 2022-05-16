package com.faceunity.app_ptag.ui.edit.weight.control.adapter

import android.animation.ObjectAnimator
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.faceunity.app_ptag.R
import com.faceunity.app_ptag.databinding.ItemMasterCategoryBinding
import com.faceunity.app_ptag.ui.edit.weight.control.AvatarControlEventListener
import com.faceunity.app_ptag.ui.edit.weight.control.adapter.diff.MasterCategoryDiffCallback
import com.faceunity.app_ptag.ui.edit.entity.control.MasterCategoryModel

/**
 * Created on 2021/7/22 0022 17:13.


 */
internal class MasterCategoryAdapter(
    private val eventListener: AvatarControlEventListener
) : ListAdapter<MasterCategoryModel, MasterCategoryAdapter.MasterViewHolder>(
    MasterCategoryDiffCallback
),
    ClickRecordInterface by ClickRecordHelper() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MasterViewHolder {
        return MasterViewHolder(
            ItemMasterCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            eventListener
        )
    }

    override fun onBindViewHolder(holder: MasterViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MasterViewHolder(
        private val binding: ItemMasterCategoryBinding,
        private val eventListener: AvatarControlEventListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MasterCategoryModel) {
            val context = binding.root.context
            val isSelect = getNowSelectPosition() == adapterPosition

//            binding.background.apply {
//                if (isSelect) {
//                    (drawable as? ColorDrawable)?.let {
//                        ObjectAnimator.ofArgb(it, "color", item.tintColor())
//                            .setDuration(500)
//                            .start()
//                    }
//                } else {
//                    (drawable as? ColorDrawable)?.let {
//                        ObjectAnimator.ofArgb(it, "color", ContextCompat.getColor(context, R.color.item_bg))
//                            .setDuration(500)
//                            .start()
//                    }
//                }
//            }

            binding.icon.apply {
                if (isSelect) {
                    (background as? GradientDrawable)?.let {
                        ObjectAnimator.ofArgb(it, "color", item.tintColor())
                            .setDuration(500)
                            .start()
                    }
                    Glide.with(this)
                        .load(item.selectIconPath.parseToBitmap(context))
                        .into(this)
//                    ImageViewCompat.setImageTintList(
//                        binding.icon,
//                        ColorStateList.valueOf(
//                            ContextCompat.getColor(
//                                binding.root.context,
//                                R.color.item_icon_tint_select
//                            )
//                        )
//                    )
                } else {
                    (background as? GradientDrawable)?.let {
                        ObjectAnimator.ofArgb(it, "color", ContextCompat.getColor(context, R.color.item_bg))
                            .setDuration(500)
                            .start()
                    }
                    Glide.with(this)
                        .load(item.iconPath.parseToBitmap(context))
                        .into(this)
//                    ImageViewCompat.setImageTintList(
//                        binding.icon,
//                        ColorStateList.valueOf(
//                            ContextCompat.getColor(
//                                binding.root.context,
//                                R.color.item_icon_tint_unselect
//                            )
//                        )
//                    )
                }

            }

            binding.root.setOnClickListener {
                eventListener.onMasterClick(item, adapterPosition)
            }
        }
    }
}