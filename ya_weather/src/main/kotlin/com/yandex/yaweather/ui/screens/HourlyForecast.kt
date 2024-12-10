package com.yandex.yaweather.ui.screens

//noinspection UsingMaterialAndMaterial3Libraries
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yandex.yaweather.R
import com.yandex.yaweather.celsius
import com.yandex.yaweather.data.network.WeatherByHour
import com.yandex.yaweather.utils.weatherIconForForecast

@SuppressLint("DefaultLocale")
@Composable
fun HourlyForecast(weatherByHour: List<WeatherByHour>) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .alpha(0.75f)
      .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(16.dp))
      .padding(start = 16.dp, top = 16.dp, bottom = 16.dp, end = 16.dp)
  ) {
    if (weatherByHour.isEmpty()) {
      CircularProgressIndicator(
        modifier = Modifier.align(Alignment.CenterHorizontally),
        color = MaterialTheme.colorScheme.inversePrimary,
        strokeWidth = 4.dp
      )
    } else {
      Text(
        modifier = Modifier.padding(start = 8.dp),
        text = stringResource(R.string.hourly_forecast),
        fontSize = 16.sp,
        color = MaterialTheme.colorScheme.inversePrimary,
        fontWeight = FontWeight.Bold
      )
      Spacer(modifier = Modifier.height(8.dp))
      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()
      ) {
        items(weatherByHour) { index ->
          Column(
            horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(start = 8.dp)
          ) {
            Spacer(modifier = Modifier.height(4.dp))
            index.temp?.let {
              val temperatureInCelsius = (it - 273.15).toInt()
              Text(
                text = if (celsius.value) {
                  "$temperatureInCelsius°C"
                } else {
                  val fahrenheit = temperatureInCelsius * 9 / 5 + 32
                  "${fahrenheit}°F"
                }, color = MaterialTheme.colorScheme.inversePrimary, fontSize = 18.sp
              )
            }
            Icon(
              painter = painterResource(weatherIconForForecast(index)),
              contentDescription = null,
              tint = MaterialTheme.colorScheme.inversePrimary,
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            index.timeStamp?.let {
              val formattedTime = it.split("T").getOrNull(1)?.split(":")?.getOrNull(0)?.plus("h") ?: "N/A"
              Text(formattedTime, color = MaterialTheme.colorScheme.inversePrimary, fontSize = 12.sp)
            }
          }
        }
      }
    }
  }
}
