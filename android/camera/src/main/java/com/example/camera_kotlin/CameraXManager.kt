package com.example.camera_kotlin

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.util.Size
import android.view.Surface
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.*
import androidx.camera.video.VideoCapture
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.lifecycle.LifecycleOwner
import com.google.common.util.concurrent.ListenableFuture
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

typealias LumaListener = (luma: ByteArray) -> Unit

class CameraXManager {

    private lateinit var mContext: Context
    private lateinit var previewProvider: Preview.SurfaceProvider

    private var imageCapture: ImageCapture? = null

    private var videoCapture: VideoCapture<Recorder>? = null
    private var recording: Recording? = null

    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>

    private lateinit var cameraExecutor: ExecutorService

    private var isBackCamera: Boolean = true
    private lateinit var analyzeEvent: (rgbByteArray: ByteArray) -> Unit

    private val ncnnbodypose: NcnnBodypose = NcnnBodypose()

    companion object {
        private const val TAG = "CameraXManager"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS =
            mutableListOf(
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()

/*        private var time: Long? = null
        private var times: Long = 0L
        private var avarage = 0L
        fun avarageTime(stamp: Long): Long {
            avarage = (avarage * times + stamp) / (times + 1)
            times++
            return avarage
        }*/

        val INSTANCE: CameraXManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            CameraXManager()
        }
    }

    /**
     * 初始化
     */
    fun init(mContext: Context) {
        this.mContext = mContext
        this.cameraProviderFuture = ProcessCameraProvider.getInstance(mContext)
        reload()
    }

    private fun contextConvertToActivity(context: Context): Context {
        return when (context) {
            is Activity -> context
            is ContextWrapper -> contextConvertToActivity(context.baseContext)
            is Application -> contextConvertToActivity(context.baseContext)
            else -> context.applicationContext
        }
    }


    fun reload() {
        val ret_init: Boolean = ncnnbodypose.loadModel(mContext.assets, 0, 0)
        if (!ret_init) {
            Log.e("MainActivity", "ncnnbodypose loadModel failed")
        }
    }

    fun detectPost(rgbByteArray: ByteArray) {
        ncnnbodypose.detectPose(rgbByteArray)
    }

    /**
     * 启动Camera
     */
    fun start(
        previewProvider: Preview.SurfaceProvider,
        analyzeEvent: (rgbByteArray: ByteArray) -> Unit
    ) {
        if (allPermissionsGranted()) {
            this.previewProvider = previewProvider
            this.analyzeEvent = analyzeEvent
            cameraExecutor = Executors.newSingleThreadExecutor()
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                mContext as Activity,
                REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }
    }

    /**
     * 切换相机
     */
    fun switchCamera() {
        isBackCamera = !isBackCamera
        //关闭相机服务并重启
        cameraExecutor.shutdown()
        cameraExecutor = Executors.newSingleThreadExecutor()
        cameraProviderFuture.cancel(true)
        startCamera()
    }

    /**
     * 启动相机
     */
    private fun startCamera() {
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build()
                .also { it.setSurfaceProvider(previewProvider) }
            //初始化相片捕捉
//            imageCapture = ImageCapture.Builder()
//                .build()

//            val recorder = Recorder.Builder()
//                .setQualitySelector(QualitySelector.from(Quality.HIGHEST))
//                .build()
//            videoCapture = VideoCapture.withOutput(recorder)

            val imageAnalyzer =
                ImageAnalysis.Builder().setTargetResolution(Size(480, 640))
                    .setOutputImageRotationEnabled(true).setTargetRotation(Surface.ROTATION_0)
                    .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_YUV_420_888)
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build()
                    .also { luma ->
                        luma.setAnalyzer(cameraExecutor, LuminosityAnalyzer {
                            this.analyzeEvent(it)
                        })
                    }

            val cameraSelector =
                if (isBackCamera) CameraSelector.DEFAULT_BACK_CAMERA else CameraSelector.DEFAULT_FRONT_CAMERA

            try {
                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle(
                    mContext as LifecycleOwner,
                    cameraSelector,
                    preview,
//                    videoCapture,
//                    imageCapture,
                    imageAnalyzer
                )
                System.nanoTime()
            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(mContext))
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            mContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * 拍摄照片并存储
     */
    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val name =
            SimpleDateFormat(FILENAME_FORMAT, Locale.CHINESE).format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-image")
            }
        }

        val outputOptions = ImageCapture.OutputFileOptions.Builder(
            mContext.contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        ).build()
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(mContext),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val msg = "Photo capture succeeded: ${outputFileResults.savedUri}"
                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show()
                    Log.d(TAG, msg)
                }

                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

            }
        )
    }

    /**
     * 拍摄并存储视频
     */
    private fun captureVideo() {
        val videoCapture = this.videoCapture ?: return

//        viewBinding.videoCaptureButton.isEnabled = false

        val curRecording = recording
        if (curRecording != null) {
            curRecording.stop()
            recording = null
            return
        }

        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/CameraX-Video")
            }
        }

        val mediaStoreOutputOptions = MediaStoreOutputOptions
            .Builder(mContext.contentResolver, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
            .setContentValues(contentValues)
            .build()

        recording = videoCapture.output.prepareRecording(mContext, mediaStoreOutputOptions).apply {
            if (PermissionChecker.checkSelfPermission(
                    mContext,
                    Manifest.permission.RECORD_AUDIO
                ) ==
                PermissionChecker.PERMISSION_GRANTED
            ) {
                withAudioEnabled()
            }
        }.start(ContextCompat.getMainExecutor(mContext)) {
            when (it) {
                is VideoRecordEvent.Start -> {
                    Log.i(TAG, "Start")
                }
                is VideoRecordEvent.Finalize -> {
                    if (!it.hasError()) {
                        val msg = "Video capture succeeded: " +
                                "${it.outputResults.outputUri}"
                        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT)
                            .show()
                        Log.d(TAG, msg)
                    } else {
                        recording?.close()
                        recording = null
                        Log.e(
                            TAG, "Video capture ends with error: " +
                                    "${it.error}"
                        )
                    }
                }
            }
        }
    }

    /**
     * 分析视频帧数据
     */
    private class LuminosityAnalyzer(private val listener: LumaListener) : ImageAnalysis.Analyzer {

        private fun ByteBuffer.toByteArray(): ByteArray {
            rewind()    // Rewind the buffer to zero
            val data = ByteArray(remaining())
            get(data)   // Copy the buffer into a byte array
            return data // Return the byte array
        }

        override fun analyze(image: ImageProxy) {
       /*     if (time == null) {
                time = System.nanoTime()
                image.close()
                return
            }
            Log.i(TAG, avarageTime(System.nanoTime() - time!!).toString())
            time = System.nanoTime()*/
            val buffer = image.planes[0].buffer
            val data = buffer.toByteArray()
//            val pixels = data.map { it.toInt() and 0xFF }
//            val luma = pixels.average()
//            var res = ByteArray(data.size)
//            NV212RGBorBGR(data, image.width, image.height, res, true)

            listener(data)

            image.close()
        }
    }
}