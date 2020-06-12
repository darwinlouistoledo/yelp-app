package com.yelpbusiness.android.di.module

import androidx.lifecycle.ViewModel
import com.yelpbusiness.android.di.mapkey.ViewModelKey
import com.yelpbusiness.android.modules.business.BusinessDetailsViewModel
import com.yelpbusiness.android.modules.home.HomeViewModel
import com.yelpbusiness.android.modules.map.BusinessesMapViewModel
import com.yelpbusiness.android.modules.search.SearchInputManagerViewModel
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.FragmentComponent
import dagger.multibindings.IntoMap

@Module
@InstallIn(ActivityComponent::class, FragmentComponent::class)
abstract class ViewModelModule {

  @Binds
  @IntoMap
  @ViewModelKey(HomeViewModel::class)
  abstract fun homeViewModel(homeViewModel: HomeViewModel): ViewModel

  @Binds
  @IntoMap
  @ViewModelKey(SearchInputManagerViewModel::class)
  abstract fun searchInputManagerViewModel(searchInputManagerViewModel: SearchInputManagerViewModel): ViewModel

  @Binds
  @IntoMap
  @ViewModelKey(BusinessDetailsViewModel::class)
  abstract fun businessDetailsViewModel(businessDetailsViewModel: BusinessDetailsViewModel): ViewModel

  @Binds
  @IntoMap
  @ViewModelKey(BusinessesMapViewModel::class)
  abstract fun businessesMapViewModel(businessesMapViewModel: BusinessesMapViewModel): ViewModel

}