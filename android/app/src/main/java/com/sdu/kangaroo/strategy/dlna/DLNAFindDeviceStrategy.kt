package com.sdu.kangaroo.strategy.dlna

import com.sdu.kangaroo.MethodChannelStrategy
import com.ykbjson.lib.screening.DLNAManager
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel


class DLNAFindDeviceStrategy : MethodChannelStrategy {
    override fun handle(call: MethodCall ,result: MethodChannel.Result) {
        DLNAManager.getInstance().startBrowser()
        result.success(null)
    }

    companion object {
        val instance: DLNAFindDeviceStrategy by lazy { DLNAFindDeviceStrategy() }
        val TAG = "DLNAFindDevice"
    }
}