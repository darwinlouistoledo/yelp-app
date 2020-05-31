package com.yelpbusiness.android.view.diffcallback

import androidx.recyclerview.widget.DiffUtil
import com.yelpbusiness.domain.model.Schedule

object ScheduleAdapterDiffCallback : DiffUtil.ItemCallback<Schedule>() {

  override fun areItemsTheSame(
    oldItem: Schedule,
    newItem: Schedule
  ): Boolean = oldItem.day == newItem.day &&
    oldItem.startTime == newItem.startTime &&
    oldItem.endTime == newItem.endTime &&
    oldItem.isOvernight == newItem.isOvernight

  override fun areContentsTheSame(
    oldItem: Schedule,
    newItem: Schedule
  ): Boolean = oldItem == newItem
}