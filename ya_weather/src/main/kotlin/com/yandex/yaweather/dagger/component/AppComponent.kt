package com.yandex.yaweather.dagger.component

import com.yandex.yaweather.MainActivity
import com.yandex.yaweather.data.diModules.ApplicationContextProvider
import com.yandex.yaweather.data.diModules.CityFinderProvider
import com.yandex.yaweather.data.diModules.FavoriteCitiesModule
import com.yandex.yaweather.data.diModules.LocationModule
import com.yandex.yaweather.data.diModules.MeteostatRepositoryProvider
import com.yandex.yaweather.data.diModules.NetworkProvider
import com.yandex.yaweather.data.diModules.OpenWeatherRepositoryProvider
import com.yandex.yaweather.notification.NotificationWorker
import com.yandex.yaweather.widget.WeatherWorker
import dagger.Component

@Component(
  modules = [
    ApplicationContextProvider::class,
    NetworkProvider::class,
    OpenWeatherRepositoryProvider::class,
    CityFinderProvider::class,
    LocationModule::class,
    FavoriteCitiesModule::class,
    MeteostatRepositoryProvider::class
  ]
)
interface AppComponent {
  fun inject(activity: MainActivity)
  fun inject(worker: WeatherWorker)
  fun inject(worker: NotificationWorker)
}