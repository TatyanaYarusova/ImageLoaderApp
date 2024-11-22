package com.example.imageloaderapp.presentation.state

sealed class ErrorEvent {

    object ServerError : ErrorEvent()

    object NetworkError: ErrorEvent()

}