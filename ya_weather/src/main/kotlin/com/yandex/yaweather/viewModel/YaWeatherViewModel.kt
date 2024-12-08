package com.yandex.yaweather.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.yandex.yaweather.data.diModules.FavoriteCitiesService
import com.yandex.yaweather.data.network.CityItem
import com.yandex.yaweather.repository.CityFinderRepository
import com.yandex.yaweather.data.network.WeatherByHour
import com.yandex.yaweather.repository.HourlyWeatherRepository
import com.yandex.yaweather.repository.OpenWeatherRepository
import com.yandex.yaweather.viewModel.WeatherUiState.WidgetsUiState
import data.network.Coordinates
import data.network.CoordinatesResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class YaWeatherViewModel @Inject constructor(
  private val weatherRepository: OpenWeatherRepository,
  private val cityFinderRepository: CityFinderRepository,
  private  val hourlyWeatherRepository: HourlyWeatherRepository
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

  private val _currentHourlyData = MutableStateFlow<List<WeatherByHour>>(emptyList())
  private val currentHourlyData = _currentHourlyData.asStateFlow()

  private fun getCurrentHourlyWeather(lat: Double, lon: Double) : Job
  {
    return viewModelScope.launch(Dispatchers.IO)
    {
      hourlyWeatherRepository.getHourlyWeather(lat, lon)
        .onSuccess {
          it.data?.let { it1 -> _currentHourlyData.emit(it1) }
        }.onFailure {
          _errorMessage.emit(it.message.toString())
        }
    }
  }


  private fun getCurrentWeather(lat: String, lon: String) : Job {
    return viewModelScope.launch(Dispatchers.IO) {
      weatherRepository.getCurrentWeather(lat, lon).onSuccess {
        _currentWeather.emit(mapResponseToUiState(it))
      }.onFailure {
        _errorMessage.emit(it.message.toString())
      }
    }
  }

  fun updateCurrentWeatherScreen(lat: Double, lon: Double)
  {
    viewModelScope.launch(Dispatchers.IO) {
      getCurrentHourlyWeather(lat, lon).join()
      getCurrentWeather(lat.toString(), lon.toString()).join()
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
          it.cityItem3,
          it.cityItem4,
          it.cityItem5,
          it.cityItem6,
          it.cityItem7,
          it.cityItem8,
          it.cityItem9,
          it.cityItem10,
          it.cityItem11,
          it.cityItem12,
          it.cityItem13,
          it.cityItem14
        )
        _cities.value = cityList.toMutableList()
      }.onFailure {
        _errorMessage.value = it.message.toString()
      }
    }
  }
  private fun mapScreenResponseToUiState(response: CoordinatesResponse): MapUIState {
    return MapUIState(
      markerPosition = response.coordinates,
      temperature = response.main?.temp?.minus(273)?.toInt().toString(),
      windSpeed = response.wind?.speed.toString(),
      humidity = response.main?.humidity.toString(),
      pressure = response.main?.pressure.toString()
    )
  }
  private fun mapResponseToUiState(response: CoordinatesResponse): WeatherUiState {
    return WeatherUiState(
      cityName = response.name ?: "N/A",
      temperature = getCurrentTemperature(response),
      feelsLike = ((response.main?.feelsLike)?.minus(273))?.toInt().toString(),
      temperatureMax = ((response.main?.tempMax)?.minus(273))?.toInt().toString(),
      temperatureMin = ((response.main?.tempMin)?.minus(273))?.toInt().toString(),
      description = response.weather?.firstOrNull()?.description ?: "N/A",
      widgetsUiState = widgetResponeToUiState(response),
      hourlyWeather = _currentHourlyData.value,
      markerPosition = response.coordinates
    )
  }

  private fun widgetResponeToUiState(response: CoordinatesResponse): WidgetsUiState {
    return WidgetsUiState(
      feelsLike = response.main?.feelsLike?.minus(273)?.toInt().toString(), // Преобразование из Кельвинов в Цельсии
      humidity = response.main?.humidity?.toString() ?: "-",
      windSpeed = response.wind?.speed?.toString() ?: "-",
      sealevel = response.main?.seaLevel?.toString() ?: "-",

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
  fun loadFavoriteCities(service: FavoriteCitiesService) {
    viewModelScope.launch {
      service.getAllCities().forEach {
          item ->
        weatherRepository.getCurrentWeather(item.lat.toString(), item.lon.toString()).onSuccess {
            coordinatesResponse ->
          _favoriteCityItems.update { (it + CitySelectionUIState(item, mapResponseToUiState(coordinatesResponse))).toMutableList() }
        }.onFailure {
          _errorMessage.value = it.message.toString()
        }
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
  val humidity : String = "",
  val windSpeed : String = "",
  val pressure : String = "",

)

data class WeatherUiState(
  val markerPosition: Coordinates? = null,
  val cityName: String = "",
  val temperature: String = "",
  val temperaturePerHour: String = "",
  val description: String = "",
  val feelsLike: String = "",
  val temperatureMin: String = "",
  val temperatureMax: String = "",
  val widgetsUiState: WidgetsUiState = WidgetsUiState(),
  val hourlyWeather: List<WeatherByHour> = emptyList(),
  val code: String = ""
) {

  data class WidgetsUiState(
    val id: String = "",
    val sealevel: String = "",
    val windSpeed: String = "",
    val feelsLike: String = "",
    val humidity: String = ""
  )
}

