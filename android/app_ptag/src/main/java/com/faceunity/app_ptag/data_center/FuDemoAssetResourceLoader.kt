package com.faceunity.app_ptag.data_center

import android.content.Context
import com.faceunity.editor_ptag.util.FuLog
import com.faceunity.pta.pta_core.data_build.FuResourceLoader
import com.faceunity.pta.pta_core.util.expand.readText
import java.io.File

/**
 * 基于 Assets 的资源加载器
 */
class FuDemoAssetResourceLoader(val context: Context) : FuResourceLoader {
    override fun loadString(path: String): String {
        return context.assets.open(path).readText()
    }

    override fun loadByteArray(path: String): ByteArray {
        return context.assets.open(path).readBytes()
    }

    override fun listFile(path: String): List<String> {
        return context.assets.list(path)?.toList() ?: emptyList()
    }

    override fun isExist(path: String): Boolean {
        //Assets 默认提供的资源都是存在的，不然 try catch 耗性能
        return true
    }

    override fun writeStringToPath(content: String, path: String) {
        val split = path.split("/")
        val tempDir = File(context.externalCacheDir, split.get(split.size - 2))
        tempDir.mkdir()
        val tempFile = File(tempDir, split.last())
        FuLog.error("Assets can't write file,$path will be write to $tempFile")
        tempFile.writeText(content)
    }

    override fun delete(path: String): Boolean {
        FuLog.error("Assets can't delete file")
        return false
    }

}