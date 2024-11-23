package com.example.imageloaderapp.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.imageloaderapp.data.api.ImageApiFactory
import com.example.imageloaderapp.data.mapper.ImageMapper
import com.example.imageloaderapp.domain.entity.Image

class ImagePagingSource: PagingSource<Int, Image>() {

    private val apiService = ImageApiFactory.apiService
    private val mapper = ImageMapper()

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Image> {
        val position = params.key ?: 1

        return try {
            if (position > 5000) {
                return LoadResult.Page(
                    data = emptyList(),
                    prevKey = null,
                    nextKey = null
                )
            }

            val endId = if (position == 1) 3 else position

            val response = apiService.getImageList()
            if (response.isSuccessful) {
                val images = response.body()
                    ?.filter { it.id in position..endId }
                    ?.map { mapper.mapDtoToEntity(it) }
                    ?: emptyList()

                LoadResult.Page(
                    data = images,
                    prevKey = if (position == 1) null else position - 1,
                    nextKey = if (images.isEmpty() || endId >= 5000) null else endId + 1
                )
            } else {
                LoadResult.Error(Exception("API error: ${response.code()}"))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Image>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}