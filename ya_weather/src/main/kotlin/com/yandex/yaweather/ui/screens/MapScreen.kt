package com.yandex.yaweather.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.yandex.yaweather.viewModel.YaWeatherViewModel
import data.network.CoordinatesResponse
@Composable
fun MapScreen(
  currentWeatherState: State<CoordinatesResponse>,
  onMapClick: (LatLng) -> Unit
) {
  val toshekent = LatLng(41.2995, 69.2401)
  val cameraPositionState = rememberCameraPositionState {
    position = CameraPosition.fromLatLngZoom(toshekent, 10f)
  }
  var markerPosition by remember { mutableStateOf<LatLng?>(toshekent) }

  GoogleMap(
    modifier = Modifier.fillMaxSize(),
    cameraPositionState = cameraPositionState,
    onMapClick = { latLng ->
      markerPosition = latLng
      onMapClick(latLng) // Har safar xarita bosilganda onMapClick ni chaqiramiz
    },
  ) {
    markerPosition?.let { position ->
      Marker(
        state = MarkerState(position = position),
        title = "Temperature: ${currentWeatherState.value.main?.temp?.minus(273)?.toInt() ?: "N/A"}°C"
      )
    }
  }
}
