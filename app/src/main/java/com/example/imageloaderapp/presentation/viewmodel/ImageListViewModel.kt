package com.example.imageloaderapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.imageloaderapp.domain.entity.Image
import com.example.imageloaderapp.domain.usecase.GetImageListUseCase
import com.example.imageloaderapp.presentation.state.ScreenState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class ImageListViewModel @Inject constructor(
    private val getImageList: GetImageListUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<ScreenState<PagingData<Image>>>(ScreenState.Loading)
    val state: StateFlow<ScreenState<PagingData<Image>>> = _state

    private val images: Flow<PagingData<Image>> = getImageList().cachedIn(viewModelScope)

    init {
        observePagingData()
    }

    private fun observePagingData() {
        images
            .onEach { pagingData ->
                _state.value = ScreenState.Content(pagingData)
            }
            .catch {
                _state.value = ScreenState.Error
            }
            .launchIn(viewModelScope)
    }

    fun handleLoadState(loadState: CombinedLoadStates) {
        if(loadState.source.refresh is LoadState.Loading)
            _state.value = ScreenState.Loading

        if (loadState.source.refresh is LoadState.Error ||
            loadState.source.append is LoadState.Error ||
            loadState.source.prepend is LoadState.Error) {
            _state.value = ScreenState.Error
        } else {
            observePagingData()
        }
    }
}
