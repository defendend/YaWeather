package com.yandex.yaweather.dagger.component

import com.yandex.yaweather.MainActivity
import dagger.Component
import com.yandex.yaweather.data.LocalStorage
import com.yandex.yaweather.data.diModules.ApplicationContextProvider

@Component(modules = [ApplicationContextProvider::class])
interface AppComponent {
    fun inject(activity: MainActivity)
}