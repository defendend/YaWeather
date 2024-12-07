package com.yandex.yaweather.repository

import com.yandex.yaweather.data.network.HourlyWeatherResponse

interface HourlyWeatherRepository {
  suspend fun getHourlyWeather(lat: Double, lon: Double) : Result<HourlyWeatherResponse>
}