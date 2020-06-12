package com.yelpbusiness.android.di.module

import android.app.Application
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.yelpbusiness.android.BuildConfig
import com.yelpbusiness.common_android.di.qualifiers.ApiEndpoint
import com.yelpbusiness.common_android.di.qualifiers.ApiEndpointBearer
import com.yelpbusiness.common_android.di.qualifiers.AppHttpInterceptor
import com.yelpbusiness.common_android.di.qualifiers.AppHttpLoggingInterceptor
import com.yelpbusiness.common_android.di.qualifiers.IsDebug
import com.yelpbusiness.data.remote.interceptor.HttpInterceptor
import com.yelpbusiness.domain.rx.SchedulerProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class ApplicationModule {

  @Provides
  @Singleton
  @ApiEndpoint
  fun provideApiEndpoint(): String = BuildConfig.WEB_SERVICES_DOMAIN

  @Provides
  @Singleton
  @ApiEndpointBearer
  fun provideApiBearerToken(): String = BuildConfig.WEB_SERVICES_API_KEY

  @Provides
  @Singleton
  @IsDebug
  fun provideIsDebug(): Boolean = true

  @Provides
  @Singleton
  fun provideGson(): Gson = GsonBuilder()
      .setPrettyPrinting()
      .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
      .create()

  @Provides
  @Singleton
  @AppHttpInterceptor
  fun providesHttpInterceptor(
    @ApiEndpointBearer bearer: String
  ): Interceptor =
    HttpInterceptor(bearer)

  @Provides
  @Singleton
  @AppHttpLoggingInterceptor
  fun providesHttpLoggingInterceptor(): Interceptor =
    HttpLoggingInterceptor().apply {
      level = HttpLoggingInterceptor.Level.BODY
    }

  @Provides
  @Singleton
  fun provideHttpCache(application: Application): Cache {
    val cacheSize = 10 * 1024 * 1024
    return Cache(application.cacheDir, cacheSize.toLong())
  }

  @Provides
  @Singleton
  fun provideOkhttpClient(
    cache: Cache,
    @AppHttpLoggingInterceptor httpLoggingInterceptor: Interceptor,
    @AppHttpInterceptor httpInterceptor: Interceptor,
    @IsDebug debugMode: Boolean
  ): OkHttpClient = OkHttpClient.Builder()
      .cache(cache)
      .apply {
        if (debugMode) {
          addNetworkInterceptor(httpLoggingInterceptor)
        }
      }
      .addInterceptor(httpInterceptor)
      .readTimeout(60, TimeUnit.SECONDS)
      .connectTimeout(60, TimeUnit.SECONDS)
      .writeTimeout(60, TimeUnit.SECONDS)
      .retryOnConnectionFailure(true)
      .build()

  @Provides
  @Singleton
  fun provideGsonConverterFactory(gson: Gson): GsonConverterFactory =
    GsonConverterFactory.create(gson)

  @Provides
  @Singleton
  fun provideRxJava2CallAdapterFactory(
    schedulerProvider: SchedulerProvider
  ): RxJava2CallAdapterFactory =
    RxJava2CallAdapterFactory.createWithScheduler(schedulerProvider.io())

  @Provides
  @Singleton
  fun provideMainApiRetrofit(
    gsonFactory: GsonConverterFactory,
    rxFactory: RxJava2CallAdapterFactory,
    okHttpClient: OkHttpClient,
    @ApiEndpoint endpoint: String
  ): Retrofit = Retrofit.Builder()
      .addConverterFactory(gsonFactory)
      .addCallAdapterFactory(rxFactory)
      .baseUrl(endpoint)
      .client(okHttpClient)
      .build()

}