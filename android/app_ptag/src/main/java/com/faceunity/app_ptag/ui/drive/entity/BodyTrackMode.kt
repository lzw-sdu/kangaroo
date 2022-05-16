package com.faceunity.app_ptag.ui.drive.entity

/**
 * Created on 2021/12/10 0010 16:36.


 */
enum class BodyTrackMode {
    Full, Half, Close;

    fun showText(): String {
        return when(this) {
            Full -> "全身"
            Half -> "半身"
            Close -> "关闭"
        }
    }
}