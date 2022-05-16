package com.faceunity.app_ptag.client_cloud

import com.faceunity.fupta.cloud_download.entity.CloudConfig
import com.faceunity.fupta.cloud_download.entity.CloudMode
import com.faceunity.fupta.cloud_download.util.*
import com.faceunity.toolbox.log.FuLogInterface
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

/**
 *
 */
class ClientCloudRepository(
    val fuLog: FuLogInterface,
    val request: NetRequest,
    val domain:String = "https://fu-ptag-assets.oss-cn-hangzhou.aliyuncs.com",
    val mode: String = "release"
) {

    fun getAppAssetsVersion(netCallback: NetCallback) {
        val requestWrapper = RequestWrapper(domain + "/${mode}/version/version.json")
        request.request(requestWrapper, netCallback)
    }

    fun getAppAssetsZip(version: String, file: File, netDownloadCallback: NetDownloadCallback) {
        val requestWrapper = RequestWrapper(domain + "/${mode}/$version/AppAssets.zip")

        request.download(requestWrapper, file, netDownloadCallback)
    }

}