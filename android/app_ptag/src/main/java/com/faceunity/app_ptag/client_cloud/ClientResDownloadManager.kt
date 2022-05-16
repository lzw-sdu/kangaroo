package com.faceunity.app_ptag.client_cloud

import com.faceunity.editor_ptag.business.cloud.CloudException
import com.faceunity.editor_ptag.business.cloud.CloudResDownloadManager
import com.faceunity.editor_ptag.parser.IFuJsonParser
import com.faceunity.editor_ptag.util.FuLog
import com.faceunity.fupta.cloud_download.CloudRepository
import com.faceunity.fupta.cloud_download.entity.client.FuAppAssetsVersionResult
import com.faceunity.fupta.cloud_download.util.NetCallback
import com.faceunity.fupta.cloud_download.util.NetDownloadCallback
import java.io.File

/**
 * 负责客户端资源的下载相关逻辑。
 */
class ClientResDownloadManager(
    private val repository: ClientCloudRepository,
    private val jsonParser: IFuJsonParser
) {

    fun requestAppAssetsVersion(
        requestCallback: CloudResDownloadManager.RequestCallback? = null,
        onLoadSuccess: (resource: FuAppAssetsVersionResult) -> Unit
    ) {
        val netCallback = object : NetCallback {
            override fun onSuccess(body: String) {
                val fuAppAssetsVersionResult = try {
                    jsonParser.parse(body, FuAppAssetsVersionResult::class.java)
                } catch (ex: java.lang.Exception) {
                    requestCallback?.onError(CloudException.FormatException(ex, "requestAppAssetsVersion 异常"))
                    return
                }
                onLoadSuccess(fuAppAssetsVersionResult)
            }

            override fun onError(ex: Exception) {
                requestCallback?.onError(
                    CloudException.NetworkException("requestAppAssetsVersion", ex)
                )
            }
        }
        repository.getAppAssetsVersion(netCallback)
    }

    fun downloadAppAssetsZip(
        requestCallback: CloudResDownloadManager.RequestCallback? = null,
        assetsVersion: String,
        downloadFile: File,
        onLoadSuccess: (downloadFile: File) -> Unit
    ) {
        val netDownloadCallback = object : NetDownloadCallback {
            override fun onSuccess(file: File) {
                onLoadSuccess(file)
            }

            override fun onError(ex: Exception) {
                requestCallback?.onError(
                    CloudException.NetworkException("downloadAppAssetsZip", ex)
                )
            }
        }
        repository.getAppAssetsZip(assetsVersion, downloadFile, netDownloadCallback)
    }

    fun checkUseVersion(appVersion: String, fuAppAssetsVersionResult: FuAppAssetsVersionResult) : String {
        fuAppAssetsVersionResult.version_map[appVersion]?.let {
            return it
        }
        appVersion.split('.').let { versionSplitList -> //通配符版本号查询
            if(versionSplitList.size <= 1) return@let //如果版本号无法被 . 分隔，则无法解析
            for(i in versionSplitList.size - 1 downTo 0) {
                val wildcardString = versionSplitList.subList(0, i).joinToString(".") + "*"
                fuAppAssetsVersionResult.version_map[wildcardString]?.let {
                    return it
                }
            }
        }

        return fuAppAssetsVersionResult.version_list.last()
    }

}
