package com.yandex.yaweather.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yandex.yaweather.R
import com.yandex.yaweather.celsius
import com.yandex.yaweather.pressurePHa
import com.yandex.yaweather.viewModel.WeatherUiState.WidgetsUiState
import com.yandex.yaweather.windSpeedMS

@Composable
fun Widgets(uiState: WidgetsUiState) {
  val widgetName = listOf(
    stringResource(R.string.humidity),
    stringResource(R.string.feelsLike),
    stringResource(R.string.settings_bottom_sheet_wind),
    stringResource(R.string.settings_bottom_sheet_pressure)
  )
  val widgetData = listOf(
    uiState.humidity, uiState.feelsLike, uiState.windSpeed, uiState.sealevel
  )

  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    LazyRow(
      modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      item {
        Column(
          modifier = Modifier
            .height(150.dp)
            .width(150.dp)
            // .padding(bottom = 16.dp)
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.75f), RoundedCornerShape(16.dp)),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.SpaceEvenly
        ) {
          Text(
            text = widgetName[0],
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.inversePrimary,
            fontWeight = FontWeight.SemiBold
          )
          Text(
            text = (widgetData[0] + " %") ?: "-",
            fontSize = 28.sp,
            color = MaterialTheme.colorScheme.inversePrimary,
            fontWeight = FontWeight.Bold
          )
        }
      }
      item {
        Column(
          modifier = Modifier
            .height(150.dp)
            .width(150.dp)
            // .padding(bottom = 16.dp)
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.75f), RoundedCornerShape(16.dp)),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.SpaceEvenly
        ) {
          Text(
            text = widgetName[1],
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.inversePrimary,
            fontWeight = FontWeight.SemiBold
          )
          Text(
            text = if (celsius.value) {
              (widgetData[1] + '\u00B0' + "C") ?: "-"
            } else {
              val fahrenheit = widgetData[1].toInt() * 9 / 5 + 32
              "${fahrenheit}°F"
            }, fontSize = 28.sp, color = MaterialTheme.colorScheme.inversePrimary, fontWeight = FontWeight.Bold
          )
        }
      }
      item {
        Column(
          modifier = Modifier
            .height(150.dp)
            .width(150.dp)
            // .padding(bottom = 16.dp)
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.75f), RoundedCornerShape(16.dp)),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.SpaceEvenly
        ) {
          Text(
            text = widgetName[2],
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.inversePrimary,
            fontWeight = FontWeight.SemiBold
          )
          val wind = widgetData[2].toFloat()
          Text(
            text = if (windSpeedMS.value) {
              ((wind / 3.6).toString().substring(
                0, (wind / 3.6).toString().indexOf('.') + 3
              ) + "\n" + stringResource(R.string.settings_bottom_sheet_wind_speed2)) ?: "-"
            } else {
              (wind.toString() + "\n" + stringResource(R.string.settings_bottom_sheet_wind_speed1)) ?: "-"
            },
            fontSize = 28.sp,
            color = MaterialTheme.colorScheme.inversePrimary,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
          )
        }
      }
      item {
        Column(
          modifier = Modifier
            .height(150.dp)
            .width(150.dp)
            // .padding(bottom = 16.dp)
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.75f), RoundedCornerShape(16.dp)),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.SpaceEvenly
        ) {
          Text(
            text = widgetName[3],
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.inversePrimary,
            fontWeight = FontWeight.SemiBold
          )
          Text(
            text = if (pressurePHa.value) {
              (widgetData[3] + "\n" + stringResource(R.string.settings_bottom_sheet_pressure1)) ?: "-"
            } else {
              ((widgetData[3].toInt() * 0.7501).toString() + "\n" + stringResource(R.string.settings_bottom_sheet_pressure2))
                ?: "-"
            },
            fontSize = 28.sp,
            color = MaterialTheme.colorScheme.inversePrimary,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
          )
        }
      }
    }

    SunriseSunsetBox(
      sunrise = uiState.sunrise, sunset = uiState.sunset
    )
  }
}

@Composable
fun WidgetBox(title: String, value: String?) {
  Column(
    modifier = Modifier
      .height(150.dp)
      .width(150.dp)
      // .padding(bottom = 16.dp)
      .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.75f), RoundedCornerShape(16.dp)),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.SpaceEvenly
  ) {
    Text(
      text = title, fontSize = 16.sp, color = MaterialTheme.colorScheme.inversePrimary, fontWeight = FontWeight.SemiBold
    )
    Text(
      text = value ?: "-",
      fontSize = 28.sp,
      color = MaterialTheme.colorScheme.inversePrimary,
      fontWeight = FontWeight.Bold
    )
  }
}

@Composable
fun SunriseSunsetBox(sunrise: Long, sunset: Long) {
  val sunriseTime = formatTime(sunrise)
  val sunsetTime = formatTime(sunset)

  Row(
    modifier = Modifier
      .height(150.dp)
      .fillMaxWidth()
      .padding(bottom = 16.dp)
      .background(
        MaterialTheme.colorScheme.primary.copy(alpha = 0.75f), RoundedCornerShape(16.dp)
      ), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically
  ) {
    SunriseSunsetItem(
      title = stringResource(R.string.sunrise), time = sunriseTime, icon = R.drawable.sun_fill
    )
    SunriseSunsetItem(
      title = stringResource(R.string.sunset), time = sunsetTime, icon = R.drawable.haze_fill
    )
  }
}

@Composable
fun SunriseSunsetItem(title: String, time: String, icon: Int) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Icon(
      painter = painterResource(id = icon),
      contentDescription = title,
      tint = MaterialTheme.colorScheme.inversePrimary,
      modifier = Modifier.size(32.dp)
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
      text = title, fontSize = 16.sp, color = MaterialTheme.colorScheme.inversePrimary, fontWeight = FontWeight.SemiBold
    )
    Text(
      text = time, fontSize = 28.sp, color = MaterialTheme.colorScheme.inversePrimary, fontWeight = FontWeight.Bold
    )
  }
}

fun formatTime(timestamp: Long): String {
  val dateFormat = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault())
  return dateFormat.format(java.util.Date(timestamp * 1000))
}