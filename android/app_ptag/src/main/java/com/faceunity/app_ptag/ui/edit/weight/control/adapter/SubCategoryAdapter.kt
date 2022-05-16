package com.faceunity.app_ptag.ui.edit.weight.control.adapter

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.faceunity.app_ptag.databinding.ItemSubCategoryBinding
import com.faceunity.app_ptag.databinding.ItemSubCategoryColorBinding
import com.faceunity.app_ptag.ui.edit.weight.control.AvatarControlEventListener
import com.faceunity.app_ptag.ui.edit.weight.control.adapter.diff.SubCategoryDiffCallback
import com.faceunity.app_ptag.R
import com.faceunity.app_ptag.ui.edit.entity.control.SubCategoryColorModel
import com.faceunity.app_ptag.ui.edit.entity.control.SubCategoryModel
import com.faceunity.app_ptag.ui.edit.entity.control.SubCategoryNormalModel
import com.faceunity.editor_ptag.util.gone
import com.faceunity.editor_ptag.util.visible

/**
 * Created on 2021/7/22 0022 17:13.
 */
internal class SubCategoryAdapter(
    private val eventListener: AvatarControlEventListener
) : ListAdapter<SubCategoryModel, RecyclerView.ViewHolder>(SubCategoryDiffCallback),
    ClickRecordInterface by ClickRecordHelper(),
    StyleRecordInterface by StyleRecordHelper() {

    companion object {
        private val NormalType = 0
        private val ColorType = 1
    }

    var tintColor: Int? = null


    override fun getItemViewType(position: Int): Int {
        return when(getItem(position).type) {
            SubCategoryModel.Type.Normal -> NormalType
            SubCategoryModel.Type.Color -> ColorType
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            NormalType -> SubViewHolder(
                ItemSubCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                eventListener
            )
            ColorType -> SubColorViewHolder(
                ItemSubCategoryColorBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                eventListener
            )
            else -> throw IllegalArgumentException("not support type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is SubViewHolder -> holder.bind(getItem(position) as SubCategoryNormalModel)
            is SubColorViewHolder -> holder.bind(getItem(position) as SubCategoryColorModel)
        }
    }

    inner class SubViewHolder(
        private val binding: ItemSubCategoryBinding,
        private val eventListener: AvatarControlEventListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SubCategoryNormalModel) {
            val context = binding.root.context
            binding.name.text = if (item.hasIcon()) {
                ""
            } else {
                item.id().split("/").last().replace(".bundle", "")
            }
            binding.name.setTextColor(tintColor ?: Color.BLACK)
            if (getNowSelectPosition() == adapterPosition && !item.isReset()) {
                binding.selectBg.visible()
            } else {
                binding.selectBg.gone()
            }
            when(downloadStyleSafe(item.useFUItem().fuEditBundleItem?.fileID)) {
                StyleRecordInterface.DownloadStyle.Normal -> {
                    binding.downloadImageView.gone()
                }
                StyleRecordInterface.DownloadStyle.NeedDownload -> {
                    binding.downloadImageView.apply {
                        visible()
                        setImageResource(R.drawable.icon_tab2_download)
                    }

                }
                StyleRecordInterface.DownloadStyle.Downloading -> {
                    binding.downloadImageView.apply {
                        visible()
                        setImageResource(R.drawable.icon_tab2_loading)
                        val anim = RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f).apply {
                            repeatCount = -1
                            duration = 1000
                            interpolator = LinearInterpolator()
                        }
                        animation = anim
                    }
                }
                StyleRecordInterface.DownloadStyle.Error -> {
                    binding.downloadImageView.apply {
                        visible()
                        setImageResource(R.drawable.icon_tab2_download)
                    }
                }
            }
            if (item.isReset()) {
                binding.icon.setImageResource(R.drawable.icon_tab2_no_choose)
                binding.background.gone()
            } else {
                if (item.iconPath.isUrl()) {
                    Glide.with(binding.icon)
                        .load(item.iconPath.path)
                        .error(R.drawable.icon_load_error)
                        .into(binding.icon)
                } else {
                    Glide.with(binding.icon)
                        .load(item.iconPath.parseToBitmap(context))
                        .error(R.drawable.icon_load_error)
                        .into(binding.icon)
                }

//                binding.background.visible()
            }
            binding.root.setOnClickListener {
                eventListener.onSubClick(item, adapterPosition)
            }
        }
    }

    inner class SubColorViewHolder(
        private val binding: ItemSubCategoryColorBinding,
        private val eventListener: AvatarControlEventListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SubCategoryColorModel) {
            val isSelect = getNowSelectPosition() == adapterPosition
            binding.background.apply {
                (drawable as? GradientDrawable)?.let {
                    it.setColor(ContextCompat.getColor(context, R.color.item_bg))
                }
            }
            binding.color.apply {
                if (isSelect) {
                    binding.color.animate()
                        .scaleX(1.1f)
                        .scaleY(1.1f)
                        .start()
                } else {
                    binding.color.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .start()
                }
//                ImageViewCompat.setImageTintList(binding.color, ColorStateList.valueOf(item.color))

                (drawable as? GradientDrawable)?.let {
                    it.setColor(item.color)
                }

            }

            binding.selectIcon.apply {
                if (isSelect) {
                    visible()
                } else {
                    gone()
                }

            }

            binding.root.setOnClickListener {
                eventListener.onSubClick(item, adapterPosition)
            }
        }
    }
}