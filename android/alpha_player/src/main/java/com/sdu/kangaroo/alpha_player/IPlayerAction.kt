package com.sdu.kangaroo.alpha_player

import com.sdu.kangaroo.alpha_player.model.ScaleType

/**
 *
 *
 * Interface definition for a callback to be invoked when a player on some condition.
 */
interface IPlayerAction {

    /**
     * Called when the media source is prepared.
     *
     * @param videoWidth    the media source width information.
     * @param videoHeight   the media source height information.
     * @param scaleType     the scale type be defined.
     */
    fun onVideoSizeChanged(videoWidth: Int, videoHeight: Int, scaleType: ScaleType)

    /**
     * Called when the media source is ready to start.
     */
    fun startAction()

    /**
     * Called when the end of a media source is reached during playback.
     */
    fun endAction()
}