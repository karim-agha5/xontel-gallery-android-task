package com.example.gallerydemokarimnabil.features.images.views

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.gallerydemokarimnabil.R
import com.example.gallerydemokarimnabil.core.interfaces.GalleryStartDestination
import com.example.gallerydemokarimnabil.databinding.FragmentImagesBinding
import com.example.gallerydemokarimnabil.features.images.ImagesFetchStatus
import com.example.gallerydemokarimnabil.features.images.data.ImageUriRepositoryImpl
import com.example.gallerydemokarimnabil.features.images.data.LocalStorageImageUriDataSource
import com.example.gallerydemokarimnabil.features.images.helpers.MediaStoreImageUriFetcher
import com.example.gallerydemokarimnabil.features.images.helpers.UriToDrawableMapperImpl
import com.example.gallerydemokarimnabil.features.images.viewmodel.ImagesViewModel
import com.example.gallerydemokarimnabil.features.images.viewmodel.ImagesViewModelFactory
import com.example.gallerydemokarimnabil.core.helpers.MediaPermissionsHandler
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class ImagesFragment : Fragment(),GalleryStartDestination {

    private lateinit var binding: FragmentImagesBinding
    private lateinit var permissionsHandler: MediaPermissionsHandler
    private val imagesViewModel: ImagesViewModel by viewModels {
        val application = requireActivity().application
        val fetcher = MediaStoreImageUriFetcher(application)
        ImagesViewModelFactory(
            ImageUriRepositoryImpl(LocalStorageImageUriDataSource(fetcher)),
            UriToDrawableMapperImpl(application)
        )
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initPermissionHandlerAccordingly()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_images,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if(arePermissionsGranted()){
            lifecycleScope.launch {
                // TODO loads videos when config changes -- fix later
                imagesViewModel.loadImages()
                imagesViewModel.imagesState.collect{
                    setUiState(it)
                }
            }
        }

    }

    private fun setUiState(status: ImagesFetchStatus){
        when(status){
            is ImagesFetchStatus.Success -> {
                if(status.list.isEmpty()){
                    onListIsEmpty()
                }
                else{
                    initRecyclerView(status.list)
                    binding.imagesCircularLoadingIndicator.visibility = View.GONE
                }
            }
            is ImagesFetchStatus.Failure -> {
                Log.i("MainActivity", "Unable to retrieve images in " +
                        this@ImagesFragment.javaClass.simpleName
                )
                status.throwable.printStackTrace()
                displayFailureDialog()
            }
            else -> {/*Loading case*/}
        }
    }

    private fun onListIsEmpty(){
        binding.tvNoImagesToShow.visibility = View.VISIBLE
        binding.imagesCircularLoadingIndicator.visibility = View.GONE
        binding.rvImages.visibility = View.GONE
    }

    private fun initRecyclerView(drawablesList: List<Drawable?>){
        binding.adapter = ImageAdapter(drawablesList.toList())
        binding.layoutManager = GridLayoutManager(requireContext(),3)
    }

    /*
    * Invoked by the host activity when permissions are granted. Since the host activity is responsible for
    * handling the permissions being given or not, it's also responsible for handling the permissions
    * result callback.
    * */
    override fun invokeWhenPermissionsGranted() {
        lifecycleScope.launch {
            imagesViewModel.imagesState.collect{
                setUiState(it)
            }
        }
        imagesViewModel.loadImages()
    }

    override fun arePermissionsGranted() : Boolean{
        return permissionsHandler.arePermissionsGranted()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun initPermissionHandlerAccordingly(){

        when{
            Build.VERSION.SDK_INT < Build.VERSION_CODES.P -> {
                permissionsHandler =
                    MediaPermissionsHandler
                        .Builder(requireActivity().application)
                        .readExternalStorage()
                        .build()
            }

            else -> {
                permissionsHandler =
                    MediaPermissionsHandler
                        .Builder(requireActivity().application)
                        .readMediaImages()
                        .readMediaVideos()
                        .build()
            }
        }

    }

    private fun displayFailureDialog(){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.dialog_error_title))
            .setMessage(resources.getString(R.string.images_error_message))
            .setNeutralButton(resources.getString(R.string.error_neutral_button_text)){_,_->
                /*Do Nothing*/
            }
    }
}