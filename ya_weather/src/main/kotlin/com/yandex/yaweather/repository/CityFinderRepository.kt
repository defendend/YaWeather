package com.yandex.yaweather.repository

import com.yandex.yaweather.data.network.CityResponse

interface CityFinderRepository {
  suspend fun getCities(query: String) : Result<CityResponse>
}