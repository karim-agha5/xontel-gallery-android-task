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
import com.example.gallerydemokarimnabil.features.images.helpers.MediaStoreImageUriFetcher
import com.example.gallerydemokarimnabil.features.images.helpers.UriToDrawableMapperImpl
import com.example.gallerydemokarimnabil.features.images.viewmodel.ImagesViewModel
import com.example.gallerydemokarimnabil.features.images.viewmodel.ImagesViewModelFactory
import com.example.gallerydemokarimnabil.features.main.MediaPermissionsHandler
import kotlinx.coroutines.launch

class ImagesFragment : Fragment(),GalleryStartDestination {

    private lateinit var binding: FragmentImagesBinding
    private lateinit var permissionsHandler: MediaPermissionsHandler
    private val imagesViewModel: ImagesViewModel by viewModels {
        val application = requireActivity().application
        ImagesViewModelFactory(
            MediaStoreImageUriFetcher(application),
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
            // If config. changes
            if(imagesViewModel.imagesState.value.isNotEmpty()){
                binding.imagesCircularLoadingIndicator.visibility = View.GONE
                initRecyclerView(imagesViewModel.imagesState.value)
            }
            else{
                lifecycleScope.launch {
                    imagesViewModel.imagesState.collect{ setUiState(it) }
                }
            }
        }

    }

    private fun setUiState(drawablesList: List<Drawable?>){
        when{
            drawablesList.isNotEmpty() -> {
                // TODO constantly attaching new adapters and layout managers on config changes - fix later.
                initRecyclerView(drawablesList)
                binding.imagesCircularLoadingIndicator.visibility = View.GONE
            }
            else -> {
                imagesViewModel.loadImages()
            }
        }
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
                initRecyclerView(it)
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
}