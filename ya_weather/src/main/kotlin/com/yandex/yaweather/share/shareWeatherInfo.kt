package com.yandex.yaweather.share

import android.content.Context
import android.content.Intent

fun shareWeatherInfo(context: Context, weatherInfo: String){
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
      type="text/plain"
      putExtra(Intent.EXTRA_SUBJECT, "Погодная информация")
      putExtra(Intent.EXTRA_TEXT, weatherInfo)

    }
  context.startActivity(Intent.createChooser(shareIntent,"отправлять"))


}