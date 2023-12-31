package com.example.gallerydemokarimnabil.features.images.data

import android.net.Uri
import com.example.gallerydemokarimnabil.core.interfaces.data.IImageUriRepository
import com.example.gallerydemokarimnabil.core.interfaces.data.ILocalStorageImageUriDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Functions in the repository are main-safe
 * */
class ImageUriRepositoryImpl @Inject constructor(
  private val localStorageImageUriDataSource: ILocalStorageImageUriDataSource
) : IImageUriRepository{

  override suspend fun fetchImageUris(): Flow<List<Uri>>{
    return flow{
      emit(localStorageImageUriDataSource.fetchImageUris())
    }
  }

}