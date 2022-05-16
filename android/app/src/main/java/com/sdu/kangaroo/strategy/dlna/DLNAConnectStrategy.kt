package com.sdu.kangaroo.strategy.dlna

import com.sdu.kangaroo.MethodChannelStrategy
import com.sdu.kangaroo.handler.DLNAHandler
import com.sdu.kangaroo.screening.bean.DeviceInfo
import com.sdu.kangaroo.screening.bean.MediaInfo
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

/**
 * @param call
 *  DeviceInfo
 */

class DLNAConnectStrategy : MethodChannelStrategy {
    override fun handle(call: MethodCall, result: MethodChannel.Result) {
        if (DLNAHandler.instance.curItemType == MediaInfo.TYPE_UNKNOWN) {
            result.error("", "", null)
            return
        }
        var deviceInfo: DeviceInfo? = null
        for (i in 0 until DLNAHandler.instance.deviceList.size) {
            if (DLNAHandler.instance.deviceList[i].name == call.arguments as String) {
                deviceInfo = DLNAHandler.instance.deviceList[i]
            }
        }
        if (deviceInfo == null) {
            result.error("", "", null)
            return
        }
        DLNAHandler.instance.mDLNAPlayer?.connect(deviceInfo)
        result.success(null)
    }

    companion object {
        val instance: DLNAConnectStrategy by lazy { DLNAConnectStrategy() }
        val TAG = "DLNAConnect"
    }
}