package com.example.gallerydemokarimnabil.features.main

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MediaPermissionsHandler private constructor(private val builder: Builder) {

    private var _context: Context? = null
    private var context: Context

    private var requestPermissionLauncher: ActivityResultLauncher<Array<String>>

    private val readExternalStorage: String?
    private val writeExternalStorage: String?

    private var permissionsList = mutableListOf<String>()

    companion object{
        val READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE
        val WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE
    }

    init {
        _context = builder.application.applicationContext
        context = _context!!
        readExternalStorage = builder.readExternalStorage
        writeExternalStorage = builder.writeExternalStorage
        requestPermissionLauncher = builder.requestPermissionLauncher
        permissionsList = builder.permissionsList
    }


    fun requestPermissions(){
        requestPermissionLauncher.launch(permissionsList.toTypedArray())
    }

    fun shouldShowRationaleForReadExternalStorage(activity: Activity) : Boolean{
        if(readExternalStorage != null){
            return ActivityCompat.shouldShowRequestPermissionRationale(activity,readExternalStorage)
        }
        return false // TODO throw custom ReadExternalStoragePermissionException
    }

    fun isReadExternalStoragePermissionGranted() : Boolean{
        if(readExternalStorage != null){
            return ContextCompat.checkSelfPermission(
                context,
                readExternalStorage
            ) == PackageManager.PERMISSION_GRANTED
        }
        return false // TODO throw custom ReadExternalStoragePermissionException
    }

    fun isWriteExternalStoragePermissionGranted() : Boolean{
        if(writeExternalStorage != null){
            return ContextCompat.checkSelfPermission(
                context,
                writeExternalStorage
            ) == PackageManager.PERMISSION_GRANTED
        }
        return false // TODO throw custom WriteExternalStoragePermissionException
    }

    // Get the application instance instead of context to make sure you receive an application's context
    // to avoid possible memory leaks.
    class Builder(val application: Application){

        var readExternalStorage: String? = null
            private set

        var writeExternalStorage: String? = null
            private set

        lateinit var requestPermissionLauncher: ActivityResultLauncher<Array<String>>
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
         * Initializes [MediaPermissionsHandler.requestPermissionLauncher]
         * An ActivityResultLauncher<Array<String>>should be passed because the client itself should determine
         * how granted permissions should be handled.
         * @return [Builder]
         * */
        fun activityResultLauncher(requestPermissionLauncher: ActivityResultLauncher<Array<String>>) = apply {
            this.requestPermissionLauncher = requestPermissionLauncher
        }

        /**
         * @return Instance of [MediaPermissionsHandler]
         * */
        fun build() = MediaPermissionsHandler(this)

    }
}