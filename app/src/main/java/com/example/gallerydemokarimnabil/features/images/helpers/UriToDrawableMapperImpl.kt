package com.example.gallerydemokarimnabil.features.images.helpers

import android.app.Application
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import com.example.gallerydemokarimnabil.core.interfaces.mappers.IFromUriCollectionToDrawables
import com.example.gallerydemokarimnabil.core.interfaces.mappers.IFromUriToDrawable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import java.io.InputStream

class UriToDrawableMapperImpl(
    private val application: Application
    ) : IFromUriToDrawable, IFromUriCollectionToDrawables {

    private var inputStream: InputStream? = null

    override fun fromUriToDrawable(uri: Uri) : Drawable? {
        inputStream = application.contentResolver.openInputStream(uri)
        inputStream?.close()
        return Drawable.createFromStream(inputStream,uri.toString())
    }

    /*
    * This function is main-safe.
    * */
    override suspend fun fromUrisToDrawables(listOfUris: List<Uri>) : List<Drawable?> {
        val drawablesList = mutableListOf<Drawable?>()

        /*
        * TODO ensure that the coroutine isn't doing heavy work
        * if the view model gets destroyed.
        * */
        withContext(Dispatchers.IO){
            for(uri in listOfUris){
                if(!isActive){
                    Log.i("MainActivity", "The coroutine wrapping mapping loop is in active! " +
                            "in ${this.javaClass.simpleName}")
                }
                inputStream = application.contentResolver.openInputStream(uri)
                drawablesList.add(Drawable.createFromStream(inputStream,uri.toString()))
                inputStream?.close()
            }
        }

        return drawablesList.toList()
    }
}