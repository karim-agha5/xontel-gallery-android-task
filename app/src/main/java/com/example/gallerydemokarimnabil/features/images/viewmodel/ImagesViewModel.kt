package com.example.gallerydemokarimnabil.features.images.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gallerydemokarimnabil.core.interfaces.data.IImageUriRepository
import com.example.gallerydemokarimnabil.core.interfaces.mappers.IFromUriCollectionToDrawables
import com.example.gallerydemokarimnabil.features.images.ImagesFetchStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

// TODO inject the class responsible for loading the images' Uris and the mapper
class ImagesViewModel(
    private val imageUriRepository: IImageUriRepository,
    private val uriCollectionToDrawablesMapper: IFromUriCollectionToDrawables
    ) : ViewModel() {

    private val _imagesState = MutableStateFlow<ImagesFetchStatus>(ImagesFetchStatus.Loading)
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
        imageUriRepository.fetchImageUris()
            .catch {
                _imagesState.value = ImagesFetchStatus.Failure(it)
            }
            .collect{
            val images = uriCollectionToDrawablesMapper.fromUrisToDrawables(it)
            _imagesState.value = ImagesFetchStatus.Success(images)
        }
    }
}