package com.example.imageloaderapp.data.api

import com.example.imageloaderapp.data.model.ImageDto
import retrofit2.Response
import retrofit2.http.GET

interface ImageApiService {
    @GET("/photos")
    suspend fun getImageList(): Response<List<ImageDto>>
}