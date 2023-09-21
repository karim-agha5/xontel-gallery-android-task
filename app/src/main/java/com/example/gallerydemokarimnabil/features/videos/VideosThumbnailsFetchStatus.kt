package com.example.gallerydemokarimnabil.features.videos

import android.graphics.Bitmap

sealed class VideosThumbnailsFetchStatus{
    object Loading : VideosThumbnailsFetchStatus()
    class Success(val list: List<Bitmap?>) : VideosThumbnailsFetchStatus()
    object Failure : VideosThumbnailsFetchStatus() // TODO impl later
}
