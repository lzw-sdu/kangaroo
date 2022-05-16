package com.faceunity.app_ptag.view_model

import androidx.lifecycle.ViewModel
import com.faceunity.core.entity.FUBundleData
import com.faceunity.core.entity.FUColorRGBData
import com.faceunity.core.faceunity.FURenderKit
import com.faceunity.core.model.prop.gesture.GestureRecognition
import com.faceunity.core.model.prop.sticker.Sticker
import com.faceunity.editor_ptag.store.DevAIRepository
import com.faceunity.editor_ptag.store.DevAvatarManagerRepository
import com.faceunity.editor_ptag.store.DevDrawRepository

/**
 * AI 能力的封装 ViewModel
 */
class FuDriveViewModel: ViewModel() {

    private var currentBackgroundColor: FUColorRGBData? = null
    private var currentBackgroundBundle: FUBundleData? = null


    fun openArMode() {
        DevDrawRepository.getScene().let {
            DevAIRepository.openArModeSmart(it)
            currentBackgroundColor = it.getBackgroundColor()
            currentBackgroundBundle = it.getBackgroundBundle()
            it.setBackgroundColor(null)
            it.setBackgroundBundle(null)
        }
    }

    fun closeArMode() {
        DevDrawRepository.getScene().let {
            DevAIRepository.closeArModeSmart(it)
            it.setBackgroundColor(currentBackgroundColor)
            it.setBackgroundBundle(currentBackgroundBundle)
        }
    }

    fun openFaceTrack() {
        DevDrawRepository.getScene()?.let { DevAIRepository.openFaceTrack(it) }

    }

    fun closeFaceTrack() {
        DevDrawRepository.getScene()?.let { DevAIRepository.closeFaceTrack(it) }
    }

    fun openBodyFullTrack() {
        DevDrawRepository.getScene()?.let { DevAIRepository.openBodyFullTrack(it) }

    }

    fun openBodyHalfTrack() {
        DevDrawRepository.getScene()?.let { DevAIRepository.openBodyHalfTrack(it) }

    }

    fun closeBodyTrack() {
        DevDrawRepository.getScene()?.let { DevAIRepository.closeBodyTrack(it) }
    }

    fun setBodyFollowModeFix() {
        DevAIRepository.setBodyFollowModeFix()
    }

    fun setBodyFollowModeAlight() {
        DevAIRepository.setBodyFollowModeAlight()
    }

    fun setBodyFollowModeStage() {
        DevAIRepository.setBodyFollowModeStage()
    }

}