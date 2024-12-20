package com.example.imageloaderapp.presentation.state

sealed class ScreenState<out T> {

    object Loading : ScreenState<Nothing>()

    data class Content<T>(val content: T) : ScreenState<T>()

    object Error : ScreenState<Nothing>()

}