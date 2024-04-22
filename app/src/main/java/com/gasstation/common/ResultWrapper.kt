package com.gasstation.common


import java.io.IOException

sealed class ResultWrapper<out T> {

    data class Success<out T>(val value: T) : ResultWrapper<T>()

    data class GenericError(val throwable: Throwable? = null) : ResultWrapper<Nothing>()

    data class AppServerError(val serverError: ServerError) : ResultWrapper<Nothing>()

    data object Start : ResultWrapper<Nothing>()

    data object Loading : ResultWrapper<Nothing>()

    data object NetworkError : ResultWrapper<Nothing>()

    @Throws(Exception::class)
    fun takeValueOrThrow(): T {
        return when (this) {
            is Success -> value
            is GenericError -> throw throwable ?: Throwable()
            is NetworkError -> throw IOException()
            is AppServerError -> throw Throwable(serverError.message)
            is Loading -> throw Throwable("Take value from loading object")
            is Start -> throw Throwable("Take value from start object")
        }
    }
}