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
  lateinit var api: WeatherApi

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
    val response = api.current(getLatitude(), getLongitude())
    if (response.main == null || response.sys == null) {
      throw Exception("Invalid API response")
    }
    val data = WidgetData(
      response.name,
      response.main.temp?.toInt(),
      response.weather?.get(0)?.id,
      response.sys.sunrise,
      response.sys.sunset
    )
    data
  }

  private fun saveWeatherData(data: WidgetData) {
    val prefs = applicationContext.getSharedPreferences("widget", Context.MODE_PRIVATE)
    prefs.edit().apply {
      putString("name", data.name)
      putInt("temp", data.temp ?: 0)
      putInt("code", data.code?: 0)
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
  val name: String? = null,
  val temp: Int? = null,
  val code: Int? = null,
  val sunrise: Long? = null,
  val sunset: Long? = null
)