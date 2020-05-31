package com.yelpbusiness.android.di.module.android

import com.yelpbusiness.android.di.component.ActivitySubComponent
import com.yelpbusiness.android.modules.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * declare activity that used dagger
 */
@Module(
  subcomponents = [
    ActivitySubComponent::class
  ]
)
abstract class ActivityComponentModule {

  @ContributesAndroidInjector
  abstract fun mainActivity(): MainActivity

}