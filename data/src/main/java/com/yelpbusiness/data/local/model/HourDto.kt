package com.yelpbusiness.data.local.model

import com.yelpbusiness.domain.exceptions.RealmCascadeException
import com.yelpbusiness.domain.model.Hour
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import java.util.UUID

@RealmClass
open class HourDto() : RealmObject() {
  @PrimaryKey var reference: String = ""
  var businessId: String = ""
  var schedule: RealmList<ScheduleDto> = RealmList()
  var hoursType: String? = ""
  var isOpenNow: Boolean? = false

  constructor(
    item: Hour,
    bId: String,
    ref: String
  ) : this() {
    reference = ref
    businessId = bId
    schedule = RealmList(
      *item.schedule.map {
        ScheduleDto(
          it, bId, UUID.randomUUID()
          .toString()
        )
      }
        .toTypedArray()
    )
    hoursType = item.hoursType
    isOpenNow = item.isOpenNow
  }

  val toHour: Hour
    get() = Hour(
      schedule = schedule.map { it.toSchedule },
      hoursType = hoursType ?: "",
      isOpenNow = isOpenNow ?: false
    )
}