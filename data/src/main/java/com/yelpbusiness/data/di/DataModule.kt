package com.yelpbusiness.data.di

import com.yelpbusiness.data.remote.services.BusinessesApiService
import com.yelpbusiness.data.remote.source.BusinessListRemoteSource
import com.yelpbusiness.data.remote.source.BusinessRemoteSource
import com.yelpbusiness.data.usecase.AppInitializationUseCaseImpl
import com.yelpbusiness.data.usecase.BusinessUseCaseImpl
import com.yelpbusiness.domain.base.RemoteSource
import com.yelpbusiness.domain.keys.BusinessListKey
import com.yelpbusiness.domain.model.Business
import com.yelpbusiness.domain.usecase.AppInitializationUseCase
import com.yelpbusiness.domain.usecase.BusinessUseCase
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class DataModule {

  @Provides
  @Singleton
  fun businessApiService(retrofit: Retrofit): BusinessesApiService =
    retrofit.create(BusinessesApiService::class.java)

  @Provides
  fun businessListRepository(
    businessApiService: BusinessesApiService,
    remoteSource: BusinessListRemoteSource
  ): RemoteSource<BusinessListKey, List<Business>> = BusinessListRemoteSource(businessApiService)

  @Provides
  fun businessRepository(
    businessApiService: BusinessesApiService,
  ): RemoteSource<String, Business> = BusinessRemoteSource(businessApiService)

  @Provides
  fun appInitializationUseCase(appInitializationUseCaseImpl: AppInitializationUseCaseImpl): AppInitializationUseCase =
    appInitializationUseCaseImpl

  @Provides
  fun businessUseCase(businessUseCaseImpl: BusinessUseCaseImpl): BusinessUseCase =
    businessUseCaseImpl
}