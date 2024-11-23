package com.example.imageloaderapp.domain.usecase

import com.example.imageloaderapp.domain.repository.ImageRepository

class GetImageListUseCase (private val repository: ImageRepository) {
    operator fun invoke() = repository.getImageList()
}