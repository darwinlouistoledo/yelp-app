package com.yelpbusiness.data.remote.source

import com.yelpbusiness.data.remote.services.BusinessesApiService
import com.yelpbusiness.domain.base.RemoteSource
import com.yelpbusiness.domain.keys.BusinessListKey
import com.yelpbusiness.domain.model.Business
import io.reactivex.Single
import javax.inject.Inject

class BusinessListRemoteSource @Inject constructor(
  private val businessesApiService: BusinessesApiService
) : RemoteSource<BusinessListKey, List<Business>> {

  override fun fetch(key: BusinessListKey): Single<List<Business>> =
    businessesApiService.getBusinesses(
      term = key.term,
      location = key.location,
      categories = key.categories,
      latitude = key.lat,
      longitude = key.lon,
      sortBy = key.sortBy?.name?.toLowerCase()
    )
      .map {
        it.data.map { repo -> repo.toBusiness }
      }
}