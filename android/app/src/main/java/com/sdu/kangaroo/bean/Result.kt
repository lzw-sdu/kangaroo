package com.sdu.kangaroo.bean

data class Result<T>(
    val code: Int = 200,
    val msg: String = "",
    val data: T? = null
)

