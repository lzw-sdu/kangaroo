package com.faceunity.app_ptag.ui.edit.weight.control.adapter

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.faceunity.app_ptag.databinding.ItemMinorCategoryBinding
import com.faceunity.app_ptag.ui.edit.weight.control.AvatarControlEventListener
import com.faceunity.app_ptag.ui.edit.weight.control.adapter.diff.MinorCategoryDiffCallback
import com.faceunity.app_ptag.R
import com.faceunity.app_ptag.ui.edit.entity.control.MinorCategoryModel

/**
 * Created on 2021/7/22 0022 17:13.


 */
internal class MinorCategoryAdapter(
    private val eventListener: AvatarControlEventListener
) : ListAdapter<MinorCategoryModel, MinorCategoryAdapter.MinorViewHolder>(MinorCategoryDiffCallback),
    ClickRecordInterface by ClickRecordHelper() {

    var tintColor: Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MinorViewHolder {
        return MinorViewHolder(
            ItemMinorCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            eventListener
        )
    }

    override fun onBindViewHolder(holder: MinorViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MinorViewHolder(
        private val binding: ItemMinorCategoryBinding,
        private val eventListener: AvatarControlEventListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MinorCategoryModel) {
            val context = binding.root.context
            val isSelect = getNowSelectPosition() == adapterPosition

//            binding.background.apply {
//                if (isSelect) {
//                    val color = tintColor ?: Color.DKGRAY
////                    (binding.background.drawable as? ColorDrawable)?.let {
////                        ObjectAnimator.ofArgb(it, "color", color)
////                            .setDuration(500)
////                            .start()
////                    }
//                    (drawable as? ColorDrawable)?.color = color
//                } else {
//                    val color = ContextCompat.getColor(context, R.color.item_bg)
////                    (binding.background.drawable as? ColorDrawable)?.let {
////                        ObjectAnimator.ofArgb(it, "color", color)
////                            .setDuration(500)
////                            .start()
////                    }
//                    (drawable as? ColorDrawable)?.color = color
//                }
//
//            }

            binding.icon.apply {
                if (isSelect) {
                    val color = tintColor ?: Color.DKGRAY
                    (background as? GradientDrawable)?.let {
                        it.setColor(color)
                    }
//                    ImageViewCompat.setImageTintList(binding.icon, ColorStateList.valueOf(ContextCompat.getColor(binding.root.context, R.color.item_icon_tint_select)))
                    Glide.with(this)
                        .load(item.selectIconPath.parseToBitmap(context))
                        .into(this)
                } else {
                    val color = ContextCompat.getColor(context, R.color.item_bg)
                    (background as? GradientDrawable)?.let {
                        it.setColor(color)
                    }
//                    ImageViewCompat.setImageTintList(binding.icon, ColorStateList.valueOf(ContextCompat.getColor(binding.root.context, R.color.item_minor_icon_tint_unselect)))
                    Glide.with(this)
                        .load(item.iconPath.parseToBitmap(context))
                        .into(this)
                }

            }

            if (item.iconPath.isNull()) {
                binding.name.text = item.name
                binding.name.setTextColor(tintColor ?: Color.GREEN)
            } else {
                binding.name.text = ""
            }


            binding.root.setOnClickListener {
                eventListener.onMinorClick(item, adapterPosition)
            }
        }
    }
}