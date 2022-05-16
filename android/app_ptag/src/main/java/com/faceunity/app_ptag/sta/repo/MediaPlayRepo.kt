package com.faceunity.sta.repo

import android.content.Context
import com.faceunity.sta.core.enumeration.FUAudioProgressTypeEnum
import com.faceunity.sta.core.media.BaseMediaPlayer
import com.faceunity.sta.core.media.FUPlayerConfig
import com.faceunity.sta.core.media.MediaPlayerHandler


/**
 *
 * DESC：播放器相关数据配置
 * Created on 2022/2/10
 * @author Jason Lu
 *
 */
class MediaPlayRepo {

    /* 媒体播放器 */
    private lateinit var mMediaPlayerHandler: MediaPlayerHandler

    /**
     * 初始化构造播放器
     * @param listener MediaPlayerListener
     * @return MediaPlayerHandler
     */
    fun initMediaPlay(context: Context, listener: BaseMediaPlayer.MediaPlayerListener) {
        mMediaPlayerHandler = MediaPlayerHandler()
        val config = getPlayerConfig()
        mMediaPlayerHandler?.initPlayer(config, listener)
    }


    /**
     * 获取播放器配置
     * @return FUPlayerConfig
     */
    private fun getPlayerConfig(): FUPlayerConfig = FUPlayerConfig()


    /**
     * 输入播放数据
     * @param pcmBytes ByteArray
     * @param progressType FUAudioProgressTypeEnum
     */
    fun appendPcmBytes(pcmBytes: ByteArray, progressType: FUAudioProgressTypeEnum) {
        if (pcmBytes == null) return
        mMediaPlayerHandler?.appendPcmBytes(pcmBytes, progressType)
    }

    /**
     * 停止播放
     */
    fun stopMediaPlayer() {
        mMediaPlayerHandler?.stopMediaPlayer()
    }

    /**
     * 获取当前播放进度
     * @return Long
     */
    fun getCurrentPosition() = mMediaPlayerHandler?.currentPosition

    /**
     * 释放资源
     */
    fun releaseMediaPlayer() {
        mMediaPlayerHandler?.releaseMediaPlayer()
    }


    /**
     * 音频采样率 sta目前只支持16k的采样率
     * @return Int
     */
    fun getBytesPerSeconds(): Int {
        return 16000
    }

}