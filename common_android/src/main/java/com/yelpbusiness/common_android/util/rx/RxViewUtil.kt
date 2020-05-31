package com.yelpbusiness.common_android.util.rx

import android.view.View
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import java.util.concurrent.TimeUnit.MILLISECONDS

object RxViewUtil {

  fun click(
    view: View
  ): Observable<Any> = RxView.clicks(view)
      .debounce(200, MILLISECONDS)

}