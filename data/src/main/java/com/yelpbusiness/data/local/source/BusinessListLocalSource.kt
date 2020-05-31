package com.yelpbusiness.data.local.source

import com.yelpbusiness.data.local.dao.BusinessDao
import com.yelpbusiness.data.local.model.BusinessDto
import com.yelpbusiness.data.local.util.RealmInstance
import com.yelpbusiness.domain.base.LocalSource
import com.yelpbusiness.domain.keys.BusinessListKey
import com.yelpbusiness.domain.model.Business
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class BusinessListLocalSource @Inject constructor(
  private val realmInstance: RealmInstance,
  private val businessDao: BusinessDao
) : LocalSource<BusinessListKey, List<Business>> {

  override fun get(key: BusinessListKey): Observable<List<Business>> =
    businessDao.getAll(
      realm = realmInstance.getRealm(),
      sortBy = key.sortBy
    )
      .map { it.map { dto -> dto.toBusiness } }

  override fun save(
    key: BusinessListKey,
    data: List<Business>
  ): Single<List<Business>> =
    businessDao.saveAll(
      data = data.map { BusinessDto(it) },
      sortBy = key.sortBy
    )
      .map { it.map { dto -> dto.toBusiness } }
}