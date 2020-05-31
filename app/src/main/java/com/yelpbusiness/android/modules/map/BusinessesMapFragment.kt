package com.yelpbusiness.android.modules.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.yelpbusiness.android.R
import com.yelpbusiness.android.base.AppFragment
import com.yelpbusiness.android.databinding.FragmentBusinessMapBinding
import com.yelpbusiness.android.modules.business.BusinessDetailsFragment
import com.yelpbusiness.android.modules.map.BusinessesMapViewModel.Action
import com.yelpbusiness.android.modules.map.BusinessesMapViewModel.State
import com.yelpbusiness.common_android.ext.aac.observe
import com.yelpbusiness.common_android.ext.aac.withViewModel
import com.yelpbusiness.common_android.ext.component.onBackPressed
import com.yelpbusiness.common_android.ext.databinding.withBinding
import com.yelpbusiness.domain.model.Business
import com.yelpbusiness.domain.model.LoadBusinessesQueryData
import com.yelpbusiness.domain.rx.SchedulerProvider
import io.reactivex.Observable
import io.reactivex.rxkotlin.addTo
import timber.log.Timber
import java.util.concurrent.TimeUnit.MILLISECONDS
import javax.inject.Inject

class BusinessesMapFragment : AppFragment(), OnMapReadyCallback, OnMarkerClickListener, OnInfoWindowClickListener {

  companion object {
    fun generateArgs(
      term: String?,
      location: String?,
      categories: String?,
      sort: String?,
      lat: Float,
      lon: Float
    ): Bundle = bundleOf(
      EXTRA_ARG_TERM to term,
      EXTRA_ARG_LOCATION to location,
      EXTRA_ARG_CATEGORIES to categories,
      EXTRA_ARG_SORT to sort,
      EXTRA_ARG_LAT to lat,
      EXTRA_ARG_LON to lon
    )

    private const val EXTRA_ARG_TERM = "extra_arg_term"
    private const val EXTRA_ARG_LOCATION = "extra_arg_location"
    private const val EXTRA_ARG_CATEGORIES = "extra_arg_categories"
    private const val EXTRA_ARG_SORT = "extra_arg_sort"
    private const val EXTRA_ARG_LAT = "extra_arg_lat"
    private const val EXTRA_ARG_LON = "extra_arg_lon"
  }

  @Inject
  lateinit var schedulerProvider: SchedulerProvider

  private lateinit var binding: FragmentBusinessMapBinding
  private lateinit var viewModel: BusinessesMapViewModel

  private lateinit var mapFragment: SupportMapFragment
  private var markerCurrLocation: Marker? = null

  private var mGoogleMap: GoogleMap? = null

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding = withBinding(inflater, R.layout.fragment_business_map, container)
    return binding.root
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)

    viewModel = withViewModel(this, viewModelFactory) {
      observe(observableState, ::render)
    }

    Observable.just(true)
      .take(1)
      .delay(500, MILLISECONDS)
      .observeOn(schedulerProvider.ui())
      .subscribe {
        setupMap()
      }
      .addTo(disposeBag)

    binding.apply {
      toolbarBack.setOnClickListener { onBackPressed() }
    }
  }

  private fun render(state: State) {
    Timber.d("render")

    state.populateMap?.getContentIfNotHandled()?.let {
      placeMarkers(state.businessList, state.queryData)
    }

    state.error?.getContentIfNotHandled()
      ?.let {
        errorHandler.handle(this, it)
      }
  }

  private fun setupMap() {
    mapFragment = SupportMapFragment.newInstance(
      GoogleMapOptions()
        .mapType(GoogleMap.MAP_TYPE_NORMAL)
        .rotateGesturesEnabled(true)
        .maxZoomPreference(100f)
        .minZoomPreference(10f)
        .zoomGesturesEnabled(true)
        .zoomControlsEnabled(true)
        .useViewLifecycleInFragment(true)
    )
      .apply {
        retainInstance = true
      }

    childFragmentManager.beginTransaction()
      .replace(R.id.mapContainer, mapFragment)
      .commit()
    mapFragment.getMapAsync(this)
  }

  override fun onMapReady(googleMap: GoogleMap?) {
    googleMap?.let {
      mGoogleMap = it
      mGoogleMap?.setOnMarkerClickListener(this)
      mGoogleMap?.setOnInfoWindowClickListener(this)

      viewModel.dispatch(
        Action.LoadBusinesses(
          lat = requireArguments().getFloat(EXTRA_ARG_LAT),
          lon = requireArguments().getFloat(EXTRA_ARG_LON),
          term = requireArguments().getString(EXTRA_ARG_TERM),
          location = requireArguments().getString(EXTRA_ARG_LOCATION),
          categories = requireArguments().getString(EXTRA_ARG_CATEGORIES),
          sort = requireArguments().getString(EXTRA_ARG_SORT)
        )
      )

      viewModel.dispatch(Action.PopulateMap)
    }
  }

  override fun onMarkerClick(marker: Marker?): Boolean {
    marker?.let {
      it.showInfoWindow()
    }
    return true
  }

  override fun onInfoWindowClick(marker: Marker?) {
    marker?.let {
      if (it.tag is Business) {
        val b = it.tag as Business
        findNavController().navigate(
          R.id.businessDetailsFragment,
          BusinessDetailsFragment.generateArgs(b.id)
        )
      }
    }
  }

  private fun placeMarkers(list: List<Business>, queryData: LoadBusinessesQueryData?) {
    mGoogleMap?.let {
      it.clear()
      list.forEach { b ->
        Timber.d("creating business marker = ${b.name}")
        val latLon = LatLng(b.coordinates.latitude.toDouble(), b.coordinates.longitude.toDouble())
        val marker = it.addMarker(
          MarkerOptions()
            .position(latLon)
            .title(b.name)
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_pin_business))
        )
        marker?.tag = b
      }
    }

    queryData?.let {
      mGoogleMap?.let {map->
        Timber.d("creating business Your Location marker")
        markerCurrLocation?.remove()
        val latLon = LatLng(it.lat!!.toDouble(), it.lon!!.toDouble())
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLon, 12f))
        markerCurrLocation = map.addMarker(
          MarkerOptions()
            .position(latLon)
            .title("Your Location")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_pin_you))
        )
        markerCurrLocation?.tag = "currLoc"
      }
    }
  }
}