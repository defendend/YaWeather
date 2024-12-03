package com.yandex.yaweather.data.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CityResponse(
  @SerialName("limit")
  val limit : Int = 4,
  @SerialName("0")
  val cityItem0: CityItem? = null,
  @SerialName("1")
  val cityItem1: CityItem? = null,
  @SerialName("2")
  val cityItem2: CityItem? = null,
  @SerialName("3")
  val cityItem3: CityItem? = null
)

@Serializable
data class CityItem(
  @SerialName("name")
  val name: String? = null,
  @SerialName("full_name")
  val fullName: String? = null,
  @SerialName("english")
  val engName: String? = null,
  @SerialName("latitude")
  val lat: Double? = null,
  @SerialName("longitude")
  val lon: Double? = null,
  @SerialName("time_zone")
  val timeZone: Int? = null
)

