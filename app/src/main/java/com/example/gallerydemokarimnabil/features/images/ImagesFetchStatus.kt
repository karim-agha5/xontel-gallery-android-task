package com.example.gallerydemokarimnabil.features.images

import android.graphics.drawable.Drawable

sealed class ImagesFetchStatus {
    object Loading : ImagesFetchStatus()
    class Success(val list: List<Drawable?>) : ImagesFetchStatus()
    object Failure : ImagesFetchStatus() // TODO impl later
}