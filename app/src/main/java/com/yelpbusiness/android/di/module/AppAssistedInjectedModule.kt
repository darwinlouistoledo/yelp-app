package com.yelpbusiness.android.di.module

import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Module

@Module(includes = [AssistedInject_AppAssistedInjectedModule::class])
@AssistedModule
interface AppAssistedInjectedModule