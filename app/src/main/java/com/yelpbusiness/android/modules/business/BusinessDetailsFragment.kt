package com.yelpbusiness.android.modules.business

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.yelpbusiness.android.R
import com.yelpbusiness.android.base.AppFragment
import com.yelpbusiness.android.databinding.FragmentBusinessDetailsBinding
import com.yelpbusiness.android.modules.business.BusinessDetailsViewModel.Action
import com.yelpbusiness.android.modules.business.BusinessDetailsViewModel.State
import com.yelpbusiness.android.view.adapter.ScheduleListItemAdapter
import com.yelpbusiness.common_android.base.mvi_coroutines.MviView
import com.yelpbusiness.common_android.ext.aac.observe
import com.yelpbusiness.common_android.ext.aac.withViewModel
import com.yelpbusiness.common_android.ext.component.onBackPressed
import com.yelpbusiness.common_android.ext.databinding.withBinding
import com.yelpbusiness.common_android.ext.view.makeVisibleOrGone
import com.yelpbusiness.domain.exceptions.RequiredArgumentException
import com.yelpbusiness.domain.model.Category
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BusinessDetailsFragment : AppFragment(), MviView<State> {

  companion object {
    fun generateArgs(
      businessId: String
    ): Bundle = bundleOf(
        EXTRA_ARG_BUSINESS_iD to businessId
    )

    const val EXTRA_ARG_BUSINESS_iD = "extra_arg_business_id"
  }

  private lateinit var viewModel: BusinessDetailsViewModel
  private lateinit var binding: FragmentBusinessDetailsBinding

  private val adapterSchedule = ScheduleListItemAdapter()

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding = withBinding(inflater, R.layout.fragment_business_details, container)
    return binding.root
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)

    viewModel = withViewModel(this, viewModelFactory) {
      observe(observableState, ::render)

      dispatch(
          Action.LoadBusinessData(
              requireArguments().getString(EXTRA_ARG_BUSINESS_iD)
                  ?: throw RequiredArgumentException(
                      "businessId"
                  )
          )
      )
    }

    binding.apply {

      toolbarBack.setOnClickListener { onBackPressed() }

      viewImageSlider.showEmpty(R.drawable.img_placeholder_blurred)

      viewImageSlider.setupAdapter()

      ViewCompat.setNestedScrollingEnabled(rvSchedule, false)
      rvSchedule.apply {
        adapter = adapterSchedule
        layoutManager = GridLayoutManager(requireContext(), 3, RecyclerView.VERTICAL, false)
      }

    }
  }

  override fun render(state: State) {

    binding.apply {
      detailsContainer.makeVisibleOrGone(state.business != null)
      spinKit.makeVisibleOrGone(state.business == null && state.showLoading)

      state.business?.let { b ->

        inflateCategories(b.categories)

        viewImageSlider.addItems(b.photos)
        viewImageSlider.makeVisibleOrGone(b.photos.isNotEmpty())

        tvBusinessName.text = b.name
        tvAddress.text = b.addressToDisplay

        tvPhone.text = b.displayPhone
        tvPhone.makeVisibleOrGone(b.displayPhone != null || b.displayPhone != "")
        appCompatImageView3.makeVisibleOrGone(b.displayPhone != null || b.displayPhone != "")

        tvRateValue.text = requireContext().resources.getQuantityString(
            R.plurals.lbl_review_count, b.reviewCountDisplay, b.reviewCountDisplay
        )

        rvSchedule.makeVisibleOrGone(b.hours.isNotEmpty())
        appCompatImageView4.makeVisibleOrGone(b.hours.isNotEmpty())
        tvHours.makeVisibleOrGone(b.hours.isNotEmpty())
        tvOpen.makeVisibleOrGone(b.hours.isNotEmpty())

        tvOpen.text = when (state.isOpenNow) {
          true -> getString(R.string.lbl_open_now)
          false -> getString(R.string.lbl_close_now)
        }

        tvOpen.setTextColor(
            when (state.isOpenNow) {
              true -> Color.GREEN
              false -> Color.RED
            }
        )

        adapterSchedule.submitList(state.schedules)
      }
    }

    state.error?.getContentIfNotHandled()
        ?.let {
          errorHandler.handle(this, it)
        }
  }

  private fun inflateCategories(list: List<Category>) {
    binding.apply {
      chipCategories.removeAllViews()
      list.forEach {
        val chip = Chip(requireContext())
        chip.setChipBackgroundColorResource(R.color.white)
        chip.isClickable = false
        chip.isCheckable = false
        chip.isCloseIconVisible = false
        chip.text = it.title
        chipCategories.addView(chip)
      }
    }
  }
}