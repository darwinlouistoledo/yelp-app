package com.yelpbusiness.domain.sealedclass

sealed class LoadBusinessesQuery {

  data class Filters(val values: Triple<String?,String?,String?>): LoadBusinessesQuery()

  data class LatLon(val lat: Float, val lon: Float): LoadBusinessesQuery()

  data class Sort(val value: String?): LoadBusinessesQuery()
}