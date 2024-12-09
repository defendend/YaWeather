package com.yandex.yaweather.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.yandex.yaweather.dagger.application.MainApplication
import com.yandex.yaweather.data.network.CityApi
import com.yandex.yaweather.data.network.WeatherApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val LATITUDE = "latitude"
private const val LONGITUDE = "longitude"
private const val LOCATION_FILE = "_location"

class WeatherWorker @Inject constructor(
  context: Context, workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {

  @Inject
  lateinit var api1: CityApi

  @Inject
  lateinit var api2: WeatherApi

  init {
    (context.applicationContext as MainApplication).mainComponent.inject(this)
  }

  override suspend fun doWork(): Result {
    return try {
      val weatherData = fetchWeatherData()
      saveWeatherData(weatherData)
      notifyWidgetUpdate()
      Result.success()
    } catch (e: Exception) {
      e.printStackTrace()
      Result.retry()
    }
  }

  private suspend fun fetchWeatherData(): WidgetData = withContext(Dispatchers.IO) {
    val response1 = api1.getCity(getLatitude().toDouble(), getLongitude().toDouble())
    val response2 = api2.current(getLatitude(), getLongitude())
    if (response1.items == null) {
      throw Exception("Invalid API response")
    }
    if (response2.main == null || response2.sys == null) {
      throw Exception("Invalid API response")
    }
    val data = WidgetData(
      response1.items[1].engName,
      response1.items[1].name,
      response2.main.temp?.toInt(),
      response2.weather?.get(0)?.description,
      response2.sys.sunrise,
      response2.sys.sunset
    )
    data
  }

  private fun saveWeatherData(data: WidgetData) {
    val prefs = applicationContext.getSharedPreferences("widget", Context.MODE_PRIVATE)
    prefs.edit().apply {
      putString("engName", data.engName)
      putString("ruName", data.ruName)
      putInt("temp", data.temp ?: 0)
      putString("description", data.description)
      putLong("sunrise", data.sunrise ?: 0L)
      putLong("sunset", data.sunset ?: 0L)
    }.apply()
  }

  private fun notifyWidgetUpdate() {
    val intent = Intent(applicationContext, WeatherWidget::class.java).apply {
      action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
    }
    applicationContext.sendBroadcast(intent)
  }

  private val preferences =
    context.getSharedPreferences(applicationContext.packageName + LOCATION_FILE, Context.MODE_PRIVATE)

  private fun getLatitude(): String {
    return preferences.getString(LATITUDE, "").orEmpty()
  }

  private fun getLongitude(): String {
    return preferences.getString(LONGITUDE, "").orEmpty()
  }
}

data class WidgetData(
  val engName: String? = null,
  val ruName: String? = null,
  val temp: Int? = null,
  val description: String? = null,
  val sunrise: Long? = null,
  val sunset: Long? = null
)