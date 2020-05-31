package com.yelpbusiness.common_android.base.mvi

typealias Reducer<S, C> = (state: S, change: C) -> S