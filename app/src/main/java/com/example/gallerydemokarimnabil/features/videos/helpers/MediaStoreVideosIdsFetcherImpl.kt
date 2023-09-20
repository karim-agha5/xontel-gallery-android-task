package com.example.gallerydemokarimnabil.features.videos.helpers

import android.app.Application
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore
import com.example.gallerydemokarimnabil.core.interfaces.mediastorefetchers.IVideosIdsFetcher
import java.io.File
import java.net.URLConnection

class MediaStoreVideosIdsFetcherImpl(private val application: Application) : IVideosIdsFetcher {

    val thumbnailsList = mutableListOf<Bitmap?>()
    val retriever = MediaMetadataRetriever()


    override fun fetchVideosIdsUris(): List<Long> {
        val urisList = mutableListOf<Long>()

        /*
        * The list of columns to request.
        * Request only the ID column.
        * */
        val projection = arrayOf(MediaStore.Video.Media._ID)


        /*
        * Sort the records in the ID column in descending order to display the videos from the most recent
        * video taken to the latest.
        * */
        val sortOrder = "${MediaStore.Video.Media.DATE_ADDED} DESC"


        /*
        * returns and uses a cursor over the result set retrieved.
        * */
        application.contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            sortOrder
        )?.use {

            // Get the column index of the video's ID.
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media._ID)

            while(it.moveToNext()){

                /*
               * Get the image's ID, append it
               * to the base URI from MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
               * and then add it to the list of URIs.
               * */
                val id = it.getLong(idColumn)
                urisList.add(id)
            }
        }

        return urisList.toList()
    }

    private fun addThumbnailsFromDownloadsDir(fileList: Array<File>){
        for(file in fileList){
            val mimeType = URLConnection.guessContentTypeFromName(file.path)
            val contentUri = Uri.fromFile(file)
            if(mimeType != null && mimeType.startsWith("video")){
                retriever.setDataSource(application.applicationContext,contentUri)
                val bitmap = retriever.frameAtTime
                thumbnailsList.add(bitmap)
            }

        }
    }

    private fun addThumbnailsFromDcimDir(fileList: Array<File>){
        for(file in fileList){
            val mimeType = URLConnection.guessContentTypeFromName(file.path)
            val contentUri = Uri.fromFile(file)
            if(mimeType != null && mimeType.startsWith("video")){
                retriever.setDataSource(application.applicationContext,contentUri)
                val bitmap = retriever.frameAtTime
                thumbnailsList.add(bitmap)
            }

        }
    }

    private fun addThumbnailsFromMoviesDir(fileList: Array<File>){
        for(file in fileList){
            val mimeType = URLConnection.guessContentTypeFromName(file.path)
            val contentUri = Uri.fromFile(file)
            if(mimeType != null && mimeType.startsWith("video")){
                retriever.setDataSource(application.applicationContext,contentUri)
                val bitmap = retriever.frameAtTime
                thumbnailsList.add(bitmap)
            }

        }
    }

    private fun addThumbnailsFromPicturesDir(fileList: Array<File>){
        for(file in fileList){
            val mimeType = URLConnection.guessContentTypeFromName(file.path)
            val contentUri = Uri.fromFile(file)
            if(mimeType != null && mimeType.startsWith("video")){
                retriever.setDataSource(application.applicationContext,contentUri)
                val bitmap = retriever.frameAtTime
                thumbnailsList.add(bitmap)
            }

        }
    }

}