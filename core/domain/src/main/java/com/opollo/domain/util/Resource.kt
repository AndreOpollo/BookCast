package com.opollo.domain.util

sealed class Resource<out T>{
    data class Success<T>(val data: T): Resource<T>()
    data class Error<T>(val throwable: Throwable):Resource<T>()
    data object Loading: Resource<Nothing>()
}