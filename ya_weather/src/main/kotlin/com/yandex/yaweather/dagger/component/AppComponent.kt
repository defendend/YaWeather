package com.yandex.yaweather.dagger.component

import com.yandex.yaweather.MainActivity
import com.yandex.yaweather.data.diModules.ApplicationContextProvider
import com.yandex.yaweather.data.diModules.CityFinderProvider
import com.yandex.yaweather.data.diModules.LocationModule
import com.yandex.yaweather.data.diModules.NetworkProvider
import com.yandex.yaweather.data.diModules.OpenWeatherRepositoryProvider
import dagger.Component

@Component(
  modules = [
    ApplicationContextProvider::class,
    NetworkProvider::class,
    OpenWeatherRepositoryProvider::class,
    CityFinderProvider::class,
    LocationModule::class
  ]
)
interface AppComponent {
  fun inject(activity: MainActivity)
}