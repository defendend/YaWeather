package com.yandex.yaweather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yandex.yaweather.ui.screens.CitySelectionScreen
import com.yandex.yaweather.ui.screens.SplashScreen
import com.yandex.yaweather.ui.screens.WeatherScreen
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
    enableEdgeToEdge()
    (application as MainApplication).mainComponent.inject(this)
    setContent {
      val uiState by viewModel.currentWeatherState.collectAsState()
      viewModel.getCurrentWeather("41.31", "69.24")
      val navController = rememberNavController()

            // Use NavHost to manage navigation
            NavHost(navController, startDestination = "splash_screen") {
                composable("splash_screen") {
                    SplashScreen(navController)
                }
                composable("weather_screen") {
                    WeatherScreen(navController)
                }
                composable("city_selection_screen") {
                    CitySelectionScreen()
                }

            }
        }
    }
  }

  private fun handleAction(navController: NavController, action: WeatherScreenAction) {
    when (action) {
      is AddCityAction -> navController.navigate(Route.addCityScreen)
      is OpenMapAction -> {}
    }
  }
}
