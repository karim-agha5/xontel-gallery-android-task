package com.example.gallerydemokarimnabil.features.core.customexceptions

class WriteExternalStoragePermissionException : Exception() {
    override val message = "WriteExternalStorage is not initialized"
}