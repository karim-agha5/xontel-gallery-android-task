package com.example.gallerydemokarimnabil.core.interfaces.data

import android.net.Uri
import kotlinx.coroutines.flow.Flow

interface IImageUriRepository {
    suspend fun fetchImageUris() : Flow<List<Uri>>
}