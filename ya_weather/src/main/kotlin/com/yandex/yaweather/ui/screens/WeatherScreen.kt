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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.yandex.yaweather.Lang
import com.yandex.yaweather.R
import com.yandex.yaweather.appLanguage
import com.yandex.yaweather.darkTheme
import com.yandex.yaweather.handler.WeatherScreenAction
import com.yandex.yaweather.share.shareWeatherInfo
import com.yandex.yaweather.utils.getMessage
import com.yandex.yaweather.utils.getWeatherDescription
import com.yandex.yaweather.utils.weatherBackground
import com.yandex.yaweather.utils.weatherEmoji
import com.yandex.yaweather.viewModel.CitySelectionUIState
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "DefaultLocale")
@Composable
fun WeatherScreen(uiState: CitySelectionUIState, action: (WeatherScreenAction) -> Unit) {
  val context = LocalContext.current
  var openBottomSheet by rememberSaveable { mutableStateOf(false) }
  val skipPartiallyExpanded by rememberSaveable { mutableStateOf(false) }
  val coroutineScope = rememberCoroutineScope()
  val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = skipPartiallyExpanded)
  val weatherInfo = """
    🏙️ ${LocalContext.current.resources.getString(R.string.weather_today_in_city)} ${if (appLanguage.value == Lang.ru) { uiState.cityItem.name ?: "Not found" } else { uiState.cityItem.engName ?: "Not found" }} 
    🌡️ ${LocalContext.current.resources.getString(R.string.temperature)}: ${uiState.weatherUiState.temperature}°C  
    ${weatherEmoji(uiState.weatherUiState.weatherId)} ${LocalContext.current.resources.getString(R.string.condition)}: ${getWeatherDescription(uiState.weatherUiState.weatherId, LocalContext.current)}
    
    -------------------  
    📱 ${getMessage(uiState.weatherUiState.weatherId, LocalContext.current)} - YaWeather  
""".trimIndent()
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
    CustomTopAppBar(onMenuClick = {
      action(WeatherScreenAction.AddCityAction)
    },
      onSettingsClick = { openBottomSheet = !openBottomSheet },
      onShareClick = { shareWeatherInfo(context, weatherInfo) })
  }) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(color = Color.Transparent)
    ) {
      Image(painter = rememberDrawablePainter(
        drawable = getDrawable(
          LocalContext.current, weatherBackground(uiState.weatherUiState.weatherId)
        )
      ), contentDescription = "Weather background", modifier = Modifier
        .fillMaxSize()
        .graphicsLayer {
          alpha = 1f
        }
        .blur(10.dp), contentScale = ContentScale.Crop)
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
            modifier = Modifier.padding(top = 64.dp), horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Text(
              text = if (appLanguage.value == Lang.ru) {
                uiState.cityItem.name ?: "Not found"
              } else {
                uiState.cityItem.engName ?: "Not found"
              }, fontSize = 36.sp, color = Color.White, fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text ="${uiState.weatherUiState.temperature}°",
              fontSize = 64.sp,
              color = Color.White,
              fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = stringResource(R.string.max) + "${uiState.weatherUiState.temperatureMax}°" + stringResource(R.string.min) + "${uiState.weatherUiState.temperatureMin}°",
              fontSize = 16.sp,
              color = Color.LightGray
            )
          }
        }
        item {
          HourlyForecast(uiState.hourlyWeather)
        }

        item {
          TenDayForecast(uiState.weatherUiState)
        }

        item {
          MapWidget(uiState.weatherUiState, action)
        }

        item {
          Widgets(uiState.weatherUiState.widgetsUiState)
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
          Spacer(modifier = Modifier.weight(1f))
          Text(
            text = stringResource(R.string.settings_bottom_sheet),
            color = MaterialTheme.colorScheme.inversePrimary,
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp)
          )
          Spacer(modifier = Modifier.weight(1f))
          IconButton(
            onClick = { action.invoke(WeatherScreenAction.OpenInfoAction) },
            colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.inversePrimary)
          ) {
            Icon(
              imageVector = Icons.Default.Info,
              contentDescription = stringResource(R.string.weather_screen_info_icon),
              modifier = Modifier
                .clip(shape = CircleShape)
                .padding(end = 16.dp)
            )
          }
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