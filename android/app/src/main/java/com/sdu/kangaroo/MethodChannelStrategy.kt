package com.sdu.kangaroo

import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

interface MethodChannelStrategy {
    fun handle(call: MethodCall, result: MethodChannel.Result)
}