package com.yandex.yaweather.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.yandex.yaweather.handler.MapScreenAction
import com.yandex.yaweather.viewModel.WeatherUiState

@Composable
fun MapScreen(
  uiState: WeatherUiState,
  action: (MapScreenAction) -> Unit
) {
  val toshekent = LatLng(41.2995, 69.2401)
  val cameraPositionState = rememberCameraPositionState {
    position = CameraPosition.fromLatLngZoom(toshekent, 10f)
  }
  var markerPosition by remember { mutableStateOf<LatLng?>(toshekent) }
  var isNightMode by remember { mutableStateOf(false) }
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
            "elementType": "labels.text.fill",
            "stylers": [
              {
                "color": "#d59563"
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
  Column(modifier = Modifier.fillMaxSize()) {
    Button(
      onClick = { isNightMode = !isNightMode },
      modifier = Modifier
        .align(Alignment.CenterHorizontally)
        .padding(8.dp)
    ) {
      Text(text = if (isNightMode) "Day Mode" else "Night Mode")
    }

    GoogleMap(
      modifier = Modifier.fillMaxSize(),
      cameraPositionState = cameraPositionState,
      onMapClick = { latLng ->
        markerPosition = latLng
        println("bosilyapti")
        action(MapScreenAction.UpdateMarkerPositionAction(latLng))
      },
      properties = MapProperties(
        mapStyleOptions = if (isNightMode) {
          MapStyleOptions(nightModeStyle)
        } else null
      )
    ) {
      Log.d("MapMode", "Night Mode Active: $isNightMode")
      markerPosition?.let { position ->
        Marker(
          state = MarkerState(position = position),
          title = "Temperature: ${uiState.temperature}°C"
        )
      }
    }
  }
}
