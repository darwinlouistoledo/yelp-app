package com.yelpbusiness.data.local.model

import com.yelpbusiness.domain.model.Location
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class LocationDto() : RealmObject(){
  @PrimaryKey var reference: String = ""
  var businessId: String = ""
  var address1: String? = null
  var address2: String? = null
  var address3: String? = null
  var city: String? = null
  var zipCode: String? = null
  var country: String? = null
  var state: String? = null
  var displayAddress: RealmList<String> = RealmList()

  constructor(item: Location, bId: String, ref: String) : this(){
    reference = ref
    businessId = bId
    address1 = item.address1
    address2 = item.address2
    address3 = item.address3
    city = item.city
    zipCode = item.zipCode
    country = item.country
    state = item.state
    displayAddress = RealmList(*item.displayAddress.toTypedArray())
  }

  val toLocation: Location
    get() = Location(
      address1 = address1,
      address2 = address2,
      address3 = address3,
      city = city,
      zipCode = zipCode,
      country = country,
      state = state,
      displayAddress = displayAddress.toList()
    )
}