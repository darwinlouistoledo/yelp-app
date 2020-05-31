package com.yelpbusiness.data.remote.model

import com.google.gson.annotations.SerializedName
import com.yelpbusiness.domain.model.Location

data class LocationRepo(
  val address1: String? = null,
  val address2: String? = null,
  val address3: String? = null,
  val city: String? = null,
  @SerializedName("zip_code") val zipCode: String? = null,
  val country: String? = null,
  val state: String? = null,
  @SerializedName("display_address") val displayAddress: List<String> = emptyList()
) {
  constructor(item: Location) : this(
    address1 = item.address1,
    address2 = item.address2,
    address3 = item.address3,
    city = item.city,
    zipCode = item.zipCode,
    country = item.country,
    state = item.state,
    displayAddress = item.displayAddress
  )

  val toLocation: Location
    get() = Location(
      address1 = address1,
      address2 = address2,
      address3 = address3,
      city = city,
      zipCode = zipCode,
      country = country,
      state = state,
      displayAddress = displayAddress
    )
}