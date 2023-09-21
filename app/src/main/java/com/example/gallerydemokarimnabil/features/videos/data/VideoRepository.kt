package com.example.gallerydemokarimnabil.features.videos.data

import com.example.gallerydemokarimnabil.core.interfaces.data.ILocalStorageVideoIdFetcher
import com.example.gallerydemokarimnabil.core.interfaces.data.IVideoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class VideoRepository @Inject constructor(
  private val localStorageVideoIdFetcher: ILocalStorageVideoIdFetcher
) : IVideoRepository{
    override suspend fun fetchVideosIdsUris(): Flow<List<Long>> {
        return flow {
            emit(localStorageVideoIdFetcher.fetchVideosIdsUris())
        }
    }
}