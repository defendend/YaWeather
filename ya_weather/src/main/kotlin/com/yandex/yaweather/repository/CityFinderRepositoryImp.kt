package com.yandex.yaweather.repository

import android.view.WindowManager.BadTokenException
import com.yandex.yaweather.data.network.CityApi
import com.yandex.yaweather.data.network.CityResponse
import jakarta.inject.Inject


class CityFinderRepositoryImp @Inject constructor(private val cityApi: CityApi) : CityFinderRepository {
  override suspend fun getCities(query: String): Result<CityResponse> {
    return try {
      val response = cityApi.getCityList(query)
      if (response.cityItem0 != null) {
        Result.success(response)
      } else {
        throw BadTokenException()
      }
    } catch (e: Exception) {
      Result.failure(e)
    }
  }
}