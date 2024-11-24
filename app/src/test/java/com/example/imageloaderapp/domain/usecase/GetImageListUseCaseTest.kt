package com.example.imageloaderapp.domain.usecase

import androidx.paging.PagingData
import com.example.imageloaderapp.domain.entity.Image
import com.example.imageloaderapp.domain.repository.ImageRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetImageListUseCaseTest {
    private val repository: ImageRepository = mockk()
    private val useCase = GetImageListUseCase(repository)

    @Test
    fun `invoke calls repository and returns flow of PagingData`() = runTest {
        val fakeImages = PagingData.from(listOf(Image(1, "https://example.com/image1.jpg")))
        every { repository.getImageList() } returns flowOf(fakeImages)

        val result = useCase()

        result.collect { pagingData ->
            assertEquals(fakeImages, pagingData)
        }
    }
}