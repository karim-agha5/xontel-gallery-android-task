package com.example.gallerydemokarimnabil.core.interfaces.mediastorefetchers

import android.net.Uri

interface IImageUriFetcher {
    suspend fun fetchImageUris() : List<Uri>
}