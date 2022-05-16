package com.sdu.kangaroo.handler

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.core.content.PermissionChecker.checkCallingOrSelfPermission
import com.sdu.kangaroo.MainActivity
import com.sdu.kangaroo.MethodChannelStrategy
import com.sdu.kangaroo.strategy.dlna.DLNAConnectStrategy
import com.sdu.kangaroo.strategy.dlna.DLNAFindDeviceStrategy
import com.sdu.kangaroo.strategy.dlna.DLNASelectModeStrategy
import com.sdu.kangaroo.utils.SingletonHolder
import com.sdu.kangaroo.screening.DLNAManager
import com.sdu.kangaroo.screening.DLNAPlayer
import com.sdu.kangaroo.screening.bean.DeviceInfo
import com.sdu.kangaroo.screening.bean.MediaInfo
import com.sdu.kangaroo.screening.listener.DLNAControlCallback
import com.sdu.kangaroo.screening.listener.DLNADeviceConnectListener
import com.sdu.kangaroo.screening.listener.DLNARegistryListener
import com.sdu.kangaroo.screening.listener.DLNAStateCallback
import com.sdu.kangaroo.simplepermission.PermissionsManager
import com.sdu.kangaroo.simplepermission.PermissionsRequestCallback
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import org.fourthline.cling.model.action.ActionInvocation

class DLNAHandler private constructor(val mContext: Context) : DLNADeviceConnectListener,
    MethodChannel.MethodCallHandler {

    private val TAG = "DLNAContext"
    private val CODE_REQUEST_PERMISSION = 1010

    var curItemType = MediaInfo.TYPE_UNKNOWN
    var mMediaPath: String? = null

    var mDeviceInfo: DeviceInfo? = null
    var mDLNAPlayer: DLNAPlayer? = null
    var deviceList: List<DeviceInfo> = mutableListOf()
    private var mDLNARegistryListener: DLNARegistryListener? = null

    private val methodMap: MutableMap<String, out MethodChannelStrategy> = hashMapOf(
        DLNAFindDeviceStrategy.TAG to DLNAFindDeviceStrategy.instance,
        DLNAConnectStrategy.TAG to DLNAConnectStrategy.instance,
        DLNASelectModeStrategy.TAG to DLNASelectModeStrategy.instance
    )

    init {
        init()
    }


    companion object : SingletonHolder<DLNAHandler, Context>(::DLNAHandler) {
        val instance: DLNAHandler
            get() = getSingleInstance()
    }

    private fun init() {
        PermissionsManager.getInstance()
            .requestAllManifestPermissionsIfNecessary(CODE_REQUEST_PERMISSION,
                mContext as Activity, object : PermissionsRequestCallback {
                    override fun onGranted(requestCode: Int, permission: String) {
                        val hasPermission = PackageManager.PERMISSION_GRANTED ==
                                (checkCallingOrSelfPermission(
                                    mContext,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                                )
                                        and checkCallingOrSelfPermission(
                                    mContext,
                                    Manifest.permission.RECORD_AUDIO
                                ))
                        if (hasPermission) {
                            DLNAManager.getInstance()
                                .init(mContext, object : DLNAStateCallback {
                                    override fun onConnected() {
                                        Log.d(
                                            TAG,
                                            "DLNAManager ,onConnected"
                                        )
                                        initDlna()
                                    }

                                    override fun onDisconnected() {
                                        Log.d(
                                            TAG,
                                            "DLNAManager ,onDisconnected"
                                        )
                                    }
                                })
                        }
                    }

                    override fun onDenied(requestCode: Int, permission: String) {
                        Log.d(TAG, "DLNAManager ,onDenied")
                    }

                    override fun onDeniedForever(requestCode: Int, permission: String) {
                        Log.d(
                            TAG,
                            "DLNAManager ,onDeniedForever"
                        )
                    }

                    override fun onFailure(requestCode: Int, deniedPermissions: Array<String>) {
                        Log.d(TAG, "DLNAManager ,onFailure")
                    }

                    override fun onSuccess(requestCode: Int) {
                        Log.d(TAG, "DLNAManager onSuccess,")
                    }
                })
    }


    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        methodMap[call.method]?.handle(call, result)
    }

    override fun onConnect(deviceInfo: DeviceInfo?, errorCode: Int) {
        if (errorCode == DLNADeviceConnectListener.CONNECT_INFO_CONNECT_SUCCESS) {
            mDeviceInfo = deviceInfo
            Toast.makeText(mContext, "连接设备成功", Toast.LENGTH_SHORT).show()
            startPlay()
        }
    }

    override fun onDisconnect(deviceInfo: DeviceInfo?, type: Int, errorCode: Int) {
        TODO("Not yet implemented")
    }

    private fun initDlna() {
        mDLNAPlayer = DLNAPlayer(mContext)
        mDLNAPlayer!!.setConnectListener(this)
        mDLNARegistryListener = object : DLNARegistryListener() {
            override fun onDeviceChanged(deviceInfoList: List<DeviceInfo>) {
                deviceList = deviceInfoList
                val deviceList: List<String> = deviceInfoList.map { it.name }
                MainActivity.deviceChannel.invokeMethod("onDeviceChanged", deviceList)
            }
        }
        DLNAManager.getInstance().registerListener(mDLNARegistryListener)
    }

    /**
     * 开始播放
     */
    private fun startPlay() {
        val sourceUrl = mMediaPath
        val mediaInfo = MediaInfo()
        if (sourceUrl != null && !TextUtils.isEmpty(sourceUrl)) {
            mediaInfo.mediaId = Base64.encodeToString(sourceUrl.toByteArray(), Base64.NO_WRAP)
            mediaInfo.uri = sourceUrl
        }
        mediaInfo.mediaType = curItemType
        mDLNAPlayer!!.setDataSource(mediaInfo)
        mDLNAPlayer!!.start(object : DLNAControlCallback {
            override fun onSuccess(invocation: ActionInvocation<*>?) {
                Toast.makeText(mContext, "投屏成功", Toast.LENGTH_SHORT).show()
            }

            override fun onReceived(invocation: ActionInvocation<*>?, vararg extra: Any?) {}
            override fun onFailure(
                invocation: ActionInvocation<*>?,
                errorCode: Int,
                errorMsg: String?
            ) {
                Toast.makeText(mContext, "投屏失败", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun onDestroy() {
        if (mDLNAPlayer != null) {
            mDLNAPlayer!!.destroy()
        }
        DLNAManager.getInstance().unregisterListener(mDLNARegistryListener)
        DLNAManager.getInstance().destroy()
    }

}