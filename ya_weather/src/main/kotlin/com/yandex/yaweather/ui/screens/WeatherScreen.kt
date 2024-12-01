package com.yandex.yaweather.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.yandex.yaweather.R
import com.yandex.yaweather.handler.WeatherScreenAction
import com.yandex.yaweather.viewModel.WeatherUiState
import com.yandex.yaweather.viewModel.WeatherUiState.WidgetsUiState

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun WeatherScreen(uiState: WeatherUiState, action: (WeatherScreenAction) -> Unit) {
  Scaffold(
    topBar = {
      TopBar(Modifier, action)
    }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(color = Color.Gray)
        .padding(16.dp)
    ) {
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .background(color = Color.Gray)
          .padding(top = 54.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        item {
          Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Text(
              text = uiState.cityName,
              fontSize = 24.sp,
              color = Color.White,
              fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = uiState.temperature,
              fontSize = 64.sp,
              color = Color.White,
              fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = "Макс. 14° Мин. 11°",
              fontSize = 16.sp,
              color = Color.LightGray
            )
          }
        }

        item {
          HourlyForecast(modifier = Modifier, uiState)
        }

        item {
          TenDayForecast()
        }
      }
    }
  }
  val systemUiController = rememberSystemUiController()
  systemUiController.setStatusBarColor(Color.Gray)
  systemUiController.setNavigationBarColor(Color.Gray)
}

@Composable
fun HourlyForecast(modifier: Modifier, uiState: WeatherUiState) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .background(Color.DarkGray, RoundedCornerShape(16.dp))
      .padding(start = 16.dp, top = 16.dp, bottom = 16.dp)
  ) {
    Text(
      modifier = modifier.padding(start = 8.dp),
      text = "3 ЧАСОВОЙ ПРОГНОЗ",
      fontSize = 16.sp,
      color = Color.LightGray,
      fontWeight = FontWeight.Bold
    )
    Spacer(modifier = modifier.height(8.dp))

    LazyRow(
      horizontalArrangement = Arrangement.spacedBy(16.dp),
      modifier = modifier.fillMaxWidth()
    ) {
      items(50) { index ->
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = modifier.padding(start = 8.dp)
        ) {
          Spacer(modifier = modifier.height(4.dp))
          Text(text = "10°", color = Color.White, fontSize = 18.sp)
          Icon(
            painter = painterResource(id = R.drawable.heavy_showers_fill),
            contentDescription = null,
            tint = Color.LightGray,
            modifier = modifier.size(24.dp)
          )
          Spacer(modifier = modifier.height(4.dp))
          Text(
            text = "${String.format("%02d", (index + 1) % 24)} h",
            color = Color.LightGray,
            fontSize = 14.sp
          )
        }
      }
    }
  }
}

@Composable
fun TopBar(modifier: Modifier, action: (WeatherScreenAction) -> Unit) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .padding(start = 8.dp, end = 8.dp, top = 36.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    IconButton(onClick = {}) {
      Icon(
        imageVector = Icons.Default.Settings,
        contentDescription = "Settings"
      )
    }
    IconButton(onClick = { action(WeatherScreenAction.AddCityAction) }) {
      Icon(
        imageVector = Icons.Default.Add,
        contentDescription = "Add"
      )
    }
  }
}

@Composable
fun TenDayForecast() {
  var itemCount by remember { mutableIntStateOf(5) }
  var showMoreButton by remember { mutableStateOf(true) }

  Column(
    modifier = Modifier
      .fillMaxWidth()
      .background(Color.DarkGray, RoundedCornerShape(16.dp))
      .padding(16.dp)
  ) {
    Text(
      text = "10-ДНЕВНЫЙ ПРОГНОЗ",
      fontSize = 16.sp,
      color = Color.LightGray,
      fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(8.dp))

    repeat(itemCount) {
      Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 12.dp)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Spacer(modifier = Modifier.width(4.dp))
          Text(text = "Сб", color = Color.White, fontSize = 16.sp)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            painter = painterResource(id = R.drawable.heavy_showers_fill),
            contentDescription = null,
            tint = Color.LightGray,
            modifier = Modifier.size(14.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(text = "11°", color = Color.LightGray, fontSize = 14.sp)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
          Spacer(modifier = Modifier.width(4.dp))
          Text(text = "14°", color = Color.White, fontSize = 16.sp)
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
        },
        modifier = Modifier.align(Alignment.CenterHorizontally)
      ) {
        Text(
          text = "Показать ещё",
          color = Color.LightGray
        )
      }
    } else {
      TextButton(
        onClick = {
          itemCount = 5
          showMoreButton = true
        },
        modifier = Modifier.align(Alignment.CenterHorizontally)
      ) {
        Text(
          text = "Скрыть",
          color = Color.LightGray
        )
      }
    }
  }
}

@Composable
fun Wigets(modifier: Modifier, uiState: WidgetsUiState) {
}