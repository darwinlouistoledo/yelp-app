package com.yelpbusiness.domain.sealedclass

sealed class DataResult<out T> {

  data class Success<T>(val value: T) : DataResult<T>()

  data class Failed(val error: Throwable) : DataResult<Nothing>()

}