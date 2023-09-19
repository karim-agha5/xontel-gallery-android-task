package com.example.gallerydemokarimnabil.features.core.interfaces.mappers

import android.graphics.drawable.Drawable
import android.net.Uri

interface IFromUriCollectionToDrawables {
    fun fromUrisToDrawables(listOfUris: List<Uri>) : List<Drawable?>
}