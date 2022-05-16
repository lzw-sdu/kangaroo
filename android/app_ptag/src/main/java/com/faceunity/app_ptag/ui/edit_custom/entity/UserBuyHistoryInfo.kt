package com.faceunity.app_ptag.ui.edit_custom.entity

/**
 * 用户购买或者解锁的记录
 */
data class UserBuyHistoryInfo(val list: List<Item>) {

    data class Item(
        /**
         * 服务端的唯一 ID
         */
        val key: String,
        /**
         * item_list 里的 path
         */
        val fuFileID: String,
        /**
         * 解锁途径，例如积分解锁，付费解锁，限时体验
         */
        val unlockMode: String,
        /**
         * 有效期
         */
        val expirationDate: String,
    )
}
