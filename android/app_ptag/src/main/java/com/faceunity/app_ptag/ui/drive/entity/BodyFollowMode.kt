package com.faceunity.app_ptag.ui.drive.entity

/**
 * Created on 2021/12/10 0010 16:39.


 */
enum class BodyFollowMode {
    Fix, Stage, Align;

    fun showText(): String {
        return when(this) {
            Fix -> "FIX（固定）"
            Stage -> "STAGE（稳定）"
            Align -> "ALIGN（全跟随）"
        }
    }
}