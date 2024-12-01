package com.yandex.yaweather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yandex.yaweather.ui.screens.WeatherScreen
import com.yandex.yaweather.viewModel.YaWeatherViewModel
import dagger.application.MainApplication
import com.yandex.yaweather.ui.screens.MapScreen
import javax.inject.Inject

class MainActivity : ComponentActivity() {

  object Route {
    const val mainScreen = "MainScreen"
  }


  @Inject
  lateinit var viewModel: YaWeatherViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    (application as MainApplication).mainComponent.inject(this)
    setContent {
      val currentWeatherState = viewModel.currentWeatherState.collectAsState()
      val navController = rememberNavController()

      NavHost(navController, startDestination = "weather_screen") {
        composable("weather_screen") {
          WeatherScreen()
        }
      }
    }
  }
}
