package com.yandex.yaweather.data.diModules

import android.content.Context
import android.util.Log
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken



private const val CITIES_LIST_KEY = "cities_list"
private const val LOCATION_FILE = "_favorite_cities"

data class CityItem(
  val name: String? = null,
  val fullName: String? = null,
  val engName: String? = null,
  val lat: Double? = null,
  val lon: Double? = null,
  val timeZone: Int? = null
)

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

    // Логирование списка до перемещения
    Log.d("FavoriteCitiesService", "Before move: ${gson.toJson(cities)}")

    // Проверка на допустимость индексов
    if (fromIndex in cities.indices && toIndex in cities.indices) {
      // Если индексы одинаковые, то ничего не делаем
      if (fromIndex == toIndex) {
        Log.d("FavoriteCitiesService", "No movement: fromIndex and toIndex are the same.")
        return
      }

      // Удаляем элемент по индексу fromIndex и добавляем его на новый индекс toIndex
      val movedItem = cities.removeAt(fromIndex)
      cities.add(toIndex, movedItem)

      // Логирование информации о перемещении
      Log.d("FavoriteCitiesService", "Moved city: $movedItem from index $fromIndex to $toIndex")

      // Логирование списка после перемещения
      Log.d("FavoriteCitiesService", "After move: ${gson.toJson(cities)}")

      // Сохраняем обновленный список в SharedPreferences
      saveCitiesToPreferences(cities)
    } else {
      // Логирование ошибки, если индексы выходят за пределы
      Log.e("FavoriteCitiesService", "Invalid indices: fromIndex = $fromIndex, toIndex = $toIndex")
    }
  }

}

