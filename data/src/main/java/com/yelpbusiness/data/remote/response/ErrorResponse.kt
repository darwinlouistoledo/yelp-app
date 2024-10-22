package com.yelpbusiness.data.remote.response

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator

data class ErrorResponse(
  val code: Int? = null,
  val type: String? = null,
  val message: String? = null
) : Parcelable {
  constructor(parcel: Parcel) : this(
    parcel.readValue(Int::class.java.classLoader) as? Int,
    parcel.readString(),
    parcel.readString()
  ) {
  }

  override fun writeToParcel(parcel: Parcel, flags: Int) {
    parcel.writeValue(code)
    parcel.writeString(type)
    parcel.writeString(message)
  }

  override fun describeContents(): Int {
    return 0
  }

  companion object CREATOR : Creator<ErrorResponse> {
    override fun createFromParcel(parcel: Parcel): ErrorResponse {
      return ErrorResponse(parcel)
    }

    override fun newArray(size: Int): Array<ErrorResponse?> {
      return arrayOfNulls(size)
    }
  }
}