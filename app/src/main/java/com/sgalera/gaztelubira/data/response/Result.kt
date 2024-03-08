package com.sgalera.gaztelubira.data.response

sealed class Result {
    data class Success<T>(val data: T): Result()
    data object Error: Result()
    data object Loading: Result()
}