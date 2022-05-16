package com.faceunity.app_ptag

import android.app.Application

/**
 * Created on 2021/7/21 0021 11:57.


 */
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        FuDevInitializeWrapper.initSDK(this)
    }
}