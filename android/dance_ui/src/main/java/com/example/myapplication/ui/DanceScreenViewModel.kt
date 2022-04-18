package com.example.myapplication.ui

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.camera.view.PreviewView
import androidx.lifecycle.*
import com.example.camera_kotlin.CameraXManager
import com.example.myapplication.player.VideoGiftView
import com.example.myapplication.util.ErrorMessage
import com.ss.ugc.android.alpha_player.IMonitor
import com.ss.ugc.android.alpha_player.IPlayerAction
import com.ss.ugc.android.alpha_player.model.ScaleType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class DanceScreenViewModel(mContext: Context) : ViewModel() {
    init {
//        initCameraAndModel(mContext)
        initVideoGiftView(mContext)
    }

    private val _danceScreenState = MutableLiveData<DanceScreenState>().also {
        it.value = DanceScreenState(isLoading = false)
    }

    val danceScreenState: LiveData<DanceScreenState>
        get() = _danceScreenState

    fun startDetectPose(preview: PreviewView) {
        viewModelScope.launch {
            CameraXManager.INSTANCE.start(preview.surfaceProvider, ::detectPose)
        }
    }

    private fun initCameraAndModel(mContext: Context) {
        viewModelScope.launch {
            suspend fun init() {
                withContext(Dispatchers.Default) {
//                    CameraXManager.INSTANCE.init(mContext)
                    _danceScreenState.postValue(
                        _danceScreenState.value!!.copy(isLoading = false)
                    )
                }
            }
            init()
        }
    }

    private val comState = MutableLiveData<Boolean>().also {
        it.value = false
    }

    private var index: Int = 0
    private fun detectPose(rgbBytearray: ByteArray) {
        when (index) {
            2 -> {
                if (_danceScreenState.value!!.analyse) {
                    val res = CameraXManager.INSTANCE.detectPost(rgbBytearray)
                }
                _danceScreenState.postValue(_danceScreenState.value!!.copy(score = _danceScreenState.value!!.score + 1))
                index %= 2
            }
            else -> {
                index++
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    lateinit var videoGiftView: VideoGiftView
    private fun initVideoGiftView(mContext: Context) {
        videoGiftView = VideoGiftView(mContext)

        val monitor = object : IMonitor {
            override fun monitor(
                state: Boolean,
                playType: String,
                what: Int,
                extra: Int,
                errorInfo: String
            ) {
                Log.i(
                    TAG,
                    "call monitor(), state: $state, playType = $playType, what = $what, extra = $extra, errorInfo = $errorInfo"
                )
            }
        }

        val playerAction = object : IPlayerAction {
            override fun onVideoSizeChanged(
                videoWidth: Int,
                videoHeight: Int,
                scaleType: ScaleType
            ) {
                Log.i(
                    TAG,
                    "call onVideoSizeChanged(), videoWidth = $videoWidth, videoHeight = $videoHeight, scaleType = $scaleType"
                )
            }

            override fun startAction() {
                _danceScreenState.postValue(
                    _danceScreenState.value!!.copy(analyse = false)
                )
                Log.i(TAG, "call startAction()")
            }

            override fun endAction() {
                _danceScreenState.postValue(
                    _danceScreenState.value!!.copy(analyse = true)
                )
                Log.i(TAG, "call endAction")
            }
        }

        videoGiftView.initPlayerController(
            mContext,
            mContext as LifecycleOwner,
            playerAction,
            monitor
        )
    }

    fun attachView() {
        videoGiftView.attachView()
    }

    fun playGift() {

//        val testPath = getResourcePath()
//        Log.i("dzy", "play gift file path : $testPath")
        /*    if ("".equals(testPath)) {
                Toast.makeText(
                    mContext,
                    "please run 'gift_install.sh gift/demoRes' for load alphaVideo resource.",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }*/
        videoGiftView.startVideoGift(getResourcePath())
    }

    fun detachView() {
        videoGiftView.detachView()
    }

    val basePath = mContext.getApplicationInfo().dataDir + "/" + "files"
    private fun getResourcePath(): String {
        val dirPath = basePath + File.separator + "alphaVideoGift" + File.separator
        val dirFile = File(dirPath)
        if (dirFile.exists() && dirFile.listFiles() != null && dirFile.listFiles().isNotEmpty()) {
            return dirFile.listFiles()[0].absolutePath
        }
        return ""
    }

    companion object {
        private const val TAG = "DanceScreenViewModel"
        fun provideFactory(context: Context): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return DanceScreenViewModel(context) as T
                }
            }
    }
}

data class DanceScreenState(
    val isLoading: Boolean = false,
    val errorMessage: List<ErrorMessage> = emptyList(),
    val score: Int = 0,
    val status: String = "建议休息",
    val effect: String = "优秀",
    val bloodOxygen: Int = 0,
    val heartRate: Int = 0,
    val calorie: Int = 0,
    val analyse: Boolean = false,
)