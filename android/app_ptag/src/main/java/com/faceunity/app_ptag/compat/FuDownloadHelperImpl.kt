package com.faceunity.app_ptag.compat

import com.faceunity.download.FUDownloadKit
import com.faceunity.download.entity.FUDownloadSource
import com.faceunity.download.infe.OnFUDownloadListener
import com.faceunity.editor_ptag.business.cloud.download.IDownloadControl
import java.io.File

/**
 *
 */
class FuDownloadHelperImpl : IDownloadControl {

    override fun downloadGroup(
        downloadSourceList: List<IDownloadControl.DownloadSource>,
        groupListener: IDownloadControl.GroupDownloadListener
    ) {
        FUDownloadKit.getInstance().setLogEnable(false)
        val successSourceList = mutableListOf<IDownloadControl.DownloadSource>()
        val errorSourceList = mutableListOf<IDownloadControl.DownloadSource>()
        downloadSourceList.forEach {
            FUDownloadKit.getInstance().addTask(toFuDownloadSource(it), object : OnFUDownloadListener {
                override fun onCanceled(downloadBytes: Long, totalBytes: Long) {
                    errorSourceList.add(it)
                }

                override fun onError(httpStatus: Int, errorCode: Int, msg: String?) {
                    errorSourceList.add(it)
                    groupListener.onError(msg)
                }

                override fun onFinished(file: File) {
                    successSourceList.add(it)
                    groupListener.onProgress(successSourceList.size, downloadSourceList.size)

                    if (successSourceList.size + errorSourceList.size == downloadSourceList.size) {
                        if (errorSourceList.size == 0) {
                            groupListener.onSuccessFinished()
                        } else {
                            groupListener.onErrorFinished(errorSourceList)
                        }
                    }
                }

                override fun onPrepared() {

                }

                override fun onProgress(downloadBytes: Long, totalBytes: Long) {

                }

                override fun onStart(totalBytes: Long) {

                }
            })
        }
        groupListener.onProgress(successSourceList.size, downloadSourceList.size)
    }

    override fun downloadSingle(
        downloadSource: IDownloadControl.DownloadSource,
        singleListener: IDownloadControl.SingleDownloadListener
    ) {
        FUDownloadKit.getInstance().setLogEnable(false)
        FUDownloadKit.getInstance().addTask(toFuDownloadSource(downloadSource), object : OnFUDownloadListener {
            override fun onCanceled(downloadBytes: Long, totalBytes: Long) {

            }

            override fun onError(httpStatus: Int, errorCode: Int, msg: String?) {
                singleListener.onError(msg)
            }

            override fun onFinished(file: File) {
                singleListener.onFinished()
            }

            override fun onPrepared() {

            }

            override fun onProgress(downloadBytes: Long, totalBytes: Long) {
                singleListener.onProgress(downloadBytes, totalBytes)
            }

            override fun onStart(totalBytes: Long) {

            }
        })
    }

    private fun toFuDownloadSource(downloadSource: IDownloadControl.DownloadSource): FUDownloadSource {
        return FUDownloadSource(downloadSource.url, downloadSource.savePath)
    }

    private fun bytesToHex(bytes: ByteArray): String {
        val hexArray = "0123456789abcdef".toCharArray()
        val hexChars = CharArray(bytes.size * 2)
        var j = 0
        var v: Int
        while (j < bytes.size) {
            v = bytes[j].toInt() and 0xFF
            hexChars[j * 2] = hexArray[v ushr 4]
            hexChars[j * 2 + 1] = hexArray[v and 0x0F]
            j++
        }
        return String(hexChars)
    }
}