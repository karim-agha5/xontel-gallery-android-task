package com.example.gallerydemokarimnabil.features.images.views

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.gallerydemokarimnabil.R
import com.example.gallerydemokarimnabil.databinding.ImageItemLayoutBinding

class ImageAdapter(
    private val imagesList: List<Drawable?>
) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>(){

    private lateinit var binding: ImageItemLayoutBinding

    class ImageViewHolder(val binding: ImageItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate(inflater,R.layout.image_item_layout,parent,false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.binding.ivImageItem.setImageDrawable(imagesList[position])
    }

    override fun getItemCount() = imagesList.size
}