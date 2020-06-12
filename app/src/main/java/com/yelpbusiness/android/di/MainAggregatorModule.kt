package com.yelpbusiness.android.di

import com.yelpbusiness.android.di.module.ApplicationModule
import com.yelpbusiness.android.di.module.ManagerModule
import com.yelpbusiness.android.di.module.ViewModelModule
import com.yelpbusiness.data.di.DataModule
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@InstallIn(ApplicationComponent::class)
@Module(
    includes = [
      ApplicationModule::class,
      ManagerModule::class,
      DataModule::class,
      ViewModelModule::class
    ]
)
interface MainAggregatorModule