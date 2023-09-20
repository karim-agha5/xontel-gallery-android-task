package com.example.gallerydemokarimnabil.core.interfaces.mappers

import android.graphics.Bitmap

interface IFromIdCollectionToBitmaps {
    fun fromIdsToBitmaps(listOfIds: List<Long>) : List<Bitmap?>
}