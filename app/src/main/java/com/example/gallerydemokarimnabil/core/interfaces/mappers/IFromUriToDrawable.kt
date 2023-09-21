package com.example.gallerydemokarimnabil.core.interfaces.mappers

import android.graphics.drawable.Drawable
import android.net.Uri

interface IFromUriToDrawable {
    fun fromUriToDrawable(uri: Uri) : Drawable?
}