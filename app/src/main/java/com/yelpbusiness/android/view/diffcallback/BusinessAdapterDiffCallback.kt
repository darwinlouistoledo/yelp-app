package com.yelpbusiness.android.view.diffcallback

import androidx.recyclerview.widget.DiffUtil
import com.yelpbusiness.domain.model.Business

object BusinessAdapterDiffCallback : DiffUtil.ItemCallback<Business>() {

  override fun areItemsTheSame(
    oldItem: Business,
    newItem: Business
  ): Boolean = oldItem.id == newItem.id

  override fun areContentsTheSame(
    oldItem: Business,
    newItem: Business
  ): Boolean = oldItem == newItem
}