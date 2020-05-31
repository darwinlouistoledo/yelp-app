package com.yelpbusiness.android.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.yelpbusiness.android.R
import com.yelpbusiness.android.view.adapter.ImageSliderAdapter
import com.yelpbusiness.common_android.ext.view.makeGone
import com.yelpbusiness.common_android.ext.view.makeVisible
import me.relex.circleindicator.CircleIndicator2
import timber.log.Timber

class ImageSliderView : FrameLayout {

  private var container: View? = null
  private var indicator: CircleIndicator2? = null
  private var adapter: ImageSliderAdapter? = null
  private var placeholder: AppCompatImageView? = null
  private var recyclerView: RecyclerView? = null

  constructor(context: Context) : super(context)
  constructor(
    context: Context,
    attrs: AttributeSet?
  ) : super(context, attrs, 0)

  constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyle: Int
  ) : super(context, attrs, defStyle)

  init {
    container = LayoutInflater.from(context)
        .inflate(R.layout.viewgroup_image_slider, this)

    indicator = container?.findViewById(R.id.indicator)

    placeholder = container?.findViewById(R.id.ivPlaceholder)
  }

  fun setupAdapter(
    onImageClicked: ((itemPath: String) -> Unit)? = null
  ) {
    recyclerView = container?.findViewById(R.id.recyclerView)
    recyclerView?.layoutManager =
      LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

    val snapHelper = PagerSnapHelper()
    recyclerView?.onFlingListener = null
    snapHelper.attachToRecyclerView(recyclerView)

    indicator?.attachToRecyclerView(recyclerView!!, snapHelper)

    adapter = ImageSliderAdapter(onImageClicked = onImageClicked)

    adapter?.registerAdapterDataObserver(indicator!!.adapterDataObserver)

    recyclerView?.adapter = adapter
  }

  fun addItems(items: List<String>) {
    Timber.d("ImageSliderView:: items -> $items")
    placeholder?.makeGone()
    adapter?.submitList(items)
    recyclerView?.makeVisible()
  }

  fun showEmpty(resId: Int) {
    placeholder?.makeVisible()
    placeholder?.setImageResource(resId)
    recyclerView?.makeGone()
  }

}