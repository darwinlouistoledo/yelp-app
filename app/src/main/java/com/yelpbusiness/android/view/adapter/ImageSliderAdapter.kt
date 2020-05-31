package com.yelpbusiness.android.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.yelpbusiness.android.R
import com.yelpbusiness.android.view.adapter.ImageSliderAdapter.ImageSliderViewHolder

internal class ImageSliderAdapter(
  private val onImageClicked: ((itemPath: String) -> Unit)? = null
) : ListAdapter<String, ImageSliderViewHolder>(ImageSliderDiffCallback) {

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): ImageSliderViewHolder {
    return ImageSliderViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.viewholder_image_slider_item, parent, false)
    )
  }

  override fun onBindViewHolder(
    holder: ImageSliderViewHolder,
    position: Int
  ) {
    val path = getItem(position)

    holder.ivSliderPhoto.setOnClickListener {
      onImageClicked?.invoke(path)
    }

    Glide.with(holder.itemView.context)
        .load(path)
        .placeholder(R.drawable.img_placeholder_blurred)
        .into(holder.ivSliderPhoto)

  }

  inner class ImageSliderViewHolder(view: View) : ViewHolder(view) {
    val ivSliderPhoto: AppCompatImageView = view.findViewById(R.id.ivSliderPhoto)
  }

  object ImageSliderDiffCallback : DiffUtil.ItemCallback<String>() {

    override fun areItemsTheSame(
      oldItem: String,
      newItem: String
    ): Boolean = (oldItem == newItem)

    override fun areContentsTheSame(
      oldItem: String,
      newItem: String
    ): Boolean = (oldItem == newItem)

  }

}