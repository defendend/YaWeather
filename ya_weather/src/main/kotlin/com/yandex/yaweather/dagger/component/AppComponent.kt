package com.yandex.yaweather.dagger.component

import com.yandex.yaweather.MainActivity
import dagger.Component
import com.yandex.yaweather.data.diModules.ApplicationContextProvider
import com.yandex.yaweather.data.diModules.OpenWeatherRepositoryProvider
import com.yandex.yaweather.data.diModules.NetworkProvider

@Component(modules = [ApplicationContextProvider::class, NetworkProvider::class, OpenWeatherRepositoryProvider::class])
interface AppComponent {
    fun inject(activity: MainActivity)
}