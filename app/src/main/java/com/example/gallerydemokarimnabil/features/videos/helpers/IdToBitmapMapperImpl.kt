package com.example.gallerydemokarimnabil.features.videos.helpers

import android.app.Application
import android.graphics.Bitmap
import android.provider.MediaStore
import com.example.gallerydemokarimnabil.core.interfaces.mappers.IFromIdCollectionToBitmaps
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class IdToBitmapMapperImpl @Inject constructor(
    private val application: Application
    ) : IFromIdCollectionToBitmaps {

    override suspend fun fromIdsToBitmaps(listOfIds: List<Long>): List<Bitmap?> {
        val bitmapsList = mutableListOf<Bitmap>()

        withContext(Dispatchers.IO){
            for(id in listOfIds){
                val bitmap = MediaStore.Video.Thumbnails.getThumbnail(
                    application.contentResolver,
                    id,
                    MediaStore.Video.Thumbnails.MINI_KIND,
                    null
                )
                bitmapsList.add(bitmap)
            }
        }

        return bitmapsList
    }

}