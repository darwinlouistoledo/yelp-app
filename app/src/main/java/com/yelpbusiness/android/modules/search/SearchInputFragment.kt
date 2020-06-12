package com.yelpbusiness.android.modules.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.yelpbusiness.android.R
import com.yelpbusiness.android.base.AppFragment
import com.yelpbusiness.android.databinding.FragmentSearchInputBinding
import com.yelpbusiness.common_android.ext.aac.withViewModel
import com.yelpbusiness.common_android.ext.component.onBackPressed
import com.yelpbusiness.common_android.ext.databinding.withBinding
import com.yelpbusiness.common_android.util.rx.RxViewUtil
import com.yelpbusiness.domain.rx.SchedulerProvider
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

@AndroidEntryPoint
class SearchInputFragment : AppFragment() {

  companion object {
    fun generateArgs(
      term: String,
      location: String,
      categories: String
    ): Bundle = bundleOf(
        EXTRA_ARG_TERM to term,
        EXTRA_ARG_LOCATION to location,
        EXTRA_ARG_CATEGORIES to categories
    )

    const val EXTRA_ARG_TERM = "extra_arg_term"
    const val EXTRA_ARG_LOCATION = "extra_arg_location"
    const val EXTRA_ARG_CATEGORIES = "extra_arg_categories"
  }

  @Inject
  lateinit var schedulerProvider: SchedulerProvider

  private lateinit var binding: FragmentSearchInputBinding
  private lateinit var searchInputManagerViewModel: SearchInputManagerViewModel

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding = withBinding(inflater, R.layout.fragment_search_input, container)
    return binding.root
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)

    searchInputManagerViewModel = withViewModel(requireActivity(), viewModelFactory)

    binding.apply {
      toolbarBack.setOnClickListener { onBackPressed() }

      etTerm.setText(requireArguments().getString(EXTRA_ARG_TERM))
      etLocation.setText(requireArguments().getString(EXTRA_ARG_LOCATION))
      etCategories.setText(requireArguments().getString(EXTRA_ARG_CATEGORIES))

      RxViewUtil.click(btnApply)
          .observeOn(schedulerProvider.ui())
          .subscribe {
            searchInputManagerViewModel.applySearchFilters(
                term = etTerm.text.toString(),
                location = etLocation.text.toString(),
                categories = etCategories.text.toString()
            )
            onBackPressed()
          }
          .addTo(disposeBag)

      RxViewUtil.click(btnClear)
          .observeOn(schedulerProvider.ui())
          .subscribe {
            etTerm.setText("")
            etLocation.setText("")
            etCategories.setText("")
          }
          .addTo(disposeBag)

    }
  }
}