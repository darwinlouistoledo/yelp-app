package com.yelpbusiness.android.manager

import com.yelpbusiness.domain.coroutines.DispatcherProvider
import com.yelpbusiness.domain.rx.SchedulerProvider
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import timber.log.Timber

class AppDispatcherProvider : DispatcherProvider {

  init {
    Timber.d("DispatcherProvider: AppDispatcherProvider")
  }

  //This dispatcher is used for processing in other threads
  override fun io(): CoroutineDispatcher = Dispatchers.IO

  //This dispatcher is used for processing in Main UI thread
  override fun ui(): CoroutineDispatcher = Dispatchers.Main

}