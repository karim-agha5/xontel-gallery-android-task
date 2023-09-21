package com.example.gallerydemokarimnabil.features.videos.viewmodel

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gallerydemokarimnabil.core.interfaces.data.IVideoRepository
import com.example.gallerydemokarimnabil.core.interfaces.mappers.IFromIdCollectionToBitmaps
import com.example.gallerydemokarimnabil.core.interfaces.mediastorefetchers.IVideosIdsFetcher
import com.example.gallerydemokarimnabil.features.videos.VideosThumbnailsFetchStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VideosViewModel(
    //private val mediaStoreVideosIdsFetcherImpl: IVideosIdsFetcher,
    private val videoRepository: IVideoRepository,
    private val idToBitmapMapperImpl: IFromIdCollectionToBitmaps
    ) : ViewModel() {

    //private val _videosThumbnailsState = MutableStateFlow<List<Bitmap?>>(listOf())
    private val _videosThumbnailsState =
        MutableStateFlow<VideosThumbnailsFetchStatus>(VideosThumbnailsFetchStatus.Loading)
    val videosThumbnailState = _videosThumbnailsState.asStateFlow()

    fun loadVideosThumbnails(){
        viewModelScope.launch {
            loadVideosThumbnailFromStorage()
        }
    }

    private suspend fun loadVideosThumbnailFromStorage(){

        videoRepository
            .fetchVideosIdsUris()
            .catch {
                _videosThumbnailsState.value = VideosThumbnailsFetchStatus.Failure(it)
            }
            .collect{
                val bitmapsList = idToBitmapMapperImpl.fromIdsToBitmaps(it)
                _videosThumbnailsState.value = VideosThumbnailsFetchStatus.Success(bitmapsList)
            }

       /* var bitmapsList: List<Bitmap?>

        withContext(Dispatchers.IO){
            val idsList = mediaStoreVideosIdsFetcherImpl.fetchVideosIdsUris()
            bitmapsList = idToBitmapMapperImpl.fromIdsToBitmaps(idsList)
        }

        _videosThumbnailsState.value = VideosThumbnailsFetchStatus.Success(bitmapsList)*/
        //_videosThumbnailsState.value = bitmapsList
    }
}