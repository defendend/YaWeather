package com.yandex.yaweather.data.network

import retrofit2.http.GET
import retrofit2.http.Query

interface CityApi {
  @GET("geo/api.php")
  suspend fun getCityList(
    @Query("city_name") cityName: String,
    @Query("json") json: String =  "",
    @Query("limit") limit: Int = 15,
    @Query("api_key") apiKey: String = CITY_API_KEY
  ) : CityListResponse

  @GET("geo/city_coming/")
  suspend fun getCity(
    @Query("latitude") latitude: Double,
    @Query("longitude") longitude: Double,
    @Query("length") length: Int = 500,
    @Query("level") level: Int = 2,
    @Query("perpage") perpage: Int = 1,
    @Query("api_key") apiKey: String = CITY_API_KEY
  ) : CityResponse
  companion object {
    private  const val CITY_API_KEY = "7083ec4aed7944afcc3d3ecb09cb53ac"
  }
}
