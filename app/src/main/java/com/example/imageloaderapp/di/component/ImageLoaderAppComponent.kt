package com.example.imageloaderapp.di.component

import com.example.imageloaderapp.di.annotation.AppScope
import com.example.imageloaderapp.di.module.DataModule
import com.example.imageloaderapp.di.module.ViewModelModule
import com.example.imageloaderapp.presentation.ui.ImageListFragment
import dagger.Component

@Component(
    modules = [
        DataModule::class,
        ViewModelModule::class,
    ]
)
@AppScope
interface ImageLoaderAppComponent {
    fun inject(fragment: ImageListFragment)
}