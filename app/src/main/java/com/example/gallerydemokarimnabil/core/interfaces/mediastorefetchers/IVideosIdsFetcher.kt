package com.example.gallerydemokarimnabil.core.interfaces.mediastorefetchers

interface IVideosIdsFetcher {
    suspend fun fetchVideosIdsUris() : List<Long>
}