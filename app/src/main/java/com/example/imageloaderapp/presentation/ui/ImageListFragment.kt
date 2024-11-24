package com.example.imageloaderapp.presentation.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import com.example.imageloaderapp.ImageLoaderApp
import com.example.imageloaderapp.databinding.FragmentImageListBinding
import com.example.imageloaderapp.domain.entity.Image
import com.example.imageloaderapp.presentation.state.ScreenState
import com.example.imageloaderapp.presentation.ui.adapter.ImageAdapter
import com.example.imageloaderapp.presentation.viewmodel.ImageListViewModel
import com.example.imageloaderapp.presentation.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject

class ImageListFragment : Fragment() {

    private var _binding: FragmentImageListBinding? = null
    private val binding get() = _binding!!

    private val component by lazy {
        (requireActivity().application as ImageLoaderApp).component
    }

    @Inject
    lateinit var adapter: ImageAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ImageListViewModel::class.java]
    }

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImageListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        binding.recyclerView.adapter = adapter

        adapter.addLoadStateListener { loadState ->
            viewModel.handleLoadState(loadState)
        }

        binding.retryButton.setOnClickListener {
            adapter.retry()
        }
    }

    private fun renderContent(images: PagingData<Image>) {
        with(binding) {
            progressBar.isVisible = false
            errorText.isVisible = false
            retryButton.isVisible = false
            recyclerView.isVisible = true
        }

        adapter.submitData(lifecycle, images)
    }

    private fun renderLoading() {
        with(binding) {
            progressBar.isVisible = true
            errorText.isVisible = false
            retryButton.isVisible = false
            recyclerView.isVisible = false
        }
    }

    private fun renderError() {
        with(binding) {
            recyclerView.isVisible = false
            progressBar.isVisible = false
            errorText.isVisible = true
            retryButton.isVisible = true
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->
                renderState(state)
            }
        }
    }

    private fun renderState(state: ScreenState<PagingData<Image>>) {
        when(state) {
            is ScreenState.Loading -> renderLoading()
            is ScreenState.Content -> renderContent(state.content)
            is ScreenState.Error -> renderError()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = ImageListFragment()
    }
}