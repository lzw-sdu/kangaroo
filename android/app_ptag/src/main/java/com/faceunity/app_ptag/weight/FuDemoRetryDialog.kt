package com.faceunity.app_ptag.weight

import android.content.Context
import android.widget.TextView
import com.faceunity.app_ptag.R

/**
 *
 */
class FuDemoRetryDialog(ctx: Context, val tipText: String = "", var callback: Callback? = null) : FuBaseDialog(ctx) {
    override fun getLayoutId() = R.layout.dialog_fu_demo_retry

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
//        if (tipText.isNotBlank()) {
//            mRootView.findViewById<TextView>(R.id.desc).apply {
//                text = tipText
//                visible()
//            }
//        }
    }



    interface Callback {
        fun onCancel()
        fun onFinish()
    }
}