package com.yandex.yaweather.handler

import com.yandex.yaweather.data.network.CityItem

sealed class CityScreenAction {
  data class SearchCityAction(val query: String) : CityScreenAction()
  data class AddToFavoriteCityList(val cityItem: CityItem) : CityScreenAction()
  data class OpenSelectedCityScreen(val index: Int): CityScreenAction()
  data class MoveCity(val fromIndex: Int, val toIndex: Int): CityScreenAction()
}
