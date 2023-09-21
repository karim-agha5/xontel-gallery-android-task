package com.example.gallerydemokarimnabil.features.videos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gallerydemokarimnabil.core.interfaces.data.IVideoRepository
import com.example.gallerydemokarimnabil.core.interfaces.mappers.IFromIdCollectionToBitmaps
import javax.inject.Inject

class VideosViewModelFactory @Inject constructor(
    private val videoRepository: IVideoRepository,
    private val idToBitmapMapperImpl: IFromIdCollectionToBitmaps
) : ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return VideosViewModel(videoRepository,idToBitmapMapperImpl) as T
    }
}