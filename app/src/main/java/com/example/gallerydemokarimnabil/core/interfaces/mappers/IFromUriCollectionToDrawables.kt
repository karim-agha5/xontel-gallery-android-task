package com.example.gallerydemokarimnabil.core.interfaces.mappers

import android.graphics.drawable.Drawable
import android.net.Uri

interface IFromUriCollectionToDrawables {
    suspend fun fromUrisToDrawables(listOfUris: List<Uri>) : List<Drawable?>
}