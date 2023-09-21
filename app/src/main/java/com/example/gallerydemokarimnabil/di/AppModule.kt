package com.example.gallerydemokarimnabil.di

import android.content.Context
import com.example.gallerydemokarimnabil.core.GalleryDemoApp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApp(@ApplicationContext context: Context): GalleryDemoApp = context as GalleryDemoApp
}