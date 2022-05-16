package com.faceunity.app_ptag.view_model.entity

/**
 * Created on 2022/2/21 0021 15:51.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
sealed class FuUploadAvatarState {


    class Success(val avatarId: String) : FuUploadAvatarState()

    class Error(val errorInfo: String) : FuUploadAvatarState()

}
