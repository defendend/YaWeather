package com.yandex.yaweather.dagger.application

import android.app.Application
import com.yandex.yaweather.dagger.component.AppComponent
import com.yandex.yaweather.dagger.component.DaggerAppComponent
import com.yandex.yaweather.data.diModules.ApplicationContextProvider


class MainApplication : Application() {

  val mainComponent: AppComponent by lazy {
    DaggerAppComponent.builder()
      .applicationContextProvider(ApplicationContextProvider(this))
      .build()
  }
}