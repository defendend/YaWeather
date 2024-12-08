package com.yandex.yaweather.dagger.application

import android.app.Application
import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.yandex.yaweather.dagger.component.AppComponent
import com.yandex.yaweather.dagger.component.DaggerAppComponent
import com.yandex.yaweather.data.diModules.ApplicationContextProvider
import com.yandex.yaweather.data.diModules.FavoriteCitiesModule
import com.yandex.yaweather.data.diModules.LocationModule
import com.yandex.yaweather.notification.NotificationWorker
import com.yandex.yaweather.widget.WeatherWorker
import java.util.concurrent.TimeUnit


class MainApplication : Application() {

  val mainComponent: AppComponent by lazy {
    DaggerAppComponent.builder()
      .applicationContextProvider(ApplicationContextProvider(this))
      .locationModule(LocationModule(this))
      .favoriteCitiesModule(FavoriteCitiesModule(this))
      .build()
  }

  override fun onCreate() {
    super.onCreate()
    setupPeriodicWeatherUpdate(this)
    setupPeriodicNotificationUpdate(this)
  }

  fun setupPeriodicWeatherUpdate(context: Context) {
    val workRequest = PeriodicWorkRequestBuilder<WeatherWorker>(3, TimeUnit.HOURS)
      .setConstraints(
        Constraints.Builder()
          .setRequiredNetworkType(NetworkType.CONNECTED)
          .build()
      )
      .build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
      "weather_update",
      ExistingPeriodicWorkPolicy.REPLACE,
      workRequest
    )
  }
  fun setupPeriodicNotificationUpdate(context: Context) {
    val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(2, TimeUnit.HOURS)
      .setInitialDelay(16, TimeUnit.MINUTES)
      .setConstraints(
        Constraints.Builder()
          .setRequiredNetworkType(NetworkType.CONNECTED)
          .build()
      )

      .build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
      "notification_update",
      ExistingPeriodicWorkPolicy.KEEP,
      workRequest
    )
  }
}