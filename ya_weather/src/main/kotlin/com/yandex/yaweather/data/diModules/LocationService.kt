package com.yandex.yaweather.data.diModules

import android.content.Context
import androidx.core.content.edit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

private const val LATITUDE = "latitude"
private const val LONGITUDE = "longitude"
private const val LOCATION_FILE = "_location"

@Singleton
class LocationService @Inject constructor(context: Context) {
  private val preferences = context.getSharedPreferences(
    context.applicationContext.packageName + LOCATION_FILE,
    Context.MODE_PRIVATE
  )
  private val _coordinates = MutableStateFlow(getInitialCoordinates())
  val coordinates = _coordinates.asStateFlow()

  fun setLocation(location: Pair<Double, Double>) {
    _coordinates.value = location
    saveLocationToPref(location)
  }

  suspend fun takeCurrentCoordinates(): Pair<Double, Double>? {
    return _coordinates.first()
  }

  private fun saveLocationToPref(location: Pair<Double, Double>) {
    val (lat, lon) = location

    setLatitude(latitude = lat.toString())
    setLongitude(longitude = lon.toString())

  }

  private fun getInitialCoordinates(): Pair<Double, Double>? {
    val lat = getLatitude()?.toDoubleOrNull()
    val lon = getLongitude()?.toDoubleOrNull()
    if (lat == null || lon == null) {
      return null
    }
    return lat to lon
  }

  private fun getLatitude(): String? {
    return preferences.getString(LATITUDE, "")
  }

  private fun getLongitude(): String? {
    return preferences.getString(LONGITUDE, "")
  }

  private fun setLatitude(latitude: String) {
    preferences.edit {
      putString(LATITUDE, latitude)
    }
  }

  private fun setLongitude(longitude: String) {
    preferences.edit {
      putString(LONGITUDE, longitude)
    }
  }
}