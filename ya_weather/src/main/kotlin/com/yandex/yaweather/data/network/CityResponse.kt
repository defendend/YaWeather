package com.yandex.yaweather.data.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CityResponse(
  @SerialName("limit")
  val limit : Int = 4,
  @SerialName("0")
  val item0: Item? = null,
  @SerialName("1")
  val item1: Item? = null,
  @SerialName("2")
  val item2: Item? = null,
  @SerialName("3")
  val item4: Item? = null
)

@Serializable
data class Item(
  @SerialName("name")
  val name: String? = null,
  @SerialName("full_name")
  val fullName: String? = null,
  @SerialName("english")
  val engName: String? = null,
  @SerialName("latitude")
  val lat: String? = null,
  @SerialName("longitude")
  val lon: String? = null,
  @SerialName("time_zone")
  val timeZone: String? = null
)

