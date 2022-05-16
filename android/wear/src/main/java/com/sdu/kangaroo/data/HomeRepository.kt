package com.example.android.wearable.composeforwearos.data


class HomeRepository(private val homeDateSource: HomeDataSource) {
    val videos: List<VideoModel> = homeDateSource.data

    fun getVideo(id: Int): VideoModel {
        return homeDateSource.data[id]
    }
}