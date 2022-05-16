package com.sdu.kangaroo.alpha_player.render

import android.graphics.SurfaceTexture
import android.opengl.GLSurfaceView
import android.view.Surface
import com.sdu.kangaroo.alpha_player.model.ScaleType
import com.sdu.kangaroo.alpha_player.widget.GLTextureView

/**
 *
 *
 *  A generic renderer with opengles interface.
 */
interface IRender : GLTextureView.Renderer, GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener {

    fun setSurfaceListener(surfaceListener: SurfaceListener)

    /**
     * Called when the AlphaVideoView received first frame from media player.
     */
    fun onFirstFrame()

    /**
     * Called when the AlphaVideoView received completion event from media player.
     */
    fun onCompletion()

    /**
     * set scaleType for VideoRenderer
     */
    fun setScaleType(scaleType: ScaleType)

    /**
     * Called when the AlphaVideoView touch onMeasure() callback or the media source be parsed mate data.
     */
    fun measureInternal(viewWidth: Float, viewHeight: Float, videoWidth: Float, videoHeight: Float)

    interface SurfaceListener {

        /**
         * Called when Surface is prepared.
         */
        fun onSurfacePrepared(surface: Surface)

        /**
         * Called when Surface is Destroyed.
         */
        fun onSurfaceDestroyed()
    }
}