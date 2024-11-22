package com.example.imageloaderapp.domain.repository

import com.example.imageloaderapp.domain.entity.Image
import com.example.imageloaderapp.domain.entity.result.RequestResult

interface ImageRepository {
    suspend fun getImageList(): RequestResult<List<Image>>

    suspend fun getImage(id: Int): RequestResult<Image>
}