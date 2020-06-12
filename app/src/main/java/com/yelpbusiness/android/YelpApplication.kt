package com.yelpbusiness.android

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import com.yelpbusiness.common_android.di.qualifiers.DebugTree
import com.yelpbusiness.domain.usecase.AppInitializationUseCase
import dagger.hilt.android.HiltAndroidApp
import rx_activity_result2.RxActivityResult
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class YelpApplication : Application() {

  @Inject
  lateinit var appInitializationUseCase: AppInitializationUseCase

  @Inject
  lateinit var lifecycleListener: AppLifecycleListener

  @field:[Inject DebugTree]
  lateinit var debugTree: Timber.Tree

  override fun onCreate() {
    super.onCreate()

    Timber.plant(debugTree)

    RxActivityResult.register(this)

    appInitializationUseCase.init()

    ProcessLifecycleOwner.get()
        .lifecycle.addObserver(lifecycleListener)

  }

}