package com.yelpbusiness.android.di.module

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.yelpbusiness.android.R
import com.yelpbusiness.android.manager.ApiErrorHandler
import com.yelpbusiness.android.manager.AppSchedulerProvider
import com.yelpbusiness.common_android.di.qualifiers.DebugTree
import com.yelpbusiness.common_android.util.location.LocationManager
import com.yelpbusiness.common_android.util.location.LocationManagerImpl
import com.yelpbusiness.data.errorhandler.DefaultErrorHandler
import com.yelpbusiness.data.errorhandler.MultiErrorHandler
import com.yelpbusiness.data.manager.RealmLocalCacheManager
import com.yelpbusiness.data.manager.RepositoryCachePrefsImpl
import com.yelpbusiness.domain.base.RepositoryCachePrefs
import com.yelpbusiness.domain.manager.ErrorHandler
import com.yelpbusiness.domain.manager.LocalCacheManager
import com.yelpbusiness.domain.rx.SchedulerProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class ManagerModule {

  @Provides
  @Singleton
  fun providesSchedulerProvider(): SchedulerProvider =
    AppSchedulerProvider()

  @Singleton
  @Provides
  @DebugTree
  fun appDebugTree(): Timber.Tree = Timber.DebugTree()

  @Singleton
  @Provides
  fun providesRepositoryCachePrefs(
    @ApplicationContext context: Context
  ): RepositoryCachePrefs =
    RepositoryCachePrefsImpl(context)

  @Singleton
  @Provides
  fun errorHandler(gson: Gson): ErrorHandler<Fragment> {
    val multiErrorHandler =
      MultiErrorHandler()
    multiErrorHandler.add(
        ApiErrorHandler(gson)
    )
    // should be last
    multiErrorHandler.add(
        DefaultErrorHandler(
            multiErrorHandler, "Debug"
        )
    )
    return multiErrorHandler
  }

  @Provides
  fun locationManager(@ApplicationContext context: Context): LocationManager =
    LocationManagerImpl(
        ContextCompat.getColor(context, R.color.colorPrimary),
        ContextCompat.getColor(context, R.color.colorPrimary)
    )

  @Singleton
  @Provides
  fun localCacheManager(
    realmLocalCacheManager: RealmLocalCacheManager
  ): LocalCacheManager = realmLocalCacheManager

}