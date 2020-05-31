package com.yelpbusiness.android.di.component

import com.yelpbusiness.common_android.di.scopes.PerActivity
import dagger.Subcomponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerAppCompatActivity

@PerActivity
@Subcomponent(
  modules = [
  ]
)
interface ActivitySubComponent : AndroidInjector<DaggerAppCompatActivity> {

  @Subcomponent.Factory
  interface Factory : AndroidInjector.Factory<DaggerAppCompatActivity>

}