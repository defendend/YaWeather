package com.yandex.yaweather.dagger.component

import com.yandex.yaweather.MainActivity
import dagger.Component
import com.yandex.yaweather.data.diModules.ApplicationContextProvider
import com.yandex.yaweather.data.diModules.CityFinderProvider
import com.yandex.yaweather.data.diModules.OpenWeatherRepositoryProvider
import data.network.NetworkProvider

@Component(
  modules = [
    ApplicationContextProvider::class,
    NetworkProvider::class,
    OpenWeatherRepositoryProvider::class,
    CityFinderProvider::class
  ]
)
interface AppComponent {
  fun inject(activity: MainActivity)
}