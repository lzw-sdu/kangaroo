package com.faceunity.sta.tts

import android.content.Context


/**
 *
 * DESC：TTS播报抽象类
 * Created on 2022/2/16
 * @author Jason Lu
 *
 */
abstract class AbsTTSSpeaker {

    /**
     * 初始化
     */
    abstract fun init(context: Context)

    /**
     * 绑定回调
     * @param listener TTSPcmListener
     */
    abstract fun bindTTSPcmListener(listener: TTSPcmListener)

    /**
     * 设置语音合成名称
     * @param name String
     */
    abstract fun setSpeakerName(name: String)


    /**
     * 播报
     */
    abstract fun speak(text: String)

    /**
     * 取消播报
     */
    abstract fun cancel()

    /**
     * 释放资源
     */
    abstract fun release()

}