package com.sdu.kangaroo.utils.start_avtivity_result

import android.content.Intent

interface StartActivityActionCallBack {
    fun onSuccess(resultCode: Int, data: Intent?)

    fun onFail(resultCode: Int, data: Intent?)
}