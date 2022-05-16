package com.faceunity.app_ptag.view_model.entity

/**
 *
 */
class FuRequestErrorInfo(val type: Type) {


    sealed class Type {
        class Retry(val tip : String) : Type()
        class Error(val tip : String) : Type()
    }
}