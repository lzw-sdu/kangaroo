package com.faceunity.app_ptag.data_center

import com.faceunity.pta.pta_core.data_build.FuResourcePath

/**
 *
 */
abstract class MGResourcePath(
    ossAssets: String,
    appAssets: String,
    binAssets: String
) : FuResourcePath(
    ossAssets, appAssets, binAssets
) {

    override fun ossBuildAvatarInfoList() = listOf(
        "/GConfig/face_feature_fit_mg.json",
        "/GConfig/face_components_fit.json",
        "/GGenerateJson/item_list.json",
        "/GConfig/default_avatar_list.json",
        "/GConfig/edit_color_list.json"
    ).map { ossAssets + it }

    override fun binBuildAvatarFileList() = listOf(
        "/pta_core.bin",
        "/pta_server_dl_lite_g.bin",
        "/pta_server_g_mg.bin"
    ).map { binAssets + it }
}