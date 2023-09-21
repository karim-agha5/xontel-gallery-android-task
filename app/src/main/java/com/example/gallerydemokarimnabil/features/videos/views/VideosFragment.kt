package com.example.gallerydemokarimnabil.features.videos.views

import android.graphics.Bitmap
import android.os.Bundle
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
import com.example.gallerydemokarimnabil.features.videos.VideosThumbnailsFetchStatus
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

        lifecycleScope.launch {
            // TODO loads videos when config changes -- fix later
            videosViewModel.loadVideosThumbnails()
            videosViewModel.videosThumbnailState.collect{
                setUiState(it)
            }
        }

        // If config. changes
        /*if(videosViewModel.videosThumbnailState.value.isNotEmpty()){
            initRecyclerView(videosViewModel.videosThumbnailState.value)
            binding.videoCircularLoadingIndicator.visibility = View.GONE
        }
        else{
            lifecycleScope.launch {
                videosViewModel.videosThumbnailState.collect{ setUiState(it) }
            }
        }*/
    }

    /*private fun setUiState(bitmapsList: List<Bitmap?>){
        when{
            bitmapsList.isNotEmpty() -> {
                // TODO constantly attaching new adapters and layout managers on config changes - fix later.
                binding.videoCircularLoadingIndicator.visibility = View.GONE
                initRecyclerView(bitmapsList)
            }
            else -> {
                videosViewModel.loadVideosThumbnails()
            }
        }
    }*/

    private fun setUiState(status: VideosThumbnailsFetchStatus){
        when(status){
            is VideosThumbnailsFetchStatus.Success -> {
                if(status.list.isEmpty()){
                    onListIsEmpty()
                }
                else{
                    initRecyclerView(status.list)
                    binding.videoCircularLoadingIndicator.visibility = View.GONE
                }
            }
            is VideosThumbnailsFetchStatus.Failure -> {

            }
            else -> {}
        }
    }

    private fun onListIsEmpty(){
        binding.tvNoVideosToShow.visibility = View.VISIBLE
        binding.videoCircularLoadingIndicator.visibility = View.GONE
        binding.rvVideos.visibility = View.GONE
    }

    private fun initRecyclerView(bitmapsList: List<Bitmap?>){
        binding.adapter = VideoAdapter(bitmapsList.toList())
        binding.layoutManager = GridLayoutManager(requireContext(),3)
    }
}