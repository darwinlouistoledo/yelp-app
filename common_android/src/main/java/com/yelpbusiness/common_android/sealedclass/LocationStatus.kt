package com.yelpbusiness.common_android.sealedclass

import android.location.Location

sealed class LocationStatus {

  object PermissionDenied: LocationStatus()

  object OpenAppInfo: LocationStatus()

  object AskForPermission : LocationStatus()

  data class ShouldOpenSettings(val shouldOpenSettings: Boolean): LocationStatus()

  data class IsLocationOn(val isLocationOn: Boolean): LocationStatus()

  data class CurrentLocation(val location: Location) : LocationStatus()


}