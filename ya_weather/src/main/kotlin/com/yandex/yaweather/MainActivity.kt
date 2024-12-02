package com.yandex.yaweather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yandex.yaweather.dagger.application.MainApplication
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

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    (application as MainApplication).mainComponent.inject(this)
    setContent {
      val uiState by viewModel.currentWeatherState.collectAsState()
      viewModel.getCurrentWeather("41.31", "69.24")
      val navController = rememberNavController()

      NavHost(navController, startDestination = Route.openMapScreen) {
        composable(Route.mainScreen) {
          WeatherScreen(uiState, { uiAction -> handleAction(navController, uiAction) })
        }
        composable(Route.addCityScreen) {
          CitySelectionScreen()
        }
        composable(Route.openMapScreen) {
          MapScreen(uiState, { action -> handleMapAction(action) })
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
}
