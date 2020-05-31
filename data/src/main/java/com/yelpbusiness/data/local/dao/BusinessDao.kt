package com.yelpbusiness.data.local.dao

import com.yelpbusiness.data.local.model.BusinessDto
import com.yelpbusiness.data.local.util.RealmHelper
import com.yelpbusiness.data.local.util.findAllObservable
import com.yelpbusiness.data.local.util.findObservable
import com.yelpbusiness.data.local.util.saveToRealm
import com.yelpbusiness.domain.enums.BusinessSort
import io.reactivex.Observable
import io.reactivex.Single
import io.realm.Realm
import io.realm.RealmQuery
import io.realm.Sort.ASCENDING
import io.realm.Sort.DESCENDING
import io.realm.kotlin.where
import javax.inject.Inject

class BusinessDao @Inject constructor() {
  fun get(
    realm: Realm,
    id: String
  ): Observable<BusinessDto> = realm.where<BusinessDto>()
    .equalTo(BusinessDto::id.name, id)
    .findObservable()

  fun getAll(
    realm: Realm,
    query: String?,
    sortBy: BusinessSort?
  ): Observable<List<BusinessDto>> = realm.where<BusinessDto>()
    .query(query, sortBy)
    .findAllObservable()

  fun save(data: BusinessDto): Single<BusinessDto> = RealmHelper.rxTransaction{ realm ->
    val oldData: BusinessDto? = realm.where<BusinessDto>()
      .equalTo(BusinessDto::id.name, data.id)
      .findFirst()

    if (oldData!=null)
      data.distance = oldData.distance

    realm.saveToRealm(data)
  }

  fun saveAll(
    data: List<BusinessDto>,
    delete: Boolean,
    query: String?,
    sortBy: BusinessSort?
  ): Single<List<BusinessDto>> = RealmHelper.rxTransaction { realm ->
    realm.where<BusinessDto>()
      .query(query, sortBy)
      .findAll()
      .deleteAllFromRealm()
    realm.saveToRealm(data)
  }

  private fun RealmQuery<BusinessDto>.query(
    query: String?,
    sortBy: BusinessSort?
  ): RealmQuery<BusinessDto> = this
    .let {rq->
      when(sortBy){
        BusinessSort.DISTANCE ->{
          rq.sort(BusinessDto::distance.name, ASCENDING)
        }
        BusinessSort.RATING ->{
          rq.sort(BusinessDto::rating.name, DESCENDING)
        }
        else -> rq
      }
    }
}