package com.yandex.yaweather.handler

import com.yandex.yaweather.data.network.CityItem

sealed class CityScreenAction {
  data class SearchCityAction(val query: String) : CityScreenAction()
  data class AddToFavoriteCityList(val cityItem: CityItem) : CityScreenAction()
}