package com.yandex.yaweather.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.yandex.yaweather.data.diModules.FavoriteCitiesService
import com.yandex.yaweather.data.network.CityItem
import com.yandex.yaweather.data.network.ForecastResponse
import com.yandex.yaweather.data.network.Per3Hour
import com.yandex.yaweather.data.network.WeatherByHour
import com.yandex.yaweather.repository.CityFinderRepository
import com.yandex.yaweather.repository.HourlyWeatherRepository
import com.yandex.yaweather.repository.OpenWeatherRepository
import com.yandex.yaweather.viewModel.WeatherUiState.WidgetsUiState
import data.network.Coordinates
import data.network.CoordinatesResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class YaWeatherViewModel @Inject constructor(
  private val weatherRepository: OpenWeatherRepository,
  private val cityFinderRepository: CityFinderRepository,
  private val hourlyWeatherRepository: HourlyWeatherRepository
) : ViewModel() {

  private val _currentWeatherUIState = MutableStateFlow(CitySelectionUIState(CityItem(), WeatherUiState()))
  val userCurrentWeatherState: StateFlow<CitySelectionUIState>
    get() = _currentWeatherUIState.asStateFlow()

  private val _mapWeather = MutableStateFlow(MapUIState())
  val mapWeatherState: StateFlow<MapUIState>
    get() = _mapWeather.asStateFlow()

  private val _errorMessage = MutableStateFlow<String>("")
  val errorMessage = _errorMessage.asStateFlow()

  private val _cities = MutableStateFlow<MutableList<CityItem>>(mutableListOf())
  val cities = _cities.asStateFlow()

  private val _favoriteCityItems = MutableStateFlow<MutableList<CitySelectionUIState>>(mutableListOf())
  val favoriteCityItems = _favoriteCityItems.asStateFlow()

  private val _currentWeather = MutableStateFlow<CoordinatesResponse>(CoordinatesResponse(cod = 404))
  private val _currentCity = MutableStateFlow<List<CityItem>>(emptyList())
  private val _currentHourlyWeather = MutableStateFlow<List<WeatherByHour>>(emptyList())
  private val _currentForecast = MutableStateFlow(ForecastResponse())

  private fun getCurrentWeather(lat: Double, lon: Double): Job {
    return viewModelScope.launch(Dispatchers.IO) {
      weatherRepository.getCurrentWeather(lat.toString(), lon.toString()).onSuccess {
        _currentWeather.emit(it)
      }.onFailure {
        _errorMessage.emit(it.message.toString())
      }
    }
  }


  private fun getCurrentCity(lat: Double, lon: Double): Job {
    return viewModelScope.launch(Dispatchers.IO) {
      cityFinderRepository.getCity(lat, lon).onSuccess {
        if (it.items != null) {
          _currentCity.emit(it.items)
        }
      }.onFailure {
        _errorMessage.emit(it.message.toString())
      }
    }
  }

  private fun getCurrentHourlyWeather(lat: Double, lon: Double): Job {
    return viewModelScope.launch(Dispatchers.IO) {
      hourlyWeatherRepository.getHourlyWeather(lat, lon).onSuccess {
        if (it.data != null) {
          _currentHourlyWeather.emit(it.data)
        }
      }.onFailure {
        _errorMessage.emit(it.message.toString())
      }
    }
  }

  private fun getForecastWeather(lat: Double, lon: Double) : Job
  {
    return viewModelScope.launch(Dispatchers.IO)
    {
        weatherRepository.getForecastWeather(lat.toString(), lon.toString()).onSuccess {
        _currentForecast.emit(it)
      }.onFailure {
        _errorMessage.emit(it.message.toString())
      }
    }
  }

  fun getCurrentData(lat: Double, lon: Double) {
    viewModelScope.launch(Dispatchers.IO) {
      val currentWeather = getCurrentWeather(lat, lon)
      val currentHourlyWeather = getCurrentHourlyWeather(lat, lon)
      val currentCity = getCurrentCity(lat, lon)
      val getForecastWeather = getForecastWeather(lat, lon)

      currentWeather.join()
      currentHourlyWeather.join()
      currentCity.join()
      getForecastWeather.join()

      _currentWeatherUIState.emit(
        CitySelectionUIState(
          _currentCity.value[0],
          mapResponseToUiState(_currentWeather.value),
          _currentHourlyWeather.value,
          _currentForecast.value.list ?: emptyList()
        )
      )
      _mapWeather.emit(
        MapUIState(
          Coordinates(
            _currentWeather.value.coordinates?.lat, _currentWeather.value.coordinates?.lon
          )
        )
      )
    }
  }


  fun getMapInfo(lat: String, lon: String) {
    viewModelScope.launch(Dispatchers.IO) {
      weatherRepository.getCurrentWeather(lat, lon).onSuccess {
        _mapWeather.emit(mapScreenResponseToUiState(it))
      }.onFailure {
        _errorMessage.emit(it.message.toString())
      }
    }
  }


  fun updateFavoriteCityItems(cityItem: CityItem) {
    viewModelScope.launch(Dispatchers.IO) {
      weatherRepository.getCurrentWeather(cityItem.lat.toString(), cityItem.lon.toString())
        .onSuccess { coordinatesResponse ->
          _favoriteCityItems.update {
            (it + CitySelectionUIState(
              cityItem, mapResponseToUiState(coordinatesResponse)
            )).toMutableList()
          }
        }.onFailure {
          _errorMessage.value = it.message.toString()
        }
    }
  }

  fun getCitiesByName(query: String) {
    viewModelScope.launch(Dispatchers.IO) {
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
      weatherId = response.weather?.firstOrNull()?.id ?: 0,
      widgetsUiState = widgetResponeToUiState(response),
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

  fun getWeatherDataToCityById(index: Int) {
    viewModelScope.launch {
      val currentList = _favoriteCityItems.value.toMutableList()
      val cityState = currentList[index]

      val hourlyWeatherDeferred = async {
        if (cityState.hourlyWeather.isEmpty()) {
          getHourlyWeather(cityState.cityItem.lat, cityState.cityItem.lon)
        } else {
          cityState.hourlyWeather
        }
      }
      val forecastDeferred = async {
        if (cityState.forecast.isEmpty()) {
          getForecast(cityState.cityItem.lat, cityState.cityItem.lon)
        } else {
          cityState.forecast
        }
      }

      try {
        val hourlyWeather = hourlyWeatherDeferred.await()
        val forecast = forecastDeferred.await()

        val updatedCityState = cityState.copy(
          hourlyWeather = hourlyWeather,
          forecast = forecast
        )

        currentList[index] = updatedCityState
        _favoriteCityItems.value = currentList
      } catch (e: Exception) {
        _errorMessage.emit("Failed to fetch data: ${e.message}")
      }
    }
  }

  private suspend fun getForecast(lat: Double?, lon: Double?): List<Per3Hour>
  {
    return try{
      val response = weatherRepository.getForecastWeather(lat.toString(), lon.toString())
      if(response.isSuccess) {
        response.getOrNull()?.list ?: emptyList()
      } else {
        emptyList()
      }
    } catch (e: Exception) {
      _errorMessage.emit(e.message.toString())
      emptyList()
    }
  }

  private suspend fun getHourlyWeather(lat: Double?, lon: Double?): List<WeatherByHour> {
    return try {
      val response = hourlyWeatherRepository.getHourlyWeather(lat, lon)
      if (response.isSuccess) {
        response.getOrNull()?.data ?: emptyList()
      } else {
        emptyList()
      }
    } catch (e: Exception) {
      _errorMessage.emit(e.message.toString())
      emptyList()
    }
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
    viewModelScope.launch(Dispatchers.IO) {
      service.getAllCities().forEach { item ->
        weatherRepository.getCurrentWeather(item.lat.toString(), item.lon.toString()).onSuccess { coordinatesResponse ->
          _favoriteCityItems.update {
            (it + CitySelectionUIState(
              item, mapResponseToUiState(coordinatesResponse)
            )).toMutableList()
          }
        }.onFailure {
          _errorMessage.value = it.message.toString()
        }
      }
    }
  }
}

data class CitySelectionUIState(
  val cityItem: CityItem,
  val weatherUiState: WeatherUiState,
  val hourlyWeather: List<WeatherByHour> = emptyList(),
  val forecast: List<Per3Hour> = emptyList(),
)

data class MapUIState(
  val markerPosition: Coordinates? = null,
  val temperature: String = "",
  val humidity: String = "",
  val windSpeed: String = "",
  val pressure: String = "",

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
  val code: String = "",
  val weatherId: Int = 0
) {

  data class WidgetsUiState(
    val id: String = "",
    val sealevel: String = "",
    val windSpeed: String = "",
    val feelsLike: String = "",
    val humidity: String = ""
  )
}

