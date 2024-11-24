package com.example.imageloaderapp.data.mapper

import com.example.imageloaderapp.data.model.ImageDto
import org.junit.Assert.assertEquals
import org.junit.Test

class ImageMapperTest {
    private val mapper = ImageMapper()

    @Test
    fun `mapDtoToEntity maps ImageDto to Image correctly`() {
        val dto = ImageDto(id = 1, url = "https://example.com/image1.jpg")

        val result = mapper.mapDtoToEntity(dto)

        assertEquals(1, result.id)
        assertEquals("https://example.com/image1.jpg", result.img)
    }
}