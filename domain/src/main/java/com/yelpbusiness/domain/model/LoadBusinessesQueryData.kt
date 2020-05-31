package com.yelpbusiness.domain.model

data class LoadBusinessesQueryData(
  val term: String? = null,
  val location: String? = null,
  val categories: String? = null,
  val lat: Float?=null,
  val lon: Float?=null,
  val sort: String? = null
)