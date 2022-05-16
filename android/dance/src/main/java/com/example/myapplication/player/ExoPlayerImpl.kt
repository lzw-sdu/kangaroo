package com.example.myapplication.player

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.Surface
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.video.VideoSize
import com.sdu.kangaroo.alpha_player.model.VideoInfo
import com.sdu.kangaroo.alpha_player.player.AbsPlayer


class ExoPlayerImpl(private val context: Context) : AbsPlayer(context) {

    private lateinit var exoPlayer: ExoPlayer
    private val dataSource: DefaultDataSource.Factory = DefaultDataSource.Factory(context)
    private var videoSource: MediaSource? = null

    private var currVideoWidth: Int = 0
    private var currVideoHeight: Int = 0
    private var isLooping: Boolean = false

    private val exoPlayerListener: Player.Listener = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            when (playbackState) {
                Player.STATE_READY -> {
                    preparedListener?.onPrepared()
                }
                Player.STATE_ENDED -> {
                    completionListener?.onCompletion()
                }
                else -> {
                }
            }
        }

        override fun onPlayerError(error: PlaybackException) {
            errorListener?.onError(0, 0, "ExoPlayer on error: " + Log.getStackTraceString(error))
        }

        override fun onVideoSizeChanged(videoSize: VideoSize) {
            currVideoWidth = videoSize.width
            currVideoHeight = videoSize.height
        }

        override fun onRenderedFirstFrame() {
            firstFrameListener?.onFirstFrame()
        }
    }

    /**
     * Maybe will init mediaPlayer on sub thread.
     */
    override fun initMediaPlayer() {
        exoPlayer = ExoPlayer.Builder(context).build()
        exoPlayer.addListener(exoPlayerListener)
    }

    /**
     * Sets the Surface to be used as the sink for the video portion of the media.
     */
    override fun setSurface(surface: Surface) {
        exoPlayer.setVideoSurface(surface)
    }

    /**
     * Sets the data source to use.
     *
     * @param path the path of the file you want to play.
     */
    override fun setDataSource(dataPath: String) {
        videoSource = ProgressiveMediaSource.Factory(dataSource)
            .createMediaSource(MediaItem.fromUri(Uri.parse(dataPath)))
    }

    override fun prepareAsync() {
        exoPlayer.prepare(videoSource!!)
        exoPlayer.playWhenReady = true
    }

    override fun start() {
        exoPlayer.playWhenReady = true
    }

    override fun pause() {
        exoPlayer.playWhenReady = false
    }

    override fun stop() {
        exoPlayer.stop()
    }

    override fun reset() {
        exoPlayer.stop(true)
    }

    override fun release() {
        exoPlayer.release()
    }

    override fun setLooping(looping: Boolean) {
        this.isLooping = looping
    }

    override fun setScreenOnWhilePlaying(onWhilePlaying: Boolean) {
    }

    override fun getVideoInfo(): VideoInfo {
        return VideoInfo(currVideoWidth, currVideoHeight)
    }

    override fun getPlayerType(): String {
        return "ExoPlayerImpl"
    }
}