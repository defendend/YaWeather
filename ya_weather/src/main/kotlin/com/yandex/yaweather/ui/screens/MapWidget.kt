package com.yandex.yaweather.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
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
import com.yandex.yaweather.handler.WeatherScreenAction
import com.yandex.yaweather.viewModel.WeatherUiState
import java.net.URL

@Composable
fun MapWidget(uiState: WeatherUiState, action: (WeatherScreenAction) -> Unit) {
  val currentLocation = uiState.markerPosition?.lon?.let {
    uiState.markerPosition.lat?.let { lat ->
      LatLng(lat, it)
    }
  }
  val context = LocalContext.current

  val cameraPositionState = rememberCameraPositionState {
    position = if (currentLocation != null) CameraPosition.fromLatLngZoom(currentLocation, 10f)
    else CameraPosition.fromLatLngZoom(LatLng(0.0, 0.0), 1f)
  }
  val nightModeStyleOptions = remember {
    val jsonString = getJsonFromRaw(context, R.raw.night_mode_map) // R.raw.night_mode_style fayl ID'si
    MapStyleOptions(jsonString)
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

    properties = MapProperties(
      isMyLocationEnabled = false,
      mapStyleOptions = nightModeStyleOptions
      ),
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
      Marker(state = it,
        title = "",
        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE),
        snippet = "",
        onClick = {
          action(WeatherScreenAction.OpenMapAction)
          true
        })
    }
  }
}
