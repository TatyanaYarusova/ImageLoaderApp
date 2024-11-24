package com.example.imageloaderapp.data.paging

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.imageloaderapp.data.api.ImageApiService
import com.example.imageloaderapp.data.mapper.ImageMapper
import com.example.imageloaderapp.data.model.ImageDto
import com.example.imageloaderapp.domain.entity.Image
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class ImagePagingSourceTest {

    private lateinit var apiService: ImageApiService
    private lateinit var mapper: ImageMapper
    private lateinit var pagingSource: ImagePagingSource

    @Before
    fun setUp() {
        apiService = mockk()
        mapper = mockk()
        pagingSource = ImagePagingSource(apiService, mapper)
    }

    @Test
    fun `load returns success page when API returns valid data`() = runTest {
        val fakeDtoList = listOf(
            ImageDto(id = 1, url = "https://example.com/image1.jpg"),
            ImageDto(id = 2, url = "https://example.com/image2.jpg"),
            ImageDto(id = 3, url = "https://example.com/image3.jpg"),
            ImageDto(id = 4, url = "https://example.com/image4.jpg")
        )
        val fakeImageList = fakeDtoList.map { Image(it.id, it.url) }

        coEvery { apiService.getImageList() } returns Response.success(fakeDtoList)
        fakeDtoList.forEachIndexed { index, dto ->
            every { mapper.mapDtoToEntity(dto) } returns fakeImageList[index]
        }

        val result = pagingSource.load(PagingSource.LoadParams.Refresh(null, 3, false))

        assertTrue(result is PagingSource.LoadResult.Page)
        val page = result as PagingSource.LoadResult.Page
        assertEquals(fakeImageList.take(3), page.data)
        assertNull(page.prevKey)
        assertEquals(4, page.nextKey)
    }

    @Test
    fun `load returns error when API fails`() = runTest {
        coEvery { apiService.getImageList() } throws Exception("Network error")

        val result = pagingSource.load(PagingSource.LoadParams.Refresh(null, 1, false))

        assertTrue(result is PagingSource.LoadResult.Error)
        val error = result as PagingSource.LoadResult.Error
        assertTrue(error.throwable is Exception)
        assertEquals("Network error", error.throwable.message)
    }

    @Test
    fun `load returns empty page when position exceeds max`() = runTest {
        val result = pagingSource.load(PagingSource.LoadParams.Refresh(MAX_POSITION + 1, 1, false))

        assertTrue(result is PagingSource.LoadResult.Page)
        val page = result as PagingSource.LoadResult.Page
        assertTrue(page.data.isEmpty())
        assertNull(page.prevKey)
        assertNull(page.nextKey)
    }

    @Test
    fun `getRefreshKey returns correct key`() {
        val fakeDtoList = listOf(
            ImageDto(id = 1, url = "https://example.com/image1.jpg"),
            ImageDto(id = 2, url = "https://example.com/image2.jpg"),
            ImageDto(id = 3, url = "https://example.com/image3.jpg"),
            ImageDto(id = 4, url = "https://example.com/image4.jpg")
        )
        val fakeImageList = fakeDtoList.map { Image(it.id, it.url) }

        val state = PagingState(
            pages = listOf(
                PagingSource.LoadResult.Page(
                    data = fakeImageList,
                    prevKey = null,
                    nextKey = 4
                )
            ),
            anchorPosition = 0,
            config = PagingConfig(pageSize = 1),
            leadingPlaceholderCount = 0
        )

        val refreshKey = pagingSource.getRefreshKey(state)
        assertEquals(3, refreshKey)
    }

    @Test
    fun `getRefreshKey returns null when no anchor position`() {
        val state = PagingState<Int, Image>(
            pages = emptyList(),
            anchorPosition = null,
            config = PagingConfig(pageSize = 1),
            leadingPlaceholderCount = 0
        )

        val refreshKey = pagingSource.getRefreshKey(state)
        assertNull(refreshKey)
    }

    companion object {
        private const val MAX_POSITION = 5000
    }
}