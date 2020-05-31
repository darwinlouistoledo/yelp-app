package com.yelpbusiness.android.di.module

import com.yelpbusiness.android.di.module.android.ActivityComponentModule
import com.yelpbusiness.android.di.module.android.FragmentComponentModule
import dagger.Module

@Module(
  includes = [
    ActivityComponentModule::class,
    FragmentComponentModule::class
  ]
)
abstract class AndroidComponentModule