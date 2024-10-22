package com.yelpbusiness.data.manager

import com.yelpbusiness.domain.manager.LocalCacheManager
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RealmLocalCacheManager @Inject constructor() : LocalCacheManager {

  override fun deleteAll(): Single<Boolean> = Single.just(true)

}