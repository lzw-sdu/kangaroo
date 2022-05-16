package com.faceunity.app_ptag.client_cloud

import com.faceunity.app_ptag.client_cloud.entity.UpdateAppAssetsCallback
import com.faceunity.app_ptag.client_cloud.entity.UpdateAppAssetsConfig
import java.io.File

/**
 *
 */
interface IClientCloudControl {

    fun updateAppAssets(appVersion: String, saveDir: File, config: UpdateAppAssetsConfig = UpdateAppAssetsConfig(), callback: UpdateAppAssetsCallback? = null)

}