package com.faceunity.app_ptag.weight.avatar_manager.entity

/**
 * Created on 2021/12/13 0013 20:34.


 */
class FuAvatarContainer(val avatarList: MutableList<FuAvatarWrapper>, var selectId: MutableList<String>) {

    override fun toString(): String {
        return "FuAvatarContainer(avatarList=$avatarList, selectId=$selectId)"
    }
}