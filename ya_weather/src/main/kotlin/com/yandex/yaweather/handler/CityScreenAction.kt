package com.yandex.yaweather.handler

sealed class CityScreenAction {
  data class SearchCityAction(val query: String) : CityScreenAction()
}