package com.faceunity.app_ptag.weight.avatar_manager.parser

import com.faceunity.app_ptag.weight.avatar_manager.entity.FuAvatarWrapper
import com.faceunity.pta.pta_core.model.AvatarInfo

/**
 * 将 [AvatarInfo] 解析为 [FuAvatarWrapper]
 */
class FuAvatarContainerParser {

    fun parserAvatarInfoToFuAvatarWrapper(avatarInfo: AvatarInfo): FuAvatarWrapper {
        return FuAvatarWrapper(
            avatarInfo.id,
            avatarInfo.avatarLogo,
            FuAvatarWrapper.Type.UnKnow
        )
    }
}