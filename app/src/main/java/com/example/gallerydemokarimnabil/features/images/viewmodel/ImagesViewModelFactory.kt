package com.example.gallerydemokarimnabil.features.images.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gallerydemokarimnabil.features.core.interfaces.mappers.IFromUriCollectionToDrawables
import com.example.gallerydemokarimnabil.features.core.interfaces.mediastorefetchers.ImageUriFetcher

class ImagesViewModelFactory(
    private val imageUriFetcher: ImageUriFetcher,
    private val uriCollectionToDrawablesMapper: IFromUriCollectionToDrawables
    ) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ImagesViewModel(imageUriFetcher,uriCollectionToDrawablesMapper) as T
    }
}