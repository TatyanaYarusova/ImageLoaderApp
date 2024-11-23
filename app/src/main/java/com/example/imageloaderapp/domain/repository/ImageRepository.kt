package com.example.imageloaderapp.domain.repository

import androidx.paging.PagingData
import com.example.imageloaderapp.domain.entity.Image
import com.example.imageloaderapp.domain.entity.result.RequestResult
import kotlinx.coroutines.flow.Flow

interface ImageRepository {
    fun getImageList(): Flow<PagingData<Image>>

    suspend fun getImage(id: Int): RequestResult<Image>
}