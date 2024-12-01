package com.yandex.yaweather.repository

import com.yandex.yaweather.data.network.ForecastResponse
import data.network.CoordinatesResponse

interface OpenWeatherRepository {
  suspend fun getCurrentWeather(lat: String, lon: String): Result<CoordinatesResponse>
  suspend fun getForecastWeather(lat: String, lon: String): Result<ForecastResponse>
}