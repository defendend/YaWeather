package com.yandex.yaweather.ui.screens

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.UrlTileProvider
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.TileOverlay
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberTileOverlayState
import com.yandex.yaweather.R
import com.yandex.yaweather.Theme.BackSwitch
import com.yandex.yaweather.Theme.BackTime
import com.yandex.yaweather.Theme.Green
import com.yandex.yaweather.Theme.SettingsAnotherBack
import com.yandex.yaweather.Theme.SettingsBack
import com.yandex.yaweather.Theme.SettingsItemBack
import com.yandex.yaweather.Theme.SettingsSelected
import com.yandex.yaweather.handler.WeatherScreenAction
import com.yandex.yaweather.viewModel.WeatherUiState
import com.yandex.yaweather.viewModel.WeatherUiState.WidgetsUiState
import kotlinx.coroutines.launch
import java.net.URL

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun WeatherScreen(uiState: WeatherUiState, action: (WeatherScreenAction) -> Unit) {
  var openBottomSheet by rememberSaveable { mutableStateOf(false) }
  val skipPartiallyExpanded by rememberSaveable { mutableStateOf(false) }
  val coroutineScope = rememberCoroutineScope()
  val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = skipPartiallyExpanded)

  LaunchedEffect(openBottomSheet) {
    if (openBottomSheet) {
      coroutineScope.launch {
        bottomSheetState.show()
      }
    } else {
      coroutineScope.launch {
        bottomSheetState.hide()
      }
    }
  }
  Scaffold(topBar = {
    TopBar(Modifier, action) {
      openBottomSheet = !openBottomSheet
    }
  }) { innerPadding ->
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
          MapWidget(modifier = Modifier,uiState, action)
        }

        item {
          Widgets(modifier = Modifier, uiState.widgetsUiState)
        }
      }
    }
  }
  val temperatureFahrenheit = remember {
    mutableStateOf(false)
  }
  if (openBottomSheet) {
    ModalBottomSheet(
      onDismissRequest = { openBottomSheet = false },
      sheetState = bottomSheetState,
      containerColor = SettingsBack,
    ) {
      Column(modifier = Modifier.fillMaxSize()) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.Center,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Настройки", color = Color.White, fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp)
          )
        }
        Text(text = "Температура", color = Color.White, fontSize = 20.sp, modifier = Modifier.padding(start = 16.dp))
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 4.dp)
            .background(color = SettingsItemBack, shape = RoundedCornerShape(10.dp)),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceEvenly
        ) {
          Text(text = "по Фаренгейту",
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier
              .weight(1f)
              .clip(shape = RoundedCornerShape(10.dp))
              .background(
                color = if (temperatureFahrenheit.value) {
                  SettingsSelected
                } else {
                  Color.Transparent
                }
              )
              .clickable {
                temperatureFahrenheit.value = true
              }
              .padding(4.dp),
            textAlign = TextAlign.Center)
          Text(text = "Цельсия",
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier
              .weight(1f)
              .clip(shape = RoundedCornerShape(10.dp))
              .background(
                color = if (temperatureFahrenheit.value) {
                  Color.Transparent
                } else {
                  SettingsSelected
                }
              )
              .clickable {
                temperatureFahrenheit.value = false
              }
              .padding(4.dp),
            textAlign = TextAlign.Center)
        }
        val windSpeedKm = remember {
          mutableStateOf(true)
        }
        Text(
          text = "Ветер", color = Color.White, fontSize = 20.sp, modifier = Modifier.padding(start = 16.dp, top = 20.dp)
        )
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 4.dp)
            .background(color = SettingsItemBack, shape = RoundedCornerShape(10.dp)),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceEvenly
        ) {
          Text(text = "km/h",
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier
              .weight(1f)
              .clip(shape = RoundedCornerShape(10.dp))
              .background(
                color = if (windSpeedKm.value) {
                  SettingsSelected
                } else {
                  Color.Transparent
                }
              )
              .clickable {
                windSpeedKm.value = true
              }
              .padding(4.dp),
            textAlign = TextAlign.Center)
          Text(text = "m/s",
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier
              .weight(1f)
              .clip(shape = RoundedCornerShape(10.dp))
              .background(
                color = if (windSpeedKm.value) {
                  Color.Transparent
                } else {
                  SettingsSelected
                }
              )
              .clickable {
                windSpeedKm.value = false
              }
              .padding(4.dp),
            textAlign = TextAlign.Center)
        }
        val pressureHPa = remember {
          mutableStateOf(true)
        }
        Text(
          text = "Давление",
          color = Color.White,
          fontSize = 20.sp,
          modifier = Modifier.padding(start = 16.dp, top = 20.dp)
        )
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 4.dp)
            .background(color = SettingsItemBack, shape = RoundedCornerShape(10.dp)),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceEvenly
        ) {
          Text(text = "hPa",
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier
              .weight(1f)
              .clip(shape = RoundedCornerShape(10.dp))
              .background(
                color = if (pressureHPa.value) {
                  SettingsSelected
                } else {
                  Color.Transparent
                }
              )
              .clickable {
                pressureHPa.value = true
              }
              .padding(4.dp),
            textAlign = TextAlign.Center)
          Text(text = "mmHg",
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier
              .weight(1f)
              .clip(shape = RoundedCornerShape(10.dp))
              .background(
                color = if (pressureHPa.value) {
                  Color.Transparent
                } else {
                  SettingsSelected
                }
              )
              .clickable {
                pressureHPa.value = false
              }
              .padding(4.dp),
            textAlign = TextAlign.Center)
        }

        val precipitationMillis = remember {
          mutableStateOf(false)
        }
        Text(
          text = "Осадки",
          color = Color.White,
          fontSize = 20.sp,
          modifier = Modifier.padding(start = 16.dp, top = 20.dp)
        )
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 4.dp)
            .background(color = SettingsItemBack, shape = RoundedCornerShape(10.dp)),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceEvenly
        ) {
          Text(text = "Дюймы",
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier
              .weight(1f)
              .clip(shape = RoundedCornerShape(10.dp))
              .background(
                color = if (precipitationMillis.value) {
                  SettingsSelected
                } else {
                  Color.Transparent
                }
              )
              .clickable {
                precipitationMillis.value = true
              }
              .padding(4.dp),
            textAlign = TextAlign.Center)
          Text(text = "Миллиметры",
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier
              .weight(1f)
              .clip(shape = RoundedCornerShape(10.dp))
              .background(
                color = if (precipitationMillis.value) {
                  Color.Transparent
                } else {
                  SettingsSelected
                }
              )
              .clickable {
                precipitationMillis.value = false
              }
              .padding(4.dp),
            textAlign = TextAlign.Center)
        }
        val checked1 = remember {
          mutableStateOf(false)
        }
        val checked2 = remember {
          mutableStateOf(false)
        }
        val time1 = remember {
          mutableStateOf("08:00")
        }
        val time2 = remember {
          mutableStateOf("20:00")
        }
        val context = LocalContext.current
        val isTimePickerOpen1 = remember { mutableStateOf(false) }
        val isTimePickerOpen2 = remember { mutableStateOf(false) }
        Column(
          modifier = Modifier
            .padding(16.dp)
            .clip(shape = RoundedCornerShape(10.dp))
            .background(color = SettingsAnotherBack)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 16.dp)
              .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column {
              Text(text = "Утренний отчёт", color = Color.White, fontSize = 16.sp)
              Text(text = "Информация о погоде каждый день", color = Color.White, fontSize = 12.sp)
            }
            Switch(
              checked = checked1.value, onCheckedChange = {
                checked1.value = it
              }, colors = SwitchColors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Green,
                checkedBorderColor = Color.Transparent,
                checkedIconColor = Color.Transparent,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = BackSwitch,
                uncheckedBorderColor = Color.Transparent,
                uncheckedIconColor = Color.Transparent,
                disabledCheckedThumbColor = Color.Transparent,
                disabledCheckedTrackColor = Color.Transparent,
                disabledCheckedBorderColor = Color.Transparent,
                disabledCheckedIconColor = Color.Transparent,
                disabledUncheckedThumbColor = Color.Transparent,
                disabledUncheckedTrackColor = Color.Transparent,
                disabledUncheckedBorderColor = Color.Transparent,
                disabledUncheckedIconColor = Color.Transparent
              )
            )
          }
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 16.dp)
              .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column {
              Text(text = "Час", color = Color.White, fontSize = 16.sp)
              Text(text = "Выберите время уведомления", color = Color.White, fontSize = 12.sp)
            }
            Text(text = time1.value,
              color = Color.White,
              modifier = Modifier
                .clip(shape = RoundedCornerShape(4.dp))
                .background(color = BackTime)
                .padding(10.dp)
                .clickable {
                  isTimePickerOpen1.value = true
                })
          }
          if (isTimePickerOpen1.value) {
            TimePickerDialog(
              context, { _, hour: Int, minute: Int ->
                time1.value = String.format("%02d:%02d", hour, minute)
                isTimePickerOpen1.value = false
              }, 12, 0, true
            ).show()
          }

          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 16.dp)
              .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column {
              Text(text = "Вечерний отчёт", color = Color.White, fontSize = 16.sp)
              Text(text = "Получить уведомление за день", color = Color.White, fontSize = 12.sp)
            }
            Switch(
              checked = checked2.value, onCheckedChange = {
                checked2.value = it
              }, colors = SwitchColors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Green,
                checkedBorderColor = Color.Transparent,
                checkedIconColor = Color.Transparent,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = BackSwitch,
                uncheckedBorderColor = Color.Transparent,
                uncheckedIconColor = Color.Transparent,
                disabledCheckedThumbColor = Color.Transparent,
                disabledCheckedTrackColor = Color.Transparent,
                disabledCheckedBorderColor = Color.Transparent,
                disabledCheckedIconColor = Color.Transparent,
                disabledUncheckedThumbColor = Color.Transparent,
                disabledUncheckedTrackColor = Color.Transparent,
                disabledUncheckedBorderColor = Color.Transparent,
                disabledUncheckedIconColor = Color.Transparent
              )
            )
          }
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 16.dp)
              .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column {
              Text(text = "Час", color = Color.White, fontSize = 16.sp)
              Text(text = "Выберите время уведомления", color = Color.White, fontSize = 12.sp)
            }
            Text(text = time2.value,
              color = Color.White,
              modifier = Modifier
                .clip(shape = RoundedCornerShape(4.dp))
                .background(color = BackTime)
                .padding(10.dp)
                .clickable {
                  isTimePickerOpen2.value = true
                })
          }
          if (isTimePickerOpen2.value) {
            TimePickerDialog(
              context, { _, hour: Int, minute: Int ->
                time2.value = String.format("%02d:%02d", hour, minute)
                isTimePickerOpen2.value = false
              }, 12, 0, true
            ).show()
          }
        }
      }
    }
  }
  val systemUiController = rememberSystemUiController()
  systemUiController.setStatusBarColor(Color.Gray)
  systemUiController.setNavigationBarColor(Color.Gray)
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
fun TopBar(modifier: Modifier, action: (WeatherScreenAction) -> Unit, bottomSheet: (Unit) -> Unit) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .padding(start = 8.dp, end = 8.dp, top = 36.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Row {
      IconButton(onClick = {
        bottomSheet.invoke(Unit)
      }) {
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
fun MapWidget(modifier: Modifier,uiState: WeatherUiState, action: (WeatherScreenAction) -> Unit) {
  val currentLocation  = uiState.markerPosition?.lon?.let { uiState.markerPosition.lat?.let { it1 -> LatLng(it1, it) } }
  val cameraPositionState = rememberCameraPositionState {
    position = currentLocation?.let { CameraPosition.fromLatLngZoom(it, 10f) }!!
  }

  val tileProvider = remember {
    object : UrlTileProvider(256, 256) {
      override fun getTileUrl(x: Int, y: Int, zoom: Int): URL? {
        return try {
          URL("https://tile.openweathermap.org/map/temp_new/$zoom/$x/$y.png?appid=62b18818f899c80e1d2f4285220bc90b")
        } catch (e: Exception) {
          null
        }
      }
    }}
  val tileOverlayState = rememberTileOverlayState()


  GoogleMap(
    modifier = Modifier
      .fillMaxWidth()
      .height(200.dp)
      .background(Color.Gray,RoundedCornerShape(16.dp))
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
    TileOverlay(
      state = tileOverlayState,
      tileProvider = tileProvider
    )
    currentLocation?.let { MarkerState(position = it) }?.let {
      Marker(state = it, title = "", snippet = "", onClick = {
        action(WeatherScreenAction.OpenMapAction)
        true
      })
    }
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