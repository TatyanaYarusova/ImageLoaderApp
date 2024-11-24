package com.example.imageloaderapp.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.imageloaderapp.data.api.ImageApiService
import com.example.imageloaderapp.data.mapper.ImageMapper
import com.example.imageloaderapp.data.model.ImageDto
import com.example.imageloaderapp.domain.entity.Image
import javax.inject.Inject

class ImagePagingSource @Inject constructor(
    private val apiService: ImageApiService,
    private val mapper: ImageMapper
) : PagingSource<Int, Image>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Image> {
        val position = params.key ?: INITIAL_POSITION

        return try {
            if (isPositionExceedsMax(position)) {
                return createEmptyPage()
            }

            val endId = calculateEndId(position)

            val response = apiService.getImageList()
            if (response.isSuccessful) {
                val images = filterAndMapImages(response.body(), position, endId)

                LoadResult.Page(
                    data = images,
                    prevKey = calculatePreviousKey(position),
                    nextKey = calculateNextKey(images, endId)
                )
            } else {
                LoadResult.Error(Exception(ERROR_MESSAGE +response.code().toString()))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Image>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(STEP_PAGE)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(STEP_PAGE)
        }
    }

    private fun isPositionExceedsMax(position: Int): Boolean = position > MAX_POSITION

    private fun createEmptyPage(): LoadResult.Page<Int, Image> {
        return LoadResult.Page(
            data = emptyList(),
            prevKey = null,
            nextKey = null
        )
    }

    private fun calculateEndId(position: Int): Int {
        return if (position == INITIAL_POSITION) INITIAL_END_ID else position
    }

    private fun filterAndMapImages(
        body:List<ImageDto>?,
        startId: Int,
        endId: Int
    ): List<Image> {
        return body
            ?.filter { it.id in startId..endId }
            ?.map { mapper.mapDtoToEntity(it) }
            ?: emptyList()
    }

    private fun calculatePreviousKey(position: Int): Int? {
        return if (position == INITIAL_POSITION) null else position - STEP_PAGE
    }

    private fun calculateNextKey(images: List<Image>, endId: Int): Int? {
        return if (images.isEmpty() || endId >= MAX_POSITION) null else endId + STEP_PAGE
    }

    private companion object {
        private const val INITIAL_POSITION = 1
        private const val MAX_POSITION = 5000
        private const val INITIAL_END_ID = 3
        private const val STEP_PAGE = 1
        private const val ERROR_MESSAGE = "API error:"
    }
}