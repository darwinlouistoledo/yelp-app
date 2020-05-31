package com.yelpbusiness.data.remote.model

import com.yelpbusiness.domain.model.Coordinates

data class CoordinatesRepo(
  val latitude: Float,
  val longitude: Float
) {
  constructor(item: Coordinates) : this(
    latitude = item.latitude,
    longitude = item.longitude
  )

  val toCoordinates: Coordinates
    get() = Coordinates(
      latitude = latitude,
      longitude = longitude
    )
}