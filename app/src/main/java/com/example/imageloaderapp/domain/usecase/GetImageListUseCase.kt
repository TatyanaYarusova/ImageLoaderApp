package com.example.imageloaderapp.domain.usecase

import com.example.imageloaderapp.domain.repository.ImageRepository
import javax.inject.Inject

class GetImageListUseCase @Inject constructor(private val repository: ImageRepository) {
    operator fun invoke() = repository.getImageList()
}