package com.sdu.kangaroo.utils.start_avtivity_result

import android.content.Intent

class StartActivityAction(
    val requestCode: Int,
    val activity: Class<*>,
    private val startActivityActionCallBack: StartActivityActionCallBack
) {

/*    private val startActivityActionCallBack: StartActivityActionCallBack? = null
    private val requestCode: Int? = null
    private val activity: Class<*>? = null*/

    companion object {
        private val TAG = "StartActivityAction"
    }

    fun onSuccess(resultCode: Int, data: Intent?) {
        startActivityActionCallBack.onSuccess(resultCode, data)
    }

    fun onFail(resultCode: Int, data: Intent?) {
        startActivityActionCallBack.onFail(resultCode, data)
    }
}