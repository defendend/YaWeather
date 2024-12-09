package com.yandex.yaweather.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yandex.yaweather.R
import com.yandex.yaweather.viewModel.WeatherUiState.WidgetsUiState


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
  LazyRow(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
    itemsIndexed(widgetData) { index, item ->
      WidgetBox(widgetName[index], item)
    }
  }
}

@Composable
fun WidgetBox(title: String, value: String?) {
  Column(
    modifier = Modifier
      .height(150.dp)
      .width(150.dp)
      .padding(bottom = 16.dp)
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
