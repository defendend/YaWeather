package com.yandex.yaweather.repository

import data.network.CoordinatesResponse
import retrofit2.Response

interface OpenWeatherRepository {
  suspend fun getCurrentWeather(lat: String, lon : String) : Result<CoordinatesResponse>
}