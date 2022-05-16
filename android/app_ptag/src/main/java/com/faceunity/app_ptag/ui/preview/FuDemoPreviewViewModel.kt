package com.faceunity.app_ptag.ui.preview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FuDemoPreviewViewModel : ViewModel() {

    private var isLockScale = true
    private val _lockScaleLiveData = MutableLiveData<Boolean>(isLockScale)
    val lockScaleLiveData: LiveData<Boolean> get() = _lockScaleLiveData

    fun switchLockScaleStatus() {
        isLockScale = !isLockScale
        _lockScaleLiveData.postValue(isLockScale)
    }

}