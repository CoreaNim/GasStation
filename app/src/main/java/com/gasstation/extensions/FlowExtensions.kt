package com.gasstation.extensions

import androidx.compose.runtime.MutableState
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gasstation.common.ResultWrapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

typealias ErrorHandler = (exception: Throwable?) -> Unit
typealias SuccessHandler<T> = (value: T) -> Unit
typealias LoadingHandler = () -> Unit

fun <T> LifecycleCoroutineScope.collect(
    flow: StateFlow<ResultWrapper<T>>,
    errorHandler: ErrorHandler? = null,
    successHandler: SuccessHandler<T>? = null,
    loadingHandler: LoadingHandler? = null
) {
    launch {
        flow.collect {
            when (it) {
                is ResultWrapper.Success<*> -> {
                    successHandler?.invoke(it.takeValueOrThrow())
                }

                is ResultWrapper.GenericError -> {
                    errorHandler?.invoke(it.throwable)
                }

                is ResultWrapper.Loading -> {
                    loadingHandler?.invoke()
                }

                else -> {
                }
            }
        }
    }
}

fun <T> ViewModel.resultFlow(
    firstValue: ResultWrapper<T> = ResultWrapper.Start,
    callback: suspend () -> ResultWrapper<T>
): MutableStateFlow<ResultWrapper<T>> = MutableStateFlow(firstValue).apply {
    viewModelScope.launch {
        emit(ResultWrapper.Loading)
        value = callback.invoke()
    }
}

fun <T> ViewModel.resultCallbackFlow(
    resultFlow: MutableStateFlow<ResultWrapper<T>>,
    callback: suspend () -> ResultWrapper<T>
) {
    viewModelScope.launch {
        with(this@resultCallbackFlow) {
            resultFlow.emit(ResultWrapper.Loading)
            resultFlow.emit(callback.invoke())
        }
    }
}

fun <T> ViewModel.uiStateFlow(
    state: MutableState<ResultWrapper<T>>,
    callback: suspend () -> ResultWrapper<T>
) {
    viewModelScope.launch {
        with(this@uiStateFlow) {
            state.value = ResultWrapper.Loading
            state.value = callback.invoke()
        }
    }
}