package com.faceunity.app_ptag.client_cloud.entity

import com.faceunity.editor_ptag.business.cloud.CloudException
import java.lang.Exception

/**
 *
 */
interface UpdateAppAssetsCallback {

    fun onSuccess()

    fun onFastReturn()

    fun onCloudFailed(cloudException: CloudException)

    fun onFailed(exception: Exception)
}