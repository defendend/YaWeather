package com.yandex.yaweather.handler

sealed class WeatherScreenAction {
  data object AddCityAction: WeatherScreenAction()
  data object OpenMapAction: WeatherScreenAction()
}