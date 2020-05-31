package com.yelpbusiness.domain.usecase

import com.yelpbusiness.domain.enums.BusinessSort
import com.yelpbusiness.domain.model.Business
import com.yelpbusiness.domain.sealedclass.DataResult
import io.reactivex.Observable

interface BusinessUseCase {

  fun getBusinesses(
    term: String?,
    location: String?,
    categories: String?,
    lat: Float?,
    lon: Float?,
    sort: BusinessSort?
  ): Observable<DataResult<List<Business>>>

  fun getBusiness(id: String): Observable<DataResult<Business>>
}