package com.example.gallerydemokarimnabil.features.videos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gallerydemokarimnabil.core.interfaces.data.IVideoRepository
import com.example.gallerydemokarimnabil.core.interfaces.mappers.IFromIdCollectionToBitmaps
import com.example.gallerydemokarimnabil.features.videos.VideosThumbnailsFetchStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideosViewModel @Inject constructor(
    private val videoRepository: IVideoRepository,
    private val idToBitmapMapperImpl: IFromIdCollectionToBitmaps
    ) : ViewModel() {

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
    }
}