package com.example.gallerydemokarimnabil.features.videos.views

import android.graphics.Bitmap
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
import com.example.gallerydemokarimnabil.databinding.FragmentVideosBinding
import com.example.gallerydemokarimnabil.features.images.views.ImageAdapter
import com.example.gallerydemokarimnabil.features.videos.helpers.IdToBitmapMapperImpl
import com.example.gallerydemokarimnabil.features.videos.helpers.MediaStoreVideosIdsFetcherImpl
import com.example.gallerydemokarimnabil.features.videos.viewmodel.VideosViewModelFactory
import com.example.gallerydemokarimnabil.features.videos.viewmodel.VideosViewModel
import kotlinx.coroutines.launch

class VideosFragment : Fragment() {

    private lateinit var binding: FragmentVideosBinding
    private val videosViewModel: VideosViewModel by viewModels{
        val application = requireActivity().application
        VideosViewModelFactory(
            application,
            MediaStoreVideosIdsFetcherImpl(application),
            IdToBitmapMapperImpl(application)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_videos,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // If config. changes
        if(videosViewModel.videosThumbnailState.value.isNotEmpty()){
            initRecyclerView(videosViewModel.videosThumbnailState.value)
        }
        else{
            lifecycleScope.launch {
                videosViewModel.videosThumbnailState.collect{ setUiState(it) }
            }
        }
    }

    private fun setUiState(bitmapsList: List<Bitmap?>){
        when{
            bitmapsList.isNotEmpty() -> {
                // TODO constantly attaching new adapters and layout managers on config changes - fix later.
                initRecyclerView(bitmapsList)
            }
            else -> {
                videosViewModel.loadVideosThumbnails()
            }
        }
    }

    private fun initRecyclerView(bitmapsList: List<Bitmap?>){
        binding.adapter = VideoAdapter(bitmapsList.toList())
        binding.layoutManager = GridLayoutManager(requireContext(),3)
    }
}