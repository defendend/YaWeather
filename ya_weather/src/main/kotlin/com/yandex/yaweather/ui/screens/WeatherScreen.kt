package com.yandex.yaweather.ui.screens

import android.annotation.SuppressLint
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.yandex.yaweather.R
import com.yandex.yaweather.handler.WeatherScreenAction
import com.yandex.yaweather.viewModel.WeatherUiState
import com.yandex.yaweather.viewModel.WeatherUiState.WidgetsUiState

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun WeatherScreen(uiState: WeatherUiState, action: (WeatherScreenAction) -> Unit) {
  Scaffold(topBar = {
    TopBar(Modifier, action)
  }) {  innerPaddining ->
    Box(modifier = Modifier
      .fillMaxSize()
      .background(color = Color.Transparent)) {
      Image(
        painter = rememberDrawablePainter(
          drawable = getDrawable(
            LocalContext.current,
            R.drawable.mist
          )
        ),
        contentDescription = "Background Image",
        modifier = Modifier.fillMaxSize().blur(10.dp), contentScale = ContentScale.Crop
      )
    }
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(color = Color.Transparent)
        .padding(16.dp)
    ) {
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .background(color = Color.Transparent)
          .padding(top = 54.dp)
          .alpha(0.75f),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        item {
          Column(
            modifier = Modifier, horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Text(
              text = uiState.cityName, fontSize = 24.sp, color = Color.White, fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = uiState.temperature, fontSize = 64.sp, color = Color.White, fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = "Макс. ${uiState.temperatureMax}°  Мин. ${uiState.temperatureMin}°", fontSize = 16.sp, color = Color.LightGray
            )
          }
        }
        item {
          HourlyForecast(modifier = Modifier, uiState)
        }

        item {
          TenDayForecast(modifier = Modifier, uiState)
        }

        item {
          MapWidget(Modifier, action)
        }

        item {
          Widgets(modifier = Modifier, uiState.widgetsUiState)
        }
      }
    }
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(Color.Gray.copy(alpha = 0.75f))
    systemUiController.setNavigationBarColor(Color.Gray.copy(alpha = 0.75f))
  }
}

@SuppressLint("DefaultLocale")
@Composable
fun HourlyForecast(modifier: Modifier, uiState: WeatherUiState) {
  Column(
    modifier = modifier
      .fillMaxWidth().alpha(0.75f)
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
      horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = modifier.fillMaxWidth()
    ) {
      items(50) { index ->
        Column(
          horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier.padding(start = 8.dp)
        ) {
          Spacer(modifier = modifier.height(4.dp))
          Text(uiState.temperature, color = Color.White, fontSize = 18.sp)
          Icon(
            painter = painterResource(id = R.drawable.heavy_showers_fill),
            contentDescription = null,
            tint = Color.LightGray,
            modifier = modifier.size(24.dp)
          )
          Spacer(modifier = modifier.height(4.dp))
          Text(
            text = "${String.format("%02d", (index + 1) % 24)} h", color = Color.LightGray, fontSize = 14.sp
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
    Row {
      IconButton(onClick = {}) {
        Icon(
          imageVector = Icons.Default.Settings, contentDescription = "Settings"
        )
      }
      IconButton(onClick = {
        action.invoke(WeatherScreenAction.OpenInfoAction)
      }) {
        Icon(
          imageVector = Icons.Default.Info, contentDescription = "Info"
        )
      }
    }
    IconButton(onClick = { action(WeatherScreenAction.AddCityAction) }) {
      Icon(
        imageVector = Icons.Default.Add, contentDescription = "Add"
      )
    }
  }
}

@Composable
fun TenDayForecast(modifier: Modifier, uiState: WeatherUiState) {
  var itemCount by remember { mutableIntStateOf(5) }
  var showMoreButton by remember { mutableStateOf(true) }

  Column(
    modifier = Modifier
      .fillMaxWidth()
      .background(Color.DarkGray, RoundedCornerShape(16.dp))
      .padding(16.dp)
  ) {
    Text(
      text = "5-ДНЕВНЫЙ ПРОГНОЗ", fontSize = 16.sp, color = Color.LightGray, fontWeight = FontWeight.Bold
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
        }, modifier = Modifier.align(Alignment.CenterHorizontally)
      ) {
        Text(
          text = "Показать ещё", color = Color.LightGray
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
          text = "Скрыть", color = Color.LightGray
        )
      }
    }
  }
}

@Composable
fun MapWidget(action1: Modifier, action: (WeatherScreenAction) -> Unit) {
  val toshekent = LatLng(41.2995, 69.2401)
  val cameraPositionState = rememberCameraPositionState {
    position = CameraPosition.fromLatLngZoom(toshekent, 10f)
  }

  GoogleMap(
    modifier = Modifier
      .fillMaxWidth()
      .height(200.dp)
      .background(Color.Gray)
      .clip(RoundedCornerShape(16.dp)),
    cameraPositionState = cameraPositionState,
    properties = MapProperties(isMyLocationEnabled = false),
    onMapClick = {
      action(WeatherScreenAction.OpenMapAction)

    },
    uiSettings = MapUiSettings(
      zoomGesturesEnabled = false,
      zoomControlsEnabled = false,
      scrollGesturesEnabled = false,
      compassEnabled = false,
      rotationGesturesEnabled = false,
      tiltGesturesEnabled = false
    )
  ) {
    Marker(state = MarkerState(position = toshekent), title = "", snippet = "", onClick = {
      action(WeatherScreenAction.OpenMapAction)
      true
    })
  }

}
@Composable
fun Widgets(modifier: Modifier = Modifier, uiState: WidgetsUiState) {
  val widgetData = listOf(
    "Влажность" to uiState.humidity,
    "Ощущается" to uiState.feelsLike,
    "Ветер" to uiState.windSpeed,
    "Давление" to uiState.sealevel
  )

  LazyRow(
    modifier = modifier
      .fillMaxWidth()
      .padding(8.dp),
    horizontalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    items(widgetData) { (title, value) ->
      WidgetBox(title = title, value = value)
    }
  }
}

@Composable
fun WidgetBox(title: String, value: String?) {
  Column(
    modifier = Modifier
      .height(150.dp)
      .width(150.dp)
      .background(Color.DarkGray.copy(alpha = 0.75f), RoundedCornerShape(16.dp))
      .padding(8.dp),

    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.SpaceEvenly
  ) {
    Text(
      text = title,
      fontSize = 16.sp,
      color = Color.White,
      fontWeight = FontWeight.SemiBold

    )
    Text(
      text = value ?: "-",
      fontSize = 28.sp,
      color = Color.White,
      fontWeight = FontWeight.Bold
    )
  }
}