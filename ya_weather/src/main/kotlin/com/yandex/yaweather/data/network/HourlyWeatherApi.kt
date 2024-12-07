package com.yandex.yaweather.data.network

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface HourlyWeatherApi {
  @GET("forecast/hourly")
  suspend fun getHourlyWeather(
    @Query("key") key: String = HOURLY_API_KEY,
    @Query("lang") lang: String = "ru",
    @Query("units") units: String = "S",
    @Query("hours") hours: Int = 24,
    @Query("lat") lat: Double,
    @Query("lon") lon: Double
  ) : HourlyWeatherResponse
  companion object{
    private const val HOURLY_API_KEY = "96db97e1f672441db4fcdcf18117a99f"
  }
}