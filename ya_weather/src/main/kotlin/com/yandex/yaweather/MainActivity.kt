package com.yandex.yaweather

import android.Manifest.permission
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.yandex.yaweather.Theme.YaWeatherTheme
import com.yandex.yaweather.dagger.application.MainApplication
import com.yandex.yaweather.data.diModules.FavoriteCitiesService
import com.yandex.yaweather.data.diModules.LocationService
import com.yandex.yaweather.handler.CityScreenAction
import com.yandex.yaweather.handler.CityScreenAction.AddToFavoriteCityList
import com.yandex.yaweather.handler.CityScreenAction.MoveCity
import com.yandex.yaweather.handler.CityScreenAction.OpenSelectedCityScreen
import com.yandex.yaweather.handler.CityScreenAction.SearchCityAction
import com.yandex.yaweather.handler.InfoScreenAction
import com.yandex.yaweather.handler.MapScreenAction
import com.yandex.yaweather.handler.MapScreenAction.UpdateMarkerPositionAction
import com.yandex.yaweather.handler.SplashScreenAction
import com.yandex.yaweather.handler.SplashScreenAction.OpenMainScreen
import com.yandex.yaweather.handler.WeatherScreenAction
import com.yandex.yaweather.handler.WeatherScreenAction.AddCityAction
import com.yandex.yaweather.handler.WeatherScreenAction.OpenInfoAction
import com.yandex.yaweather.handler.WeatherScreenAction.OpenMapAction
import com.yandex.yaweather.handler.WeatherScreenAction.SetLanguage
import com.yandex.yaweather.handler.WeatherScreenAction.SetTheme
import com.yandex.yaweather.ui.screens.CitySelectionScreen
import com.yandex.yaweather.ui.screens.InfoScreen
import com.yandex.yaweather.ui.screens.MapScreen
import com.yandex.yaweather.ui.screens.SplashScreen
import com.yandex.yaweather.ui.screens.WeatherScreen
import com.yandex.yaweather.viewModel.YaWeatherViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class Lang {
  uz, ru, en
}

val darkTheme = mutableStateOf(false)
val appLanguage = mutableStateOf(Lang.ru)
private const val LOCATION_FILE = "_location"

class MainActivity : ComponentActivity() {

  object Route {
    const val mainScreen = "MainScreen"
    const val addCityScreen = "AddCityScreen"
    const val openMapScreen = "OpenMapScreen"
    const val SelectedCityScreen = "SelectedCityScreen"
    const val infoScreen = "InfoScreen"
    const val splashScreen = "SplashScreen"
  }

  @Inject
  lateinit var viewModel: YaWeatherViewModel

  @Inject
  lateinit var favoriteCitiesService: FavoriteCitiesService

  @Inject
  lateinit var locationService: LocationService

  private var coordinatesJob: Job? = null
  private var startStopScope: CoroutineScope? = null

  private lateinit var fusedLocationClient: FusedLocationProviderClient
  private val LOCATION_PERMISSION_REQUEST_CODE = 1

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    favoriteCitiesService = FavoriteCitiesService(this)
    (application as MainApplication).mainComponent.inject(this)
    setContent {
      val weatherUiState by viewModel.userCurrentWeatherState.collectAsState()
      val mapUIState by viewModel.mapWeatherState.collectAsState()
      val cityItems = viewModel.cities.collectAsState()
      val favoriteCityItems by viewModel.favoriteCityItems.collectAsState()
      viewModel.loadFavoriteCities(favoriteCitiesService)
      fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
      if (ContextCompat.checkSelfPermission(
          this, permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
      ) {
        ActivityCompat.requestPermissions(
          this, arrayOf(permission.ACCESS_COARSE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE
        )
      } else {
        fetchLocation()
      }
      val navController = rememberNavController()
      val preferences =
        applicationContext.getSharedPreferences(applicationContext.packageName + LOCATION_FILE, MODE_PRIVATE)
      darkTheme.value = preferences.getBoolean("lightTheme", true)
      appLanguage.value = if (preferences.getString("lang", "ru") == "ru") {
        Lang.ru
      } else if (preferences.getString("lang", "uz") == "uz") {
        Lang.uz
      } else Lang.en
      setLanguage(appLanguage.value, this)
      YaWeatherTheme(darkTheme.value) {

        NavHost(navController, startDestination = Route.splashScreen,
          ) {
          composable(Route.mainScreen) {
            WeatherScreen(weatherUiState) { uiAction -> handleAction(navController, uiAction) }
          }
          composable(
            Route.splashScreen,
            enterTransition = {
              when (initialState.destination.route) {
                "details" -> slideIntoContainer(
                  AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(700)
                )

                else -> null
              }
            },
            exitTransition = {
              when (targetState.destination.route) {
                "details" -> slideOutOfContainer(
                  AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(700)
                )

                else -> null
              }
            },
          ) {
            SplashScreen { action -> handleSplashScreenAction(navController, action) }
          }
          composable(Route.addCityScreen) {
            CitySelectionScreen(cityItems, favoriteCitiesService, favoriteCityItems) { action ->
              handleCityAction(
                navController, action
              )
            }
          }
          composable(Route.openMapScreen) {
            MapScreen(mapUIState, { action -> handleMapAction(action)})
          }
          composable(
            route = "${Route.SelectedCityScreen}/{weatherUiStateIndex}",
            arguments = listOf(navArgument("weatherUiStateIndex") { type = NavType.IntType })
          ) { navBackStackEntry ->
            val weatherUiStateIndex = navBackStackEntry.arguments?.getInt("weatherUiStateIndex")
              WeatherScreen(favoriteCityItems[weatherUiStateIndex ?: 0]) { uiAction ->
                handleAction(navController, uiAction)
              }
          }
          composable(Route.infoScreen) {
            InfoScreen { handleInfoScreenAction(navController, it) }
          }
        }
      }
    }
  }

  override fun onStart() {
    super.onStart()
    startStopScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    subscribeToCoordinates()
    subscribeToCoordinatesMap()
  }

  override fun onResume() {
    super.onResume()
    if (checkPermissions()) {
      fetchLocation()
    }
  }

  override fun onStop() {
    super.onStop()
    startStopScope?.cancel()
    startStopScope = null
  }

  private fun subscribeToCoordinates() {
    coordinatesJob = startStopScope?.launch {
      locationService.coordinates.mapNotNull { it }.collect { coordinates ->
        val latitude = kotlin.math.floor(coordinates.first * 10) / 10
        val longitude = kotlin.math.floor(coordinates.second * 10) / 10
        viewModel.getCurrentData(latitude, longitude)
      }
    }
  }

  private fun subscribeToCoordinatesMap() {
    coordinatesJob = startStopScope?.launch {
      locationService.coordinates.mapNotNull { it }.collect { coordinates ->
        val latitude = String.format("%.2f", coordinates.first).replace(",", ".")
        val longitude = String.format("%.2f", coordinates.second).replace(",", ".")
        viewModel.getMapInfo(latitude, longitude)
      }
    }
  }

  private fun handleAction(navController: NavController, action: WeatherScreenAction) {
    when (action) {
      is AddCityAction -> navController.navigate(Route.addCityScreen)
      is OpenMapAction -> navController.navigate(Route.openMapScreen)
      is OpenInfoAction -> navController.navigate(Route.infoScreen)
      is SetTheme -> setTheme(action.lightTheme)
      is SetLanguage -> setLanguage(action.language, this)
    }
  }


  private fun setTheme(lightTheme: Boolean) {
    val preferences =
      applicationContext.getSharedPreferences(applicationContext.packageName + LOCATION_FILE, MODE_PRIVATE)
    preferences.edit().apply {
      putBoolean("lightTheme", lightTheme)
    }.apply()
  }

  fun setLanguage(language: Lang, context: Context) {
    val config = context.resources.configuration
    val locale = java.util.Locale(language.name)
    java.util.Locale.setDefault(locale)
    config.setLocale(locale)
    context.createConfigurationContext(config)
    context.resources.updateConfiguration(config, context.resources.displayMetrics)
    val preferences =
      applicationContext.getSharedPreferences(applicationContext.packageName + LOCATION_FILE, MODE_PRIVATE)
    if (language == Lang.uz) {
      preferences.edit().apply {
        putString("lang", "uz")
      }.apply()
      appLanguage.value = Lang.uz
    } else if (language == Lang.ru) {
      preferences.edit().apply {
        putString("lang", "ru")
      }.apply()
      appLanguage.value = Lang.ru
    } else {
      preferences.edit().apply {
        putString("lang", "en")
      }.apply()
      appLanguage.value = Lang.en
    }
  }

  private fun handleInfoScreenAction(navController: NavController, action: InfoScreenAction) {
    when (action) {
      is InfoScreenAction.CloseScreenAction -> navController.popBackStack()
    }
  }

  private fun checkPermissions(): Boolean {
    return ActivityCompat.checkSelfPermission(
      this, permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
      this, permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
  }

  private fun handleMapAction(action: MapScreenAction) {
    when (action) {
      is UpdateMarkerPositionAction -> {
        viewModel.updateMarkerPosition(action.latLng)
      }
    }
  }

  private fun handleCityAction(navController: NavController, action: CityScreenAction) {

    when (action) {
      is SearchCityAction -> {
        viewModel.getCitiesByName(action.query)
      }

      is AddToFavoriteCityList -> {
        viewModel.updateFavoriteCityItems(action.cityItem)
      }

      is OpenSelectedCityScreen -> {
        navController.navigate("${Route.SelectedCityScreen}/${action.index}")
      }

      is MoveCity -> {
        viewModel.moveCity(action.fromIndex, action.toIndex)
      }
    }
  }

  private fun handleSplashScreenAction(navController: NavController, action: SplashScreenAction) {
    when (action) {
      OpenMainScreen -> {
        navController.navigate(Route.mainScreen) {
          popUpTo(navController.graph.startDestinationId) {
            inclusive = true
          }
          launchSingleTop = true
        }
      }
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
    if (ActivityCompat.checkSelfPermission(
        this, permission.ACCESS_COARSE_LOCATION
      ) == PackageManager.PERMISSION_GRANTED
    ) {
      fusedLocationClient.lastLocation.addOnCompleteListener { task ->
        val location = task.result
        location?.let {
          val latitude = it.latitude
          val longitude = it.longitude
          locationService.setLocation(latitude to longitude)
        } ?: run {
          requestNewLocationData()
          Toast.makeText(this, "Unable to get location. Try again.", Toast.LENGTH_SHORT).show()
        }
      }
    }
  }

  @SuppressLint("MissingPermission")
  private fun requestNewLocationData() {

    // Initializing LocationRequest
    // object with appropriate methods
    val mLocationRequest = LocationRequest()
    mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    mLocationRequest.interval = 5
    mLocationRequest.fastestInterval = 0
    mLocationRequest.numUpdates = 1

    // setting LocationRequest
    // on FusedLocationClient
    fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    fusedLocationClient.requestLocationUpdates(
      mLocationRequest, mLocationCallback, Looper.getMainLooper()
    )
  }

  private val mLocationCallback: LocationCallback = object : LocationCallback() {
    override fun onLocationResult(locationResult: LocationResult) {
      val mLastLocation = locationResult.lastLocation
      if (mLastLocation != null) {
        locationService.setLocation(mLastLocation.latitude to mLastLocation.longitude)
      }
    }
  }

}
