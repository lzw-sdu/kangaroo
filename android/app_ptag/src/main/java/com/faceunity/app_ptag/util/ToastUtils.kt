package com.faceunity.app_ptag.util

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.faceunity.app_ptag.R
import com.faceunity.editor_ptag.util.FuLog
import com.faceunity.toolbox.utils.FUDensityUtils

/**
 * Created on 2022/2/17 0017 11:19.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
object ToastUtils {

    fun showSuccessToast(context: Context, msg: String) {
        val toast = Toast(context)
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layoutInflater.inflate(R.layout.toast_custom, null)
        view.findViewById<TextView>(R.id.toast_text).text = msg
        toast.view = view
        toast.setGravity(Gravity.TOP, 0, FUDensityUtils.dp2px(context, 70f))
        toast.duration = Toast.LENGTH_SHORT
        toast.show()
        FuLog.info("showSuccessToast: $msg")
    }

    fun showFailureToast(context: Context, msg: String) {
        val toast = Toast(context)
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layoutInflater.inflate(R.layout.toast_custom, null)
        view.findViewById<ImageView>(R.id.toast_icon).setImageResource(R.drawable.icon_toast_failure)
        view.findViewById<TextView>(R.id.toast_text).text = msg
        toast.view = view
        toast.setGravity(Gravity.TOP, 0, FUDensityUtils.dp2px(context, 70f))
        toast.duration = Toast.LENGTH_SHORT
        toast.show()
        FuLog.info("showFailureToast: $msg")
    }
}