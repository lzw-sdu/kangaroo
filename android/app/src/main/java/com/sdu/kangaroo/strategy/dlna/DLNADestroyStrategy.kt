package com.sdu.kangaroo.strategy.dlna

import com.sdu.kangaroo.MethodChannelStrategy
import com.sdu.kangaroo.handler.DLNAHandler
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

class DLNADestroyStrategy : MethodChannelStrategy {
    override fun handle(call: MethodCall, result: MethodChannel.Result) {
        DLNAHandler.instance.onDestroy()
        result.success(null)
    }
}