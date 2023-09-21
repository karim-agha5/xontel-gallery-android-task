package com.example.gallerydemokarimnabil.features.videos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gallerydemokarimnabil.core.interfaces.data.IVideoRepository
import com.example.gallerydemokarimnabil.core.interfaces.mappers.IFromIdCollectionToBitmaps

class VideosViewModelFactory(
    private val videoRepository: IVideoRepository,
    val idToBitmapMapperImpl: IFromIdCollectionToBitmaps
) : ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return VideosViewModel(videoRepository,idToBitmapMapperImpl) as T
    }
}