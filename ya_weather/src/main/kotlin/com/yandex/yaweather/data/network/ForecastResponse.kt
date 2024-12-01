package com.yandex.yaweather.data.network

import data.network.Clouds
import data.network.Coordinates
import data.network.Main
import data.network.Sys
import data.network.WeatherBody
import data.network.Wind
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForecastResponse(
  @SerialName("cod")
  val cod: String? = null,
  @SerialName("message")
  val message: Int? = null,
  @SerialName("cnt")
  val cnt: Int? = null,
  @SerialName("list")
  val list: List<Per3Hour>? = null,
  @SerialName("name")
  val city: City
)

@Serializable
data class Per3Hour(
  @SerialName("dt")
  val dt: Long? = null,
  @SerialName("main")
  val main: Main? = null,
  @SerialName("weather")
  val weather: WeatherBody? = null,
  @SerialName("clouds")
  val clouds: Clouds? = null,
  @SerialName("wind")
  val wind: Wind? = null,
  @SerialName("visibility")
  val visibility: Int? = null,
  @SerialName("pop")
  val pop: Int? = null,
  @SerialName("sys")
  val sys: Sys? = null,
  @SerialName("dt_txt")
  val dtTxt: String? = null
)

@Serializable
data class City(
  @SerialName("id")
  val id: Long? = null,
  @SerialName("name")
  val name: String? = null,
  @SerialName("coord")
  val coord: Coordinates? = null,
  @SerialName("country")
  val country: String? = null,
  @SerialName("population")
  val population: Int? = null,
  @SerialName("timezone")
  val timezone: Int? = null,
  @SerialName("sunrise")
  val sunrise: Long? = null,
  @SerialName("sunset")
  val sunset: Long? = null
)
