package com.yelpbusiness.android.di.module.android

import com.yelpbusiness.android.modules.business.BusinessDetailsFragment
import com.yelpbusiness.android.modules.home.HomeFragment
import com.yelpbusiness.android.modules.search.SearchInputFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * declare fragment that used dagger
 */
@Module
abstract class FragmentComponentModule {

  @ContributesAndroidInjector
  abstract fun homeFragment(): HomeFragment

  @ContributesAndroidInjector
  abstract fun searchInputFragment(): SearchInputFragment

  @ContributesAndroidInjector
  abstract fun businessDetailsFragment(): BusinessDetailsFragment

}