package com.example.imageloaderapp.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ImageApiFactory {
    private const val BASE_URL: String =
        "https://jsonplaceholder.typicode.com/"

    val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

    val apiService = retrofit.create(ImageApiService::class.java)
}