package com.yelpbusiness.android.view.adapter

import com.yelpbusiness.android.R
import com.yelpbusiness.android.databinding.ViewholderItemScheduleBinding
import com.yelpbusiness.android.view.diffcallback.ScheduleAdapterDiffCallback
import com.yelpbusiness.common_android.base.SimpleListAdapter
import com.yelpbusiness.domain.enums.ScheduleDay
import com.yelpbusiness.domain.model.Schedule

class ScheduleListItemAdapter :
  SimpleListAdapter<ViewholderItemScheduleBinding, Schedule>(
    R.layout.viewholder_item_schedule,
    ScheduleAdapterDiffCallback
  ) {

  override fun bind(
    holder: ViewHolder<ViewholderItemScheduleBinding>,
    item: Schedule,
    position: Int
  ) {
    val context = holder.itemView.context

    holder.binding.apply {
      tvDay.text = ScheduleDay.values().toList().first { it.value == item.day }.display

      val overnight = when(item.isOvernight){
        true -> context.getString(R.string.lbl_yes)
        else -> context.getString(R.string.lbl_no)
      }

      tvOvernight.text = context.getString(R.string.lbl_open_overnight, overnight)
      tvStartTime.text = context.getString(R.string.lbl_start_time, "${item.startTime} Hr")
      tvEndTime.text = context.getString(R.string.lbl_end_time, "${item.endTime} Hr")

    }
  }
}