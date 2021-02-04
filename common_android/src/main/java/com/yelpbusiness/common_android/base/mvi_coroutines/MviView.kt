package com.yelpbusiness.common_android.base.mvi_coroutines

interface MviView<S: MviState>{
  fun render(state: S)
}