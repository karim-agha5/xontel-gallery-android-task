package com.example.gallerydemokarimnabil.features.core.interfaces.mappers

import android.graphics.drawable.Drawable
import android.net.Uri

interface IFromUriCollectionToDrawables {
    suspend fun fromUrisToDrawables(listOfUris: List<Uri>) : List<Drawable?>
}