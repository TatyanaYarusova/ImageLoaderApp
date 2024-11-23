package com.example.imageloaderapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.imageloaderapp.data.repository.ImageRepositoryImpl
import com.example.imageloaderapp.domain.entity.Image
import com.example.imageloaderapp.domain.entity.result.RequestError
import com.example.imageloaderapp.domain.entity.result.RequestResult
import com.example.imageloaderapp.domain.usecase.GetImageListUseCase
import com.example.imageloaderapp.presentation.state.ErrorEvent
import com.example.imageloaderapp.presentation.state.ScreenState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ImageListViewModel() : ViewModel() {
    private val repo = ImageRepositoryImpl()
    private val getImageList = GetImageListUseCase(repo)


    private val _state: MutableLiveData<ScreenState<PagingData<Image>>> =
        MutableLiveData(ScreenState.Loading)
    val state: LiveData<ScreenState<PagingData<Image>>> = _state

    private val cachedFlow: Flow<PagingData<Image>> = getImageList()
        .map { result ->
            when (result) {
                is RequestResult.Success -> result.content
                is RequestResult.Error -> {
                    handleError(result.requestError)
                    PagingData.empty()
                }
            }
        }
        .cachedIn(viewModelScope)

    init {
        loadImageList()
    }


    private fun loadImageList() {
        _state.value = ScreenState.Loading

        viewModelScope.launch {
            cachedFlow
                .catch {
                    _state.value = ScreenState.Error(ErrorEvent.ServerError)
                }
                .collect { pagingData ->
                    _state.value = ScreenState.Content(pagingData)
                }
        }
    }

    private fun handleError(errorType: RequestError) {
        _state.value = when (errorType) {
            is RequestError.NetworkError -> ScreenState.Error(ErrorEvent.NetworkError)
            is RequestError.ServerError -> ScreenState.Error(ErrorEvent.ServerError)
        }
    }
}
