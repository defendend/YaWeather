package com.yandex.yaweather.repository

import android.view.WindowManager.BadTokenException
import com.yandex.yaweather.data.network.HourlyWeatherResponse
import com.yandex.yaweather.data.network.HourlyWeatherApi
import javax.inject.Inject

class HourlyWeatherRepositoryImp @Inject constructor(private val hourlyWeatherApi: HourlyWeatherApi) : HourlyWeatherRepository {
  override suspend fun getHourlyWeather(
    lat: Double?,
    lon: Double?,
  ): Result<HourlyWeatherResponse> {
    return try {
      if(lat == null || lon == null) {
        throw IllegalArgumentException()
      }
      val response = hourlyWeatherApi.getHourlyWeather(lat = lat, lon = lon)
      if (response.data != null) {
        Result.success(response)
      } else {
        throw BadTokenException()
      }
    } catch (e: Exception) {
      Result.failure(e)
    }
  }
}