package com.example.gallerydemokarimnabil.features.videos.data

import com.example.gallerydemokarimnabil.core.interfaces.data.ILocalStorageVideoIdFetcher
import com.example.gallerydemokarimnabil.core.interfaces.mediastorefetchers.IVideosIdsFetcher

class LocalStorageVideoIdDataSource(
  private val videoIdFetcher : IVideosIdsFetcher
) : ILocalStorageVideoIdFetcher{

    override suspend fun fetchVideosIdsUris(): List<Long> {
        return videoIdFetcher.fetchVideosIdsUris()
    }

}