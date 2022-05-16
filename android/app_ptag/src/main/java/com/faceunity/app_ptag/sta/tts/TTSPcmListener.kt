package com.faceunity.sta.tts


/**
 *
 * DESC：TTS调用过程回调-支持Pcm返回
 * Created on 2022/2/16
 * @author Jason Lu
 *
 */
interface TTSPcmListener {
    /**
     * 开始合成
     */
    fun onStart()

    /**
     * 数据准备完成
     */
    fun onPrepared()

    /**
     * 合成中间数据返回
     * @param align String 播报文本
     * @param timestamp String 时间戳数据
     * @param data ByteArray 音频
     * @param isEnd Boolean 是否结束
     */
    fun onReceived(align: String, timestamp: String, data: ByteArray, isEnd: Boolean)

    /**
     * 合成完成
     */
    fun onCompleted()

    /**
     * 合成出错
     * @param code Int 错误码
     * @param msg String 错误信息
     */
    fun onError(code: String, msg: String)


}