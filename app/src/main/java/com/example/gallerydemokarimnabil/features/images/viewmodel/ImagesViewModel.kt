package com.example.gallerydemokarimnabil.features.images.viewmodel

import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gallerydemokarimnabil.features.core.interfaces.mappers.IFromUriCollectionToDrawables
import com.example.gallerydemokarimnabil.features.core.interfaces.mediastorefetchers.IImageUriFetcher
import com.example.gallerydemokarimnabil.features.main.MainActivity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// TODO inject the class responsible for loading the images' Uris and the mapper
class ImagesViewModel(
    private val imageUriFetcher: IImageUriFetcher,
    private val uriCollectionToDrawablesMapper: IFromUriCollectionToDrawables
    ) : ViewModel() {

    private val _imagesState = MutableStateFlow<List<Drawable?>>(listOf())
    val imagesState = _imagesState.asStateFlow()

    /*
    * This function is main-safe
    * */
    fun loadImages(){
        Log.i("MainActivity", "calling loadImages()....")
        // Fetch the data as long as the view model is kept alive in case of config change happens.
        viewModelScope.launch {
            loadImagesFromInternalStorage()
        }
    }

    private suspend fun loadImagesFromInternalStorage(){
        val urisList = imageUriFetcher.fetchImageUris()
        val images = uriCollectionToDrawablesMapper.fromUrisToDrawables(urisList)

        _imagesState.value = images
    }
}