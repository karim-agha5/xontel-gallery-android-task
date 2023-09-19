package com.example.gallerydemokarimnabil.features.images.viewmodel

import android.app.Application
import android.content.ContentUris
import android.graphics.drawable.Drawable
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream

// TODO inject the class responsible for loading the images' Uris and the mapper
class ImagesViewModel(private val application: Application) : AndroidViewModel(application) {

    private val _imagesState = MutableStateFlow<MutableList<Drawable?>>(mutableListOf())
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

    // TODO encapsulate the logic of loading the images inside a class
    // TODO encapsulate the logic of mapping the image's Uris in a mapper
    private suspend fun loadImagesFromInternalStorage(){
        val images = mutableListOf<Drawable?>()
        var inputStream: InputStream? = null

        withContext(Dispatchers.IO){
            val projections = arrayOf(MediaStore.Images.Media._ID)
            val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"
            application.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projections,
                null,
                null,
                sortOrder
            )?.use {
                val columnId = it.getColumnIndex(MediaStore.Images.Media._ID)
                while (it.moveToNext()){
                    val id = it.getLong(columnId)
                    val contentUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,id)
                    inputStream = application.contentResolver.openInputStream(contentUri)
                    images.add(Drawable.createFromStream(inputStream,contentUri.toString()))
                    if(!isActive){
                        Log.i("MainActivity", "The current coroutine is inactive.")
                    }
                }
            }
            inputStream?.close()
        }
        _imagesState.value = images
    }
}