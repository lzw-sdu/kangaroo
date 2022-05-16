package com.faceunity.app_ptag.weight.avatar_manager.entity

import androidx.recyclerview.widget.DiffUtil

/**
 * Created on 2021/12/13 0013 20:01.


 */
class FuAvatarWrapper(val id: String, val icon: String?, val type: Type) {

    override fun toString(): String {
        return "FuAvatarWrapper(id='$id', icon=$icon, type=$type)"
    }

    enum class Type {
        Preset, PhotoBuild, UnKnow
    }
}
object FuAvatarWrapperDiffCallback: DiffUtil.ItemCallback<FuAvatarWrapper>() {
    override fun areItemsTheSame(oldItem: FuAvatarWrapper, newItem: FuAvatarWrapper): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: FuAvatarWrapper, newItem: FuAvatarWrapper): Boolean {
        return oldItem.icon == newItem.icon
                && oldItem.type == newItem.type
    }

}