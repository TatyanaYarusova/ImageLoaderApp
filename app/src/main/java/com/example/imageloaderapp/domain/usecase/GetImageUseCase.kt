package com.example.imageloaderapp.domain.usecase

import com.example.imageloaderapp.domain.repository.ImageRepository

class GetImageUseCase (private val repository: ImageRepository) {
    suspend operator fun invoke(id: Int) = repository.getImage(id)
}