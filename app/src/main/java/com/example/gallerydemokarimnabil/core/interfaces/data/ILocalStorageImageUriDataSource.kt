package com.example.gallerydemokarimnabil.core.interfaces.data

import android.net.Uri

interface ILocalStorageImageUriDataSource {
    suspend fun fetchImageUris() : List<Uri>
}