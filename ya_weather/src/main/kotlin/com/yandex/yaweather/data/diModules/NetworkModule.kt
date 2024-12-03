package com.yandex.yaweather.data.diModules

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.yandex.yaweather.data.network.CityApi
import com.yandex.yaweather.data.network.WeatherApi
import dagger.Module
import dagger.Provides
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@Module
class NetworkProvider {
  private val json = Json { ignoreUnknownKeys = true }

  @Provides
  fun providesOkHttpClient(context: Context): OkHttpClient =
    OkHttpClient.Builder().addInterceptor(ChuckerInterceptor.Builder(context).build()).build()

  @WeatherRetrofitQualifier
  @Provides
  fun providesWeatherRetrofit(okHttpClient: OkHttpClient): Retrofit =
    Retrofit.Builder()
      .baseUrl("https://api.openweathermap.org/")
      .client(okHttpClient)
      .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
      .build()

  @Provides
  fun providesWeatherApi(@WeatherRetrofitQualifier retrofit: Retrofit): WeatherApi =
    retrofit.create(WeatherApi::class.java)

  @CityRetrofitQualifier
  @Provides
  fun providesCityRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
    .baseUrl("https://htmlweb.ru/")
    .client(okHttpClient)
    .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
    .build()

  @Provides
  fun providerCityApi(@CityRetrofitQualifier retrofit: Retrofit): CityApi = retrofit.create(CityApi::class.java)
}