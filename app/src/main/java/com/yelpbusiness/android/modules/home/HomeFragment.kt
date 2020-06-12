package com.yelpbusiness.android.modules.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.google.android.material.chip.Chip
import com.yelpbusiness.android.BuildConfig
import com.yelpbusiness.android.R
import com.yelpbusiness.android.base.AppFragment
import com.yelpbusiness.android.databinding.FragmentHomeBinding
import com.yelpbusiness.android.modules.business.BusinessDetailsFragment
import com.yelpbusiness.android.modules.home.HomeViewModel.Action
import com.yelpbusiness.android.modules.map.BusinessesMapFragment
import com.yelpbusiness.android.modules.search.SearchInputFragment
import com.yelpbusiness.android.modules.search.SearchInputManagerViewModel
import com.yelpbusiness.android.view.adapter.BusinessListItemAdapter
import com.yelpbusiness.common_android.ext.aac.observe
import com.yelpbusiness.common_android.ext.aac.withViewModel
import com.yelpbusiness.common_android.ext.databinding.withBinding
import com.yelpbusiness.common_android.sealedclass.LocationStatus.CurrentLocation
import com.yelpbusiness.common_android.sealedclass.LocationStatus.OpenAppInfo
import com.yelpbusiness.common_android.util.location.LocationManager
import com.yelpbusiness.common_android.util.rx.RxViewUtil
import com.yelpbusiness.domain.enums.BusinessSort
import com.yelpbusiness.domain.model.Business
import com.yelpbusiness.domain.rx.SchedulerProvider
import com.yelpbusiness.domain.sealedclass.LiveResult
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import io.reactivex.rxkotlin.addTo
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : AppFragment() {

  private lateinit var binding: FragmentHomeBinding
  private lateinit var viewModel: HomeViewModel
  private lateinit var searchInputManagerViewModel: SearchInputManagerViewModel

  @Inject
  lateinit var schedulerProvider: SchedulerProvider

  @Inject
  lateinit var locationManager: LocationManager

  private val adapterItems = BusinessListItemAdapter(::onViewBusinessDetails)

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding = withBinding(inflater, R.layout.fragment_home, container)
    return binding.root
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)

    viewModel = withViewModel(this, viewModelFactory) {
      observe(observableState, ::render)
    }

    searchInputManagerViewModel = withViewModel(requireActivity(), viewModelFactory) {
      observe(searchFilters) {
        it.getContentIfNotHandled()
            ?.let { value ->
              viewModel.dispatch(Action.SetSearchFilters(value))
            }
      }
    }

    val sortDialog = MaterialDialog(requireContext())
        .title(res = R.string.title_sort_by)
        .listItemsSingleChoice(
            items = listOf(
                BusinessSort.DISTANCE.name.toLowerCase()
                    .capitalize(),
                BusinessSort.RATING.name.toLowerCase()
                    .capitalize()
            )
        ) { dialog, index, text ->
          viewModel.dispatch(Action.SetSort(text.toString()))
        }

    binding.apply {
      recyclerView.apply {
        adapter = adapterItems
        layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
      }

      RxViewUtil.click(ivSearch)
          .observeOn(schedulerProvider.ui())
          .subscribe {
            val args = SearchInputFragment.generateArgs(
                term = viewModel.observableState.value?.queryData?.term ?: "",
                location = viewModel.observableState.value?.queryData?.location ?: "",
                categories = viewModel.observableState.value?.queryData?.categories ?: ""
            )
            findNavController().navigate(R.id.searchFragment, args)
          }
          .addTo(disposeBag)

      RxViewUtil.click(ivSort)
          .observeOn(schedulerProvider.ui())
          .subscribe {
            sortDialog.show()
          }
          .addTo(disposeBag)

      RxViewUtil.click(fabMap)
          .observeOn(schedulerProvider.ui())
          .subscribe {
            val args = BusinessesMapFragment.generateArgs(
                term = viewModel.observableState.value?.queryData?.term,
                location = viewModel.observableState.value?.queryData?.location,
                categories = viewModel.observableState.value?.queryData?.categories,
                sort = viewModel.observableState.value?.queryData?.sort,
                lat = viewModel.observableState.value?.queryData?.lat
                    ?: throw IllegalStateException("Latitude should not be null"),
                lon = viewModel.observableState.value?.queryData?.lon
                    ?: throw IllegalStateException("Longitude should not be null")
            )
            findNavController().navigate(R.id.businessMapFragment, args)
          }
          .addTo(disposeBag)

    }

    Observable.just(true)
        .subscribeOn(schedulerProvider.io())
        .observeOn(schedulerProvider.ui())
        .switchMap {
          locationManager.getLocation(requireActivity())
        }
        .doOnError {
          it.printStackTrace()
        }
        .subscribe { locationStatus ->
          when (locationStatus) {
            is OpenAppInfo -> {
              val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
              intent.data = Uri.parse("package:" + BuildConfig.APPLICATION_ID)
              startActivity(intent)
            }
            is CurrentLocation -> {
              Timber.i("CurrentLocation")
              viewModel.dispatch(
                  Action.LoadBusinesses(
                      latitude = locationStatus.location.latitude.toFloat(),
                      longitude = locationStatus.location.longitude.toFloat()
                  )
              )
            }
          }
        }
        .addTo(disposeBag)

  }

  private fun render(state: HomeViewModel.State) {
    adapterItems.setIsBySearchLocation(state.isBySearchLocation)
    adapterItems.submitList(state.businessList)

    inflateFilters(state.filterDisplay)

    state.loadingResult?.getContentIfNotHandled()
        ?.let {
          when (it) {
            is LiveResult.Success -> {
              dismissProgressDialog()
            }
            is LiveResult.Failed -> {
              dismissProgressDialog()
              errorHandler.handle(this, it.error)
            }
            is LiveResult.Loading -> {
              adapterItems.submitList(emptyList())
              showProgressDialog()
            }
          }
        }

    state.error?.getContentIfNotHandled()
        ?.let {
          errorHandler.handle(this, it)
        }
  }

  private fun onViewBusinessDetails(business: Business) {
    findNavController().navigate(
        R.id.businessDetailsFragment,
        BusinessDetailsFragment.generateArgs(business.id)
    )
  }

  private fun inflateFilters(list: List<String>) {
    binding.apply {
      chipFilters.removeAllViews()
      list.forEach {
        val chip = Chip(requireContext())
        chip.setChipBackgroundColorResource(R.color.white)
        chip.isClickable = false
        chip.isCheckable = false
        chip.isCloseIconVisible = false
        chip.text = it
        chipFilters.addView(chip)
      }
    }
  }
}