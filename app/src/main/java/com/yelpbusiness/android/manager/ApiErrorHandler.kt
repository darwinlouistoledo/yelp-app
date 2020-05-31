package com.yelpbusiness.android.manager

import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.yelpbusiness.common_android.ext.component.showToast
import com.yelpbusiness.data.remote.response.ErrorResponse
import com.yelpbusiness.domain.manager.ErrorHandler
import retrofit2.HttpException
import java.io.IOException

@Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
class ApiErrorHandler(private val gson: Gson) : ErrorHandler<Fragment> {

  override fun canHandle(error: Throwable): Boolean =
    error is HttpException && error.code() in 400..499 && error.code() != 401

  override fun handle(
    fragment: Fragment,
    error: Throwable
  ) {
    val httpException = error as HttpException
    val response = httpException.response()
    try {
      val responseBody = response?.errorBody()
        ?.string()
      val simpleApiResponse = gson.fromJson(responseBody, ErrorResponse::class.java)
      fragment.requireContext()
        .showToast(simpleApiResponse.message ?: "An API Error has occurred")
    } catch (e: IOException) {
      e.printStackTrace()
    } catch (e: JsonSyntaxException) {
      e.printStackTrace()
    }
  }

}