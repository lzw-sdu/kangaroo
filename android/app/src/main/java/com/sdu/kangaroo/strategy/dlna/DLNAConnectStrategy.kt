package com.sdu.kangaroo.strategy.dlna

import com.google.gson.Gson
import com.sdu.kangaroo.MethodChannelStrategy
import com.sdu.kangaroo.handler.DLNAHandler
import com.ykbjson.lib.screening.bean.DeviceInfo
import com.ykbjson.lib.screening.bean.MediaInfo
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
        val gson = Gson()
        val deviceInfo = gson.fromJson(call.arguments.toString(), DeviceInfo::class.java)
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