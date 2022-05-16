package com.example.myapplication.model

import com.google.gson.annotations.SerializedName

/**
 *
 */
class ConfigModel {
    @SerializedName("landscape")
    var landscapeItem: Item? = null

    @SerializedName("portrait")
    var portraitItem: Item? = null

    class Item {
        @SerializedName("path")
        var path: String? = null

        @SerializedName("align")
        var alignMode: Int = 0
    }
}