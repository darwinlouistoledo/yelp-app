package com.yelpbusiness.data.remote.response

import com.google.gson.annotations.SerializedName
import com.yelpbusiness.data.remote.model.BusinessRepo

data class BusinessListResponse(
  val total: Int = 0,
  @SerializedName("businesses") val data: List<BusinessRepo> = listOf()
) 