package com.yelpbusiness.data.di

import android.app.Application
import com.yelpbusiness.data.local.source.BusinessListLocalSource
import com.yelpbusiness.data.local.source.BusinessLocalSource
import com.yelpbusiness.data.remote.services.BusinessesApiService
import com.yelpbusiness.data.remote.source.BusinessListRemoteSource
import com.yelpbusiness.data.remote.source.BusinessRemoteSource
import com.yelpbusiness.data.repository.SimpleRepository
import com.yelpbusiness.data.usecase.AppInitializationUseCaseImpl
import com.yelpbusiness.data.usecase.BusinessUseCaseImpl
import com.yelpbusiness.domain.base.Repository
import com.yelpbusiness.domain.base.RepositoryCachePrefs
import com.yelpbusiness.domain.keys.BusinessListKey
import com.yelpbusiness.domain.model.Business
import com.yelpbusiness.domain.rx.SchedulerProvider
import com.yelpbusiness.domain.usecase.AppInitializationUseCase
import com.yelpbusiness.domain.usecase.BusinessUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import io.realm.Realm
import io.realm.RealmConfiguration
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class DataModule {

  @Provides
  @Singleton
  fun businessApiService(retrofit: Retrofit): BusinessesApiService =
    retrofit.create(BusinessesApiService::class.java)


  @Singleton
  @Provides
  fun realmConfiguration(application: Application): RealmConfiguration {
    Realm.init(application)
    return RealmConfiguration.Builder()
      .deleteRealmIfMigrationNeeded()
      .build()
  }

  @Provides
  fun businessListRepository(
    localSource: BusinessListLocalSource,
    remoteSource: BusinessListRemoteSource,
    cachePrefs: RepositoryCachePrefs,
    schedulerProvider: SchedulerProvider
  ): Repository<BusinessListKey, List<Business>> = SimpleRepository(
    localSource = localSource,
    remoteSource = remoteSource,
    cachePrefs = cachePrefs,
    schedulerProvider = schedulerProvider,
    tag = "businessListRepository"
  )

  @Provides
  fun businessRepository(
    localSource: BusinessLocalSource,
    remoteSource: BusinessRemoteSource,
    cachePrefs: RepositoryCachePrefs,
    schedulerProvider: SchedulerProvider
  ): Repository<String, Business> = SimpleRepository(
    localSource = localSource,
    remoteSource = remoteSource,
    cachePrefs = cachePrefs,
    schedulerProvider = schedulerProvider,
    tag = "businessRepository"
  )

  @Provides
  fun appInitializationUseCase(appInitializationUseCaseImpl: AppInitializationUseCaseImpl): AppInitializationUseCase =
    appInitializationUseCaseImpl

  @Provides
  fun businessUseCase(businessUseCaseImpl: BusinessUseCaseImpl): BusinessUseCase =
    businessUseCaseImpl
}