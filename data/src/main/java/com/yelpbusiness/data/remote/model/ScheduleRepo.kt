package com.yelpbusiness.data.remote.model

import com.google.gson.annotations.SerializedName
import com.yelpbusiness.domain.model.Schedule

data class ScheduleRepo(
  @SerializedName("is_overnight") val isOvernight: Boolean,
  @SerializedName("start") val startTime: String,
  @SerializedName("end") val endTime: String,
  val day: Int
) {
  constructor(item: Schedule) : this(
    isOvernight = item.isOvernight,
    startTime = item.startTime,
    endTime = item.endTime,
    day = item.day
  )

  val toSchedule: Schedule
    get() = Schedule(
      isOvernight = isOvernight,
      startTime = startTime,
      endTime = endTime,
      day = day
    )
}