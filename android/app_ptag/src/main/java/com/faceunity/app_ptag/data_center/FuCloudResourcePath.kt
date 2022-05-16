package com.faceunity.app_ptag.data_center

import android.content.Context
import com.faceunity.pta.pta_core.data_build.FuResourcePath

/**
 * 用于参考的 [FuResourcePath] 实现类。对应方法的路径如无修改可不重写。
 */
class FuCloudResourcePath(val context: Context) : FuResourcePath(
    ossAssets = "OSSAssets",
    appAssets = "AppAssets",
    binAssets = "pta_kit"
) {

    override fun ossCustom(path: String): String {
        val ret = context.getExternalFilesDir(null)?.path + "/download/" + path
        return ret
    }

    override fun ossItemList(): String {
        return context.getExternalFilesDir(null)?.path + "/download/item_list.json"
    }

    override fun ossAvatarListDir(): String {
        return context.getExternalFilesDir(null)?.path + "/download/GAvatarList"
    }

    override fun customAvatarListDir(): String {
        return context.getExternalFilesDir(null)?.path + "/download/CustomAvatarList"
    }

    override fun ossBuildAvatarInfoList() = listOf(
        "GConfig/face_feature_fit.json",
        "GConfig/face_components_fit.json",
        ossItemList(),
        "GConfig/default_avatar_list.json",
        "GConfig/edit_color_list.json"
    )
}