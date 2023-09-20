package com.example.gallerydemokarimnabil.features.videos.views

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
import com.example.gallerydemokarimnabil.features.videos.viewmodel.VideosViewModelFactory
import com.example.gallerydemokarimnabil.features.videos.viewmodel.VideosViewModel
import kotlinx.coroutines.launch

class VideosFragment : Fragment() {

    private lateinit var binding: FragmentVideosBinding
    private val videosViewModel: VideosViewModel by viewModels{
        val application = requireActivity().application
        VideosViewModelFactory(application)
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
        lifecycleScope.launch {
            videosViewModel.videosThumbnailState.collect{
                when{
                    it.isNotEmpty() -> {
                        binding.rvVideos.adapter = VideoAdapter(it)
                        binding.rvVideos.layoutManager = GridLayoutManager(requireContext(),3)
                        Log.i("MainActivity", "thumbnail's size -> ${it.size} ")
                    }
                    else -> {
                        videosViewModel.loadVideosThumbnails()
                    }
                }
            }
        }
    }
}