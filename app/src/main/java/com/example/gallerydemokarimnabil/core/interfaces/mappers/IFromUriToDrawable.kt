package com.example.gallerydemokarimnabil.features.core.interfaces.mappers

import android.graphics.drawable.Drawable
import android.net.Uri

interface IFromUriToDrawable {
    fun fromUriToDrawable(uri: Uri) : Drawable?
}