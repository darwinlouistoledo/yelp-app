package com.yelpbusiness.data.local.source

import com.yelpbusiness.data.local.dao.BusinessDao
import com.yelpbusiness.data.local.model.BusinessDto
import com.yelpbusiness.data.local.util.RealmInstance
import com.yelpbusiness.domain.base.LocalSource
import com.yelpbusiness.domain.model.Business
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class BusinessLocalSource @Inject constructor(
  private val realmInstance: RealmInstance,
  private val businessDao: BusinessDao
) : LocalSource<String, Business> {

  override fun get(key: String): Observable<Business> =
    businessDao.get(
        realm = realmInstance.getRealm(),
        id = key
    )
        .map { it.toBusiness }

  override fun save(
    key: String,
    data: Business
  ): Single<Business> =
    businessDao.save(BusinessDto(data))
        .map { it.toBusiness }
}