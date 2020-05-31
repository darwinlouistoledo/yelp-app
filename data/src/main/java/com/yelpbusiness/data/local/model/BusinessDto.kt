package com.yelpbusiness.data.local.model

import com.yelpbusiness.domain.exceptions.RealmCascadeException
import com.yelpbusiness.domain.model.Business
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import java.util.UUID

@RealmClass
open class BusinessDto() : RealmObject() {
  @PrimaryKey var id: String = ""
  var name: String = ""
  var alias: String? = null
  var isClaimed: Boolean? = null
  var isClosed: Boolean? = null
  var url: String? = null
  var phone: String? = null
  var displayPhone: String? = null
  var reviewCount: Int? = null
  var rating: Float? = null
  var price: String? = null
  var distance: Float? = null
  var photos: RealmList<String> = RealmList()
  var primaryImgUrl: String? = null
  var categories: RealmList<CategoryDto> = RealmList()
  var location: LocationDto? = null
  var coordinates: CoordinatesDto? = null
  var transactions: RealmList<String> = RealmList()
  var hours: RealmList<HourDto> = RealmList()

  constructor(item: Business) : this() {
    id = item.id
    name = item.name
    alias = item.alias
    isClaimed = item.isClaimed
    isClosed = item.isClosed
    url = item.url
    phone = item.phone
    displayPhone = item.displayPhone
    reviewCount = item.reviewCount
    rating = item.rating
    price = item.price
    distance = item.distance
    photos = RealmList(*item.photos.toTypedArray())
    primaryImgUrl = item.primaryImgUrl
    location = LocationDto(
      item.location, item.id, UUID.randomUUID()
      .toString()
    )
    coordinates = CoordinatesDto(
      item.coordinates, item.id, UUID.randomUUID()
      .toString()
    )
    transactions = RealmList(*item.transactions.toTypedArray())
    categories = RealmList(
      *item.categories.map {
        CategoryDto(
          it,
          item.id,
          UUID.randomUUID()
            .toString()
        )
      }
        .toTypedArray()
    )
    hours = RealmList(*item.hours.map {
      HourDto(
        it,
        item.id,
        UUID.randomUUID()
          .toString()
      )
    }
      .toTypedArray()
    )
  }

  val toBusiness: Business
    get() = Business(
      id = id,
      name = name,
      alias = alias,
      isClaimed = isClaimed,
      isClosed = isClosed,
      url = url,
      phone = phone,
      displayPhone = displayPhone,
      reviewCount = reviewCount,
      rating = rating,
      price = price,
      distance = distance ?: 0f,
      photos = photos.toList(),
      primaryImgUrl = primaryImgUrl,
      transactions = transactions.toList(),
      coordinates = coordinates?.toCoordinates ?: throw RealmCascadeException(),
      location = location?.toLocation ?: throw RealmCascadeException(),
      categories = categories.map { it.toCategory },
      hours = hours.map { it.toHour }
    )
}