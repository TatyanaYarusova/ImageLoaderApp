package com.example.imageloaderapp.domain.entity.result

sealed class RequestError {

    object ServerError : RequestError()

    object NetworkError: RequestError()

}