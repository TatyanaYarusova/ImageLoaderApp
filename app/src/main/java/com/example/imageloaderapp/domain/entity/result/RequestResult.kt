package com.example.imageloaderapp.domain.entity.result

sealed class RequestResult<T> {
    data class Success<T> (val content: T) : RequestResult<T>()

    data class Error<T> (val requestError: RequestError) : RequestResult<T>()
}