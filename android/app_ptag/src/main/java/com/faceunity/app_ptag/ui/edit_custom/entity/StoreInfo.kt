package com.faceunity.app_ptag.ui.edit_custom.entity

/**
 * 商城，记录哪些资源需要付费，多少钱等
 */
data class StoreInfo(val list: List<Goods>) {

    data class Goods(
        /**
         * 商品的 key，业务自动生成的唯一 ID
         */
        val key: String,
        /**
         * item_list 里的 path
         */
        val fuFileID: String,
        /**
         * 购买它需要的积分
         */
        val score: Int,
    )
}
