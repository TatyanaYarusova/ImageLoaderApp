package com.example.imageloaderapp.presentation.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.imageloaderapp.databinding.FragmentImageListBinding
import com.example.imageloaderapp.domain.entity.Image
import com.example.imageloaderapp.presentation.state.ScreenState
import com.example.imageloaderapp.presentation.ui.adapter.ImageAdapter
import com.example.imageloaderapp.presentation.viewmodel.ImageListViewModel

class ImageListFragment : Fragment() {

    private var _binding: FragmentImageListBinding? = null
    private val binding get() = _binding!!

    private val adapter = ImageAdapter()

    private val viewModel by lazy {
        ViewModelProvider(this)[ImageListViewModel::class.java]
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
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.state.observe(viewLifecycleOwner, ::renderState)
    }

    private fun renderState(state: ScreenState<List<Image>>) {
        when (state) {
            is ScreenState.Content -> renderContent(state.content)
            is ScreenState.Error -> {}
            is ScreenState.Loading -> {}

        }
    }

    private fun renderContent(content: List<Image>) {
        Log.d("TAG", content.toString())
        binding.rv.adapter = adapter
        adapter.submitList(content)
    }

    companion object {
        @JvmStatic
        fun newInstance() = ImageListFragment()
    }
}