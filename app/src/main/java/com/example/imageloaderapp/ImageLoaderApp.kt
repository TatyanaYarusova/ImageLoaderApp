package com.example.imageloaderapp

import android.app.Application
import com.example.imageloaderapp.di.component.DaggerImageLoaderAppComponent

class ImageLoaderApp: Application() {
    val component by lazy {
        DaggerImageLoaderAppComponent.builder().build()
    }
}