package com.example.gallerydemokarimnabil.features.videos.data

import com.example.gallerydemokarimnabil.core.interfaces.data.ILocalStorageVideoIdFetcher
import com.example.gallerydemokarimnabil.core.interfaces.data.IVideoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class VideoRepository(
  private val localStorageVideoIdFetcher: ILocalStorageVideoIdFetcher
) : IVideoRepository{

    override suspend fun fetchVideosIdsUris(): Flow<List<Long>> = withContext(Dispatchers.IO){
        return@withContext flow {
            emit(localStorageVideoIdFetcher.fetchVideosIdsUris())
        }
    }

}