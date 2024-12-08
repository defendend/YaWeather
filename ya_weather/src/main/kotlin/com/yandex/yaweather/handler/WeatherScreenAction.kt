package com.yandex.yaweather.handler

import com.yandex.yaweather.Lang


sealed class WeatherScreenAction {
  data object AddCityAction : WeatherScreenAction()
  data object OpenMapAction : WeatherScreenAction()
  data object OpenInfoAction : WeatherScreenAction()
  data class SetTheme(val lightTheme: Boolean) : WeatherScreenAction()
  data class SetLanguage(val language: Lang) : WeatherScreenAction()
}