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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.stringResource
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
import com.yandex.yaweather.Lang
import com.yandex.yaweather.R
import com.yandex.yaweather.appLanguage
import com.yandex.yaweather.darkTheme
import com.yandex.yaweather.data.network.WeatherByHour
import com.yandex.yaweather.handler.WeatherScreenAction
import com.yandex.yaweather.viewModel.CitySelectionUIState
import com.yandex.yaweather.viewModel.WeatherUiState
import com.yandex.yaweather.viewModel.WeatherUiState.WidgetsUiState
import kotlinx.coroutines.launch
import java.net.URL
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "DefaultLocale")
@Composable
fun WeatherScreen(uiState: CitySelectionUIState, action: (WeatherScreenAction) -> Unit) {
  val context = LocalContext.current
  var openBottomSheet by rememberSaveable { mutableStateOf(false) }
  val skipPartiallyExpanded by rememberSaveable { mutableStateOf(false) }
  val coroutineScope = rememberCoroutineScope()
  val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = skipPartiallyExpanded)
  var selectedIconIndex = remember {
    mutableIntStateOf(
      if (appLanguage.value == Lang.uz) {
        1
      } else if (appLanguage.value == Lang.ru) {
        2
      } else {
        3
      }
    )
  }
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
    TopBar(action) {
      openBottomSheet = !openBottomSheet
    }
  }) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(color = Color.Transparent)
    ) {
      Image(
        painter = rememberDrawablePainter(
          drawable = getDrawable(
            LocalContext.current,
            when {
              uiState.weatherUiState.description == "shower rain" -> R.drawable.fall_rain
              uiState.weatherUiState.description.contains("rain", ignoreCase = true) -> R.drawable.rain_gif
              uiState.weatherUiState.description.contains("clear", ignoreCase = true) -> R.drawable.clear_sky
              uiState.weatherUiState.description.contains(
                "clouds",
                ignoreCase = true
              ) -> R.drawable.scaffered_clouds

              uiState.weatherUiState.description.contains(
                "thunderstorm",
                ignoreCase = true
              ) -> R.drawable.thunderstorm

              uiState.weatherUiState.description.contains("snow", ignoreCase = true) -> R.drawable.snow_gif
              uiState.weatherUiState.description.contains("fog", ignoreCase = true) -> R.drawable.mist
              uiState.weatherUiState.description.contains("mist", ignoreCase = true) -> R.drawable.mist
              else -> R.drawable.clear_sky
            }

          )
        ),
        contentDescription = stringResource(R.string.weather_screen_background_image),
        modifier = Modifier
          .fillMaxSize()
          .blur(10.dp), contentScale = ContentScale.Crop
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
              text = uiState.cityItem.name ?: "Not found",
              fontSize = 24.sp,
              color = Color.White,
              fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = uiState.weatherUiState.temperature,
              fontSize = 64.sp,
              color = Color.White,
              fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = "Макс. ${uiState.weatherUiState.temperatureMax}°  Мин. ${uiState.weatherUiState.temperatureMin}°",
              fontSize = 16.sp,
              color = Color.LightGray
            )
          }
        }
        item {
          HourlyForecast(modifier = Modifier, uiState.hourlyWeather)
        }

        item {
          TenDayForecast(modifier = Modifier, uiState.weatherUiState)
        }

        item {
          MapWidget(modifier = Modifier, uiState.weatherUiState, action)
        }

        item {
          Widgets(modifier = Modifier, uiState.weatherUiState.widgetsUiState)
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
      containerColor = MaterialTheme.colorScheme.primaryContainer,
    ) {
      Column(modifier = Modifier.fillMaxSize()) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.Center,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = stringResource(R.string.settings_bottom_sheet),
            color = MaterialTheme.colorScheme.inversePrimary,
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp)
          )
        }
        Text(
          text = stringResource(R.string.settings_bottom_sheet_temperature),
          color = MaterialTheme.colorScheme.inversePrimary,
          fontSize = 20.sp,
          modifier = Modifier.padding(start = 16.dp)
        )
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 4.dp)
            .background(color = MaterialTheme.colorScheme.onPrimary, shape = RoundedCornerShape(10.dp)),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceEvenly
        ) {
          Text(text = stringResource(R.string.settings_bottom_sheet_fahrenheit),
            color = MaterialTheme.colorScheme.inversePrimary,
            fontSize = 16.sp,
            modifier = Modifier
              .weight(1f)
              .clip(shape = RoundedCornerShape(10.dp))
              .background(
                color = if (temperatureFahrenheit.value) {
                  MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                  Color.Transparent
                }
              )
              .clickable {
                temperatureFahrenheit.value = true
              }
              .padding(4.dp),
            textAlign = TextAlign.Center)
          Text(text = stringResource(R.string.settings_bottom_sheet_selcuim),
            color = MaterialTheme.colorScheme.inversePrimary,
            fontSize = 16.sp,
            modifier = Modifier
              .weight(1f)
              .clip(shape = RoundedCornerShape(10.dp))
              .background(
                color = if (temperatureFahrenheit.value) {
                  Color.Transparent
                } else {
                  MaterialTheme.colorScheme.onPrimaryContainer
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
          text = stringResource(R.string.settings_bottom_sheet_wind),
          color = MaterialTheme.colorScheme.inversePrimary,
          fontSize = 20.sp,
          modifier = Modifier.padding(start = 16.dp, top = 20.dp)
        )
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 4.dp)
            .background(color = MaterialTheme.colorScheme.onPrimary, shape = RoundedCornerShape(10.dp)),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceEvenly
        ) {
          Text(text = stringResource(R.string.settings_bottom_sheet_wind_speed1),
            color = MaterialTheme.colorScheme.inversePrimary,
            fontSize = 16.sp,
            modifier = Modifier
              .weight(1f)
              .clip(shape = RoundedCornerShape(10.dp))
              .background(
                color = if (windSpeedKm.value) {
                  MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                  Color.Transparent
                }
              )
              .clickable {
                windSpeedKm.value = true
              }
              .padding(4.dp),
            textAlign = TextAlign.Center)
          Text(text = stringResource(R.string.settings_bottom_sheet_wind_speed2),
            color = MaterialTheme.colorScheme.inversePrimary,
            fontSize = 16.sp,
            modifier = Modifier
              .weight(1f)
              .clip(shape = RoundedCornerShape(10.dp))
              .background(
                color = if (windSpeedKm.value) {
                  Color.Transparent
                } else {
                  MaterialTheme.colorScheme.onPrimaryContainer
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
          text = stringResource(R.string.settings_bottom_sheet_pressure),
          color = MaterialTheme.colorScheme.inversePrimary,
          fontSize = 20.sp,
          modifier = Modifier.padding(start = 16.dp, top = 20.dp)
        )
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 4.dp)
            .background(color = MaterialTheme.colorScheme.onPrimary, shape = RoundedCornerShape(10.dp)),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceEvenly
        ) {
          Text(text = stringResource(R.string.settings_bottom_sheet_pressure1),
            color = MaterialTheme.colorScheme.inversePrimary,
            fontSize = 16.sp,
            modifier = Modifier
              .weight(1f)
              .clip(shape = RoundedCornerShape(10.dp))
              .background(
                color = if (pressureHPa.value) {
                  MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                  Color.Transparent
                }
              )
              .clickable {
                pressureHPa.value = true
              }
              .padding(4.dp),
            textAlign = TextAlign.Center)
          Text(text = stringResource(R.string.settings_bottom_sheet_pressure2),
            color = MaterialTheme.colorScheme.inversePrimary,
            fontSize = 16.sp,
            modifier = Modifier
              .weight(1f)
              .clip(shape = RoundedCornerShape(10.dp))
              .background(
                color = if (pressureHPa.value) {
                  Color.Transparent
                } else {
                  MaterialTheme.colorScheme.onPrimaryContainer
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
          text = stringResource(R.string.settings_Bottom_sheet_precipitation),
          color = MaterialTheme.colorScheme.inversePrimary,
          fontSize = 20.sp,
          modifier = Modifier.padding(start = 16.dp, top = 20.dp)
        )
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 4.dp)
            .background(color = MaterialTheme.colorScheme.onPrimary, shape = RoundedCornerShape(10.dp)),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceEvenly
        ) {
          Text(text = stringResource(R.string.settings_bottom_sheet_precipitaion1),
            color = MaterialTheme.colorScheme.inversePrimary,
            fontSize = 16.sp,
            modifier = Modifier
              .weight(1f)
              .clip(shape = RoundedCornerShape(10.dp))
              .background(
                color = if (precipitationMillis.value) {
                  MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                  Color.Transparent
                }
              )
              .clickable {
                precipitationMillis.value = true
              }
              .padding(4.dp),
            textAlign = TextAlign.Center)
          Text(text = stringResource(R.string.settings_bottom_sheet_precipitaion2),
            color = MaterialTheme.colorScheme.inversePrimary,
            fontSize = 16.sp,
            modifier = Modifier
              .weight(1f)
              .clip(shape = RoundedCornerShape(10.dp))
              .background(
                color = if (precipitationMillis.value) {
                  Color.Transparent
                } else {
                  MaterialTheme.colorScheme.onPrimaryContainer
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
            .background(color = MaterialTheme.colorScheme.secondary)
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
              Text(
                text = stringResource(R.string.settings_bottom_sheet_morning_notification),
                color = MaterialTheme.colorScheme.inversePrimary,
                fontSize = 16.sp
              )
              Text(
                text = stringResource(R.string.settings_bottom_sheet_every_day_notification),
                color = MaterialTheme.colorScheme.inversePrimary,
                fontSize = 12.sp
              )
            }
            Switch(
              checked = checked1.value, onCheckedChange = {
                checked1.value = it
              }, colors = SwitchColors(
                checkedThumbColor = Color.White,
                checkedTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                checkedBorderColor = Color.Transparent,
                checkedIconColor = Color.Transparent,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = MaterialTheme.colorScheme.onSecondary,
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
              Text(
                text = stringResource(R.string.settings_bottom_sheet_hour),
                color = MaterialTheme.colorScheme.inversePrimary,
                fontSize = 16.sp
              )
              Text(
                text = stringResource(R.string.settings_bottom_sheet_choose_time),
                color = MaterialTheme.colorScheme.inversePrimary,
                fontSize = 12.sp
              )
            }
            Text(text = time1.value,
              color = MaterialTheme.colorScheme.inversePrimary,
              modifier = Modifier
                .clip(shape = RoundedCornerShape(4.dp))
                .background(color = MaterialTheme.colorScheme.primary)
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
              Text(
                text = stringResource(R.string.settings_bottom_sheet_night_report),
                color = MaterialTheme.colorScheme.inversePrimary,
                fontSize = 16.sp
              )
              Text(
                text = stringResource(R.string.settings_bottom_sheet_for_day_notification),
                color = MaterialTheme.colorScheme.inversePrimary,
                fontSize = 12.sp
              )
            }
            Switch(
              checked = checked2.value, onCheckedChange = {
                checked2.value = it
              }, colors = SwitchColors(
                checkedThumbColor = Color.White,
                checkedTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                checkedBorderColor = Color.Transparent,
                checkedIconColor = Color.Transparent,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = MaterialTheme.colorScheme.onSecondary,
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
              Text(
                text = stringResource(R.string.settings_bottom_sheet_hour),
                color = MaterialTheme.colorScheme.inversePrimary,
                fontSize = 16.sp
              )
              Text(
                text = stringResource(R.string.settings_bottom_sheet_choose_time),
                color = MaterialTheme.colorScheme.inversePrimary,
                fontSize = 12.sp
              )
            }
            Text(text = time2.value,
              color = MaterialTheme.colorScheme.inversePrimary,
              modifier = Modifier
                .clip(shape = RoundedCornerShape(4.dp))
                .background(color = MaterialTheme.colorScheme.primary)
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
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(
            text = stringResource(R.string.settings_bottom_sheet_change_theme),
            color = MaterialTheme.colorScheme.inversePrimary,
            fontSize = 20.sp
          )
          Switch(
            checked = darkTheme.value, onCheckedChange = {
              darkTheme.value = it
              action.invoke(WeatherScreenAction.SetTheme(it))
            }, colors = SwitchColors(
              checkedThumbColor = Color.White,
              checkedTrackColor = MaterialTheme.colorScheme.secondaryContainer,
              checkedBorderColor = Color.Transparent,
              checkedIconColor = Color.Transparent,
              uncheckedThumbColor = Color.White,
              uncheckedTrackColor = MaterialTheme.colorScheme.onSecondary,
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
            .padding(start = 16.dp, end = 16.dp, top = 4.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(
            text = stringResource(R.string.settings_bottom_sheet_language),
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.inversePrimary
          )
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
              .clip(shape = RoundedCornerShape(10.dp))
              .background(color = MaterialTheme.colorScheme.onSecondaryContainer)
          ) {
            Text(text = stringResource(R.string.uz),
              modifier = Modifier
                .padding(top = 2.dp, bottom = 2.dp, start = 2.dp)
                .clip(shape = RoundedCornerShape(10.dp))
                .background(
                  color = if (selectedIconIndex.intValue == 1) {
                    MaterialTheme.colorScheme.tertiary
                  } else {
                    Color.Transparent
                  }
                )
                .clickable(
                  enabled = selectedIconIndex.intValue != 1
                ) {
                  action.invoke(WeatherScreenAction.SetLanguage(Lang.uz))
                  selectedIconIndex.intValue = 1
                }
                .padding(vertical = 10.dp, horizontal = 16.dp),
              textAlign = TextAlign.Center,
              color = MaterialTheme.colorScheme.inversePrimary)
            Box(
              modifier = Modifier
                .size(width = 1.dp, height = 24.dp)
                .background(
                  color = if (selectedIconIndex.intValue == 3 || selectedIconIndex.intValue == -1) {
                    MaterialTheme.colorScheme.onTertiary
                  } else {
                    Color.Transparent
                  }
                )
            )
            Text(text = stringResource(R.string.ru),
              modifier = Modifier
                .padding(vertical = 2.dp)
                .clip(shape = RoundedCornerShape(10.dp))
                .background(
                  color = if (selectedIconIndex.intValue == 2) {
                    MaterialTheme.colorScheme.tertiary
                  } else {
                    Color.Transparent
                  }
                )
                .clickable(
                  enabled = selectedIconIndex.intValue != 2
                ) {
                  selectedIconIndex.intValue = 2
                  action.invoke(WeatherScreenAction.SetLanguage(Lang.ru))
                }
                .padding(vertical = 10.dp, horizontal = 16.dp),
              color = MaterialTheme.colorScheme.inversePrimary,
              textAlign = TextAlign.Center)
            Box(
              modifier = Modifier
                .size(width = 1.dp, height = 24.dp)
                .background(
                  color = if (selectedIconIndex.intValue == 1 || selectedIconIndex.intValue == -1) {
                    MaterialTheme.colorScheme.onTertiary
                  } else {
                    Color.Transparent
                  }
                )
            )
            Text(text = stringResource(R.string.en),
              modifier = Modifier
                .padding(top = 2.dp, bottom = 2.dp, end = 2.dp)
                .clip(shape = RoundedCornerShape(10.dp))
                .background(
                  color = if (selectedIconIndex.intValue == 3) {
                    MaterialTheme.colorScheme.tertiary
                  } else {
                    Color.Transparent
                  }
                )
                .clickable(
                  enabled = selectedIconIndex.intValue != 3
                ) {
                  selectedIconIndex.intValue = 3
                  action.invoke(WeatherScreenAction.SetLanguage(Lang.en))
                }
                .padding(vertical = 10.dp, horizontal = 16.dp),
              color = MaterialTheme.colorScheme.inversePrimary,
              textAlign = TextAlign.Center)
          }
        }
      }
    }
  }
  val systemUiController = rememberSystemUiController()
  systemUiController.setStatusBarColor(MaterialTheme.colorScheme.primary)
  systemUiController.setNavigationBarColor(MaterialTheme.colorScheme.primary)
}

@SuppressLint("DefaultLocale")
@Composable
fun HourlyForecast(modifier: Modifier, weatherByHour: List<WeatherByHour>) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .alpha(0.75f)
      .background(Color.DarkGray, RoundedCornerShape(16.dp))
      .padding(start = 16.dp, top = 16.dp, bottom = 16.dp)
  ) {
    Text(
      modifier = modifier.padding(start = 8.dp),
      text = "ЧАСОВОЙ ПРОГНОЗ",
      fontSize = 16.sp,
      color = Color.LightGray,
      fontWeight = FontWeight.Bold
    )
    Spacer(modifier = modifier.height(8.dp))

    LazyRow(
      horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = modifier.fillMaxWidth()
    ) {
      items(weatherByHour) { index ->
        Column(
          horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier.padding(start = 8.dp)
        ) {
          Spacer(modifier = modifier.height(4.dp))
          index.temp?.let {
            val temperatureInCelsius = (it - 273.15).toInt()
            Text("$temperatureInCelsius°", color = Color.White, fontSize = 18.sp)
          }

          Icon(
            painter = painterResource(weatherIconForForecast(index)),
            contentDescription = null,
            tint = Color.LightGray,
            modifier = modifier.size(24.dp)
          )
          Spacer(modifier = modifier.height(4.dp))
          index.timeStamp?.let {
            val formattedTime = it.split("T").getOrNull(1)?.split(":")?.getOrNull(0)?.plus("h") ?: "N/A"
            Text(formattedTime, color = Color.LightGray, fontSize = 12.sp)
          }
        }
      }
    }
  }
}

@Composable
fun TopBar(action: (WeatherScreenAction) -> Unit, bottomSheet: (Unit) -> Unit) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(start = 8.dp, end = 8.dp, top = 36.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Row {
      IconButton(onClick = {
        bottomSheet.invoke(Unit)
      }, colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.primary)) {
        Icon(
          imageVector = Icons.Default.Settings,
          contentDescription = stringResource(R.string.weather_screen_settings_icon),
        )
      }
      IconButton(onClick = {
        action.invoke(WeatherScreenAction.OpenInfoAction)
      }, colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.primary)) {
        Icon(
          imageVector = Icons.Default.Info, contentDescription = stringResource(R.string.weather_screen_info_icon)
        )
      }
    }
    IconButton(
      onClick = { action(WeatherScreenAction.AddCityAction) },
      colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.primary)
    ) {
      Icon(
        imageVector = Icons.Default.Menu, contentDescription = stringResource(R.string.weather_screen_add_icon)
      )
    }
  }
}

@Composable
fun TenDayForecast(modifier: Modifier, uiState: WeatherUiState) {
  var itemCount by remember { mutableIntStateOf(5) }
  var showMoreButton by remember { mutableStateOf(true) }


  val currentDay = remember { getCurrentDayOfWeek() }

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
          Text(text = dayOfWeek, color = Color.White, fontSize = 16.sp)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            painter = painterResource(
              when {
                uiState.description == "shower rain" -> R.drawable.showers_fill
                uiState.description.contains("rain", ignoreCase = true) -> R.drawable.rainy_fill
                uiState.description.contains("clear", ignoreCase = true) -> R.drawable.sun_fill
                uiState.description.contains(
                  "clouds",
                  ignoreCase = true
                ) -> R.drawable.cloud_windy_fill

                uiState.description.contains(
                  "thunderstorm",
                  ignoreCase = true
                ) -> R.drawable.thunderstorms_fill

                uiState.description.contains("snow", ignoreCase = true) -> R.drawable.snowy_fill
                uiState.description.contains("fog", ignoreCase = true) -> R.drawable.mist_fill
                uiState.description.contains("mist", ignoreCase = true) -> R.drawable.mist_fill
                else -> R.drawable.sun_fill
              }
            ),
            contentDescription = null,
            tint = Color.LightGray,
            modifier = Modifier.size(14.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(uiState.temperatureMin, color = Color.LightGray, fontSize = 14.sp)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
          Spacer(modifier = Modifier.width(4.dp))
          Text(uiState.temperatureMax, color = Color.White, fontSize = 16.sp)
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

fun getCurrentDayOfWeek(): Int {
  return Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1
}


fun getDayOfWeek(dayIndex: Int): String {
  val daysOfWeek = listOf("Вс", "Пн", "Вт", "Ср", "Чт", "Пт", "Сб")
  return daysOfWeek[dayIndex % 7]
}

@Composable
fun MapWidget(modifier: Modifier, uiState: WeatherUiState, action: (WeatherScreenAction) -> Unit) {

  val currentLocation = uiState.markerPosition?.lon?.let { uiState.markerPosition.lat?.let { it1 -> LatLng(it1, it) } }

  val cameraPositionState = rememberCameraPositionState {
    position = if (currentLocation != null)
      CameraPosition.fromLatLngZoom(currentLocation, 10f)
    else CameraPosition.fromLatLngZoom(LatLng(0.0, 0.0), 1f)
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
    }
  }
  val tileOverlayState = rememberTileOverlayState()


  GoogleMap(
    modifier = Modifier
      .fillMaxWidth()
      .height(200.dp)
      .background(Color.Gray, RoundedCornerShape(16.dp))
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
      state = tileOverlayState, tileProvider = tileProvider
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
      .fillMaxWidth(),

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
      .background(Color.DarkGray.copy(alpha = 0.75f), RoundedCornerShape(16.dp)),


    horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceEvenly
  ) {
    Text(
      text = title, fontSize = 16.sp, color = Color.White, fontWeight = FontWeight.SemiBold

    )
    Text(
      text = value ?: "-", fontSize = 28.sp, color = Color.White, fontWeight = FontWeight.Bold
    )
  }
}


fun weatherIconForForecast(weatherByHour: WeatherByHour): Int {
  return when (weatherByHour.weather?.code) {
    in 200..202 -> R.drawable.hail_fill
    in 230..233 -> R.drawable.thunderstorms_fill
    in 300..302 -> R.drawable.rainy_fill
    in 500..522 -> R.drawable.showers_fill
    in 600..623 -> R.drawable.snowy_fill
    in 700..751 -> R.drawable.mist_fill
    800 -> R.drawable.sun_fill
    in 801..804 -> R.drawable.cloudy_fill
    else -> R.drawable.blaze_fill    // Неизвестный код
  }
}


