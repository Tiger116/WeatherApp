package com.zhuravlev.vitaly.weatherapp.base.atom

sealed class Atom<T> {
    class Loading<T> : Atom<T>()

    data class Success<T>(
        val content: T
    ) : Atom<T>()

    data class Error<T>(
        val throwable: Throwable
    ) : Atom<T>()

    fun isSuccess() = this is Success

    fun isError() = this is java.lang.Error
}
