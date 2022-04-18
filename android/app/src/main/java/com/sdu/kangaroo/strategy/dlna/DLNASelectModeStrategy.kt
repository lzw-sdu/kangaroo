package com.sdu.kangaroo.strategy.dlna

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import com.sdu.kangaroo.MainActivity
import com.sdu.kangaroo.MethodChannelStrategy
import com.sdu.kangaroo.handler.DLNAHandler
import com.sdu.kangaroo.utils.Util
import com.sdu.kangaroo.utils.start_avtivity_result.StartActivityActionCallBack
import com.sdu.kangaroo.utils.start_avtivity_result.StartActivityManger
import com.ykbjson.lib.screening.bean.MediaInfo
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

class DLNASelectModeStrategy : MethodChannelStrategy {
    private var intent: Intent? = null
    override fun handle(call: MethodCall, result: MethodChannel.Result) {
        when (call.arguments as String) {
            "IMAGE" -> selectImage()
            "VIDEO" -> selectVideo()
            "AUDIO" -> selectAudio()
            "SCREEN" -> screeningPhone()
        }
        intent?.let {
            StartActivityManger.instance.requestStartActivityAction(
                MainActivity.CODE_REQUEST_MEDIA,
                DLNAHandler.instance.mContext as Activity,
                it,
                object : StartActivityActionCallBack {
                    override fun onSuccess(resultCode: Int, data: Intent?) {
                        val uri = data!!.data
                        val path: String? =
                            if (Build.VERSION_CODES.KITKAT <= Build.VERSION.SDK_INT) {
                                Util.getRealPathFromUriAboveApi19(
                                    DLNAHandler.instance.mContext,
                                    uri
                                )
                            } else {
                                uri!!.path
                            }
                        DLNAHandler.instance.mMediaPath = path
                    }

                    override fun onFail(resultCode: Int, data: Intent?) {
                    }

                })
        }
        result.success(null)
    }

    companion object {
        val instance: DLNASelectModeStrategy by lazy { DLNASelectModeStrategy() }
        val TAG = "DLNASelectMode"
    }

    private fun selectVideo() {
        DLNAHandler.instance.curItemType = MediaInfo.TYPE_VIDEO
        intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
    }

    fun selectAudio() {
        DLNAHandler.instance.curItemType = MediaInfo.TYPE_AUDIO
        intent = Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI)

    }

    private fun selectImage() {
        DLNAHandler.instance.curItemType = MediaInfo.TYPE_IMAGE
        intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
    }

    private fun screeningPhone() {
        DLNAHandler.instance.curItemType = MediaInfo.TYPE_MIRROR
    }

}
