package com.faceunity.app_ptag.ui.edit.entity.control

import android.content.Context
import android.graphics.Bitmap
import com.faceunity.toolbox.media.FUMediaUtils

/**
 * 一个用于旧版 SDK 的图片封装类。现废弃，不推荐使用
 */
@Deprecated("DataCenter 更新后不再需要该图片封装")
class FUImage(val path: String?) {

    fun parseToBitmap(context: Context): Bitmap? {
        if (path == null) return null
        return FUMediaUtils.loadBitmap(context, path)
    }

    fun isNull(): Boolean = path == null

    fun isUrl(): Boolean = path?.startsWith("http") ?: false

    companion object {
        fun createFromModel(path: String?): FUImage {
            return FUImage(path)
        }

        fun createByUrl(url: String?): FUImage {
            return FUImage(url)
        }

        fun createNull(): FUImage {
            return FUImage(null)
        }
    }
}