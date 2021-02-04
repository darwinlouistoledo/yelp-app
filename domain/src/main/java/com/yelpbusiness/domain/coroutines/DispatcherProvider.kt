package com.yelpbusiness.domain.coroutines

import kotlinx.coroutines.CoroutineDispatcher

interface DispatcherProvider {
  fun io(): CoroutineDispatcher
  fun ui(): CoroutineDispatcher
}