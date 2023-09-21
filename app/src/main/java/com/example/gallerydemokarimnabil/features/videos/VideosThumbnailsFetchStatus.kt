package com.example.gallerydemokarimnabil.features.videos

import android.graphics.Bitmap

sealed class VideosThumbnailsFetchStatus{
    object Loading : VideosThumbnailsFetchStatus()
    class Success(val list: List<Bitmap?>) : VideosThumbnailsFetchStatus()
    class Failure(val throwable: Throwable) : VideosThumbnailsFetchStatus()
}
