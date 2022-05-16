package com.faceunity.app_ptag.ui.build_avatar.renderer

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.opengl.EGLConfig
import android.opengl.GLES11Ext
import android.opengl.GLES20
import android.view.MotionEvent
import com.faceunity.core.camera.FUCamera
import com.faceunity.core.camera.entity.FUCameraConfig
import com.faceunity.core.camera.entity.FUCameraPreviewData
import com.faceunity.core.camera.enumeration.FUCameraFacingEnum
import com.faceunity.core.entity.FURenderInputData
import com.faceunity.core.enumeration.FUExternalInputEnum
import com.faceunity.core.enumeration.FUInputBufferEnum
import com.faceunity.core.enumeration.FUInputTextureEnum
import com.faceunity.core.enumeration.FUTransformMatrixEnum
import com.faceunity.core.listener.OnFUCameraListener
import com.faceunity.core.renderer.base.FUAbstractRenderer
import com.faceunity.toolbox.program.FUProgramTexture2d
import com.faceunity.toolbox.program.FUProgramTextureOES
import com.faceunity.toolbox.utils.FUDecimalUtils
import com.faceunity.toolbox.utils.FUDensityUtils
import com.faceunity.toolbox.utils.FUGLUtils
import kotlin.math.abs

/**
 * Created on 2021/12/10 0010 15:39.


 */
class FuDemoCameraRenderer(context: Context) : FUAbstractRenderer() {

    /**
     * 纹理Program
     **/
    private var mProgramTexture2d: FUProgramTexture2d? = null
    private var mProgramTextureOES: FUProgramTextureOES? = null

    val TEXTURE_MATRIX = floatArrayOf(
        1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
        0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f
    )
    protected var smallViewMatrix: FloatArray = TEXTURE_MATRIX.copyOf()//小窗图形绑定矩阵


    /**
     * 相机
     */
    private var mCameraTextId = 0
    private var mFUCameraConfig: FUCameraConfig? = null
    private var mFUCamera: FUCamera = FUCamera()
    private var mDeviceOrientation = 90//手机设备朝向
    private var mCameraWidth = 1
    private var mCameraHeight = 1
    private var mFUCameraMvpMatrix = FUDecimalUtils.copyArray(FUGLUtils.IDENTITY_MATRIX)
    private var mFUCameraTexMatrix = FUDecimalUtils.copyArray(FUGLUtils.IDENTITY_MATRIX)

    /** 全身 avatar 相关 **/
    protected var drawSmallViewport = false
    protected val smallViewportWidth = FUDensityUtils.dp2px(context, 120f)
    protected val smallViewportHeight = FUDensityUtils.dp2px(context, 214f)
    protected var smallViewportX = 0
    protected var smallViewportY = 0
    protected val smallViewportHorizontalPadding = FUDensityUtils.dp2px(context, 16f)
    protected val smallViewportTopPadding = FUDensityUtils.dp2px(context, 88f)
    protected val smallViewportBottomPadding = FUDensityUtils.dp2px(context, 100f)
    protected var touchX = 0
    protected var touchY = 0

    @Volatile
    private var mFUCameraPrepare = false

    /* 相机渲染数据  */
    private val mFUCameraInputData by lazy {
        FURenderInputData(720, 1280)
            .apply {
                texture = FURenderInputData.FUTexture(FUInputTextureEnum.FU_ADM_FLAG_EXTERNAL_OES_TEXTURE, mCameraTextId)
                renderConfig.apply {
                    externalInputType = FUExternalInputEnum.EXTERNAL_INPUT_TYPE_CAMERA
                    cameraFacing = FUCameraFacingEnum.CAMERA_BACK
                    inputTextureMatrix = FUTransformMatrixEnum.CCROT0_FLIPVERTICAL
                    inputBufferMatrix = FUTransformMatrixEnum.CCROT0_FLIPVERTICAL
                }
            }
    }
    private val mFUCameraInputDataLock = Object()//相机数据锁

    /**传感器**/
    private val mSensorManager by lazy { context.getSystemService(Context.SENSOR_SERVICE) as SensorManager }
    private val mSensor by lazy { mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) }


    /**
     * 绑定相机配置
     * @param cameraConfig FUCameraConfig
     */
    fun bindCameraConfig(cameraConfig: FUCameraConfig): FuDemoCameraRenderer {
        mFUCameraConfig = cameraConfig
        mSensorManager.registerListener(mSensorEventListener, mSensor, SensorManager.SENSOR_DELAY_NORMAL)
        return this
    }


    /**
     * 暂停渲染
     */
    public override fun pauseRender() {
        mSensorManager.unregisterListener(mSensorEventListener)
        setDrawFrameSwitch(false)
        closeCamera()
    }


    /**
     * 恢复渲染
     */
    public override fun resumeRender() {
        mSensorManager.registerListener(mSensorEventListener, mSensor, SensorManager.SENSOR_DELAY_NORMAL)
        setDrawFrameSwitch(true)
        openCamera()
    }


    /**
     * 窗口初始化
     * @param config EGLConfig?
     */
    public override fun surfaceCreated(config: EGLConfig?) {
        mCameraTextId = FUGLUtils.createTextureObject(GLES11Ext.GL_TEXTURE_EXTERNAL_OES)
        mProgramTexture2d = FUProgramTexture2d()
        mProgramTextureOES = FUProgramTextureOES()
        openCamera()
    }

    /**
     * 窗口尺寸修改
     * @param width Int
     * @param height Int
     */
    public override fun surfaceChanged(width: Int, height: Int) {
        mFUCameraMvpMatrix = FUGLUtils.changeMvpMatrixCrop(
            mGlTextureWidth.toFloat(), mGlTextureHeight.toFloat(), mCameraHeight.toFloat(), mCameraWidth.toFloat()
        )
        mRenderKitMvpMatrix = FUDecimalUtils.copyArray(mFUCameraMvpMatrix)

        smallViewportX = width - smallViewportWidth - smallViewportHorizontalPadding
        smallViewportY = height - smallViewportHeight - smallViewportBottomPadding
    }

    /**
     * 环境判断
     * @return Boolean
     */
    public override fun isRenderEnvironmentPrepare(): Boolean {
        if (mProgramTexture2d == null || mProgramTextureOES == null) return false
        if (mFUCameraPrepare) {
            val surfaceTexture = mFUCamera.getSurfaceTexture() ?: return false
            try {
                surfaceTexture.updateTexImage()
            } catch (e: Exception) {
                e.printStackTrace()
                return false
            }
            return true
        } else {
            return false
        }
    }

    /**
     * 构造数据
     * @return FURenderInputData
     */
    public override fun buildFURenderInputData(): FURenderInputData {
        return getCameraInputData()
    }

    /**
     * 窗口视图绑定
     */
    public override fun drawRenderFrame() {
        val faceUnity2DTexId = mCurrentOutputData?.texture?.texId ?: 0
        if (faceUnity2DTexId <= 0) {
            mProgramTextureOES!!.drawFrame(mCameraTextId, mFUCameraTexMatrix, mFUCameraMvpMatrix)
            return
        }

        if (drawSmallViewport) {
            GLES20.glViewport(smallViewportX, smallViewportY, smallViewportWidth, smallViewportHeight)
            mProgramTextureOES!!.drawFrame(mCameraTextId, mFUCameraTexMatrix, smallViewMatrix)
            GLES20.glViewport(0, 0, mGlTextureWidth, mGlTextureHeight)
        }
        mProgramTexture2d!!.drawFrame(faceUnity2DTexId, mRenderKitTexMatrix, mRenderKitMvpMatrix)
    }


    /**
     * 资源释放
     */
    public override fun release() {
        releaseCamera()
        super.release()
    }

    public override fun releaseGLResource() {
        super.releaseGLResource()
        if (mCameraTextId > 0) {
            FUGLUtils.deleteTextures(intArrayOf(mCameraTextId))
            mCameraTextId = 0
        }
        mProgramTexture2d?.release()
        mProgramTexture2d = null
        mProgramTextureOES?.release()
        mProgramTextureOES = null

    }

    //region 相机


    /**
     * 开启相机
     */
    private fun openCamera() {
        mFUCameraPrepare = false
        mFUCameraConfig?.let {
            mFUCamera.openCamera(it, mCameraTextId, mOnFUCameraListener)
        }
    }

    /**
     * 关闭相机
     */
    private fun closeCamera() {
        mFUCamera.closeCamera()
    }

    /**
     * 切换相机
     */
    fun switchCamera() {
        if (!mFUCameraPrepare) return
        mFUCamera.switchCamera()
        mFUCameraPrepare = false
    }


    /**
     * 释放相机
     */
    private fun releaseCamera() {
        mFUCamera.releaseCamera()
    }

    /**
     * 相机数据回调
     */
    private val mOnFUCameraListener = object : OnFUCameraListener {

        override fun onPreviewFrame(previewData: FUCameraPreviewData) {
            if (mCameraWidth != previewData.width || mCameraHeight != previewData.height) {
                mCameraWidth = previewData.width
                mCameraHeight = previewData.height
                mFUCameraMvpMatrix = FUGLUtils.changeMvpMatrixCrop(
                    mGlTextureWidth.toFloat(), mGlTextureHeight.toFloat(), mCameraHeight.toFloat(), mCameraWidth.toFloat()
                )
                smallViewMatrix = FUGLUtils.changeMvpMatrixCrop(smallViewportWidth.toFloat(), smallViewportHeight.toFloat(), mCameraHeight.toFloat(), mCameraWidth.toFloat())

                mRenderKitMvpMatrix = FUDecimalUtils.copyArray(mFUCameraMvpMatrix)
            }
            mFUCameraConfig?.cameraFacing = previewData.cameraFacing
            mFUCameraConfig?.cameraHeight = previewData.height
            mFUCameraConfig?.cameraWidth = previewData.width
            synchronized(mFUCameraInputDataLock) {
                mFUCameraInputData.apply {
                    width = previewData.width
                    height = previewData.height
                    imageBuffer = FURenderInputData.FUImageBuffer(FUInputBufferEnum.FU_FORMAT_NV21_BUFFER, previewData.buffer)
                    renderConfig.apply {
                        externalInputType = FUExternalInputEnum.EXTERNAL_INPUT_TYPE_CAMERA
                        inputOrientation = previewData.cameraOrientation
                        deviceOrientation = mDeviceOrientation
                        cameraFacing = previewData.cameraFacing
                        if (cameraFacing == FUCameraFacingEnum.CAMERA_FRONT) {
                            mFUCameraTexMatrix = FUGLUtils.CAMERA_TEXTURE_MATRIX
                            inputTextureMatrix = FUTransformMatrixEnum.CCROT90_FLIPHORIZONTAL
                            inputBufferMatrix = FUTransformMatrixEnum.CCROT90_FLIPHORIZONTAL
                        } else {
                            mFUCameraTexMatrix = FUGLUtils.CAMERA_TEXTURE_MATRIX_BACK
                            inputTextureMatrix = FUTransformMatrixEnum.CCROT270
                            inputBufferMatrix = FUTransformMatrixEnum.CCROT270
                        }
                    }
                }
                mFUCameraPrepare = true
            }
            mGLTextureView?.requestRender()
        }
    }

    /**
     * 构造数据
     * @return FURenderInputData
     */
    private fun getCameraInputData(): FURenderInputData {
        synchronized(mFUCameraInputDataLock) {
            return mFUCameraInputData.clone()
        }
    }

    /**
     * 内置陀螺仪
     */
    private val mSensorEventListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        }

        override fun onSensorChanged(event: SensorEvent?) {
            if (event!!.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                val x = event.values[0]
                val y = event.values[1]
                if (abs(x) > 3 || abs(y) > 3) {
                    mDeviceOrientation = if (abs(x) > abs(y)) {
                        if (x > 0) 0 else 180
                    } else {
                        if (y > 0) 90 else 270
                    }

                }
            }
        }
    }

    //endregion


    //region 小窗拖拽
    /**
     * 全身Avatar小窗口显示
     * @param isShow Boolean
     */
    fun drawSmallViewport(isShow: Boolean) {
        drawSmallViewport = isShow
    }

    /**
     * avatar 小窗拖拽
     * @param x Int
     * @param y Int
     * @param action Int
     */
    fun onTouchEvent(x: Int, y: Int, action: Int) {
        if (!drawSmallViewport) {
            return
        }
        if (action == MotionEvent.ACTION_MOVE) {
            if (x < smallViewportHorizontalPadding || x > mGlTextureWidth - smallViewportHorizontalPadding || y < smallViewportTopPadding || y > mGlTextureHeight - smallViewportBottomPadding
            ) {
                return
            }
            val touchX = touchX
            val touchY = touchY
            this.touchX = x
            this.touchY = y
            val distanceX = x - touchX
            val distanceY = y - touchY
            var viewportX = smallViewportX
            var viewportY = smallViewportY
            viewportX += distanceX
            viewportY -= distanceY
            if (viewportX < smallViewportHorizontalPadding || viewportX + smallViewportWidth > mGlTextureWidth - smallViewportHorizontalPadding || mGlTextureHeight - viewportY - smallViewportHeight < smallViewportTopPadding || viewportY < smallViewportBottomPadding
            ) {
                return
            }
            smallViewportX = viewportX
            smallViewportY = viewportY
        } else if (action == MotionEvent.ACTION_DOWN) {
            touchX = x
            touchY = y
        } else if (action == MotionEvent.ACTION_UP) {
            val alignLeft = smallViewportX < mGlTextureWidth / 2
            smallViewportX = if (alignLeft) smallViewportHorizontalPadding else mGlTextureWidth - smallViewportHorizontalPadding - smallViewportWidth
            touchX = 0
            touchY = 0
        }
    }
    //endregion 小窗拖拽


    fun getFuCamera() = mFUCamera

}