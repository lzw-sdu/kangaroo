package com.sdu.kangaroo.screenrecorder;

import android.media.MediaCodec;

import java.nio.ByteBuffer;

/**
 * Description：录屏状态回调接口
 * <BR/>
 * Creator：yankebin
 * <BR/>
 * CreatedAt：2019-07-31
 */
public interface IRecorderCallback {

    void onPrepareRecord();

    void onStartRecord();

    void onRecording(long presentationTimeUs);

    void onStopRecord(Throwable error);

    void onDestroyRecord();

    void onMuxVideo(ByteBuffer h264Buffer, MediaCodec.BufferInfo bufferInfo);

    void onMuxAudio(ByteBuffer accBuffer, MediaCodec.BufferInfo bufferInfo);
}
