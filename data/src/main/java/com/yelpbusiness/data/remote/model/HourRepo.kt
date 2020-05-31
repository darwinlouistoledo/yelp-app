package com.yelpbusiness.data.remote.model

import com.google.gson.annotations.SerializedName
import com.yelpbusiness.domain.model.Hour

data class HourRepo(
  @SerializedName("open") val schedule: List<ScheduleRepo>,
  @SerializedName("hours_type") val hoursType: String,
  @SerializedName("is_open_now") val isOpenNow: Boolean
) {
  constructor(item: Hour) : this(
    schedule = item.schedule.map { ScheduleRepo(it) },
    hoursType = item.hoursType,
    isOpenNow = item.isOpenNow
  )

  val toHour: Hour
    get() = Hour(
      schedule = schedule.map { it.toSchedule },
      hoursType = hoursType,
      isOpenNow = isOpenNow
    )
}