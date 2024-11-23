package com.example.imageloaderapp.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.example.imageloaderapp.databinding.ImageItemBinding
import com.example.imageloaderapp.domain.entity.Image
import com.squareup.picasso.Picasso
import javax.inject.Inject

class ImageAdapter @Inject constructor(
) : PagingDataAdapter<Image, ImageViewHolder>(ImageDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val image = getItem(position)
        Picasso.get().load(image?.img).into(holder.binding.image)
    }
}