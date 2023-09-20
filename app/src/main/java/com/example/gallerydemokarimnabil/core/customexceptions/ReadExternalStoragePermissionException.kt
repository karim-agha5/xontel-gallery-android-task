package com.example.gallerydemokarimnabil.features.core.customexceptions

class ReadExternalStoragePermissionException : Exception() {
    override val message = "ReadExternalStorage is not initialized"
}