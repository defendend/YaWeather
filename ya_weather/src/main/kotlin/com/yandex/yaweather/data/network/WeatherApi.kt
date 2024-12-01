package com.yandex.yaweather.data.network

import data.network.CoordinatesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
  @GET("data/2.5/weather")
  suspend fun current(
    @Query("lat") lat: String,
    @Query("lon") lon: String,
    @Query("appid") appid: String = "7083ec4aed7944afcc3d3ecb09cb53ac"
  ): CoordinatesResponse

  @GET("data/2.5/forecast")
  suspend fun forecast(
    @Query("lat") lat: String,
    @Query("lon") lon: String,
    @Query("appid") appid: String
  ): ForecastResponse
}