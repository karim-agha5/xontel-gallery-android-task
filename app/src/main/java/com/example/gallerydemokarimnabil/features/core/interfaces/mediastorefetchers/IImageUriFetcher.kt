package com.example.gallerydemokarimnabil.features.core.interfaces.mediastorefetchers

import android.net.Uri

interface ImageUriFetcher {
    fun fetchImageUris() : List<Uri>
}