package com.example.imageloaderapp.data.model

import com.google.gson.annotations.SerializedName

data class ImageDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("url")
    val url: String
)
