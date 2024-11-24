package com.example.imageloaderapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.imageloaderapp.data.paging.ImagePagingSource
import com.example.imageloaderapp.domain.entity.Image
import com.example.imageloaderapp.domain.repository.ImageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
    private val imagePagingSource: ImagePagingSource
) : ImageRepository {
    override fun getImageList(): Flow<PagingData<Image>> {
        return Pager(
            config = PagingConfig(
                pageSize = DEFAULT_PAGE_SIZE,
                enablePlaceholders = ENABLE_PLACEHOLDERS
            ),
            pagingSourceFactory = { imagePagingSource }
        ).flow
    }

    private companion object {
        private const val DEFAULT_PAGE_SIZE = 1
        private const val ENABLE_PLACEHOLDERS = false
    }
}