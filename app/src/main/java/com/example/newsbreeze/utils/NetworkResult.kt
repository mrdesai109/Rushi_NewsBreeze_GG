package com.example.newsbreeze.utils

sealed class NetworkResult<T>(
    val data : T? = null,
    val msg : String? = null
) {

    class Success<T>(data: T) : NetworkResult<T>(data = data)
    class Error<T>(data: T?,msg: String?) : NetworkResult<T>(data = data,msg = msg)
    class Loading<T> : NetworkResult<T>()
}