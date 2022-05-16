package com.faceunity.app_ptag.util

import com.faceunity.app_ptag.compat.SPStorageFieldImpl
import com.faceunity.fupta.avatar_data.entity.resource.ProjectResource

/**
 * 用于解决 project.json 更新后，预置的 avatar_list 更新时需要移出旧预置 Avatar 的问题。
 * 该问题最合理的解决方式是对不同类别（预置、形象生成、编辑）分不同的文件夹存储，或者做 Tag 区分。
 * 当下的解决办法是，每次更新检查 project.json，并将预置的 Avatar 存到该模块。然后对旧的 Avatar 移除。
 */
class DefaultAvatarHelper(
    val fuStorageField: SPStorageFieldImpl
) {

    private var defaultAvatarSet: MutableSet<String>
        set(value) = fuStorageField.setStringSet("defaultAvatarSet", value)
        get() = fuStorageField.getAsStringSet("defaultAvatarSet").toMutableSet()


    fun updateProjectJson(project: ProjectResource, onNeedRemoveAvatar:(Set<String>)->Boolean) {
        val newAvatarList = project.avatar_list.map { it.avatar_id }
        defaultAvatarSet.addAll(newAvatarList)
        val needRemoveList = defaultAvatarSet.filter { !newAvatarList.contains(it) }.toSet()
        onNeedRemoveAvatar(needRemoveList).let {
            if (it) {
                defaultAvatarSet.removeAll(needRemoveList)
            }
        }
    }

}