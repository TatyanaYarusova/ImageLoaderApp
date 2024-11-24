package com.example.imageloaderapp.presentation.viewmodel

import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import com.example.imageloaderapp.domain.entity.Image
import com.example.imageloaderapp.domain.usecase.GetImageListUseCase
import com.example.imageloaderapp.presentation.state.ScreenState
import com.example.imageloaderapp.utils.MainCoroutineRule
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ImageListViewModelTest {

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private lateinit var viewModel: ImageListViewModel
    private lateinit var getImageListUseCase: GetImageListUseCase

    @Before
    fun setUp() {
        getImageListUseCase = mockk()
        every { getImageListUseCase() } returns flowOf(PagingData.empty())
        viewModel = ImageListViewModel(getImageListUseCase)
    }

    @Test
    fun `state is Loading initially`() = runTest {
        val initialState = viewModel.state.first()
        assertTrue(initialState is ScreenState.Loading)
    }

    @Test
    fun `observePagingData updates state to Content`() = runTest {
        val fakePagingData = PagingData.from(listOf(Image(1, "https://example.com/image1.jpg")))
        every { getImageListUseCase() } returns flowOf(fakePagingData)

        viewModel = ImageListViewModel(getImageListUseCase)

        val state = viewModel.state.first { it is ScreenState.Content }
        assertTrue(state is ScreenState.Content)
    }

    @Test
    fun `handleLoadState sets Loading state during refresh`() = runTest {
        val loadState = CombinedLoadStates(
            refresh = LoadState.Loading,
            prepend = LoadState.NotLoading(false),
            append = LoadState.NotLoading(false),
            source = LoadStates(
                refresh = LoadState.Loading,
                prepend = LoadState.NotLoading(false),
                append = LoadState.NotLoading(false)
            ),
            mediator = null
        )

        viewModel.handleLoadState(loadState)

        val state = viewModel.state.first()
        assertTrue(state is ScreenState.Loading)
    }

    @Test
    fun `handleLoadState sets Error state on any LoadState Error`() = runTest {
        val loadState = CombinedLoadStates(
            refresh = LoadState.Error(Exception("Error during refresh")),
            prepend = LoadState.NotLoading(false),
            append = LoadState.NotLoading(false),
            source = LoadStates(
                refresh = LoadState.Error(Exception("Error during refresh")),
                prepend = LoadState.NotLoading(false),
                append = LoadState.NotLoading(false)
            ),
            mediator = null
        )

        viewModel.handleLoadState(loadState)

        val state = viewModel.state.first()
        assertTrue(state is ScreenState.Error)
    }
}