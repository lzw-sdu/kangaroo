package com.sdu.kangaroo.alpha_player.controller

import android.view.Surface

/**
 *
 */
interface IPlayerControllerExt : IPlayerController {

    fun surfacePrepared(surface: Surface)
}