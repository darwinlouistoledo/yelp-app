package com.yelpbusiness.data.remote.source

import com.yelpbusiness.data.remote.services.BusinessesApiService
import com.yelpbusiness.domain.base.RemoteSource
import com.yelpbusiness.domain.model.Business
import io.reactivex.Single
import javax.inject.Inject

class BusinessRemoteSource @Inject constructor(
  private val businessesApiService: BusinessesApiService
) : RemoteSource<String, Business> {

  override fun fetch(key: String): Single<Business> =
    businessesApiService.getBusiness(key)
        .map { it.toBusiness }
}