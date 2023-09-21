package com.example.gallerydemokarimnabil.features.images.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gallerydemokarimnabil.core.interfaces.data.IImageUriRepository
import com.example.gallerydemokarimnabil.core.interfaces.mappers.IFromUriCollectionToDrawables
import javax.inject.Inject

class ImagesViewModelFactory @Inject constructor(
    private val imageUriRepository: IImageUriRepository,
    private val uriCollectionToDrawablesMapper: IFromUriCollectionToDrawables
    ) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ImagesViewModel(imageUriRepository,uriCollectionToDrawablesMapper) as T
    }
}