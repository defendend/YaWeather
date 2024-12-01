package com.yandex.yaweather.data.network

import data.network.CoordinatesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
  @GET("data/2.5/weather")
  suspend fun current(
    @Query("lat") lat: String,
    @Query("lon") lon: String,
    @Query("appid") appid: String
  ): CoordinatesResponse
}