package com.yandex.yaweather.data.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HourlyWeatherResponse(
  @SerialName("data")
  val data: List<WeatherByHour>? = null
)

@Serializable
data class WeatherByHour(
  @SerialName("temp")
  val temp: Double? = null,
  @SerialName("app_temp")
  val appTemp: Double? = null,
  @SerialName("timestamp_local")
  val timeStamp: String? = null,
  @SerialName("datetime")
  val dataTime: String? = null,
  @SerialName("weather")
  val weather: Weather? = null,
)

@Serializable
data class Weather(
  @SerialName("description")
  val description: String? = null,
  @SerialName("code")
  val code: Int? = null
)