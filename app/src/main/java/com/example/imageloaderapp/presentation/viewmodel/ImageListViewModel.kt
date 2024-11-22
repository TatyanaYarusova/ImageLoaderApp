package com.example.imageloaderapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imageloaderapp.data.repository.ImageRepositoryImpl
import com.example.imageloaderapp.domain.entity.Image
import com.example.imageloaderapp.domain.entity.result.RequestError
import com.example.imageloaderapp.domain.entity.result.RequestResult
import com.example.imageloaderapp.domain.usecase.GetImageListUseCase
import com.example.imageloaderapp.presentation.state.ErrorEvent
import com.example.imageloaderapp.presentation.state.ScreenState
import kotlinx.coroutines.launch

class ImageListViewModel(): ViewModel() {
    private val repo = ImageRepositoryImpl()
    private val getImageList = GetImageListUseCase(repo)

    private val _state: MutableLiveData<ScreenState<List<Image>>> =
        MutableLiveData(ScreenState.Loading)
    val state: LiveData<ScreenState<List<Image>>> = _state

    init {
        loadImageList()
    }

    private fun handleError(errorType: RequestError) {
        _state.value = when (errorType) {
            is RequestError.NetworkError -> ScreenState.Error(ErrorEvent.NetworkError)
            is RequestError.ServerError -> ScreenState.Error(ErrorEvent.ServerError)
        }
    }

    private fun loadImageList() {
        _state.value = ScreenState.Loading
        viewModelScope.launch {
            try {
                when(val imageList = getImageList()) {
                    is RequestResult.Success -> _state.value = ScreenState.Content(imageList.content)
                    is RequestResult.Error -> handleError(imageList.requestError)
                }
            } catch (e: Exception) {
                _state.value = ScreenState.Error(ErrorEvent.ServerError)
            }
        }
    }
}