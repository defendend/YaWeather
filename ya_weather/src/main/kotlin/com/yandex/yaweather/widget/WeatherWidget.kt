package com.yandex.yaweather.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.yandex.yaweather.R

class WeatherWidget : AppWidgetProvider() {
  override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
    for (appWidgetId in appWidgetIds) {
      updateAppWidget(context, appWidgetManager, appWidgetId)
    }
  }

  override fun onReceive(context: Context, intent: Intent) {
    super.onReceive(context, intent)
    if (intent.action == AppWidgetManager.ACTION_APPWIDGET_UPDATE) {
      val appWidgetManager = AppWidgetManager.getInstance(context)
      val appWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS)
      appWidgetIds?.forEach { appWidgetId ->
        updateAppWidget(context, appWidgetManager, appWidgetId)
      }
    }
  }
}

internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
  val preferences = context.getSharedPreferences("widget", Context.MODE_PRIVATE)
  val name = preferences.getString("name", "Unknown City")
  var description = preferences.getString("description", "No description")
  val temperature = preferences.getInt("temp", 0)
  val sunrise = preferences.getLong("sunrise", 0L)
  val sunset = preferences.getLong("sunset", 0L)
  val sunriseTime = formatTime(sunrise)
  val sunsetTime = formatTime(sunset)
  val backgroundResource = when {
    description == "shower rain" -> R.drawable.fall_rain
    description?.contains("rain", ignoreCase = true) == true -> R.drawable.rain_gif
    description?.contains("clear", ignoreCase = true) == true -> R.drawable.clear_sky
    description?.contains("clouds", ignoreCase = true) == true -> R.drawable.clouds_gif
    description?.contains("thunderstorm", ignoreCase = true) == true -> R.drawable.thunderstormm
    description?.contains("snow", ignoreCase = true) == true -> R.drawable.snow_gif
    description?.contains("fog", ignoreCase = true) == true -> R.drawable.mist
    description?.contains("mist", ignoreCase = true) == true -> R.drawable.mist
    else -> R.drawable.clear_sky
  }
  val views = RemoteViews(context.packageName, R.layout.weather_widget).apply {
    setTextViewText(R.id.tv_name, name)
    setTextViewText(R.id.tv_description, description)
    setTextViewText(R.id.tv_temperature, kelvinToCelsius(temperature).toString() + '\u00B0' + "C")
    setTextViewText(R.id.tv_sunrise, "Sunrise: $sunriseTime")
    setTextViewText(R.id.tv_sunset, "Sunset: $sunsetTime")
    setInt(R.id.container_layout, "setBackgroundResource", backgroundResource)
  }

  appWidgetManager.updateAppWidget(appWidgetId, views)
}

private fun formatTime(timeInMillis: Long): String {
  return if (timeInMillis > 0) {
    val sdf = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault())
    val date = java.util.Date(timeInMillis * 1000)
    sdf.format(date)
  } else {
    "N/A"
  }
}

private fun kelvinToCelsius(kelvin: Int): Int {
  return (kelvin - 273.15).toInt()
}