package com.example.imageloaderapp.data.api

import com.example.imageloaderapp.data.model.ImageDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ImageApiService {

    @GET("/photos")
    suspend fun getImageList(): Response<List<ImageDto>>

    @GET("/photos/{id}")
    suspend fun getImage(
        @Path("id") id: Int
    ): Response<ImageDto>
}