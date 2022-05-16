package com.faceunity.app_ptag.ui.edit_custom.entity

import com.faceunity.support.entity.FUAEItem

/**
 *
 */
data class CustomControlModel(val list: MutableList<MainMenu>) {

    data class MainMenu(val icon: String?, val list: MutableList<MinorMenu>)

    data class MinorMenu(val icon: String?, val list: MutableList<Item>)

    data class Item(
        val fuaeItem: FUAEItem,
        val icon: String,

        var isLock: Boolean,
        var isNew: Boolean
    )
}
