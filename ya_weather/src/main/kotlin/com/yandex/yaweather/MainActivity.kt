package com.yandex.yaweather

import android.Manifest.permission
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.yandex.yaweather.Theme.YaWeatherTheme
import com.yandex.yaweather.dagger.application.MainApplication
import com.yandex.yaweather.data.diModules.LocationService
import com.yandex.yaweather.handler.CityScreenAction
import com.yandex.yaweather.handler.CityScreenAction.AddToFavoriteCityList
import com.yandex.yaweather.handler.CityScreenAction.SearchCityAction
import com.yandex.yaweather.handler.MapScreenAction
import com.yandex.yaweather.handler.MapScreenAction.UpdateMarkerPositionAction
import com.yandex.yaweather.handler.WeatherScreenAction
import com.yandex.yaweather.handler.WeatherScreenAction.AddCityAction
import com.yandex.yaweather.handler.WeatherScreenAction.OpenMapAction
import com.yandex.yaweather.ui.screens.CitySelectionScreen
import com.yandex.yaweather.ui.screens.MapScreen
import com.yandex.yaweather.ui.screens.WeatherScreen
import com.yandex.yaweather.viewModel.YaWeatherViewModel
import javax.inject.Inject

class MainActivity : ComponentActivity() {

  object Route {
    const val mainScreen = "MainScreen"
    const val addCityScreen = "AddCityScreen"
    const val openMapScreen = "OpenMapScreen"
  }

  @Inject
  lateinit var viewModel: YaWeatherViewModel

  @Inject
  lateinit var locationService: LocationService
  private val LOCATION_PERMISSION_REQUEST_CODE = 1


  private var lat : Double = 0.0
  private var lon : Double = 0.0

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    (application as MainApplication).mainComponent.inject(this)
    setContent {
      val uiState by viewModel.userCurrentWeatherState.collectAsState()
      val cityItems = viewModel.cities.collectAsState()
      val favoriteCityItems by viewModel.favoriteCityItems.collectAsState()
      viewModel.getCurrentWeather("41.31", "69.24")

      checkPermissionAndFetchLocation()

      if (ContextCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION)
        != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(this,
          arrayOf(permission.ACCESS_COARSE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
      } else {
        fetchLocation()
      }

      viewModel.getCurrentWeather(lat.toString(), lon.toString())
      val navController = rememberNavController()
      YaWeatherTheme {
        NavHost(navController, startDestination = Route.mainScreen) {
          composable(Route.mainScreen) {
            WeatherScreen(uiState, { uiAction -> handleAction(navController, uiAction) })
          }
          composable(Route.addCityScreen) {
            CitySelectionScreen(cityItems, favoriteCityItems) { action -> handleCityAction(action) }
          }
          composable(Route.openMapScreen) {
            MapScreen(uiState, { action -> handleMapAction(action) })
          }
        }
      }
    }
  }

  private fun handleAction(navController: NavController, action: WeatherScreenAction) {
    when (action) {
      is AddCityAction -> navController.navigate(Route.addCityScreen)
      is OpenMapAction -> {navController.navigate(Route.openMapScreen)}
    }
  }


  private fun handleMapAction(action: MapScreenAction) {
    when (action) {
      is UpdateMarkerPositionAction -> {
        viewModel.updateMarkerPosition(action.latLng)
      }
    }
  }

  private fun handleCityAction(action: CityScreenAction) {

    when (action) {
      is SearchCityAction -> {
        viewModel.getCitiesByName(action.query)
      }
      is AddToFavoriteCityList -> {
         viewModel.updateFavoriteCityItems(action.cityItem)
      }
    }
  }

  private fun checkPermissionAndFetchLocation() {
    if (ActivityCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION)
      != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this,
        arrayOf(permission.ACCESS_COARSE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
    } else {
      fetchLocation()
    }
  }

  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
      if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        fetchLocation()
      } else {
        Toast.makeText(this, "Location permission is required for weather updates", Toast.LENGTH_SHORT).show()
      }
    }
  }

  private fun fetchLocation() {
    locationService.getLastKnownLocation()?.addOnSuccessListener { location: Location? ->
      location?.let {
        lat = it.latitude
        lon = it.longitude
        Toast.makeText(this, "Lat: $lat, Lon: $lon", Toast.LENGTH_SHORT).show()
        // Use latitude and longitude to fetch weather data
      } ?: run {
        Toast.makeText(this, "Unable to get location. Try again.", Toast.LENGTH_SHORT).show()
      }
    }
  }
}
