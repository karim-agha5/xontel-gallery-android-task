package com.example.gallerydemokarimnabil.di

import com.example.gallerydemokarimnabil.core.interfaces.data.ILocalStorageVideoIdFetcher
import com.example.gallerydemokarimnabil.core.interfaces.data.IVideoRepository
import com.example.gallerydemokarimnabil.core.interfaces.mappers.IFromIdCollectionToBitmaps
import com.example.gallerydemokarimnabil.core.interfaces.mediastorefetchers.IVideosIdsFetcher
import com.example.gallerydemokarimnabil.features.videos.data.LocalStorageVideoIdDataSource
import com.example.gallerydemokarimnabil.features.videos.data.VideoRepository
import com.example.gallerydemokarimnabil.features.videos.helpers.IdToBitmapMapperImpl
import com.example.gallerydemokarimnabil.features.videos.helpers.MediaStoreVideosIdsFetcherImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class VideosModule{

    @Binds
    @Singleton
    abstract fun provideVideoIdFetcher(fetcher: MediaStoreVideosIdsFetcherImpl) : IVideosIdsFetcher

    @Binds
    @Singleton
    abstract fun provideVideoIdDataSource(
        videoIdDataSource: LocalStorageVideoIdDataSource
    ) : ILocalStorageVideoIdFetcher


    @Binds
    @Singleton
    abstract fun provideVideoRepository(
        videoRepository: VideoRepository
    ) : IVideoRepository

    @Binds
    @Singleton
    abstract fun provideIdToBitmapMapper(
        idToBitmapMapperImpl: IdToBitmapMapperImpl
    ) : IFromIdCollectionToBitmaps

}