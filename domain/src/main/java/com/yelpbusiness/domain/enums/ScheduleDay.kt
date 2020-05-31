package com.yelpbusiness.domain.enums

enum class ScheduleDay{

  MONDAY{
    override val display = "Monday"
    override val value = 0
  },
  TUESDAY{
    override val display = "Tuesday"
    override val value = 1
  },
  WEDNESDAY{
    override val display = "Wednesday"
    override val value = 2
  },
  THURSDAY{
    override val display = "Thursday"
    override val value = 3
  },
  FRIDAY{
    override val display = "Friday"
    override val value = 4
  },
  SATURDAY{
    override val display = "Saturday"
    override val value = 5
  },
  SUNDAY{
    override val display = "Sunday"
    override val value = 6
  };

  abstract val display: String
  abstract val value: Int
}