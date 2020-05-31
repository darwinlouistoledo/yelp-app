package com.yelpbusiness.domain.usecase

interface AppInitializationUseCase {

  fun init()

  fun onAppCreated()

  fun onAppDestroyed()

}