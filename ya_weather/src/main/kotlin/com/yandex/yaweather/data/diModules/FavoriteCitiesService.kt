package com.yandex.yaweather.data.diModules

import android.content.Context
import android.util.Log
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yandex.yaweather.data.network.CityItem


private const val CITIES_LIST_KEY = "cities_list"
private const val LOCATION_FILE = "_favorite_cities"


class FavoriteCitiesService(private val context: Context) {

  private val preferences = context.getSharedPreferences(
    context.applicationContext.packageName + LOCATION_FILE,
    Context.MODE_PRIVATE
  )

  private val gson = Gson()

  private fun getCitiesFromPreferences(): MutableList<CityItem> {
    val json = preferences.getString(CITIES_LIST_KEY, "[]") ?: "[]"
    return try {
      gson.fromJson<List<CityItem>>(json, object : TypeToken<List<CityItem>>() {}.type).toMutableList()
    } catch (e: Exception) {
      mutableListOf()
    }
  }


  private fun saveCitiesToPreferences(cities: List<CityItem>) {
    val json = gson.toJson(cities)
    preferences.edit(commit = true) {
      putString(CITIES_LIST_KEY, json)
    }
  }

  fun addCity(city: CityItem) {
    val cities = getCitiesFromPreferences()
    if (cities.none { it.name == city.name }) {
      cities.add(city)
      saveCitiesToPreferences(cities)
    }
  }

  fun getAllCities(): List<CityItem> {
    return getCitiesFromPreferences()
  }

  fun moveCity(fromIndex: Int, toIndex: Int) {
    val cities = getCitiesFromPreferences()


    if (fromIndex in cities.indices && toIndex in cities.indices) {
      if (fromIndex == toIndex) {
        return
      }
      val movedItem = cities.removeAt(fromIndex)
      cities.add(toIndex, movedItem)

      saveCitiesToPreferences(cities)
    }
  }
}

