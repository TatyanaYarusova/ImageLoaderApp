package com.example.imageloaderapp.presentation.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.imageloaderapp.domain.entity.Image

object ImageDiffCallback : DiffUtil.ItemCallback<Image>() {
    override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean {
        return oldItem.id == newItem.id
    }
}