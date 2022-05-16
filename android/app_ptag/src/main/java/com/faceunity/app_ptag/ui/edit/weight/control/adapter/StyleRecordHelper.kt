package com.faceunity.app_ptag.ui.edit.weight.control.adapter

import com.faceunity.editor_ptag.data_center.FuDevDataCenter
import com.faceunity.fupta.cloud_download.entity.CloudResource

/**
 *
 */
class StyleRecordHelper : StyleRecordInterface {
    private val statusMap = mutableMapOf<String, StyleRecordInterface.DownloadStyle>()

    override fun downloadStyle(fileId: String): StyleRecordInterface.DownloadStyle {
        val downloadStyle = statusMap[fileId]
        if (downloadStyle == null) {
            val cloudResourceStatus = FuDevDataCenter.getCloudResourceManager()?.getCloudResourceStatus(fileId)
            val status = when (cloudResourceStatus) {
                CloudResource.Status.Normal -> StyleRecordInterface.DownloadStyle.Normal
                CloudResource.Status.NeedDownload, CloudResource.Status.NeedUpdate -> StyleRecordInterface.DownloadStyle.NeedDownload
                CloudResource.Status.Lose -> StyleRecordInterface.DownloadStyle.Error
                else -> StyleRecordInterface.DownloadStyle.Error
            }
            statusMap[fileId] = status
        }
        return statusMap[fileId]!!
    }

    override fun notifyDownloadStart(fileId: String) {
        statusMap[fileId] = StyleRecordInterface.DownloadStyle.Downloading
    }

    override fun notifyDownloadSuccess(fileId: String) {
        statusMap[fileId] = StyleRecordInterface.DownloadStyle.Normal
    }

    override fun notifyDownloadError(fileId: String) {
        statusMap[fileId] = StyleRecordInterface.DownloadStyle.Error
    }
}