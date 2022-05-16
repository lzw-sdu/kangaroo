package com.example.android.wearable.composeforwearos

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.wearable.composeforwearos.data.HomeRepository
import com.example.android.wearable.composeforwearos.data.VideoModel
import com.oplus.ocs.icdf.BaseJobAgent
import com.oplus.ocs.icdf.RequestJobAgentCallback
import com.sdu.kangaroo.ChatCallConsumer
import com.sdu.kangaroo.Grpctest
import io.grpc.stub.StreamObserver

class MainViewModel(private val homeRepository: HomeRepository) : ViewModel() {

    private val _mainViewState = MutableLiveData<MainViewState>().also {
        it.value = MainViewState(isPaused = false, currentIndex = 0, score = 0)
    }

    val mainViewState: LiveData<MainViewState>
        get() = _mainViewState

    companion object {
        private const val TAG = "MainViewModel"
        fun provideFactory(homeRepository: HomeRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return MainViewModel(homeRepository) as T
                }
            }
    }

    fun getVideoModel(): VideoModel {
        return homeRepository.getVideo(mainViewState.value!!.currentIndex)
    }

    fun next() {
        _mainViewState.postValue(
            mainViewState.value!!.copy(
                score = mainViewState.value!!.score + 1
            )
        )
    }

    fun last() {
        _mainViewState.postValue(
            mainViewState.value!!.copy(
                score = mainViewState.value!!.score - 1
            )
        )
    }

    fun start() {

    }

    fun pause() {

    }

    private var chatConsumer: ChatCallConsumer? = null
    private val chatConnection: RequestJobAgentCallback = object : RequestJobAgentCallback {
        override fun onJobAgentAvailable(baseJobAgent: BaseJobAgent) {
            Log.i(TAG, "onJobAgentAvailable: ChatCallConsumer")
            chatConsumer = baseJobAgent as ChatCallConsumer
        }

        override fun onError(error: String) {
            Log.e(
                TAG,
                "onJobAgentError: ChatCallConsumer, errorCode = $error"
            )
        }
    }
    var chatRequ: StreamObserver<Grpctest.RequChat>? = null
    private fun bindChat() {

    }

}

data class MainViewState(val isPaused: Boolean, val currentIndex: Int, val score: Int)