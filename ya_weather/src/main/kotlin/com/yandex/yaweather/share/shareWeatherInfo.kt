package com.yandex.yaweather.share

import android.content.Context
import android.content.Intent
import com.yandex.yaweather.viewModel.WeatherUiState

fun shareWeatherInfo(context: Context, weatherInfo: String){
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
      type="text/plain"
      putExtra(Intent.EXTRA_SUBJECT, "Об-ҳаво маълумотлари") // Sarlavha
      putExtra(Intent.EXTRA_TEXT, weatherInfo)

    }
  context.startActivity(Intent.createChooser(shareIntent,"Ulashish"))


}