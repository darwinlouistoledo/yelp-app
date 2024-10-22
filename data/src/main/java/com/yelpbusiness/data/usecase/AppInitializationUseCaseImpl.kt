package com.yelpbusiness.data.usecase

import com.yelpbusiness.domain.rx.SchedulerProvider
import com.yelpbusiness.domain.usecase.AppInitializationUseCase
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppInitializationUseCaseImpl @Inject constructor(
  private val schedulerProvider: SchedulerProvider
) : AppInitializationUseCase {

  private val disposeBag = CompositeDisposable()

  override fun init() {
  }

  override fun onAppCreated() {
    Observable
      .defer {
        Observable.just(true)
      }
      .subscribeOn(schedulerProvider.ui())
      .subscribe()
      .addTo(disposeBag)
  }

  override fun onAppDestroyed() {
    Observable
      .defer {
        Observable.just(true)
      }
      .subscribeOn(schedulerProvider.ui())
      .subscribe()
      .addTo(disposeBag)

    disposeBag.clear()
  }
}