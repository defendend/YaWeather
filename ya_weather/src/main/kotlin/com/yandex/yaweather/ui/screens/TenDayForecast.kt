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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yandex.yaweather.R
import com.yandex.yaweather.utils.getCurrentDayOfWeek
import com.yandex.yaweather.utils.getDayOfWeek
import com.yandex.yaweather.viewModel.WeatherUiState

@Composable
fun TenDayForecast(uiState: WeatherUiState) {
  var itemCount by remember { mutableIntStateOf(5) }
  var showMoreButton by remember { mutableStateOf(true) }
  val currentDay = remember { getCurrentDayOfWeek() }
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(16.dp))
      .padding(16.dp)
  ) {
    Text(
      text = stringResource(R.string.fivedayforecast),
      fontSize = 16.sp,
      color = MaterialTheme.colorScheme.inversePrimary,
      fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(8.dp))
    repeat(itemCount) { index ->
      val dayOfWeek = getDayOfWeek(currentDay + index)
      Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 12.dp)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Spacer(modifier = Modifier.width(4.dp))
          Text(text = dayOfWeek, color = MaterialTheme.colorScheme.inversePrimary, fontSize = 16.sp)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            painter = painterResource(
              when {
                uiState.description == "shower rain" -> R.drawable.showers_fill
                uiState.description.contains("rain", ignoreCase = true) -> R.drawable.rainy_fill
                uiState.description.contains("clear", ignoreCase = true) -> R.drawable.sun_fill
                uiState.description.contains(
                  "clouds", ignoreCase = true
                ) -> R.drawable.cloud_windy_fill

                uiState.description.contains(
                  "thunderstorm", ignoreCase = true
                ) -> R.drawable.thunderstorms_fill

                uiState.description.contains("snow", ignoreCase = true) -> R.drawable.snowy_fill
                uiState.description.contains("fog", ignoreCase = true) -> R.drawable.mist_fill
                uiState.description.contains("mist", ignoreCase = true) -> R.drawable.mist_fill
                else -> R.drawable.sun_fill
              }
            ),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.inversePrimary,
            modifier = Modifier.size(14.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(uiState.temperatureMin, color = MaterialTheme.colorScheme.inversePrimary, fontSize = 14.sp)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
          Spacer(modifier = Modifier.width(4.dp))
          Text(uiState.temperatureMax, color = MaterialTheme.colorScheme.inversePrimary, fontSize = 16.sp)
        }
      }
    }
    Spacer(modifier = Modifier.height(8.dp))
    if (showMoreButton) {
      TextButton(
        onClick = {
          if (itemCount < 10) {
            itemCount += 5
          }
          showMoreButton = false
        }, modifier = Modifier.align(Alignment.CenterHorizontally)
      ) {
        Text(
          text = stringResource(R.string.showmore), color = MaterialTheme.colorScheme.inversePrimary
        )
      }
    } else {
      TextButton(
        onClick = {
          itemCount = 5
          showMoreButton = true
        }, modifier = Modifier.align(Alignment.CenterHorizontally)
      ) {
        Text(
          text = stringResource(R.string.hide), color = MaterialTheme.colorScheme.inversePrimary
        )
      }
    }
  }
}