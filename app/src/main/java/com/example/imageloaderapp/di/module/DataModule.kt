package com.example.imageloaderapp.di.module

import com.example.imageloaderapp.data.api.ImageApiService
import com.example.imageloaderapp.data.mapper.ImageMapper
import com.example.imageloaderapp.data.paging.ImagePagingSource
import com.example.imageloaderapp.data.repository.ImageRepositoryImpl
import com.example.imageloaderapp.di.annotation.AppScope
import com.example.imageloaderapp.domain.repository.ImageRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Module
interface DataModule {
    @Binds
    fun bindImageRepository(impl: ImageRepositoryImpl): ImageRepository

    companion object {
        @Provides
        @AppScope
        fun provideRetrofit(): Retrofit {
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .build()
        }

        @Provides
        @AppScope
        fun provideImageApiService(retrofit: Retrofit): ImageApiService {
            return retrofit.create(ImageApiService::class.java)
        }

        @Provides
        fun provideImagePagingSource(
            apiService: ImageApiService,
            mapper: ImageMapper
        ): ImagePagingSource {
            return ImagePagingSource(apiService, mapper)
        }
    }
}