package com.example.gallerydemokarimnabil.features.images.data

import android.net.Uri
import com.example.gallerydemokarimnabil.core.interfaces.data.ILocalStorageImageUriDataSource
import com.example.gallerydemokarimnabil.core.interfaces.mediastorefetchers.IImageUriFetcher

class LocalStorageImageUriDataSource(
    private val imageUriFetcher: IImageUriFetcher
    ) : ILocalStorageImageUriDataSource{

    override suspend fun fetchImageUris(): List<Uri> {
        return imageUriFetcher.fetchImageUris()
    }
}