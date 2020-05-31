package com.yelpbusiness.domain.keys

import com.yelpbusiness.domain.enums.BusinessSort

data class BusinessListKey(
  val term: String? = null,
  val location: String? = null,
  val categories: String? = null,
  val lat: Float? = null,
  val lon: Float? = null,
  val sortBy: BusinessSort? = null
)