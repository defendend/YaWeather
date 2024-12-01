package com.yandex.yaweather

//import ui.screens.WeatherScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
    }

//    @Inject
//    lateinit var YaWeatherViewModel: YaWeatherViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
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
