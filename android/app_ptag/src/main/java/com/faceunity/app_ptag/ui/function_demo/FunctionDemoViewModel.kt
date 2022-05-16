package com.faceunity.app_ptag.ui.function_demo

import androidx.lifecycle.ViewModel
import com.faceunity.editor_ptag.cache.FuCacheResource
import com.faceunity.editor_ptag.data_center.FuDevDataCenter
import com.faceunity.editor_ptag.store.DevAvatarManagerRepository
import com.faceunity.editor_ptag.store.DevDrawRepository
import com.faceunity.editor_ptag.store.DevSceneManagerRepository
import com.faceunity.editor_ptag.util.FuLog

class FunctionDemoViewModel : ViewModel() {

    fun initDefaultAvatar() {
        //进行一些数据缓存
        FuCacheResource.init(FuDevDataCenter.resourceParser,  FuDevDataCenter.resourcePath, FuDevDataCenter.resourceLoader)

        //加载 Avatar 列表
        DevAvatarManagerRepository.loadAvatar()
        val avatarInfo = DevAvatarManagerRepository.switchFirstAvatarAndGet() //切换成第一个 Avatar 并获得
        if (avatarInfo == null) {
            FuLog.warn("获取 Avatar 为空")
            return
        }

        //加载 Scene 列表
        DevSceneManagerRepository.initSceneConfigNotRepeat()
        val sceneInfo = DevSceneManagerRepository.filterGenderDefaultSceneFirst(avatarInfo.gender()) //找到符合 avatar 的 Scene 配置
        if (sceneInfo == null) {
            FuLog.warn("符合条件的 SceneInfo 找不到，无法加载 Scene 与 Avatar")
            return
        }
        DevDrawRepository.loadSceneAndAvatar(sceneInfo, avatarInfo)
    }
}