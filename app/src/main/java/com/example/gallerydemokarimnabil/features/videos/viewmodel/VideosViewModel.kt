package com.example.gallerydemokarimnabil.features.videos.viewmodel

import android.app.Application
import android.content.ContentUris
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.MediaMetadataRetriever
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.util.Size
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.InputStream
import java.net.URLConnection

class VideosViewModel(
    private val application: Application
    ) : AndroidViewModel(application) {

    private val _videosThumbnailsState = MutableStateFlow<List<Bitmap?>>(listOf())
    val videosThumbnailState = _videosThumbnailsState.asStateFlow()

    val thumbnailsList = mutableListOf<Bitmap?>()
    val retriever = MediaMetadataRetriever()

    fun loadVideosThumbnails(){
        viewModelScope.launch {
            loadVideosThumbnailFromStorage()
        }
    }

    private suspend fun loadVideosThumbnailFromStorage(){

        var inputStream: InputStream? = null

        withContext(Dispatchers.IO){


             val projection = arrayOf(MediaStore.Video.VideoColumns.DATA)
             //val sortOrder = "${MediaStore.Video.Media.DATE_ADDED} DESC"
             val query = application.contentResolver.query(
                 MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                 projection,
                 null,
                 null,
                 null
             )

             query?.use {
                 val idColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
                 Log.i("MainActivity", "can move ? ${it.moveToNext()}")
                 /*while(it.moveToNext()){

                }*/
            }




            /*val projection = arrayOf(MediaStore.Downloads._ID)
            val selection = "${MediaStore.Video.Media.DATA} like ?"
            val selectionArgs = arrayOf("")
            val sortOrder = "${MediaStore.Downloads._ID} DESC"
            application.contentResolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                sortOrder
            )?.use {
                val columnId = it.getColumnIndex(MediaStore.Video.Media.DATA)
                Log.i("MainActivity", "can move next ? ${it.moveToNext()}")
                while (it.moveToNext()){
                    val id = it.getLong(columnId)
                    val contentUri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,id)
                    inputStream = application.contentResolver.openInputStream(contentUri)
                    val bitmap = MediaStore.Video.Thumbnails.getThumbnail(
                        application.contentResolver,
                        id,
                        MediaStore.Video.Thumbnails.MINI_KIND,
                        null
                    )
                    thumbnailsList.add(bitmap)
                }

                inputStream?.close()*/



            val downloadFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val dcimDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
            val moviesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
            val picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)


            val downloadFileList = downloadFile.listFiles()
            val dcimFileList = dcimDir.listFiles()
            val moviesFileList = moviesDir.listFiles()
            val picturesFileList = picturesDir.listFiles()

            // Gets videos in the download directory
            if (downloadFileList != null) {
                addThumbnailsFromDownloadsDir(downloadFileList)
            }

            // Get videos in the dcim directory
            if (dcimFileList != null) {
                addThumbnailsFromDcimDir(dcimFileList)
            }

            // Get videos in the movies directory
            if (moviesFileList != null) {
                addThumbnailsFromMoviesDir(moviesFileList)
            }

            // Get videos in the pictures directory
            if (picturesFileList != null) {
                addThumbnailsFromPicturesDir(picturesFileList)
            }




        }

        _videosThumbnailsState.value = thumbnailsList.toList()
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