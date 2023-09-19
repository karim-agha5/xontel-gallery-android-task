package com.example.gallerydemokarimnabil.features.images.viewmodel

import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gallerydemokarimnabil.features.core.interfaces.mappers.IFromUriCollectionToDrawables
import com.example.gallerydemokarimnabil.features.core.interfaces.mediastorefetchers.ImageUriFetcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// TODO inject the class responsible for loading the images' Uris and the mapper
class ImagesViewModel(
    private val imageUriFetcher: ImageUriFetcher,
    private val uriCollectionToDrawablesMapper: IFromUriCollectionToDrawables
    ) : ViewModel() {

    private val _imagesState = MutableStateFlow<List<Drawable?>>(listOf())
    val imagesState = _imagesState.asStateFlow()

    /*
    * This function is main-safe
    * */
    fun loadImages(){
        // Fetch the data as long as the view model is kept alive in case of config change happens.
        viewModelScope.launch {
            loadImagesFromInternalStorage()
        }
    }

    private suspend fun loadImagesFromInternalStorage(){
        var images: List<Drawable?>

        withContext(Dispatchers.IO){
            val urisList = imageUriFetcher.fetchImageUris()
            images = uriCollectionToDrawablesMapper.fromUrisToDrawables(urisList)
        }

        _imagesState.value = images
    }
}