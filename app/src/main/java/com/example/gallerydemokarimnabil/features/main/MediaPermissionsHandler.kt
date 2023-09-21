package com.example.gallerydemokarimnabil.features.main

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.gallerydemokarimnabil.core.customexceptions.ActivityResultLauncherMissingException
import com.example.gallerydemokarimnabil.core.customexceptions.ReadMediaImagesPermissionMissingException
import com.example.gallerydemokarimnabil.core.customexceptions.ReadMediaVideosPermissionMissingException
import com.example.gallerydemokarimnabil.features.core.customexceptions.ReadExternalStoragePermissionException
import com.example.gallerydemokarimnabil.features.core.customexceptions.WriteExternalStoragePermissionException

class MediaPermissionsHandler private constructor(builder: Builder) {

    private var _context: Context? = null
    private var context: Context

    private var requestPermissionLauncher: ActivityResultLauncher<Array<String>>?

    private val readExternalStorage: String?
    private val writeExternalStorage: String?
    private val readMediaImages: String?
    private val readMediaVideos: String?
    private val onPermissionsGranted: (() -> Unit)?
    private val onPermissionsGrantedArray: Array<(() -> Unit)>?

    private var permissionsList = mutableListOf<String>()

    companion object{
        val READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE
        val WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        val READ_MEDIA_IMAGES = Manifest.permission.READ_MEDIA_IMAGES
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        val READ_MEDIA_VIDEOS = Manifest.permission.READ_MEDIA_VIDEO
    }

    init {
        _context                    = builder.application.applicationContext
        context                     = _context!!
        readExternalStorage         = builder.readExternalStorage
        writeExternalStorage        = builder.writeExternalStorage
        readMediaImages             = builder.readMediaImages
        readMediaVideos             = builder.readMediaVideos
        onPermissionsGranted        = builder.onPermissionsGranted
        onPermissionsGrantedArray   = builder.onPermissionsGrantedArray
        requestPermissionLauncher   = builder.requestPermissionLauncher
        permissionsList             = builder.permissionsList
    }


    fun requestPermissions(){
        if(requestPermissionLauncher == null) {
            throw ActivityResultLauncherMissingException()
        }
        requestPermissionLauncher?.launch(permissionsList.toTypedArray())
    }

    fun shouldShowRationaleForReadExternalStorage(activity: Activity) : Boolean{
        if(readExternalStorage != null){
            return ActivityCompat.shouldShowRequestPermissionRationale(activity,readExternalStorage)
        }
        throw ReadExternalStoragePermissionException("The ActivityResultLauncher<Array<String>> representing" +
                " a strings array of permissions is missing")
    }

    fun invokeOnPermissionsGrantedIfProvided() = onPermissionsGranted?.invoke()

    fun invokeMultipleOnPermissionsGrantedIfProvided(){
        if(onPermissionsGrantedArray != null) {
            for(func in onPermissionsGrantedArray){
                func.invoke()
            }
        }
    }

    fun isReadExternalStoragePermissionGranted() : Boolean{
        if(readExternalStorage != null){
            return ContextCompat.checkSelfPermission(
                context,
                readExternalStorage
            ) == PackageManager.PERMISSION_GRANTED
        }
        throw ReadExternalStoragePermissionException("ReadExternalStorage is not initialized")
    }

    fun isWriteExternalStoragePermissionGranted() : Boolean{
        if(writeExternalStorage != null){
            return ContextCompat.checkSelfPermission(
                context,
                writeExternalStorage
            ) == PackageManager.PERMISSION_GRANTED
        }
        throw WriteExternalStoragePermissionException("WriteExternalStorage is not initialized")
    }

    fun isReadMediaImagesPermissionGranted() : Boolean{
        if(readMediaImages != null){
            return ContextCompat.checkSelfPermission(
                context,
                readMediaImages
            ) == PackageManager.PERMISSION_GRANTED
        }
        // TODO throw custom exception -- this one is a placeholder
        throw ReadMediaImagesPermissionMissingException("ReadMediaImages permission is not initialized")
    }

    fun isReadMediaVideosPermissionGranted() : Boolean{
        if(readMediaVideos != null){
            return ContextCompat.checkSelfPermission(
                context,
                readMediaVideos
            ) == PackageManager.PERMISSION_GRANTED
        }
        // TODO throw custom exception -- this one is a placeholder
        throw ReadMediaVideosPermissionMissingException("ReadMediaVideos permission is not initialized")
    }

    private fun isPermissionGranted(permission: String) : Boolean{
        return ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun arePermissionsGranted() : Boolean{
        var areGranted = true

        for(permission in permissionsList){
            if(!isPermissionGranted(permission)){
                areGranted = false
                break
            }
        }

        return areGranted
    }

    // Get the application instance instead of context to make sure you receive an application's context
    // to avoid possible memory leaks.
    class Builder(val application: Application){

        var readExternalStorage: String? = null
            private set

        var writeExternalStorage: String? = null
            private set

        var readMediaImages: String? = null
            private set

        var readMediaVideos: String? = null
            private set

        var requestPermissionLauncher: ActivityResultLauncher<Array<String>>? = null
            private set

        var onPermissionsGranted: (() -> Unit)? = null
            private set

        var onPermissionsGrantedArray: Array<(() -> Unit)>? = null
            private set

        var permissionsList = mutableListOf<String>()
            private set

        /**
         * Initializes [MediaPermissionsHandler.readExternalStorage]
         * @return [Builder]
         * */
        fun readExternalStorage() = apply {
            readExternalStorage = MediaPermissionsHandler.READ_EXTERNAL_STORAGE
            permissionsList.add(readExternalStorage!!)
        }

        /**
         * Initializes [MediaPermissionsHandler.writeExternalStorage]
         * @return [Builder]
         * */
        fun writeExternalStorage() = apply {
            writeExternalStorage = MediaPermissionsHandler.WRITE_EXTERNAL_STORAGE
            permissionsList.add(writeExternalStorage!!)
        }

        /**
         * Initializes [MediaPermissionsHandler.READ_MEDIA_IMAGES]
         * @return [Builder]
         * */
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        fun readMediaImages() = apply {
            readMediaImages = MediaPermissionsHandler.READ_MEDIA_IMAGES
            permissionsList.add(readMediaImages!!)
        }

        /**
         * Initializes [MediaPermissionsHandler.READ_MEDIA_VIDEOS]
         * @return [Builder]
         * */
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        fun readMediaVideos() = apply {
            readMediaVideos = MediaPermissionsHandler.READ_MEDIA_VIDEOS
            permissionsList.add(readMediaVideos!!)
        }

        /**
         * Initializes [MediaPermissionsHandler.requestPermissionLauncher]
         * An ActivityResultLauncher<Array<String>>should be passed because the client itself should determine
         * how granted permissions should be handled.
         * @return [Builder]
         * */
        fun activityResultLauncher(requestPermissionLauncher: ActivityResultLauncher<Array<String>>) = apply {
            this.requestPermissionLauncher = requestPermissionLauncher
        }

        /**
         * Initializes [MediaPermissionsHandler.onPermissionsGranted]
         * Executes a function once a permission(s) is granted.
         * @return [Builder]
         * */
        fun onPermissionsGranted(func: (() -> Unit)?) = apply{
            this.onPermissionsGranted = func
        }

        fun onPermissionsGranted(funcs: Array<(() -> Unit)>?) = apply{
            this.onPermissionsGrantedArray = funcs
        }

        /**
         * @return Instance of [MediaPermissionsHandler]
         * */
        fun build() = MediaPermissionsHandler(this)

    }
}