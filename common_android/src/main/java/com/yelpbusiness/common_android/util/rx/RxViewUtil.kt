package com.yelpbusiness.common_android.util.rx

import android.view.View
import android.widget.EditText
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import java.util.concurrent.TimeUnit.MILLISECONDS

object RxViewUtil {

  fun click(
    view: View
  ): Observable<Any> = RxView.clicks(view)
      .debounce(200, MILLISECONDS)

  fun textChange(
    view: EditText
  ): Observable<String> = RxTextView.textChanges(view)
      .debounce(1000, MILLISECONDS)
      .map {
        if (it.isEmpty()) {
          return@map ""
        }

        return@map it.toString()
      }

}