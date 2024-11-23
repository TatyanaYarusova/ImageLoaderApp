package com.example.imageloaderapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.imageloaderapp.domain.entity.Image
import com.example.imageloaderapp.domain.usecase.GetImageListUseCase
import javax.inject.Inject

class ImageListViewModel @Inject constructor(
    private val getImageList: GetImageListUseCase,
) : ViewModel() {

    val images: LiveData<PagingData<Image>> = getImageList()
        .cachedIn(viewModelScope)
        .asLiveData()
}
