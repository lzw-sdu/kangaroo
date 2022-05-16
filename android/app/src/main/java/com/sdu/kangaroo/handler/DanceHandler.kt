package com.sdu.kangaroo.handler

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.faceunity.app_ptag.FuEditActivity
import com.sdu.kangaroo.utils.SingletonHolder
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

class DanceHandler private constructor(private val mContext: Context) :
    MethodChannel.MethodCallHandler {
    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        if (call.method == "dance_ui") {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                dance()
            }
            result.success("I'm dancing")
        } else if (call.method == "mirror") {
            mirror()
        } else {
            result.notImplemented()
        }
    }

    companion object : SingletonHolder<DanceHandler, Context>(::DanceHandler) {
        val instance: DanceHandler
            get() = DanceHandler.getSingleInstance()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun dance() {
//        FuEditActivity.startActivity(mContext, com.faceunity.app_ptag.R.id.homeFragment)

        val intent: Intent = Intent(mContext, com.example.myapplication.MainActivity::class.java)
        mContext.startActivity(intent)

/*        val thirdAppInfo: ApplicationInfo = mContext.applicationInfo
        val thirdAppName: String = mContext.resources.getString(thirdAppInfo.labelRes)
        val thirdPackageName: String = mContext.packageName
        Log.d("DanceHandler", "thirdAppName: $thirdAppInfo")
        Log.d("DanceHandler", "thirdAppName: $thirdAppName")
        Log.d("DanceHandler", "thirdPackageName: $thirdPackageName")
//        DLNASelectModeStrategy.instance.selectImage()
        HealthyManger.getInstance(mContext).getAuthority()
        for (i in 0..10) {
            HealthyManger.instance.validAuthority()
            HealthyManger.instance.readHeartRateDetail()
        }*/
    }

    private fun mirror() {
        val intent = Intent(mContext, FuEditActivity::class.java)
        intent.putExtra("fragmentId", com.faceunity.app_ptag.R.id.homeFragment)
        mContext.startActivity(intent)
    }
}