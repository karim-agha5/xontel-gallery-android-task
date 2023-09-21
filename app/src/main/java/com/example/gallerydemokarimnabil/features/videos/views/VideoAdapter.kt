package com.example.gallerydemokarimnabil.features.videos.views

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.gallerydemokarimnabil.R
import com.example.gallerydemokarimnabil.databinding.VideoItemLayoutBinding

class VideoAdapter(
    private val videosThumbnailsList: List<Bitmap?>
    ) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    private lateinit var binding: VideoItemLayoutBinding

    class VideoViewHolder(val binding: VideoItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate(inflater,R.layout.video_item_layout,parent,false)
        return VideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.binding.ivVideoItem.setImageBitmap(videosThumbnailsList[position])
    }

    override fun getItemCount() = videosThumbnailsList.size
}