package com.yelpbusiness.data.remote.services

import com.yelpbusiness.data.remote.model.BusinessRepo
import com.yelpbusiness.data.remote.response.BusinessListResponse
import io.reactivex.Single
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BusinessesApiService {

  @GET("v3/businesses/search")
  fun getBusinesses(
    @Query("term") term: String? = null,
    @Query("location") location: String? = null,
    @Query("categories") categories: String? = null,
    @Query("latitude") latitude: Float? = null,
    @Query("longitude") longitude: Float? = null,
    @Query("limit") limit: Int? = 50,
    @Query("sort_by") sortBy: String? = null
  ): Single<BusinessListResponse>

  @GET("v3/businesses/{id}")
  fun getBusiness(
    @Path("id") id: String
  ): Single<BusinessRepo>

  @GET("v3/businesses/{id}")
  fun getBusiness2(
    @Path("id") id: String
  ): Deferred<BusinessRepo>

}