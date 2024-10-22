package com.yelpbusiness.common_android.util.location

import android.Manifest.permission
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.provider.Settings
import androidx.annotation.ColorInt
import androidx.fragment.app.FragmentActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationSettingsRequest
import com.tbruyelle.rxpermissions2.Permission
import com.tbruyelle.rxpermissions2.RxPermissions
import com.yelpbusiness.common_android.sealedclass.LocationStatus
import com.yelpbusiness.domain.rx.complete
import com.yelpbusiness.domain.rx.next
import io.reactivex.Observable
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider
import rx_activity_result2.RxActivityResult
import timber.log.Timber

class LocationManagerImpl(
  @ColorInt private val positiveColor: Int,
  @ColorInt private val negativeColor: Int
) : LocationManager {

  override fun getLocation(
    context: FragmentActivity
  ): Observable<LocationStatus> = RxPermissions(context).requestEach(
    permission.ACCESS_FINE_LOCATION,
    permission.ACCESS_COARSE_LOCATION
  )
    .switchMap { permission ->
      when {
        permission.granted -> getLocationSetting(context)
        else -> showLocationRationale(context, permission)
      }
    }
    .switchMap { result ->
      checkLocationSettings(context, result)
    }
    .flatMap { result ->
      when {
        result is LocationStatus.ShouldOpenSettings && result.shouldOpenSettings ->
          RxActivityResult.on(context)
            .startIntent(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            .switchMap {
              Timber.i("RxActivityResult -> ${it.resultCode()}")
              when (it.resultCode() == Activity.RESULT_CANCELED) {
                true -> getLocationSetting(context)
                  .switchMap { result ->
                    checkLocationSettings(context, result)
                  }
                else -> Observable.just(LocationStatus.IsLocationOn(false))
              }
            }
        result is LocationStatus.ShouldOpenSettings && !result.shouldOpenSettings -> {
          Observable.just(LocationStatus.PermissionDenied)
        }
        else -> Observable.just(result)
      }
    }

  private fun showLocationSettingDialog(
    context: Activity
  ): Observable<LocationStatus> =
    Observable.create { emitter ->
      MaterialDialog(context).show {
        title(text = "Location not found.")
        message(
          text = "Please turn on Location Services. For best experience, please set it to High Accuracy."
        )
        cancelOnTouchOutside(false)
        cancelable(false)
        negativeButton(text = "Cancel") {
          emitter.onNext(LocationStatus.ShouldOpenSettings(false))
          emitter.complete()
          it.dismiss()
        }
        positiveButton(text = "Okay") {
          emitter.onNext(LocationStatus.ShouldOpenSettings(true))
          emitter.complete()
          it.dismiss()
        }
      }
    }

  private fun showLocationRationale(
    context: Activity,
    permission: Permission
  ): Observable<LocationStatus> = Observable.create { emitter ->
    MaterialDialog(context).show {
      title(text = "Location Permission.")
      message(text = "We need to access your location to continue using this application.")
      cancelOnTouchOutside(false)
      cancelable(false)
      negativeButton(text = "Don't Allow") {
        emitter.next(LocationStatus.PermissionDenied)
        context.finish()
        it.dismiss()
      }
      positiveButton(text = "Okay") {
        when (permission.shouldShowRequestPermissionRationale) {
          true -> {
            //Ask Permission
            emitter.onNext(LocationStatus.AskForPermission)
            emitter.complete()
          }
          false -> {
            //Open settings for the user to allow manually
            emitter.onNext(LocationStatus.OpenAppInfo)
            emitter.complete()
          }
        }
        it.dismiss()
      }
    }
  }

  @SuppressLint("MissingPermission")
  private fun getCurrentLocation(rxLocationProvider: ReactiveLocationProvider): Observable<LocationStatus> =
    rxLocationProvider.getUpdatedLocation(
      LocationRequest()
        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        .setInterval(1000)
    )
      .take(1)
      .distinctUntilChanged()
      .switchMap {
        Timber.d("Current Location $it")
        Observable.just(LocationStatus.CurrentLocation(it))
      }

  private fun getLocationSetting(context: Activity) = ReactiveLocationProvider(context)
    .checkLocationSettings(
      LocationSettingsRequest.Builder()
        .build()
    )
    .switchMap { result ->
      when (result.locationSettingsStates.isLocationUsable) {
        true -> Observable.just(LocationStatus.IsLocationOn(true))
        else -> Observable.just(LocationStatus.IsLocationOn(false))
      }
    }

  private fun checkLocationSettings(
    context: FragmentActivity,
    given: LocationStatus
  ): Observable<LocationStatus> {
    return when {
      //Location is Usable
      given is LocationStatus.IsLocationOn && given.isLocationOn -> {
        Timber.i("getCurrentLocation -> ${given.isLocationOn}")
        getCurrentLocation(ReactiveLocationProvider(context))
      }
      //Location is Off
      given is LocationStatus.IsLocationOn && !given.isLocationOn -> {
        //Open Settings Prompt
        showLocationSettingDialog(context)
      }
      //Current Location
      else -> Observable.just(given)
    }
  }
}