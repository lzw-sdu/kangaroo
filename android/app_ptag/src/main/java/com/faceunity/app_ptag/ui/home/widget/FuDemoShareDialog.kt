package com.faceunity.app_ptag.ui.home.widget

import android.content.Context
import android.graphics.Bitmap
import android.view.ViewGroup
import com.faceunity.app_ptag.R
import com.faceunity.app_ptag.databinding.DialogFuDemoShareBinding
import com.faceunity.app_ptag.weight.FuBaseDialog

/**
 *
 */
class FuDemoShareDialog(ctx: Context, val bitmap: Bitmap, val listener: OnClickListener? = null) : FuBaseDialog(ctx) {
    override fun getLayoutId() = R.layout.dialog_fu_demo_share

    override fun afterCreate() {
        setCanceledOnTouchOutside(false)
        val binding = DialogFuDemoShareBinding.bind(mRootView)

        binding.previewImageView.setImageBitmap(bitmap)
        binding.closeBtn.setOnClickListener {
            dismiss()
        }
        binding.saveBtn.setOnClickListener {
            listener?.onSaveClick()
        }
        binding.copyBtn.setOnClickListener {
            listener?.onCopyClick()
        }
    }

    override fun resetHeight(): Int {
        return ViewGroup.LayoutParams.MATCH_PARENT
    }

    interface OnClickListener {
        fun onSaveClick()
        fun onCopyClick()
    }
}