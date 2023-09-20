package com.example.gallerydemokarimnabil.features.videos.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gallerydemokarimnabil.core.interfaces.mappers.IFromIdCollectionToBitmaps
import com.example.gallerydemokarimnabil.core.interfaces.mediastorefetchers.IVideosIdsFetcher

class VideosViewModelFactory(
    val application: Application,
    val mediaStoreVideosIdsFetcherImpl: IVideosIdsFetcher,
    val idToBitmapMapperImpl: IFromIdCollectionToBitmaps
) : ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return VideosViewModel(application,mediaStoreVideosIdsFetcherImpl,idToBitmapMapperImpl) as T
    }
}