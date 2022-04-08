package com.mohitb117.stonks.common

sealed class ResultWrapper<T> {
    data class Success<T : Any>(val data: T, val isCachedData: Boolean = false) : ResultWrapper<T>()

    data class Error<T : Any>(val data: Any?) : ResultWrapper<T>()

    class Loading<T> : ResultWrapper<T>()
}