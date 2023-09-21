package com.example.gallerydemokarimnabil.core.interfaces.mappers

import android.graphics.Bitmap

interface IFromIdCollectionToBitmaps {
    suspend fun fromIdsToBitmaps(listOfIds: List<Long>) : List<Bitmap?>
}