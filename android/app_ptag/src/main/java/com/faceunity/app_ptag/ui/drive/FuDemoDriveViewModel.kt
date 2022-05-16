package com.faceunity.app_ptag.ui.drive

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.faceunity.app_ptag.ui.drive.entity.BodyFollowMode
import com.faceunity.app_ptag.ui.drive.entity.BodyTrackMode
import com.faceunity.app_ptag.ui.drive.entity.DrivePage

class FuDemoDriveViewModel : ViewModel() {

    private var drivePage: DrivePage = DrivePage.Track
    private val _drivePageLiveData = MutableLiveData<DrivePage>(drivePage)
    val drivePageLiveData: LiveData<DrivePage> get() = _drivePageLiveData

    private var isFaceTrack: Boolean = true
    private val _isFaceTrackLiveData = MutableLiveData<Boolean>(isFaceTrack)
    val isFaceTrackLiveData: LiveData<Boolean> get() = _isFaceTrackLiveData
    fun notifyFaceTrackLiveData() {
        _isFaceTrackLiveData.postValue(isFaceTrack)
    }

    private var bodyTrackMode: BodyTrackMode = BodyTrackMode.Full
    private val _bodyTrackModeLiveData = MutableLiveData<BodyTrackMode>(bodyTrackMode)
    val bodyTrackModeLiveData: LiveData<BodyTrackMode> get() = _bodyTrackModeLiveData
    fun notifyBodyTrackModeLiveData() {
        _bodyTrackModeLiveData.postValue(bodyTrackMode)
    }

    private var bodyFollowMode: BodyFollowMode = BodyFollowMode.Fix
    private val _bodyFollowModeLiveData = MutableLiveData<BodyFollowMode>(bodyFollowMode)
    val bodyFollowModeLiveData: LiveData<BodyFollowMode> get() = _bodyFollowModeLiveData
    fun notifyBodyFollowModeLiveData() {
        _bodyFollowModeLiveData.postValue(bodyFollowMode)
    }

    fun init() {
        _drivePageLiveData.postValue(drivePage)
    }

    fun checkoutArPage() {
        drivePage = DrivePage.Ar
        _drivePageLiveData.postValue(drivePage)
    }

    fun checkoutTrackPage() {
        drivePage = DrivePage.Track
        _drivePageLiveData.postValue(drivePage)
        notifyFaceTrackLiveData()
        notifyBodyTrackModeLiveData()
        notifyBodyFollowModeLiveData()
    }

    fun checkoutTextPage() {
        drivePage = DrivePage.Text
        _drivePageLiveData.postValue(drivePage)
    }

    fun switchFaceTrack() {
        isFaceTrack = !isFaceTrack
        notifyFaceTrackLiveData()
    }

    fun requestSetBodyTrackFull() {
        bodyTrackMode = BodyTrackMode.Full
        notifyBodyTrackModeLiveData()
    }

    fun requestSetBodyTrackHalf() {
        bodyTrackMode = BodyTrackMode.Half
        notifyBodyTrackModeLiveData()
    }

    fun requestSetBodyTrackClose() {
        bodyTrackMode = BodyTrackMode.Close
        notifyBodyTrackModeLiveData()
    }

    fun requestSetBodyFollowModeFix() {
        bodyFollowMode = BodyFollowMode.Fix
        notifyBodyFollowModeLiveData()
    }

    fun requestSetBodyFollowModeAlign() {
        bodyFollowMode = BodyFollowMode.Align
        notifyBodyFollowModeLiveData()
    }

    fun requestSetBodyFollowModeStage() {
        bodyFollowMode = BodyFollowMode.Stage
        notifyBodyFollowModeLiveData()
    }
}