package com.example.imageloaderapp.data.mapper

import com.example.imageloaderapp.data.model.ImageDto
import com.example.imageloaderapp.domain.entity.Image

class ImageMapper {

    fun mapDtoToEntity(dto: ImageDto) = Image(
        id =  dto.id,
        img = dto.url
    )
}