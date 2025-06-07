package com.ho.baselib.result

sealed interface IResult<out T> {
    data class Success<T>(val value: T) : IResult<T>
    data class Failure(val throwable: Throwable) : IResult<Nothing>
}
