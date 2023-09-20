package com.example.gallerydemokarimnabil.features.videos.viewmodel

import android.app.Application
import android.content.ContentUris
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.MediaMetadataRetriever
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.util.Size
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gallerydemokarimnabil.core.interfaces.mappers.IFromIdCollectionToBitmaps
import com.example.gallerydemokarimnabil.core.interfaces.mediastorefetchers.IVideosIdsFetcher
import com.example.gallerydemokarimnabil.features.videos.helpers.IdToBitmapMapperImpl
import com.example.gallerydemokarimnabil.features.videos.helpers.MediaStoreVideosIdsFetcherImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.InputStream
import java.net.URLConnection

class VideosViewModel(
    private val application: Application,
    private val mediaStoreVideosIdsFetcherImpl: IVideosIdsFetcher,
    private val idToBitmapMapperImpl: IFromIdCollectionToBitmaps
    ) : AndroidViewModel(application) {

    private val _videosThumbnailsState = MutableStateFlow<List<Bitmap?>>(listOf())
    val videosThumbnailState = _videosThumbnailsState.asStateFlow()

    fun loadVideosThumbnails(){
        viewModelScope.launch {
            loadVideosThumbnailFromStorage()
        }
    }

    private suspend fun loadVideosThumbnailFromStorage(){
        var bitmapsList: List<Bitmap?>

        withContext(Dispatchers.IO){
            val idsList = mediaStoreVideosIdsFetcherImpl.fetchVideosIdsUris()
            bitmapsList = idToBitmapMapperImpl.fromIdsToBitmaps(idsList)
        }

        _videosThumbnailsState.value = bitmapsList
    }
}