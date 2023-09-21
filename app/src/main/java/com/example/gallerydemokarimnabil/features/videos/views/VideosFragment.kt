package com.example.gallerydemokarimnabil.features.videos.views

import android.graphics.Bitmap
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
import com.example.gallerydemokarimnabil.features.videos.VideosThumbnailsFetchStatus
import com.example.gallerydemokarimnabil.features.videos.viewmodel.VideosViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VideosFragment : Fragment() {

    private lateinit var binding: FragmentVideosBinding
    private val videosViewModel: VideosViewModel by viewModels()

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
    }

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
                Log.i("MainActivity", "Unable to retrieve videos in " +
                        this@VideosFragment.javaClass.simpleName
                )
                status.throwable.printStackTrace()
                displayFailureDialog()
            }
            else -> {/*Loading case*/
            /*videosViewModel.loadVideosThumbnails()*/}
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

    private fun displayFailureDialog(){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.dialog_error_title))
            .setMessage(resources.getString(R.string.videos_error_message))
            .setNeutralButton(resources.getString(R.string.error_neutral_button_text)){_,_->
                /*Do Nothing*/
            }
    }
}