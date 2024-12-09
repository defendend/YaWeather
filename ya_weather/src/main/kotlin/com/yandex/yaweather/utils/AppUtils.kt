package com.yandex.yaweather.utils

import android.content.Context
import android.content.res.Resources
import com.yandex.yaweather.R
import com.yandex.yaweather.data.network.WeatherByHour
import java.util.Calendar

fun getCurrentDayOfWeek(): Int {
  return Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1
}


fun getDayOfWeek(dayIndex: Int): String {
  val daysOfWeek = listOf("Вс", "Пн", "Вт", "Ср", "Чт", "Пт", "Сб")
  return daysOfWeek[dayIndex % 7]
}

fun weatherIconForForecast(weatherByHour: WeatherByHour): Int {
  return when (weatherByHour.weather?.code) {
    in 200..202 -> R.drawable.hail_fill
    in 230..233 -> R.drawable.thunderstorms_fill
    in 300..302 -> R.drawable.rainy_fill
    in 500..522 -> R.drawable.showers_fill
    in 600..623 -> R.drawable.snowy_fill
    in 700..751 -> R.drawable.mist_fill
    800 -> R.drawable.sun_fill
    in 801..804 -> R.drawable.cloudy_fill
    else -> R.drawable.blaze_fill    // Неизвестный код
  }
}

fun weatherBackground(code: Int): Int {
  return when (code) {
    in 200..202 -> R.drawable.fall_rain
    in 230..233 -> R.drawable.thunderstormm
    in 300..302 -> R.drawable.fall_rain
    in 500..522 -> R.drawable.fall_rain
    in 600..623 -> R.drawable.snow_gif
    in 700..751 -> R.drawable.mist
    800 -> R.drawable.clear_sky
    in 801..804 -> R.drawable.scaffered_clouds
    else -> R.drawable.scaffered_clouds   // Неизвестный код
  }
}

fun weatherEmojiForForecast(weatherByHour: WeatherByHour): String {
  val code = weatherByHour.weather?.code?: 0

  return when (code) {
    in 200..202 -> "🌨️"
    in 230..233 -> "⛈️"
    in 300..302 -> "🌧️"
    in 500..522 -> "🌧️"
    in 600..623 -> "❄️"
    in 700..751 -> "🌫️"
    800 -> "☀️"
    in 801..804 -> "☁️"
    else -> "🔥"
  }
}

fun getWeatherDescription(code: Int, context: Context): String {
  val resources = context.resources
  return when (code) {
    // Thunderstorm group (2xx)
    200 -> resources.getString(R.string.thunderstorm_light_rain)
    201 -> resources.getString(R.string.thunderstorm_rain)
    202 -> resources.getString(R.string.thunderstorm_heavy_rain)
    210 -> resources.getString(R.string.light_thunderstorm)
    211 -> resources.getString(R.string.thunderstorm)
    212 -> resources.getString(R.string.heavy_thunderstorm)
    221 -> resources.getString(R.string.ragged_thunderstorm)
    230 -> resources.getString(R.string.thunderstorm_light_dizzle)
    231 -> resources.getString(R.string.thunderstorm_dizzle)
    232 -> resources.getString(R.string.thunderstorm_heavy_dizzle)

    // Drizzle group (3xx)
    300 -> resources.getString(R.string.light_intensity_dizzle)
    301 -> resources.getString(R.string.dizzle)
    302 -> resources.getString(R.string.heavy_intensity_dizzle)
    310 -> resources.getString(R.string.light_intensity_dizzle_rain)
    311 -> resources.getString(R.string.dizzle_rain)
    312 -> resources.getString(R.string.heavy_intensity_dizzle_rain)
    313 -> resources.getString(R.string.shower_rain_and_dizzle)
    314 -> resources.getString(R.string.heavy_shower_rain_and_dizzle)
    321 -> resources.getString(R.string.shower_dizzle)

    // Rain group (5xx)
    500 -> resources.getString(R.string.light_rain)
    501 -> resources.getString(R.string.moderate_rain)
    502 -> resources.getString(R.string.heavy_intensity_rain)
    503 -> resources.getString(R.string.very_heavy_rain)
    504 -> resources.getString(R.string.extreme_rain)
    511 -> resources.getString(R.string.freezing_rain)
    520 -> resources.getString(R.string.light_intensity_shower_rain)
    521 -> resources.getString(R.string.shower_rain)
    522 -> resources.getString(R.string.heavy_intensity_shower_rain)
    531 -> resources.getString(R.string.ragged_shower_rain)

    // Snow group (6xx)
    600 -> resources.getString(R.string.light_snow)
    601 -> resources.getString(R.string.snow)
    602 -> resources.getString(R.string.heavy_snow)
    611 -> resources.getString(R.string.sleet)
    612 -> resources.getString(R.string.light_shower_sleet)
    613 -> resources.getString(R.string.shower_sleet)
    615 -> resources.getString(R.string.light_rain_and_snow)
    616 -> resources.getString(R.string.rain_and_snow)
    620 -> resources.getString(R.string.light_shower_snow)
    621 -> resources.getString(R.string.shower_snow)
    622 -> resources.getString(R.string.heavy_shower_snow)

    // Atmosphere group (7xx)
    701 -> resources.getString(R.string.mist)
    711 -> resources.getString(R.string.smoke)
    721 -> resources.getString(R.string.haze)
    731 -> resources.getString(R.string.dust)
    741 -> resources.getString(R.string.fog)
    751 -> resources.getString(R.string.sand)
    761 -> resources.getString(R.string.dust_p)
    762 -> resources.getString(R.string.ash)
    771 -> resources.getString(R.string.squall)
    781 -> resources.getString(R.string.tornado)

    // Clear group (800)
    800 -> resources.getString(R.string.clear_sky)

    // Clouds group (80x)
    801 -> resources.getString(R.string.few_clouds)
    802 -> resources.getString(R.string.scattered_clouds)
    803 -> resources.getString(R.string.broken_clouds)
    804 -> resources.getString(R.string.overcast_clouds)

    // Default case
    else -> resources.getString(R.string.unknown)
  }
}

private fun getWeatherIconAndMessage(weatherCondition: String): Pair<Int, Int> {
  val icon: Int
  val messageResId: Int

  when (weatherCondition) {
    "Clouds" -> {
      icon = R.drawable.cloudsday
      messageResId = R.string.clouds_message
    }
    "Snow" -> {
      icon = R.drawable.snowday
      messageResId = R.string.snow_message
    }
    "Rain" -> {
      icon = R.drawable.rainyday
      messageResId = R.string.rain_message
    }
    "Clear" -> {
      icon = R.drawable.cloudy
      messageResId = R.string.clear_message
    }
    "Drizzle" -> {
      icon = R.drawable.cloudy
      messageResId = R.string.drizzle_message
    }
    "Thunderstorm" -> {
      icon = R.drawable.thunderstormm
      messageResId = R.string.thunderstorm_message
    }
    "Mist" -> {
      icon = R.drawable.cloudymin
      messageResId = R.string.mist_message
    }
    else -> {
      icon = R.drawable.sasha
      messageResId = R.string.perfect_time_for_walk
    }
  }

  return Pair(icon, messageResId)
}