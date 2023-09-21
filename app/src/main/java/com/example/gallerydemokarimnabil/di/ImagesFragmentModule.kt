package com.example.gallerydemokarimnabil.di

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.gallerydemokarimnabil.core.GalleryDemoApp
import com.example.gallerydemokarimnabil.core.helpers.MediaPermissionsHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(FragmentComponent::class)
object ImagesFragmentModule {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @Provides
    fun provideMediaPermissionHandler(
        @ApplicationContext context: Context
    ) : MediaPermissionsHandler {
        val application = context.applicationContext as GalleryDemoApp
        when{
            Build.VERSION.SDK_INT < Build.VERSION_CODES.P -> {
                return MediaPermissionsHandler
                        .Builder(application)
                        .readExternalStorage()
                        .build()
            }

            else -> {
                    return MediaPermissionsHandler
                        .Builder(application)
                        .readMediaImages()
                        .readMediaVideos()
                        .build()
            }
        }
    }
}