package com.bchmsl.midterm_weather.network.utils

sealed class ResponseHandler<T>(val isLoading: Boolean = false) {
    class Success<T>(val data: T) : ResponseHandler<T>()
    class Error<T>(val error: Throwable) : ResponseHandler<T>()
    class Loading<T> : ResponseHandler<T>(true)
}
