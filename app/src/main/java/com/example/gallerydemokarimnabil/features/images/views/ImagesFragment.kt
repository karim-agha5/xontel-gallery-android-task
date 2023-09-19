package com.example.gallerydemokarimnabil.features.images.views

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.gallerydemokarimnabil.R
import com.example.gallerydemokarimnabil.databinding.FragmentImagesBinding
import com.example.gallerydemokarimnabil.features.images.helpers.MediaStoreImageUriFetcher
import com.example.gallerydemokarimnabil.features.images.helpers.UriToDrawableMapperImpl
import com.example.gallerydemokarimnabil.features.images.viewmodel.ImagesViewModel
import com.example.gallerydemokarimnabil.features.images.viewmodel.ImagesViewModelFactory
import kotlinx.coroutines.launch

class ImagesFragment : Fragment() {

    private lateinit var binding: FragmentImagesBinding
    private val imagesViewModel: ImagesViewModel by viewModels {
        val application = requireActivity().application
        ImagesViewModelFactory(
            MediaStoreImageUriFetcher(application),
            UriToDrawableMapperImpl(application)
        )
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
        lifecycleScope.launch {
            imagesViewModel.imagesState.collect{ setUiState(it) }
        }
    }

    private fun setUiState(drawablesList: List<Drawable?>){
        when{
            drawablesList.isNotEmpty() -> {
                // TODO constantly attaching new adapters and layout managers on config changes - fix later.
                binding.adapter = ImageAdapter(drawablesList.toList())
                binding.layoutManager = GridLayoutManager(requireContext(),3)
                Log.i("MainActivity", "size -> ${drawablesList.size}")
            }
            else -> {
                imagesViewModel.loadImages()
            }
        }
    }
}