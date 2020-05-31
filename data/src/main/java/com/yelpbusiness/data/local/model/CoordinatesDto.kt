package com.yelpbusiness.data.local.model

import com.yelpbusiness.domain.model.Coordinates
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class CoordinatesDto() : RealmObject() {
  @PrimaryKey var reference: String = ""
  var businessId: String = ""
  var latitude: Float = 0f
  var longitude: Float = 0f

  constructor(item: Coordinates, bId: String, ref: String) : this(){
    reference = ref
    businessId = bId
    latitude = item.latitude
    longitude = item.longitude
  }

  val toCoordinates: Coordinates
    get() = Coordinates(
      latitude = latitude,
      longitude = longitude
    )
}