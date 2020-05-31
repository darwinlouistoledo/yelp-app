package com.yelpbusiness.domain.model

data class Hour(
  val schedule: List<Schedule>,
  val hoursType: String,
  val isOpenNow: Boolean
)