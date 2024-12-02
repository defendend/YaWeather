package com.yandex.yaweather.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.yandex.yaweather.repository.OpenWeatherRepository
import data.network.CoordinatesResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class YaWeatherViewModel @Inject constructor(private val weatherRepository: OpenWeatherRepository) : ViewModel() {
  private val _currentWeather = MutableStateFlow(
    WeatherUiState()
  )
  val currentWeatherState: StateFlow<WeatherUiState>
    get() = _currentWeather.asStateFlow()

  private val _errorMessage = MutableStateFlow<String>("")
  val errorMessage = _errorMessage.asStateFlow()

  fun getCurrentWeather(lat: String, lon: String) {
    viewModelScope.launch(Dispatchers.IO) {
      weatherRepository.getCurrentWeather(lat, lon).onSuccess {
        _currentWeather.value = mapResponseToUiState(it)
      }.onFailure {
        _errorMessage.value = it.message.toString()
      }
    }
  }

  private fun mapResponseToUiState(response: CoordinatesResponse): WeatherUiState {
    return WeatherUiState(
      cityName = response.name ?: "N/A",
      temperature = getCurrentTemperature(response),
      description = response.weather?.firstOrNull()?.description ?: "N/A"
    )
  }

  private fun getCurrentTemperature(response: CoordinatesResponse): String {
    return ((response.main?.temp)?.minus(273))?.toInt().toString()
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
          _currentWeather.value = mapResponseToUiState(response.getOrNull()!!)
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

data class WeatherUiState(
  val cityName: String = "",
  val temperature: String = "",
  val description: String = "",
  val widgetsUiState: WidgetsUiState = WidgetsUiState()
) {

  data class WidgetsUiState(
    val id: String = ""
  )
}