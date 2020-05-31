package com.yelpbusiness.domain.base

import com.yelpbusiness.domain.enums.CacheStatus

interface RepositoryCachePrefs {

  fun updateCacheUpdate(key: String)

  fun getCacheUpdate(
    key: String,
    staleDate: Long,
    expiryDate: Long? = null
  ): CacheStatus

  fun deleteCachePrefs()

}