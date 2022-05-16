package com.example.myapplication.ui

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.camera.view.PreviewView
import androidx.lifecycle.*
import com.example.myapplication.player.VideoGiftView
import com.example.myapplication.util.ErrorMessage
import com.sdu.kangaroo.CameraXManager
import com.sdu.kangaroo.alpha_player.IMonitor
import com.sdu.kangaroo.alpha_player.IPlayerAction
import com.sdu.kangaroo.alpha_player.model.ScaleType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*

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
//                    val res = CameraXManager.INSTANCE.detectPost(rgbBytearray)
                }
//                _danceScreenState.postValue(_danceScreenState.value!!.copy(score = _danceScreenState.value!!.score + 1))
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
        attachView()
    }

    fun play() {
        CustomThread1().start()
//        testThread().start()
//        changeData()
    }

    /*   fun test() {
           gift_index = 9
           playGift()
       }*/

    inner class testThread : Thread() {
        override fun run() {
            sleep(5000)
            gift_index = 9
            playGift()
        }
    }

    inner class CustomThread1 : Thread() {
        // 重写run()方法
        override fun run() {
            super.run()
            gift_index = -1;
            playGift()
            CustomThread2().start()
            /*    Timer().schedule(
                    object : TimerTask() {
                        override fun run() {
                            gift_index = 0
                            playGift()
                        }
                    },
                    4,
                )*/
        }
    }

    inner class TempThread1 : Thread() {
        // 重写run()方法
        override fun run() {
            super.run()

            gift_index = 10
            playGift()
            CustomThread2().start()
        }
    }

    /**
     * 第一段开始播放
     */
    inner class CustomThread2 : Thread() {
        // 重写run()方法
        override fun run() {
            super.run()
            sleep(4000)
//            TempThread().start()
            gift_index = 0
            playGift()
            TempThread2().start()
        }
    }

    inner class TempThread2 : Thread() {
        // 重写run()方法
        override fun run() {
            super.run()
            sleep(16000)
            gift_index = 10
            playGift()
            CustomThread3().start()
        }
    }

    /**
     * 第一段结束后，第二段开始
     */
    inner class CustomThread3 : Thread() {
        // 重写run()方法
        override fun run() {
            super.run()
            sleep(1500)
            sleep(20000)
            _danceScreenState.postValue(
                _danceScreenState.value!!.copy(score = 600)
            )
            gift_index = 2
            playGift()
            TempThread3().start()
        }
    }

    inner class TempThread3 : Thread() {
        // 重写run()方法
        override fun run() {
            super.run()
            sleep(10000)
            gift_index = 10
            playGift()
            CustomThread4().start()
        }
    }

    /**
     * 第二段结束后，发生错误，并播放2-1
     */
    inner class CustomThread4 : Thread() {
        // 重写run()方法
        override fun run() {
            super.run()
            sleep(1500)
            sleep(3000)
            _danceScreenState.postValue(
                _danceScreenState.value!!.copy(score = 400, effect = "良好")
            )
            gift_index = 3
            playGift()
            TempThread4().start()
        }
    }

    inner class TempThread4 : Thread() {
        // 重写run()方法
        override fun run() {
            super.run()
            sleep(11000)
            gift_index = 10
            playGift()
            CustomThread5().start()
        }
    }

    /**
     * 2-1过程，并播放3
     */
    inner class CustomThread5 : Thread() {
        // 重写run()方法
        override fun run() {
            super.run()
            sleep(1500)
            sleep(14000)
            _danceScreenState.postValue(
                _danceScreenState.value!!.copy(score = 800, effect = "优秀")
            )
            gift_index = 4
            playGift()
            CustomThread6().start()
        }
    }

    inner class TempThread5 : Thread() {
        // 重写run()方法
        override fun run() {
            super.run()
            sleep(7000)
            gift_index = 10
            playGift()
            CustomThread6().start()
        }
    }

    /**
     * 3播放5秒，跳回2
     */
    inner class CustomThread6 : Thread() {
        // 重写run()方法
        override fun run() {
            super.run()
            sleep(5000)
            gift_index = 2
            playGift()
        }
    }

    inner class TempThread : Thread() {
        // 重写run()方法
        override fun run() {
            super.run()
            gift_index = 10
            playGift()
        }
    }
/*    */
    /**
     * 2 播放，并结束
     *//*
    inner class CustomThread7 : Thread() {
        // 重写run()方法
        override fun run() {
            super.run()
            sleep(5000)
            gift_index = 2
            playGift()
        }
    }*/

    fun changeData() {
        Timer().schedule(object : TimerTask() {
            override fun run() {
                //修改数据
                val blood = (97..100).random()
                val heartRate = (80..100).random()
                val ka = (10..20).random()
                _danceScreenState.postValue(
                    _danceScreenState.value!!.copy(
                        bloodOxygen = blood,
                        heartRate = heartRate,
                        calorie = _danceScreenState.value!!.calorie + ka
                    )
                )
            }
        }, Date(), 3000)
    }


    fun attachView() {
        videoGiftView.attachView()
    }

    var gift_index = 0;
    fun playGift() {

        val path: String = when (gift_index) {
            -1 -> "start"
            0 -> "1"
            1 -> "1-1"
            2 -> "2"
            3 -> "2-1"
            4 -> "3"
            5 -> "3-1"
            9 -> "together"
            10 -> "repeat"
            else -> "1"
        }
        videoGiftView.startVideoGift(getResourcePath(path))
    }

    fun detachView() {
        videoGiftView.detachView()
    }

    val basePath = mContext.getApplicationInfo().dataDir + "/" + "files"
    private fun getResourcePath(path: String): String {
        val dirPath =
            basePath + File.separator + "alphaVideoGift" + File.separator + path + File.separator
        val dirFile = File(dirPath)
//        if (dirFile.exists() && dirFile.listFiles() != null && dirFile.listFiles().isNotEmpty()) {
//            return dirFile.listFiles()[0].absolutePath
//        }
        return dirFile.absolutePath
//        return ""
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
    val status: String = "状态良好",
    val effect: String = "优秀",
    val bloodOxygen: Int = 0,
    val heartRate: Int = 0,
    val calorie: Int = 0,
    val analyse: Boolean = false,
)