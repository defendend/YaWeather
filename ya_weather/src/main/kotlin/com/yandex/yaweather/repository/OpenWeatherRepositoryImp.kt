package com.yandex.yaweather.repository

import android.view.WindowManager.BadTokenException
import com.yandex.yaweather.data.network.ForecastResponse
import com.yandex.yaweather.data.network.WeatherApi
import data.network.CoordinatesResponse
import jakarta.inject.Inject


class OpenWeatherRepositoryImp @Inject constructor(private val weatherApi: WeatherApi) : OpenWeatherRepository {

  override suspend fun getCurrentWeather(lat: String, lon: String): Result<CoordinatesResponse> {

    return try
    {
      val response = weatherApi.current(lat, lon, "7fd3fa2b3bf71545e2ff3b1a1f0871a0")
      if (response.cod in 200..299) {
        Result.success(response)
      } else {
        throw BadTokenException()
      }
    } catch (e: Exception) {
      Result.failure(e)
    }
  }

  override suspend fun getForecastWeather(lat: String, lon: String): Result<ForecastResponse> {
    return try {
      val response = weatherApi.forecast(lat, lon, "7fd3fa2b3bf71545e2ff3b1a1f0871a0")
      if (response.cod?.toInt() in 200..299) {
        Result.success(response)
      } else {
        throw BadTokenException()
      }
    } catch (e: Exception) {
      Result.failure(e)
    }
  }
}