package com.yandex.yaweather.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.yandex.yaweather.MainActivity
import com.yandex.yaweather.R
import com.yandex.yaweather.dagger.application.MainApplication
import com.yandex.yaweather.repository.OpenWeatherRepository
import com.yandex.yaweather.utils.getMessage
import com.yandex.yaweather.utils.getMessageForNotification
import com.yandex.yaweather.utils.getWeatherIcon
import data.network.CoordinatesResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


private const val LATITUDE = "latitude"
private const val LONGITUDE = "longitude"
private const val LOCATION_FILE = "_location"

class NotificationWorker @Inject constructor(
  context: Context,
  workerParams: WorkerParameters,
) : CoroutineWorker(context, workerParams) {

  private val preferences =
    context.getSharedPreferences(applicationContext.packageName + LOCATION_FILE, Context.MODE_PRIVATE)

  private fun getLatitude(): String {
    return preferences.getString(LATITUDE, "").orEmpty()
  }

  private fun getLongitude(): String {
    return preferences.getString(LONGITUDE, "").orEmpty()
  }

  @Inject
  lateinit var weatherRepository: OpenWeatherRepository

  var icon : Int = R.drawable.sun_fill
  init {
    (context.applicationContext as MainApplication).mainComponent.inject(this)
  }
var wishYou = context.applicationContext.getString(R.string.perfect_time_for_walk)
  override suspend fun doWork(): Result {
    return withContext(Dispatchers.IO) {
      try {
        val response = weatherRepository.getCurrentWeather(getLatitude(), getLongitude())

        if (response.isSuccess) {
          val weatherInfo = parseWeatherInfo(response.getOrNull())
          showNotification(weatherInfo)
          Result.success()
        } else {
          Result.failure()
        }
      } catch (e: Exception) {
        e.printStackTrace()
        Result.retry()
      }
    }
  }
  val intent = Intent(applicationContext, MainActivity::class.java).apply {
    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
  }
  val pendingIntent = PendingIntent.getActivity(
    applicationContext,
    0,
    intent,
    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
  )

  private fun parseWeatherInfo(weatherResponse: CoordinatesResponse?): String {
    return weatherResponse?.let { it ->
      it.weather?.firstOrNull()?.id.let {
        icon = getWeatherIcon(it?: 900)
        wishYou = applicationContext.resources.getString(getMessageForNotification(it?: 900))
      }
      it.weather?.firstOrNull()?.description.let {
     }
      val temp = ((it.main?.temp)?.minus(273)?.toInt() ?: 0)
      val description = it.weather?.firstOrNull()?.description ?: "Unknown weather"
      "${applicationContext.getString(R.string.temperature)}: $temp°C, ${applicationContext.getString(R.string.condition)}: $description\n$wishYou \uD83C\uDF1F"
    } ?: "Unable to fetch weather data"
  }

  private fun showNotification(weatherInfo: String) {
    val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    val channelId = "weather_channel"
    if (notificationManager.getNotificationChannel(channelId) == null) {
      val channel = NotificationChannel(
        channelId,
        "Weather Updates",
        NotificationManager.IMPORTANCE_HIGH
      )
      notificationManager.createNotificationChannel(channel)
    }

    val notification = NotificationCompat.Builder(applicationContext, channelId)
      .setSmallIcon(R.drawable.icon)
      .setContentTitle(applicationContext.resources.getString(R.string.weather_info))
      .setDefaults(0)
      .setVisibility(NotificationCompat.VISIBILITY_SECRET)
      .setContentText(weatherInfo)
      .setPriority(NotificationCompat.PRIORITY_MAX)
      .setContentIntent(pendingIntent)
      .setLargeIcon(Icon.createWithResource(applicationContext, icon))
      .build()

    notificationManager.notify(1, notification)
  }
}
