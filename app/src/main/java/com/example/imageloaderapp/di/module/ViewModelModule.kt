package com.example.imageloaderapp.di.module

import androidx.lifecycle.ViewModel
import com.example.imageloaderapp.di.annotation.ViewModelKey
import com.example.imageloaderapp.presentation.viewmodel.ImageListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ImageListViewModel::class)
    fun bindImageViewModel(viewModel: ImageListViewModel): ViewModel
}