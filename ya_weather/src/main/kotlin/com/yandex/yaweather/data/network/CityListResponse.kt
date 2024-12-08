package com.yandex.yaweather.data.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CityListResponse(
  @SerialName("limit")
  val limit: Int = 15,
  @SerialName("0")
  val cityItem0: CityItem? = null,
  @SerialName("1")
  val cityItem1: CityItem? = null,
  @SerialName("2")
  val cityItem2: CityItem? = null,
  @SerialName("3")
  val cityItem3: CityItem? = null,
  @SerialName("4")
  val cityItem4: CityItem? = null,
  @SerialName("5")
  val cityItem5: CityItem? = null,
  @SerialName("6")
  val cityItem6: CityItem? = null,
  @SerialName("7")
  val cityItem7: CityItem? = null,
  @SerialName("8")
  val cityItem8: CityItem? = null,
  @SerialName("9")
  val cityItem9: CityItem? = null,
  @SerialName("10")
  val cityItem10: CityItem? = null,
  @SerialName("11")
  val cityItem11: CityItem? = null,
  @SerialName("12")
  val cityItem12: CityItem? = null,
  @SerialName("13")
  val cityItem13: CityItem? = null,
  @SerialName("14")
  val cityItem14: CityItem? = null
)

@Serializable
data class CityResponse(
  @SerialName("status")
  val status: Int,
  @SerialName("items")
  val items: List<CityItem>? = null
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

