package com.example.gallerydemokarimnabil.core.interfaces.data

interface ILocalStorageVideoIdFetcher {
    suspend fun fetchVideosIdsUris(): List<Long>
}