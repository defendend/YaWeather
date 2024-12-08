package com.yandex.yaweather.repository

import com.yandex.yaweather.data.network.CityListResponse
import com.yandex.yaweather.data.network.CityResponse

interface CityFinderRepository {
  suspend fun getCities(query: String) : Result<CityListResponse>
  suspend fun getCity(lat: Double, lon: Double) : Result<CityResponse>
}