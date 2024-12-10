package com.yandex.yaweather.ui.screens

//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yandex.yaweather.R
import com.yandex.yaweather.celsius
import com.yandex.yaweather.data.network.Per3Hour
import com.yandex.yaweather.utils.getDayOfWeekFromDate
import com.yandex.yaweather.utils.getForecastWeatherByDay

@Composable
fun FiveDayForecast(forecast: List<Per3Hour>) {
  val elementsPerDay = 8
  val daysCount = 5

  Column(
    modifier = Modifier
      .fillMaxWidth()
      .background(
        MaterialTheme.colorScheme.primary, RoundedCornerShape(16.dp)
      )
      .padding(16.dp)
  ) {
    if (forecast.isEmpty()) {
      CircularProgressIndicator(
        modifier = Modifier.align(Alignment.CenterHorizontally),
        color = MaterialTheme.colorScheme.inversePrimary,
        strokeWidth = 4.dp
      )
    } else {
      Text(
        text = stringResource(R.string.fivedayforecast),
        fontSize = 16.sp,
        color = MaterialTheme.colorScheme.inversePrimary,
        fontWeight = FontWeight.Bold
      )
      Spacer(modifier = Modifier.height(8.dp))

      repeat(daysCount) { dayIndex ->
        val initialPosition = dayIndex * elementsPerDay
        val endPosition = (initialPosition + elementsPerDay).coerceAtMost(forecast.size)

        if (initialPosition < forecast.size) {

          val dayForecast = forecast.subList(initialPosition, endPosition)
          val text = dayForecast.firstOrNull()?.dtTxt?.split(" ")?.get(0)
          val minTemp = dayForecast.mapNotNull { it.main?.temp }.minOrNull()?.toInt()
          val maxTemp = dayForecast.mapNotNull { it.main?.temp }.maxOrNull()?.toInt()
          val icon = getForecastWeatherByDay(dayForecast)

          Box(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 12.dp), contentAlignment = Alignment.Center
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically, modifier = Modifier.align(Alignment.CenterStart)
            ) {
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = getDayOfWeekFromDate(LocalContext.current, text ?: "") ?: "N/A",
                color = MaterialTheme.colorScheme.inversePrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
              )
            }

            Row(
              verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center
            ) {
              Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.inversePrimary,
                modifier = Modifier.size(20.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = if (celsius.value) {
                  val celsiusTemp = minTemp?.let { it - 273.15 }?.toInt() ?: "N/A"
                  "$celsiusTemp°C"
                } else {
                  val fahrenheitTemp = minTemp?.let { (it - 273.15) * 9 / 5 + 32 }?.toInt() ?: "N/A"
                  "$fahrenheitTemp°F"
                }, color = MaterialTheme.colorScheme.inversePrimary, fontSize = 20.sp
              )
            }
            Row(
              verticalAlignment = Alignment.CenterVertically, modifier = Modifier.align(Alignment.CenterEnd)
            ) {
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = if (celsius.value) {
                  val celsiusTemp = maxTemp?.let { it - 273.15 }?.toInt() ?: "N/A"
                  "$celsiusTemp°C"
                } else {
                  val fahrenheitTemp = maxTemp?.let { (it - 273.15) * 9 / 5 + 32 }?.toInt() ?: "N/A"
                  "$fahrenheitTemp°F"
                }, color = MaterialTheme.colorScheme.inversePrimary, fontSize = 20.sp
              )
            }
          }
        }
      }
    }
  }
}