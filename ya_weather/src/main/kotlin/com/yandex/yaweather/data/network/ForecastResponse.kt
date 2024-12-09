package com.yandex.yaweather.data.network

import data.network.Main
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForecastResponse(
  @SerialName("cod")
  val cod: String? = null,
  @SerialName("list")
  val list: List<Per3Hour>? = null,
)

@Serializable
data class Per3Hour(
  @SerialName("dt")
  val dt: Long? = null,
  @SerialName("main")
  val main: Main? = null,
  @SerialName("dt_txt")
  val dtTxt: String? = null
)
