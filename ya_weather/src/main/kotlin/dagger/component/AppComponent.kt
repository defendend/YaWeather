package dagger.component

import com.yandex.yaweather.MainActivity
import dagger.Component
import data.LocalStorage
import data.diModules.ApplicationContextProvider

@Component(modules = [ApplicationContextProvider::class])
interface AppComponent {
    fun inject(activity: MainActivity)
}