package com.yelpbusiness.common_android.util.location

import androidx.fragment.app.FragmentActivity
import com.yelpbusiness.common_android.sealedclass.LocationStatus
import io.reactivex.Observable

interface LocationManager {

  fun getLocation(
    context: FragmentActivity
  ): Observable<LocationStatus>

}