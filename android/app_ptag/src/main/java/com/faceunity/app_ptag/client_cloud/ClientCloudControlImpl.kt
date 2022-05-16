package com.faceunity.app_ptag.client_cloud

import com.faceunity.app_ptag.client_cloud.entity.UpdateAppAssetsCallback
import com.faceunity.app_ptag.client_cloud.entity.UpdateAppAssetsConfig
import com.faceunity.editor_ptag.business.cloud.CloudException
import com.faceunity.editor_ptag.business.cloud.CloudResDownloadManager
import com.faceunity.editor_ptag.business.cloud.interfaces.FuZipInterface
import com.faceunity.editor_ptag.parser.IFuJsonParser
import com.faceunity.fupta.cloud_download.CloudRepository
import com.faceunity.pta.pta_core.interfaces.FuStorageFieldInterface
import java.io.File

/**
 *
 */
class ClientCloudControlImpl(
    val repository: ClientCloudRepository,
    val fuJsonParser: IFuJsonParser,
    val fuStorageField: FuStorageFieldInterface,
    val zip: FuZipInterface
) : IClientCloudControl {

    private val clientResDownloadManager = ClientResDownloadManager(repository, fuJsonParser)



    override fun updateAppAssets(
        appVersion: String,
        saveDir: File,
        config: UpdateAppAssetsConfig,
        callback: UpdateAppAssetsCallback?
    ) {
        val key = "AppAssetsVersion"
        val currentAssetsVersion = fuStorageField.getAsString(key)
        if (!saveDir.exists()) {
            saveDir.mkdirs()
        }
        val zipFile = File(saveDir, "AppAssets.zip")
        val dirFile = saveDir
        val requestCallback = object : CloudResDownloadManager.RequestCallback {
            override fun onError(cloudException: CloudException) {
                callback?.onCloudFailed(cloudException)
            }

        }
        clientResDownloadManager.requestAppAssetsVersion(requestCallback) {
            val needVersion = clientResDownloadManager.checkUseVersion(appVersion, it)
            if (needVersion == currentAssetsVersion && !config.alwaysUpdate) {
                callback?.onFastReturn()
                return@requestAppAssetsVersion
            }
            clientResDownloadManager.downloadAppAssetsZip(requestCallback, needVersion, zipFile) {
                zip.unZip(it, dirFile, object : FuZipInterface.Listener {
                    override fun onStart() {

                    }

                    override fun onProgress(percentage: Int, fileName: String?) {

                    }

                    override fun onSuccess(file: File) {
                        fuStorageField.set(key, needVersion)
                        zipFile.delete()
                        callback?.onSuccess()
                    }

                    override fun onError(exception: Exception) {
                        callback?.onFailed(exception)
                    }

                    override fun onCancel() {
                        callback?.onFailed(UnsupportedOperationException("unzip cancel"))
                    }
                })
            }
        }
    }

}