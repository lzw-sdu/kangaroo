package com.faceunity.app_ptag.util

import com.faceunity.editor_ptag.cache.FuCacheResource
import com.faceunity.editor_ptag.data_center.FuDevDataCenter
import com.faceunity.editor_ptag.util.FuLog
import com.faceunity.editor_ptag.util.FuResourceCheck
import com.faceunity.pta.pta_core.model.AvatarInfo
import com.faceunity.pta.pta_core.model.SceneInfo

/**
 *
 */
object CheckResourceHelper {

    fun checkAvatarAndScene(avatarInfo: AvatarInfo, sceneInfo: SceneInfo): FuResourceCheck.ResourceStatusResult {
        val resManager = FuDevDataCenter.getCloudResourceManager()!!
        val checkAvatarResult = if (FuCacheResource.controllerConfigBundle != null && FuCacheResource.itemListText != null) {
            FuResourceCheck.checkAvatarBundleStatus(avatarInfo.avatar, resManager, FuCacheResource.controllerConfigBundle!!, FuCacheResource.itemListText!!)
        } else {
            FuResourceCheck.checkBundleStatus(avatarInfo.avatarResource.bundle_list, resManager)
        }
        val checkSceneResult = FuResourceCheck.checkBundleStatus(sceneInfo.getUsedBundleFileIdList(), resManager)
        val checkResult = checkAvatarResult.merge(checkSceneResult)
        return checkResult
    }
}