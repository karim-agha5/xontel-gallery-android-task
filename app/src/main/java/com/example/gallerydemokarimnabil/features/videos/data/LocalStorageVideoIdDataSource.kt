package com.example.gallerydemokarimnabil.features.videos.data

import com.example.gallerydemokarimnabil.core.interfaces.data.ILocalStorageVideoIdFetcher
import com.example.gallerydemokarimnabil.core.interfaces.mediastorefetchers.IVideosIdsFetcher
import javax.inject.Inject

class LocalStorageVideoIdDataSource @Inject constructor(
  private val videoIdFetcher : IVideosIdsFetcher
) : ILocalStorageVideoIdFetcher{

    override suspend fun fetchVideosIdsUris(): List<Long> {
        return videoIdFetcher.fetchVideosIdsUris()
    }

}