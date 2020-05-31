package com.yelpbusiness.data.local.model

import com.yelpbusiness.domain.model.Schedule
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class ScheduleDto() : RealmObject() {
  @PrimaryKey var reference: String = ""
  var businessId: String = ""
  var isOvernight: Boolean = false
  var startTime: String = ""
  var endTime: String = ""
  var day: Int = -1

  constructor(item: Schedule, bId: String, ref: String) : this() {
    reference = ref
    businessId = bId
    isOvernight = item.isOvernight
    startTime = item.startTime
    endTime = item.endTime
    day = item.day
  }

  val toSchedule: Schedule
    get() = Schedule(
      isOvernight = isOvernight,
      startTime = startTime,
      endTime = endTime,
      day = day
    )
}