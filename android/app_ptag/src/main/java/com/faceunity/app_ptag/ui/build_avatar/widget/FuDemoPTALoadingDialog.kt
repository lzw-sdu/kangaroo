package com.faceunity.app_ptag.ui.build_avatar.widget

import android.content.Context
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.faceunity.app_ptag.R
import com.faceunity.app_ptag.weight.FuBaseDialog

/**
 *
 */
class FuDemoPTALoadingDialog(ctx: Context) : FuBaseDialog(ctx) {
    override fun getLayoutId() = R.layout.dialog_fu_demo_pta_loading

    override fun afterCreate() {
        setCanceledOnTouchOutside(false)
        Glide.with(ctx)
            .load(R.drawable.loading_pta)
            .into(findViewById(R.id.loading_image))
    }

    override fun resetHeight() = ViewGroup.LayoutParams.MATCH_PARENT
}