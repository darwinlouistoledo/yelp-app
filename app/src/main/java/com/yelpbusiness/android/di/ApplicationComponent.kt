package com.yelpbusiness.android.di

import com.yelpbusiness.android.YelpApplication
import com.yelpbusiness.android.di.module.AndroidComponentModule
import com.yelpbusiness.android.di.module.AppAssistedInjectedModule
import com.yelpbusiness.android.di.module.ApplicationModule
import com.yelpbusiness.android.di.module.ManagerModule
import com.yelpbusiness.android.di.module.ViewModelModule
import com.yelpbusiness.data.di.DataModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
  modules = [
    AndroidSupportInjectionModule::class,
    AppAssistedInjectedModule::class,
    ApplicationModule::class,
    ViewModelModule::class,
    AndroidComponentModule::class,
    ManagerModule::class,
    DataModule::class
  ]
)
interface ApplicationComponent : AndroidInjector<YelpApplication> {

  @Component.Factory
  interface Factory : AndroidInjector.Factory<YelpApplication>

}