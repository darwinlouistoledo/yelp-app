package com.yelpbusiness.domain.rx

import io.reactivex.Flowable
import io.reactivex.functions.Function
import java.util.concurrent.TimeUnit

class RetryWithDelay(
  private val maxRetries: Int,
  private val retryDelayMillis: Long,
  private var retryCount: Int = 0
) : Function<Flowable<Throwable>, Flowable<Any>> {
  override fun apply(t: Flowable<Throwable>): Flowable<Any> {
    return t
        .flatMap {
          if (++retryCount < maxRetries) {
            return@flatMap Flowable.timer(retryDelayMillis, TimeUnit.MILLISECONDS)
          }
          return@flatMap Flowable.error<Any>(it)
        }
  }
}