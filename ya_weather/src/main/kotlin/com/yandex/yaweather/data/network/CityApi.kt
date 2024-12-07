package com.yandex.yaweather.data.network

import retrofit2.http.GET
import retrofit2.http.Query

interface CityApi {
  @GET("geo/api.php")
  suspend fun getCityList(
    @Query("city_name") cityName: String,
    @Query("json") json: String =  "",
    @Query("limit") limit: Int = 4,
    @Query("api_key") apiKey: String = CITY_API_KEY
  ) : CityResponse
  companion object {
    private  const val CITY_API_KEY = "7083ec4aed7944afcc3d3ecb09cb53ac"
  }
}