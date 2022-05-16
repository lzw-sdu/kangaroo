package com.faceunity.app_ptag.weight

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import com.faceunity.app_ptag.R
import com.google.android.material.progressindicator.CircularProgressIndicator

/**
 *
 */
class DownloadingDialog(ctx: Context) : FuBaseDialog(ctx) {
    private var loadingTextView: TextView? = null

    override fun getLayoutId() = R.layout.dialog_downloading

    override fun afterCreate() {
        setCanceledOnTouchOutside(false)
        mRootView.findViewById<CircularProgressIndicator>(R.id.progress_bar).apply {
            isIndeterminate = true
            show()
        }
        loadingTextView = mRootView.findViewById<TextView>(R.id.loading_text)
    }

    fun updateText(content: String) {
        loadingTextView?.text = content
    }

    override fun resetHeight(): Int {
        return ViewGroup.LayoutParams.MATCH_PARENT
    }

    override fun onBackPressed() {

    }
}