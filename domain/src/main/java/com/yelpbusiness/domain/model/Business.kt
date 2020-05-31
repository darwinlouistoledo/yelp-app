package com.yelpbusiness.domain.model

data class Business(
  val id: String,
  val name: String,
  val alias: String?,
  val isClaimed: Boolean?,
  val isClosed: Boolean?,
  val url: String?,
  val phone: String?,
  val displayPhone: String?,
  val reviewCount: Int?,
  val rating: Float?,
  val price: String?,
  val distance: Float = 0f,
  val photos: List<String> = emptyList(),
  val primaryImgUrl: String?,
  val coordinates: Coordinates,
  val location: Location,
  val categories: List<Category> = emptyList(),
  val transactions: List<String> = emptyList(),
  val hours: List<Hour> = emptyList()
) {

  val addressToDisplay = location.displayAddress.joinToString(", ")

  val roundedDistance = Math.round(distance * 100.0) / 100.0
  val distanceToDisplay = "$roundedDistance Meters away"

  val reviewCountDisplay = reviewCount?:0
}