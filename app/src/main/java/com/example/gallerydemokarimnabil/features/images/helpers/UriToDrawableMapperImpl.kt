package com.example.gallerydemokarimnabil.features.images.helpers

import android.app.Application
import android.graphics.drawable.Drawable
import android.net.Uri
import com.example.gallerydemokarimnabil.features.core.interfaces.mappers.IFromUriCollectionToDrawables
import com.example.gallerydemokarimnabil.features.core.interfaces.mappers.IFromUriToDrawable
import java.io.InputStream

class UriToDrawableMapperImpl(
    private val application: Application
    ) : IFromUriToDrawable, IFromUriCollectionToDrawables{

    private var inputStream: InputStream? = null

    override fun fromUriToDrawable(uri: Uri) : Drawable? {
        inputStream = application.contentResolver.openInputStream(uri)
        inputStream?.close()
        return Drawable.createFromStream(inputStream,uri.toString())
    }

    override fun fromUrisToDrawables(listOfUris: List<Uri>) : List<Drawable?> {
        val drawablesList = mutableListOf<Drawable?>()

        for(uri in listOfUris){
            inputStream = application.contentResolver.openInputStream(uri)
            drawablesList.add(Drawable.createFromStream(inputStream,uri.toString()))
        }
        inputStream?.close()

        return drawablesList.toList()
    }
}