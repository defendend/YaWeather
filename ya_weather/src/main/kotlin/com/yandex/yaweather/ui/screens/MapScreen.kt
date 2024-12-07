package com.yandex.yaweather.ui.screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
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
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.yandex.yaweather.R
import com.yandex.yaweather.handler.MapScreenAction
import com.yandex.yaweather.handler.MapScreenAction.UpdateMarkerPositionAction
import com.yandex.yaweather.viewModel.MapUIState


@Composable
fun MapScreen(
  uiState: MapUIState,
  action: (MapScreenAction) -> Unit
) {

  val currentLocotion: LatLng by lazy {
    uiState.markerPosition?.let {
      it.lat?.let { it1 ->
        it.lon?.let { it2 ->
          LatLng(it1, it2)
        }
      }
    } ?: LatLng(41.311081, 69.240562)
  }

  val cameraPositionState = rememberCameraPositionState {
    position = CameraPosition.fromLatLngZoom(currentLocotion, 10f)
  }

  var isMenuExpanded by remember { mutableStateOf(false) }
  val markerTitle: String
  var selectedMenuItem by remember { mutableStateOf<String?>("Temperature") }
  var markerPosition by remember { mutableStateOf<LatLng?>(currentLocotion) }
  var isNightMode by remember { mutableStateOf(false) }

  when (selectedMenuItem) {
    "Temperature" -> {
      markerTitle = "Temperature: ${uiState.temperature}°C"

    }

    "Wind" -> {
      markerTitle = "Wind speed: ${uiState.windSpeed}m/s"

    }

    "Air Quality" -> {
      markerTitle = "Air Quality: ${uiState.airQuality}"


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
      Log.d("MapMode", "Night Mode Active: $isNightMode")
      markerPosition?.let { position ->
        Marker(
          state = MarkerState(position = position),
          title = markerTitle
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
              text = { Text("Air Quality") },
              onClick = { selectedMenuItem = "Air Quality" },
              leadingIcon = {
                Icon(
                  painter = painterResource(id = R.drawable.haze_2_line),
                  contentDescription = null
                )
              },
              trailingIcon = {
                if (selectedMenuItem == "Air Quality") {
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
@Composable
fun CustomMarker(
  position: LatLng,
  title: String,
  onClick: () -> Unit
) {
  val markerState = rememberMarkerState(position = position)

  Marker(
    state = markerState,
    icon = remember { createCustomMarkerIcon(title) },
    onClick = {
      onClick()
      true
    }
  )
}

fun createCustomMarkerIcon(title: String): BitmapDescriptor {
  val markerWidth = 100  // Ikonka kengligi
  val markerHeight = 100 // Ikonka balandligi

  val bitmap = Bitmap.createBitmap(markerWidth, markerHeight, Bitmap.Config.ARGB_8888)
  val canvas = Canvas(bitmap)

  val paint = Paint().apply {
    color = android.graphics.Color.BLUE
    textSize = 20f
    textAlign = Paint.Align.CENTER
    isAntiAlias = true
  }

  val xPos = (markerWidth / 2).toFloat()
  val yPos = (markerHeight / 2).toFloat() - ((paint.descent() + paint.ascent()) / 2)

  canvas.drawText(title, xPos, yPos, paint)

  return BitmapDescriptorFactory.fromBitmap(bitmap) // BitmapDescriptor yaratish
}