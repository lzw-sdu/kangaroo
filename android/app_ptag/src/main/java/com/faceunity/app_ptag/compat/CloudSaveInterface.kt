package com.faceunity.app_ptag.compat

import android.content.Context
import com.faceunity.editor_ptag.data_center.FuDevDataCenter
import com.faceunity.editor_ptag.parser.IFuJsonParser
import com.faceunity.fupta.cloud_download.SaveInterface
import com.faceunity.fupta.cloud_download.entity.CloudResourceContainer
import java.io.File

/**
 *
 */
class CloudSaveInterface(context: Context) : SaveInterface {
    val file = File(context.getExternalFilesDir(null), "CloudResourceContainer.json")

    override fun load(): CloudResourceContainer {
        try {
            val text = file.readText()
            return FuDevDataCenter.fuJsonParser.parse(text, CloudResourceContainer::class.java)
        } catch (ex: Exception) {
            return CloudResourceContainer()
        }

    }

    override fun save(cloudResourceContainer: CloudResourceContainer) {
        val json = FuDevDataCenter.fuJsonParser.toJson(cloudResourceContainer)
        file.writeText(json)
    }
}