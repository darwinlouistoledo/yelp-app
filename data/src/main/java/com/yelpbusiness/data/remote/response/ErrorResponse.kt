package com.yelpbusiness.data.remote.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ErrorResponse(
  val code: Int? = null,
  val type: String? = null,
  val message: String? = null
) : Parcelable