package com.example.imageloaderapp.presentation.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LoadState
import com.example.imageloaderapp.ImageLoaderApp
import com.example.imageloaderapp.databinding.FragmentImageListBinding
import com.example.imageloaderapp.presentation.ui.adapter.ImageAdapter
import com.example.imageloaderapp.presentation.viewmodel.ImageListViewModel
import com.example.imageloaderapp.presentation.viewmodel.ViewModelFactory
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
        binding.rv.adapter = adapter

        adapter.addLoadStateListener { loadState ->
            renderLoading(loadState.source.refresh is LoadState.Loading)

            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.source.refresh as? LoadState.Error

            if (errorState != null) {
                renderError()
            } else {
                renderContent()
            }
        }

        binding.retryButton.setOnClickListener {
            adapter.retry()
        }
    }

    private fun renderContent() {
        with(binding) {
            errorView.visibility = GONE
            retryButton.visibility = GONE
            rv.visibility = VISIBLE
        }
    }
    private fun renderLoading(state: Boolean) {
        binding.pb.isVisible = state
    }

    private fun renderError() {
        with(binding) {
            rv.visibility = GONE
            errorView.visibility = VISIBLE
            retryButton.visibility = VISIBLE
        }
    }

    private fun observeViewModel() {
        viewModel.images.observe(viewLifecycleOwner) { images ->
            adapter.submitData(lifecycle, images)
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