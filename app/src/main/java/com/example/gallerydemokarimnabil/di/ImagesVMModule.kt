package com.example.gallerydemokarimnabil.di

import com.example.gallerydemokarimnabil.core.interfaces.data.IImageUriRepository
import com.example.gallerydemokarimnabil.core.interfaces.data.ILocalStorageImageUriDataSource
import com.example.gallerydemokarimnabil.core.interfaces.mappers.IFromUriCollectionToDrawables
import com.example.gallerydemokarimnabil.core.interfaces.mediastorefetchers.IImageUriFetcher
import com.example.gallerydemokarimnabil.features.images.data.ImageUriRepositoryImpl
import com.example.gallerydemokarimnabil.features.images.data.LocalStorageImageUriDataSource
import com.example.gallerydemokarimnabil.features.images.helpers.MediaStoreImageUriFetcher
import com.example.gallerydemokarimnabil.features.images.helpers.UriToDrawableMapperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ImagesViewModelModule {

    @Binds
    @Singleton
    abstract fun provideImageUriFetch(fetcher: MediaStoreImageUriFetcher) : IImageUriFetcher

    @Binds
    @Singleton
    abstract fun provideImageUriDataSource(
        localStorageImageUriDataSource: LocalStorageImageUriDataSource
    ) : ILocalStorageImageUriDataSource


    @Binds
    @Singleton
    abstract fun provideImageUriRepo(imageUriRepositoryImpl: ImageUriRepositoryImpl) : IImageUriRepository

    @Binds
    @Singleton
    abstract fun provideUriCollecToDrawablesMapper(
        uriToDrawableMapperImpl: UriToDrawableMapperImpl
    ) : IFromUriCollectionToDrawables

}