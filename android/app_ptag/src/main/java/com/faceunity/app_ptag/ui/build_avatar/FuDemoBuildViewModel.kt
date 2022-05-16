package com.faceunity.app_ptag.ui.build_avatar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.faceunity.app_ptag.ui.build_avatar.entity.FaceInfo
import com.faceunity.app_ptag.ui.build_avatar.util.FaceCheckUtil
import com.faceunity.core.camera.FUCamera
import com.faceunity.core.camera.entity.FUCameraPreviewData
import com.faceunity.core.camera.enumeration.FUCameraFacingEnum
import com.faceunity.core.entity.FURenderInputData
import com.faceunity.core.enumeration.FUInputBufferEnum
import com.faceunity.core.faceunity.FUAIKit
import com.faceunity.editor_ptag.store.DevAIRepository
import com.faceunity.editor_ptag.store.DevDrawRepository
import java.util.*


class FuDemoBuildViewModel : ViewModel() {
    private val _tipMsgLiveData = MutableLiveData<String>()
    val tipMsgLiveData: LiveData<String>
        get() = _tipMsgLiveData
    private val _isCheckFacePassLiveData = MutableLiveData<Boolean>()
    val isCheckFacePassLiveData: LiveData<Boolean>
        get() = _isCheckFacePassLiveData

    fun onRenderPrepare(inputData: FURenderInputData, cameraPreviewData: FUCameraPreviewData) {
        val faceInfo: FaceInfo? = trackFace(inputData, cameraPreviewData)
        parseFaceInfo(faceInfo, inputData.width, inputData.height)
    }

    private fun parseFaceInfo(faceInfo: FaceInfo?, width: Int, height: Int) {
        val msg: String
        var isCheckFacePass = false
        if (faceInfo == null) {
            msg = "未识别到人脸"
        } else if (faceInfo.getFaceCount() != 1) {
            msg = "请保持1人"
        } else if (FaceCheckUtil.checkRotation(faceInfo.getRotationData())) {
            msg = "请保持正面"
        } else if (FaceCheckUtil.checkFaceRect(faceInfo.getFaceRect(), width, height)) {
            msg = "请将人脸对准屏幕中央"
        } else if (FaceCheckUtil.checkExpression(faceInfo.getExpressionData())) {
            msg = "请保持面部无夸张表情"
        } else {
            msg = "已就绪"
            isCheckFacePass = true
        }
        _tipMsgLiveData.postValue(msg)
        _isCheckFacePassLiveData.postValue(isCheckFacePass)
    }


    fun trackFace(inputData: FURenderInputData, cameraPreviewData: FUCameraPreviewData): FaceInfo? {
//        if (inputData.getImageBuffer() == null) return null;
        if (cameraPreviewData == null || cameraPreviewData.buffer == null) {
            return null
        }
        val fuaiKit = FUAIKit.getInstance()
        val count: Int = fuaiKit.trackFace(
            cameraPreviewData.buffer,
            FUInputBufferEnum.FU_FORMAT_NV21_BUFFER,
            inputData.width,
            inputData.height,
            getRotMode(inputData)
        )

        if (count >= 1) {
            val rotationData = FloatArray(4)
            Arrays.fill(rotationData, 0.0f)
            fuaiKit.getFaceProcessorFaceInfo(0, "rotation", rotationData)
            val faceRect = FloatArray(4)
            Arrays.fill(faceRect, 0.0f)
            fuaiKit.getFaceProcessorFaceInfo(0, "face_rect", faceRect)
            val expressionData = FloatArray(57)
            Arrays.fill(expressionData, 0.0f)
            fuaiKit.getFaceProcessorFaceInfo(0, "expression", expressionData)
            return FaceInfo(count, rotationData, faceRect, expressionData)
        }
        return null
    }

    private fun getRotMode(inputData: FURenderInputData): Int {
        val renderConfig: FURenderInputData.FURenderConfig = inputData.renderConfig
        return if (renderConfig.cameraFacing == FUCameraFacingEnum.CAMERA_FRONT) {
            (renderConfig.inputOrientation + renderConfig.deviceOrientation + 90) % 360 / 90
        } else {
            (renderConfig.inputOrientation - renderConfig.deviceOrientation + 270) % 360 / 90
        }
    }
}