package com.yandex.yaweather.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yandex.yaweather.repository.OpenWeatherRepository
import data.network.CoordinatesResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class YaWeatherViewModel @Inject constructor(private val weatherRepository: OpenWeatherRepository) : ViewModel() {
  private val _currentWeather = MutableStateFlow(
    CoordinatesResponse(
      base = "",
      visibility = 1,
      dt = 1L,
      timezone = 1,
      id = 1,
      name = "",
      cod = 200
    )
  )
  val currentWeatherState: StateFlow<CoordinatesResponse>
    get() = _currentWeather.asStateFlow()

  private val _errorMessage = MutableStateFlow<String>("")
  val errorMessage = _errorMessage.asStateFlow()

  fun getCurrentWeather(lat: String, lon: String) {
    viewModelScope.launch {
      weatherRepository.getCurrentWeather(lat, lon).onSuccess {
        _currentWeather.value = it
      }.onFailure {
        _errorMessage.value = it.message.toString()
      }
    }
  }
  fun getCurrentTemperature(): String {
    return ((_currentWeather.value.main?.temp)?.minus(273))?.toInt().toString() ?: "N/A"
  }
}