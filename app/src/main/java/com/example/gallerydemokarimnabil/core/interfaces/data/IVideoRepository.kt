package com.example.gallerydemokarimnabil.core.interfaces.data

import kotlinx.coroutines.flow.Flow

interface IVideoRepository {
    suspend fun fetchVideosIdsUris(): Flow<List<Long>>
}