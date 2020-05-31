package com.yelpbusiness.android

import androidx.lifecycle.ProcessLifecycleOwner
import com.yelpbusiness.android.di.DaggerApplicationComponent
import com.yelpbusiness.common_android.di.qualifiers.DebugTree
import com.yelpbusiness.domain.usecase.AppInitializationUseCase
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import rx_activity_result2.RxActivityResult
import timber.log.Timber
import javax.inject.Inject

class YelpApplication : DaggerApplication() {

  @Inject
  lateinit var appInitializationUseCase: AppInitializationUseCase

  @Inject
  lateinit var lifecycleListener: AppLifecycleListener

  @field:[Inject DebugTree]
  lateinit var debugTree: Timber.Tree

  override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
    DaggerApplicationComponent.factory()
      .create(this)

  override fun onCreate() {
    super.onCreate()

    Timber.plant(debugTree)

    RxActivityResult.register(this)

    appInitializationUseCase.init()

    ProcessLifecycleOwner.get()
      .lifecycle.addObserver(lifecycleListener)

  }

}