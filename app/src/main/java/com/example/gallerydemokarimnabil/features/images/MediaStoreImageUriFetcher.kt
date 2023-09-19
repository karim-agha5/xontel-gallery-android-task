package com.example.gallerydemokarimnabil.features.images

import android.app.Application
import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import com.example.gallerydemokarimnabil.features.core.interfaces.mediastorefetchers.ImageUriFetcher

class MediaStoreImageUriFetcher(private val application: Application) : ImageUriFetcher {

    override fun fetchImageUris(): List<Uri> {
        val urisList = mutableListOf<Uri>()

        /*
        * The list of columns to request.
        * Request only the ID column.
        * */
        val projections = arrayOf(MediaStore.Images.Media._ID)

        /*
        * Sort the records in the ID column in descending order to display the images from the most recent
        * image taken to the latest.
        * */
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

        /*
        * returns and uses a cursor over the result set retrieved.
        * */
        application.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projections,
            null,
            null,
            sortOrder
        )?.use {

            // Get the column index of the image's ID.
            val columnId = it.getColumnIndex(MediaStore.Images.Media._ID)

            while (it.moveToNext()){

                /*
                * Get the image's ID, append it
                * to the base URI from MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                * and then add it to the list of URIs.
                * */
                val id = it.getLong(columnId)
                val contentUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,id)
                urisList.add(contentUri)
            }
        }

        return urisList.toList()
    }

}