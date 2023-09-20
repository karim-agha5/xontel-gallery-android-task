package com.example.gallerydemokarimnabil.core.interfaces

/**
 * Marks the app's first fragment as its start destination
 * so that the host activity can invoke the first behavior of the app
 * once the necessary permission(s) is granted
 * */
interface GalleryStartDestination {
    fun invokeWhenPermissionsGranted()
}