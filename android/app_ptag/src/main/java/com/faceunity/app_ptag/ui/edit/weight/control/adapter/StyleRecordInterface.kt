package com.faceunity.app_ptag.ui.edit.weight.control.adapter

/**
 *
 */
interface StyleRecordInterface {

    fun downloadStyleSafe(fileId: String?) : DownloadStyle {
        if (fileId == null) return DownloadStyle.Normal
        return downloadStyle(fileId)
    }

    fun downloadStyle(fileId: String) : DownloadStyle

    fun notifyDownloadStart(fileId: String)

    fun notifyDownloadSuccess(fileId: String)

    fun notifyDownloadError(fileId: String)

    enum class DownloadStyle {
        Normal,
        NeedDownload,
        Downloading,
        Error
    }
}