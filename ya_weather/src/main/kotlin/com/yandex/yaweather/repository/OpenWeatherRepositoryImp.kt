package com.yandex.yaweather.repository

import android.view.WindowManager.BadTokenException
import com.yandex.yaweather.data.network.WeatherApi
import data.network.CoordinatesResponse
import jakarta.inject.Inject
import retrofit2.Response


class OpenWeatherRepositoryImp @Inject constructor(private val weatherApi: WeatherApi) : OpenWeatherRepository {

  override suspend fun getCurrentWeather(lat: String, lon: String): Result<CoordinatesResponse> {
    return try
    {
      val response = weatherApi.current("41.311081", "69.240562", "7fd3fa2b3bf71545e2ff3b1a1f0871a0")
      if (response.cod in 200..299) {
        Result.success(response)
      } else {
        throw BadTokenException()
      }
    }catch (e: Exception)
    {
      Result.failure(e)
    }
  }
}