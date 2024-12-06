package com.yandex.yaweather.viewModel

import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yandex.yaweather.data.network.CityItem
import com.yandex.yaweather.repository.CityFinderRepository
import com.google.android.gms.maps.model.LatLng
import com.yandex.yaweather.repository.OpenWeatherRepository
import data.network.Coordinates
import data.network.CoordinatesResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class YaWeatherViewModel @Inject constructor(
  private val weatherRepository: OpenWeatherRepository,
  private val cityFinderRepository: CityFinderRepository
) : ViewModel() {

  private val _currentWeather = MutableStateFlow(WeatherUiState())
  val userCurrentWeatherState: StateFlow<WeatherUiState>
    get() = _currentWeather.asStateFlow()

  private val _mapWeather = MutableStateFlow(MapUIState())
  val mapWeatherState: StateFlow<MapUIState>
    get() = _mapWeather.asStateFlow()

  private val _errorMessage = MutableStateFlow<String>("")
  val errorMessage = _errorMessage.asStateFlow()

  private val _cities = MutableStateFlow<MutableList<CityItem>>(mutableListOf())
  val cities = _cities.asStateFlow()

  private val _favoriteCityItems = MutableStateFlow<MutableList<CitySelectionUIState>>(mutableListOf())
  val  favoriteCityItems = _favoriteCityItems.asStateFlow()

  fun getCurrentWeather(lat: String, lon: String) {
    viewModelScope.launch(Dispatchers.IO) {
      weatherRepository.getCurrentWeather(lat, lon).onSuccess {
        _currentWeather.emit(mapResponseToUiState(it))
      }.onFailure {
        _errorMessage.emit(it.message.toString())
      }
    }
  }
  fun getMapInfo(lat: String, lon: String){
    viewModelScope.launch(Dispatchers.IO){
      viewModelScope.launch {
        weatherRepository.getCurrentWeather(lat,lon).onSuccess {
          _mapWeather.emit(mapScreenResponseToUiState(it))
        }.onFailure {
          _errorMessage.emit(it.message.toString())
        }
      }
    }
  }
  fun updateFavoriteCityItems(cityItem: CityItem) {
    viewModelScope.launch(Dispatchers.IO)
    {
      weatherRepository.getCurrentWeather(cityItem.lat.toString(), cityItem.lon.toString()).onSuccess {
        coordinatesResponse ->
        _favoriteCityItems.update { (it + CitySelectionUIState(cityItem, mapResponseToUiState(coordinatesResponse))).toMutableList() }
      }.onFailure {
        _errorMessage.value = it.message.toString()
      }
    }
  }

  fun getCitiesByName(query: String)
  {
    viewModelScope.launch(Dispatchers.IO)
    {
      cityFinderRepository.getCities(query).onSuccess {
        val cityList = listOfNotNull(
          it.cityItem0,
          it.cityItem1,
          it.cityItem2,
          it.cityItem3
        )
        _cities.value = cityList.toMutableList()
      }.onFailure {
        _errorMessage.value = it.message.toString()
      }
    }
  }
  private fun mapScreenResponseToUiState(response: CoordinatesResponse): MapUIState {
    return MapUIState(
      markerPosition = response.coordinates, // CoordinatesResponse'dan olingan koordinatalar
      temperature = response.main?.temp?.minus(273)?.toInt().toString() // Haroratni Kelvin'dan Celsius'ga o'tkazish
    )
  }
  private fun mapResponseToUiState(response: CoordinatesResponse): WeatherUiState {
    return WeatherUiState(
      cityName = response.name ?: "N/A",
      temperature = getCurrentTemperature(response),
      temperatureMax = ((response.main?.tempMax)?.minus(273))?.toInt().toString(),
      temperatureMin = ((response.main?.tempMin)?.minus(273))?.toInt().toString(),
      description = response.weather?.firstOrNull()?.description ?: "N/A",

    )
  }
  private fun getCurrentTemperature(response: CoordinatesResponse): String {
    return ((response.main?.temp)?.minus(273))?.toInt().toString()
}

  fun moveCity(fromIndex: Int, toIndex: Int) {
    val movedItem = _favoriteCityItems.value.removeAt(fromIndex)
    _favoriteCityItems.value.add(toIndex, movedItem)
  }

  fun updateMarkerPosition(latLng: LatLng) {
    viewModelScope.launch(Dispatchers.IO) {
      try {
        val lat = latLng.latitude.toString()
        val lon = latLng.longitude.toString()
        println("UpdateMarkerPosition :\n lat=$lat, lon=$lon")
        val response = weatherRepository.getCurrentWeather(lat, lon)
        if (response.isSuccess) {
          println("Weather fetched: ${response.getOrNull()?.main?.temp}")
          _mapWeather.value = mapScreenResponseToUiState(response.getOrNull()!!)
        } else {
          println("Error fetching weather: ${response.exceptionOrNull()?.message}")
          _errorMessage.value = response.exceptionOrNull()?.message.toString()
        }

      } catch (e: Exception) {
        println("Exception: ${e.message}")
        _errorMessage.value = e.message.toString()
      }
    }
  }
}

data class CitySelectionUIState(
  val cityItem: CityItem,
  val weatherUiState: WeatherUiState
)

data class MapUIState(
  val markerPosition: Coordinates? = null,
  val temperature: String = "",

)

data class WeatherUiState(
  val cityName: String = "",
  val temperature: String = "",
  val description: String = "",
  val temperatureMin: String = "",
  val temperatureMax: String = "",
  val widgetsUiState: WidgetsUiState = WidgetsUiState()

) {

  data class WidgetsUiState(
    val id: String = ""
  )
}