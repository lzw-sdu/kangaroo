package com.faceunity.app_ptag.ui.home.widget

import android.content.Context
import com.faceunity.app_ptag.R
import com.faceunity.app_ptag.databinding.DialogFuDemoCopySuccessBinding
import com.faceunity.app_ptag.weight.FuBaseDialog

/**
 *
 */
class FuDemoCopySuccessDialog(ctx: Context, val copyText: String) : FuBaseDialog(ctx) {
    override fun getLayoutId(): Int {
        return R.layout.dialog_fu_demo_copy_success
    }

    override fun afterCreate() {
        val binding = DialogFuDemoCopySuccessBinding.bind(mRootView)

        binding.copyTextView.text = copyText
    }

}
