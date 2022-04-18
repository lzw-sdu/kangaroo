package com.sdu.kangaroo.utils.start_avtivity_result

import android.app.Activity
import android.content.Intent

class StartActivityManger {

    private val startActivityActions: MutableList<StartActivityAction> = mutableListOf()

    companion object {
        val instance: StartActivityManger by lazy { StartActivityManger() }
    }

    fun requestStartActivityAction(
        requestCode: Int,
        activity: Activity,
        intent: Intent,
        callback: StartActivityActionCallBack
    ) {
        val startActivityAction = StartActivityAction(requestCode, activity.javaClass, callback)
        startActivityActions.add(startActivityAction)
        activity.startActivityForResult(intent, requestCode)
    }

    fun notifyActivityResult(requestCode: Int, resultCode: Int, data: Intent?, activity: Activity) {
        val iterator: Iterator<StartActivityAction> = startActivityActions.iterator()
        while (iterator.hasNext()) {
            val action = iterator.next()
            if (action.requestCode == requestCode && action.activity == activity.javaClass) {
                if (resultCode == Activity.RESULT_OK) {
                    action.onSuccess(resultCode, data)
                } else {
                    action.onFail(resultCode, data)
                }
                startActivityActions.remove(action)
                break
            }
        }
    }
}