package com.yandex.yaweather.utils

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