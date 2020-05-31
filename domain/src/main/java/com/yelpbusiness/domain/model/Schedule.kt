package com.yelpbusiness.domain.model

data class Schedule(
  val isOvernight: Boolean,
  val startTime: String,
  val endTime: String,
  val day: Int
)