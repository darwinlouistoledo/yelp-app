package com.yelpbusiness.data.remote.model

import com.google.gson.annotations.SerializedName
import com.yelpbusiness.domain.model.Business

data class BusinessRepo(
  val id: String,
  val name: String,
  val alias: String?,
  @SerializedName("is_claimed") val isClaimed: Boolean?,
  @SerializedName("is_closed") val isClosed: Boolean?,
  val url: String?,
  val phone: String?,
  @SerializedName("display_phone") val displayPhone: String?,
  @SerializedName("review_count") val reviewCount: Int?,
  val rating: Float?,
  val price: String?,
  val distance: Float?,
  val photos: List<String>?,
  @SerializedName("image_url") val primaryImgUrl: String?,
  val categories: List<CategoryRepo>?,
  val location: LocationRepo,
  val coordinates: CoordinatesRepo,
  val transactions: List<String>?,
  val hours: List<HourRepo>?
) {
  constructor(item: Business) : this(
    id = item.id,
    name = item.name,
    alias = item.alias,
    isClaimed = item.isClaimed,
    isClosed = item.isClosed,
    url = item.url,
    phone = item.phone,
    displayPhone = item.displayPhone,
    reviewCount = item.reviewCount,
    rating = item.rating,
    price = item.price,
    distance = item.distance,
    photos = item.photos,
    primaryImgUrl = item.primaryImgUrl,
    categories = item.categories.map { CategoryRepo(it) },
    location = LocationRepo(item.location),
    coordinates = CoordinatesRepo(item.coordinates),
    transactions = item.transactions,
    hours = item.hours.map { HourRepo(it) }
  )

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
      photos = photos ?: emptyList(),
      primaryImgUrl = primaryImgUrl,
      coordinates = coordinates.toCoordinates,
      location = location.toLocation,
      categories = categories?.map { it.toCategory } ?: emptyList(),
      transactions = transactions ?: emptyList(),
      hours = hours?.map { it.toHour } ?: emptyList()
    )
}