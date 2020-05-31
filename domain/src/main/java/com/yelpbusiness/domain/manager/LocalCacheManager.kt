package com.yelpbusiness.domain.manager

import io.reactivex.Single

interface LocalCacheManager {

  fun deleteAll() : Single<Boolean>

}