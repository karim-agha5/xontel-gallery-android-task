package com.example.gallerydemokarimnabil.features.videos.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class VideosViewModelFactory(
    val application: Application
) : ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return VideosViewModel(application) as T
    }
}