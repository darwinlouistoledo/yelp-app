package com.yelpbusiness.data.usecase

import com.yelpbusiness.domain.base.RemoteSource
import com.yelpbusiness.domain.enums.BusinessSort
import com.yelpbusiness.domain.exceptions.RequiredArgumentException
import com.yelpbusiness.domain.keys.BusinessListKey
import com.yelpbusiness.domain.manager.LocalCacheManager
import com.yelpbusiness.domain.model.Business
import com.yelpbusiness.domain.sealedclass.DataResult
import com.yelpbusiness.domain.usecase.BusinessUseCase
import io.reactivex.Observable
import javax.inject.Inject

class BusinessUseCaseImpl @Inject constructor(
  private val localCacheManager: LocalCacheManager,
  private val businessListRepository: RemoteSource<BusinessListKey, List<Business>>,
  private val businessRepository: RemoteSource<String, Business>
) : BusinessUseCase {

  /**
   * This will get all the businesses based on the filters provided.
   *
   */
  override fun getBusinesses(
    term: String?,
    location: String?,
    categories: String?,
    lat: Float?,
    lon: Float?,
    sort: BusinessSort?,
    clearCache: Boolean
  ): Observable<DataResult<List<Business>>> = when {
    lat != null && lon != null ->
      when (clearCache) {
        true -> localCacheManager.deleteAll()
          .toObservable()
          .switchMap {
            businessListRepository.fetch(
              BusinessListKey(
                term = term,
                location = location,
                categories = categories,
                lat = lat.takeIf { location == null },
                lon = lon.takeIf { location == null },
                sortBy = sort
              )
            ).toObservable()
          }
          .map<DataResult<List<Business>>> { DataResult.Success(it) }
          .onErrorReturn { DataResult.Failed(it) }
        else -> businessListRepository.fetch(
          BusinessListKey(
            term = term,
            location = location,
            categories = categories,
            lat = lat.takeIf { location == null },
            lon = lon.takeIf { location == null },
            sortBy = sort
          )
        ).toObservable()
          .map<DataResult<List<Business>>> { DataResult.Success(it) }
          .onErrorReturn { DataResult.Failed(it) }
      }

    else -> Observable.just(DataResult.Failed(RequiredArgumentException("lat and lon")))
  }

  /**
   * This will get all the details of a specific business based on the id provided.
   *
   */
  override fun getBusiness(id: String): Observable<DataResult<Business>> =
    businessRepository.fetch(id)
      .toObservable()
      .map<DataResult<Business>> { DataResult.Success(it) }
      .onErrorReturn { DataResult.Failed(it) }
}