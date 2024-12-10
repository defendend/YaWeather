package com.yandex.yaweather.ui.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.UrlTileProvider
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.TileOverlay
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberTileOverlayState
import com.yandex.yaweather.R
import com.yandex.yaweather.handler.MapScreenAction
import com.yandex.yaweather.handler.MapScreenAction.UpdateMarkerPositionAction
import com.yandex.yaweather.viewModel.MapUIState
import java.net.URL


@Composable
fun MapScreen(
  uiState: MapUIState,
  action: (MapScreenAction) -> Unit,
) {
  var selectedMenuItem by remember { mutableStateOf<String?>("Temperature") }

  val tileProvider = remember {
    object : UrlTileProvider(256, 256) {
      override fun getTileUrl(x: Int, y: Int, zoom: Int): URL? {
        val layer = when (selectedMenuItem) {
          "Temperature" -> "temp_new"
          "Precipition" -> "precipitation_new"
          "Pressue" -> "pressure_new"
          "Wind" -> "wind_new"
          else -> "temp_new"
        }
        return try {
          URL("https://tile.openweathermap.org/map/$layer/$zoom/$x/$y.png?appid=62b18818f899c80e1d2f4285220bc90b")
        } catch (e: Exception) {
          null
        }
      }
    }}
  val tileOverlayState = rememberTileOverlayState()
  val currentLocation  = uiState.markerPosition?.lon?.let { uiState.markerPosition.lat?.let { it1 -> LatLng(it1, it) } }

  val cameraPositionState = rememberCameraPositionState {
    position = if(currentLocation != null)
      CameraPosition.fromLatLngZoom(currentLocation, 10f)
    else  CameraPosition.fromLatLngZoom(LatLng(0.0,0.0), 1f)
  }
  var isMenuExpanded by remember { mutableStateOf(false) }
  val markerTitle: String
  var markerPosition by remember { mutableStateOf(currentLocation) }
  var isNightMode by remember { mutableStateOf(false) }
  val context = LocalContext.current


  when (selectedMenuItem) {
    "Temperature" -> {
      markerTitle = "${LocalContext.current.resources.getString(R.string.temperature)}: ${uiState.temperature}°C"

    }

    "Wind" -> {
      markerTitle = "${LocalContext.current.resources.getString(R.string.wind_speed)}: ${uiState.windSpeed}km/h"

    }

    "Pressue" -> {
      markerTitle = "${LocalContext.current.resources.getString(R.string.pressure)}: ${uiState.pressure} Pa"


    }

    "Precipitation" -> {
      markerTitle = "${LocalContext.current.resources.getString(R.string.precipitation)}: ${uiState.humidity}%"
    }

    else -> {
      markerTitle = ""
    }
  }

  val nightModeStyleOptions = remember {
    val jsonString = getJsonFromRaw(context, R.raw.night_mode_map)
    MapStyleOptions(jsonString)
  }
  Box(modifier = Modifier.fillMaxSize()) {

    GoogleMap(
      modifier = Modifier.fillMaxSize(),
      cameraPositionState = cameraPositionState,
      onMapClick = { latLng ->
        markerPosition = latLng
        action(UpdateMarkerPositionAction(latLng))

      },
      properties = MapProperties(
        mapStyleOptions = if (isNightMode) nightModeStyleOptions else null

      )
    ) {
      TileOverlay(
        state = tileOverlayState,
        tileProvider = tileProvider
      )
      Log.d("MapMode", "Night Mode Active: $isNightMode")
      markerPosition?.let { position ->
        Marker(
          state = MarkerState(position = position),
          title = markerTitle,
         icon =
         BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
        )
      }
    }

    Box(
      modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()

    ) {
      Box(
        modifier = Modifier
          .size(width = 60.dp, height = 120.dp)
          .padding(8.dp)
          .padding(top = 16.dp)
          .clip(RoundedCornerShape(16.dp))
          .background(color = Color.Gray.copy(alpha = 0.7f))
          .align(Alignment.TopEnd)
      ) {
        Column(
          modifier = Modifier
            .fillMaxSize()
            .padding(4.dp),
          verticalArrangement = Arrangement.SpaceEvenly,
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          IconButton(onClick = {
            isNightMode = !isNightMode
          }) {
            Icon(
              painter = if (isNightMode) painterResource(id = R.drawable.sun_fill)
              else painterResource(id = R.drawable.moon_clear_fill),
              contentDescription = "Current Location",
              tint = if (isNightMode) Color.Yellow
              else Color.DarkGray
            )
          }
          IconButton(onClick = { isMenuExpanded = true }) {
            Icon(
              imageVector = Icons.Filled.Menu,
              contentDescription = "Open Menu",
              tint = Color.White
            )
          }

          DropdownMenu(
            expanded = isMenuExpanded,
            onDismissRequest = { isMenuExpanded = false },
            Modifier.background(color = Color.Gray.copy(alpha = 0.7f))
            ,

            ) {
            DropdownMenuItem(
              text = { Text(LocalContext.current.resources.getString(R.string.precipitation)) },
              onClick = { selectedMenuItem = "Precipitation" },
              leadingIcon = {
                Icon(
                  painter = painterResource(id = R.drawable.hail_fill),
                  contentDescription = null
                )
              },
              trailingIcon = {
                if (selectedMenuItem == "Precipitation") {
                  Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = null,
                    tint = Color.DarkGray
                  )
                }
              }
            )
            DropdownMenuItem(
              text = { Text(LocalContext.current.resources.getString(R.string.temperature)) },
              onClick = { selectedMenuItem = "Temperature" },
              leadingIcon = {
                Icon(
                  painter = painterResource(id = R.drawable.temp_hot_line),
                  contentDescription = null
                )
              },
              trailingIcon = {
                if (selectedMenuItem == "Temperature") {
                  Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = null,
                    tint = Color.DarkGray
                  )
                }
              }
            )
            DropdownMenuItem(
              text = { Text(LocalContext.current.resources.getString(R.string.pressure)) },
              onClick = { selectedMenuItem = "Pressue" },
              leadingIcon = {
                Icon(
                  painter = painterResource(id = R.drawable.haze_2_line),
                  contentDescription = null
                )
              },
              trailingIcon = {
                if (selectedMenuItem == "Pressue") {
                  Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = null,
                    tint = Color.DarkGray
                  )
                }
              }

            )
            DropdownMenuItem(
              text = { Text(LocalContext.current.resources.getString(R.string.wind_speed)) },
              onClick = { selectedMenuItem = "Wind" },
              leadingIcon = {
                Icon(
                  painter = painterResource(id = R.drawable.windy_fill),
                  contentDescription = null
                )
              },
              trailingIcon = {
                if (selectedMenuItem == "Wind") {
                  Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = null,
                    tint = Color.DarkGray
                  )
                }
              }
            )
          }
        }
      }
    }
  }
}
fun getJsonFromRaw(context: Context, rawResourceId: Int): String {
  return context.resources.openRawResource(rawResourceId).bufferedReader().use { it.readText() }
}