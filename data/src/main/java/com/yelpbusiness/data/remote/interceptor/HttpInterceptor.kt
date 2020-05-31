package com.yelpbusiness.data.remote.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class HttpInterceptor @Inject constructor(private val bearerToken: String) :
    Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        /**
         * Add header Authorization
         */
        val newRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $bearerToken")
            .build()

        return chain.proceed(newRequest)
    }

}