package com.yelpbusiness.android.view.adapter

import com.bumptech.glide.Glide
import com.yelpbusiness.android.R
import com.yelpbusiness.android.databinding.ViewholderItemBusinessFeedBinding
import com.yelpbusiness.android.view.diffcallback.BusinessAdapterDiffCallback
import com.yelpbusiness.common_android.base.SimpleListAdapter
import com.yelpbusiness.domain.model.Business

class BusinessListItemAdapter(
  private val onViewBusinessDetails: ((item: Business) -> Unit)
) :
  SimpleListAdapter<ViewholderItemBusinessFeedBinding, Business>(
    R.layout.viewholder_item_business_feed,
    BusinessAdapterDiffCallback
  ) {

  private var isBySearchLocation = false

  override fun bind(
    holder: ViewHolder<ViewholderItemBusinessFeedBinding>,
    data: Business,
    position: Int
  ) {
    val context = holder.itemView.context

    holder.itemView.setOnClickListener {
      onViewBusinessDetails.invoke(data)
    }

    holder.binding.apply {
      tvName.text = data.name
      tvAddress.text = data.addressToDisplay
      tvDistance.text = when(isBySearchLocation){
        true-> context.getString(R.string.lbl_meters_away_from_searched, data.roundedDistance.toString())
        else-> context.getString(R.string.lbl_meters_away_from_you, data.roundedDistance.toString())
      }

      tvRateValue.text = context.resources.getQuantityString(R.plurals.lbl_review_count, data.reviewCountDisplay, data.reviewCountDisplay)
      ratingBar.rating = data.rating ?: 0f

      Glide.with(holder.itemView.context)
        .load(data.primaryImgUrl)
        .placeholder(R.drawable.img_placeholder_blurred)
        .into(ivPhoto)

    }
  }

  fun setIsBySearchLocation(boolean: Boolean) {
    isBySearchLocation = boolean
  }
}