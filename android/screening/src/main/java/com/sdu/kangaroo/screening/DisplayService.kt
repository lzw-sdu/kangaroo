/*
 * Copyright (C) 2021 pedroSG94.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sdu.kangaroo.screening

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.pedro.rtplibrary.base.DisplayBase
import com.pedro.rtplibrary.rtsp.RtspDisplay
import com.pedro.rtsp.rtsp.Protocol
import com.pedro.rtsp.utils.ConnectCheckerRtsp
import com.sdu.kangaroo.screenrecorder.R


/**
 * Basic RTMP/RTSP service streaming implementation with camera2
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
class DisplayService : Service() {

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        Log.i(TAG, "RTP Display service create")
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_HIGH)
            notificationManager?.createNotificationChannel(channel)
        }
        keepAliveTrick()
    }

    private fun keepAliveTrick() {
        val notification = NotificationCompat.Builder(this, channelId)
            .setOngoing(false)
            .build()
        startForeground(1, notification)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        INSTANCE = this
        Log.i(TAG, "RTP Display service started")
        displayBase = RtspDisplay(baseContext, true, connectCheckerRtp)
        displayBase?.glInterface?.setForceRender(true)
        return START_STICKY
    }

    companion object {
        private const val TAG = "DisplayService"
        private const val channelId = "rtpDisplayStreamChannel"
        const val notifyId = 123456
        var INSTANCE: DisplayService? = null
    }

    private var notificationManager: NotificationManager? = null
    private var displayBase: DisplayBase? = null

    fun sendIntent(): Intent? {
        return displayBase?.sendIntent()
    }

    fun isStreaming(): Boolean {
        return displayBase?.isStreaming ?: false
    }

    fun isRecording(): Boolean {
        return displayBase?.isRecording ?: false
    }

    fun stopStream() {
        if (displayBase?.isStreaming == true) {
            displayBase?.stopStream()
            notificationManager?.cancel(notifyId)
        }
    }

    private val connectCheckerRtp = object : ConnectCheckerRtsp {
        override fun onAuthErrorRtsp() {
            showNotification("Stream auth error")
        }

        override fun onAuthSuccessRtsp() {
            showNotification("Stream auth success")
        }

        override fun onConnectionFailedRtsp(reason: String) {
            showNotification("Stream connection failed")
            Log.i(TAG, "RTP service destroy")
        }

        override fun onConnectionStartedRtsp(rtspUrl: String) {
            Log.i(TAG, "RTP service start")
        }

        override fun onConnectionSuccessRtsp() {
            DisPlayActivity.callback.onStartRecord()
            showNotification("Stream started")
            Log.i(TAG, "RTP service start");
        }

        override fun onDisconnectRtsp() {
            showNotification("Stream stopped")
        }

        override fun onNewBitrateRtsp(bitrate: Long) {
            Log.i(TAG, "New Bitrate Rtsp")
        }

    }

    private fun showNotification(text: String) {
        val notification = NotificationCompat.Builder(baseContext, channelId)
            .setContentTitle("RTP Display Stream")
            .setContentText(text)
            .setOngoing(false)
            .setSmallIcon(R.drawable.ic_stat_recording)
            .build()
        notificationManager?.notify(notifyId, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "RTP Display service destroy")
        stopStream()
        INSTANCE = null
    }

    fun prepareStreamRtp(endpoint: String, resultCode: Int, data: Intent) {
        stopStream()
        displayBase = RtspDisplay(baseContext, true, connectCheckerRtp)
        if (displayBase is RtspDisplay) {
            (displayBase as RtspDisplay).setProtocol(Protocol.UDP);
        }
        displayBase?.setIntentResult(resultCode, data)
        displayBase?.glInterface?.setForceRender(true)
    }

    fun startStreamRtp(endpoint: String) {
        if (displayBase?.isStreaming != true) {
            if (displayBase?.prepareVideo(1080, 1920, 60, 1080 * 1920 * 60 / 8, 90, 300, -1, -1, 1)
                == true && displayBase?.prepareAudio() == true
            ) {
                displayBase?.startStream(endpoint)
            }
        } else {
            showNotification("You are already streaming :(")
        }
    }
}