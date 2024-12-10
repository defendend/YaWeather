package com.yandex.yaweather.handler

import com.yandex.yaweather.Lang


sealed class WeatherScreenAction {
  data object AddCityAction : WeatherScreenAction()
  data object OpenMapAction : WeatherScreenAction()
  data object OpenInfoAction : WeatherScreenAction()
  data class SetTheme(val lightTheme: Boolean) : WeatherScreenAction()
  data class SetLanguage(val language: Lang) : WeatherScreenAction()
  data class SetCelsius(val isCelsius: Boolean) : WeatherScreenAction()
  data class SetPressure(val isHPa: Boolean) : WeatherScreenAction()
  data class SetWindSpeed(val isMS: Boolean) : WeatherScreenAction()
}