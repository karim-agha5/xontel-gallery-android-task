package com.example.gallerydemokarimnabil.features.core.interfaces.mediastorefetchers

import android.net.Uri

interface IImageUriFetcher {
    fun fetchImageUris() : List<Uri>
}