package com.yelpbusiness.common_android.base.mvi_coroutines

typealias Reducer<S, C> = (state: S, change: C) -> S