package com.yandex.yaweather.ui.screens

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

  when (selectedMenuItem) {
    "Temperature" -> {
      markerTitle = "Temperature: ${uiState.temperature}°C"

    }

    "Wind" -> {
      markerTitle = "Wind speed: ${uiState.windSpeed}m/s"

    }

    "Pressue" -> {
      markerTitle = "Pressue: ${uiState.pressure} Pa"


    }

    "Precipitation" -> {
      markerTitle = "Precipitation: ${uiState.humidity}%"
    }

    else -> {
      markerTitle = ""
    }
  }
  val nightModeStyle = """
    [
      {
        "elementType": "geometry",
        "stylers": [
          {
            "color": "#242f3e"
          }
        ]
      },
      {
        "elementType": "labels.text.fill",
        "stylers": [
          {
            "color": "#746855"
          }
        ]
      },
      {
        "elementType": "labels.text.stroke",
        "stylers": [
          {
            "color": "#242f3e"
          }
        ]
      },
      {
        "featureType": "administrative.locality",
        "elementType": "labels.text.fill",
        "stylers": [
          {
            "color": "#d59563"
          }
        ]
      },
      {
        "featureType": "poi",
        "elementType": "labels.text",
        "stylers": [
          {
            "visibility": "off"
          }
        ]
      },
      {
        "featureType": "poi",
        "elementType": "labels.text.fill",
        "stylers": [
          {
            "color": "#d59563"
          }
        ]
      },
      {
        "featureType": "poi.business",
        "stylers": [
          {
            "visibility": "off"
          }
        ]
      },
      {
        "featureType": "poi.park",
        "elementType": "geometry",
        "stylers": [
          {
            "color": "#263c3f"
          }
        ]
      },
      {
        "featureType": "poi.park",
        "elementType": "labels.text.fill",
        "stylers": [
          {
            "color": "#6b9a76"
          }
        ]
      },
      {
        "featureType": "road",
        "elementType": "geometry",
        "stylers": [
          {
            "color": "#38414e"
          }
        ]
      },
      {
        "featureType": "road",
        "elementType": "geometry.stroke",
        "stylers": [
          {
            "color": "#212a37"
          }
        ]
      },
      {
        "featureType": "road",
        "elementType": "labels.icon",
        "stylers": [
          {
            "visibility": "off"
          }
        ]
      },
      {
        "featureType": "road",
        "elementType": "labels.text.fill",
        "stylers": [
          {
            "color": "#9ca5b3"
          }
        ]
      },
      {
        "featureType": "road.highway",
        "elementType": "geometry",
        "stylers": [
          {
            "color": "#746855"
          }
        ]
      },
      {
        "featureType": "road.highway",
        "elementType": "geometry.stroke",
        "stylers": [
          {
            "color": "#1f2835"
          }
        ]
      },
      {
        "featureType": "road.highway",
        "elementType": "labels.text.fill",
        "stylers": [
          {
            "color": "#f3d19c"
          }
        ]
      },
      {
        "featureType": "transit",
        "stylers": [
          {
            "visibility": "off"
          }
        ]
      },
      {
        "featureType": "transit",
        "elementType": "geometry",
        "stylers": [
          {
            "color": "#2f3948"
          }
        ]
      },
      {
        "featureType": "transit.station",
        "elementType": "labels.text.fill",
        "stylers": [
          {
            "color": "#d59563"
          }
        ]
      },
      {
        "featureType": "water",
        "elementType": "geometry",
        "stylers": [
          {
            "color": "#17263c"
          }
        ]
      },
      {
        "featureType": "water",
        "elementType": "labels.text.fill",
        "stylers": [
          {
            "color": "#515c6d"
          }
        ]
      },
      {
        "featureType": "water",
        "elementType": "labels.text.stroke",
        "stylers": [
          {
            "color": "#17263c"
          }
        ]
      }
    ]
""".trimIndent()
  val nightModeStyleOptions = remember {
    MapStyleOptions(nightModeStyle)
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
            Modifier.background(Color.White),

            ) {
            DropdownMenuItem(
              text = { Text("Precipition") },
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
              text = { Text("Temperature") },
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
              text = { Text("Pressue") },
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
              text = { Text("Wind") },
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