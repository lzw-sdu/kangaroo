package com.sdu.kangaroo

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.annotation.RequiresApi
import com.heytap.databaseengine.HeytapHealthApi
import com.heytap.databaseengine.apiv2.HResponse
import com.heytap.databaseengine.apiv2.IHeytapHealthApi
import com.heytap.databaseengine.apiv2._HeytapHealth
import com.heytap.databaseengine.apiv3.DataReadRequest
import com.heytap.databaseengine.apiv3.data.DataSet
import com.heytap.databaseengine.apiv3.data.DataType
import com.heytap.databaseengine.model.proxy.UserDeviceInfoProxy
import com.heytap.databaseengine.utils.HLog
import java.time.LocalDate
import java.time.ZoneId

class HealthyManger private constructor(private val context: Context) {

    var mApi: IHeytapHealthApi? = null

    companion object : SingletonHolder<HealthyManger, Context>(::HealthyManger) {
        val instance: HealthyManger
            get() = getSingleInstance()
        private val TAG = "HealthyManger"

    }

    init {
        HeytapHealthApi.init(context)
        HeytapHealthApi.setLoggable(true)
        while (true) {
            if (_HeytapHealth.hasInit()) {
                break
            }
        }
        mApi = HeytapHealthApi.getInstance()
    }

    @RequiresApi(api = 26)
    fun readHeartRateDetail() {
        val startTime: Long =
            LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val endTime: Long =
            LocalDate.now().plusDays(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()
                .toEpochMilli() - 1
        val readRequest = DataReadRequest.Builder()
            .read(DataType.TYPE_HEART_RATE)
            .setTimeRange(startTime, endTime)
            .build()
        try {
            mApi!!.dataApi().read(readRequest, object : HResponse<List<DataSet?>> {
                override fun onSuccess(sportDataStatList: List<DataSet?>) {
                    for (sportDataStat in sportDataStatList) {
                        Log.d(TAG, "onSuccess: " + sportDataStat!!.toString())
                    }
                }

                override fun onFailure(i: Int) {
                    HLog.e(
                        TAG,
                        "read data fail, error code: $i\n"
                    )
                }
            })
        } catch (e: Exception) {
            HLog.e(TAG, e.toString())
        }
    }

    fun validAuthority() {
        mApi!!.authorityApi().valid(object : HResponse<List<String?>> {
            override fun onSuccess(strings: List<String?>) {
                for (string in strings) {
                    Log.d(TAG, "onSuccess: " + string!!)
                }
            }

            override fun onFailure(i: Int) {
                HLog.e(
                    TAG,
                    "get authority fail, error code: $i\n"
                )
            }
        })
    }

    fun getAuthority() {
        mApi!!.authorityApi().request(context as Activity)
    }

    fun getDeviceInfo() {
        mApi!!.deviceApi().deviceInfoApi()
            .queryBoundDevice(object : HResponse<List<UserDeviceInfoProxy?>> {
                override fun onSuccess(userDeviceInfoProxies: List<UserDeviceInfoProxy?>) {
                    for (userDeviceInfoProxy in userDeviceInfoProxies) {
                        Log.d(TAG, "onSuccess: " + userDeviceInfoProxy!!.toString())
                    }
                }

                override fun onFailure(i: Int) {
                    HLog.e(
                        TAG,
                        "get device info fail, error code: $i\n"
                    )
                }
            })
    }
}