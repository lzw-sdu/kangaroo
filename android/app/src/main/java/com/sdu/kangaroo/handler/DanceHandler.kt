package com.sdu.kangaroo.handler

import android.content.Context
import android.content.Intent
import com.sdu.kangaroo.utils.SingletonHolder
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

class DanceHandler private constructor(val mContext: Context) : MethodChannel.MethodCallHandler {
    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        if (call.method == "dance_ui") {
            dance()
            result.success("I'm dancing")
        } else {
            result.notImplemented()
        }
    }

    companion object : SingletonHolder<DanceHandler, Context>(::DanceHandler) {
        val instance: DanceHandler
            get() = DanceHandler.getSingleInstance()
    }

    fun dance() {
        val intent: Intent = Intent(mContext, com.example.myapplication.MainActivity::class.java)
        mContext.startActivity(intent)
    }
}