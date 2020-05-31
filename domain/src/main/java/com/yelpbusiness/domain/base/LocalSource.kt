package com.yelpbusiness.domain.base

import io.reactivex.Observable
import io.reactivex.Single

interface LocalSource<Key, Data> {

  fun get(key: Key): Observable<Data>

  fun save(
    key: Key,
    data: Data
  ): Single<Data>

}