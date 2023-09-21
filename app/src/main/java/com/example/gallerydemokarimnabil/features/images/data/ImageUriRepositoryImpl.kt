package com.example.gallerydemokarimnabil.features.images.data

import android.net.Uri
import com.example.gallerydemokarimnabil.core.interfaces.data.IImageUriRepository
import com.example.gallerydemokarimnabil.core.interfaces.data.ILocalStorageImageUriDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Functions in the repository are main-safe
 * */
class ImageUriRepositoryImpl(
  private val localStorageImageUriDataSource: ILocalStorageImageUriDataSource
) : IImageUriRepository{

  override suspend fun fetchImageUris(): Flow<List<Uri>>{
    return flow{
      emit(localStorageImageUriDataSource.fetchImageUris())
    }
  }

}