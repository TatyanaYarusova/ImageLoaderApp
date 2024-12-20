package com.example.imageloaderapp.domain.repository

import androidx.paging.PagingData
import com.example.imageloaderapp.domain.entity.Image
import kotlinx.coroutines.flow.Flow

interface ImageRepository {
    fun getImageList(): Flow<PagingData<Image>>
}