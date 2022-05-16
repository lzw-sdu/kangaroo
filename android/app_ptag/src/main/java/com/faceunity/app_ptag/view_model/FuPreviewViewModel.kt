package com.faceunity.app_ptag.view_model

import android.view.animation.DecelerateInterpolator
import androidx.lifecycle.ViewModel
import com.faceunity.core.avatar.model.Avatar
import com.faceunity.editor_ptag.store.DevDrawRepository
import com.faceunity.pta.pta_core.util.expand.equalsDelta
import kotlin.concurrent.thread
import kotlin.math.absoluteValue
import kotlin.math.sign

/**
 * 角色展示的一些辅助功能的 ViewModel
 */
class FuPreviewViewModel : ViewModel() {
    private var controlAvatarIndex = 0

    /**
     * 设置当 Scene 有多个角色时，操纵哪一个 Avatar
     */
    fun setControlAvatarIndex(index: Int) {
        controlAvatarIndex = index
    }

    private fun loadControlAvatar(): Avatar? {
        val avatar = DevDrawRepository.getPreviewAvatarByIndex(controlAvatarIndex)
        if (avatar == null) {

        }
        return avatar
    }

    fun rotateAvatar(offset: Float) {
        if (offset.equalsDelta(0f)) return
        val avatar = loadControlAvatar() ?: return
        avatar.transForm.setRotDelta(offset)
    }

    fun moveVerticalAvatar(offset: Float) {
        if (offset.equalsDelta(0f)) return
        val avatar = loadControlAvatar() ?: return
        avatar.transForm.setTranslateDelta(offset)
    }


    fun scaleAvatar(offset: Float) {
        if (offset.equalsDelta(0f)) return
        val avatar = loadControlAvatar() ?: return
        avatar.transForm.setScaleDelta(offset)
    }

    private var isCancelRollAvatar = false
    private var isRollNow = false

    fun cancelRollAvatar() {
        isCancelRollAvatar = true
    }

    /**
     * 带惯性滚动的旋转 Avatar
     */
    fun rollAvatar(offset: Float) {
        if (isRollNow) return //如果正在滚动，则忽略新的滚动事件
        isCancelRollAvatar = false
        if (offset.equalsDelta(0f)) return
        val avatar = loadControlAvatar() ?: return

        var moveX = 0.02f * offset.sign //固定从一个速度开始惯性滚动.可视情况改为依据 [offset] 的比例设置.
        if (offset.absoluteValue <= 0.02f) {
            moveX = offset
        }
        val interpolator = DecelerateInterpolator()
        var process = 0f
        thread {
            isRollNow = true
            while (process <= 1.0 && !isCancelRollAvatar) {
                val interpolation = interpolator.getInterpolation(process)
                avatar.transForm.setRotDelta(moveX * (1.0f - interpolation))
                process += 0.005f //
                Thread.sleep(33)
            }
            isRollNow = false
        }

    }
}