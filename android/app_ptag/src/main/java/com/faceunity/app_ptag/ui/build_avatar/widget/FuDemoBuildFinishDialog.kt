package com.faceunity.app_ptag.ui.build_avatar.widget

import android.content.Context
import android.widget.TextView
import com.faceunity.app_ptag.R
import com.faceunity.app_ptag.weight.FuBaseDialog

/**
 *
 */
class FuDemoBuildFinishDialog(ctx: Context, var callback: Callback? = null) : FuBaseDialog(ctx) {
    override fun getLayoutId() = R.layout.dialog_fu_demo_build_finish

    override fun afterCreate() {
        setCanceledOnTouchOutside(false)
        mRootView.findViewById<TextView>(R.id.cancel).apply {
            setOnClickListener {
                callback?.onCancel()
            }
        }
        mRootView.findViewById<TextView>(R.id.finish).apply {
            setOnClickListener {
                callback?.onFinish()
            }
        }
    }



    interface Callback {
        fun onCancel()
        fun onFinish()
    }
}