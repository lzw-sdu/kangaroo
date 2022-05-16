package com.sdu.kangaroo.alpha_player.controller

import android.view.View
import android.view.ViewGroup
import com.sdu.kangaroo.alpha_player.IMonitor
import com.sdu.kangaroo.alpha_player.IPlayerAction
import com.sdu.kangaroo.alpha_player.model.DataSource

/**
 *
 */
interface IPlayerController {

    fun start(dataSource: DataSource)

    fun pause()

    fun resume()

    fun stop()

    fun reset()

    fun release()

    fun setVisibility(visibility: Int)

    fun setPlayerAction(playerAction: IPlayerAction)

    fun setMonitor(monitor: IMonitor)

    fun attachAlphaView(parentView: ViewGroup)

    fun detachAlphaView(parentView: ViewGroup)

    fun getView(): View

    fun getPlayerType(): String
}