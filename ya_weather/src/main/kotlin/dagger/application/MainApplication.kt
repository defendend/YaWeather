package dagger.application

import android.app.Application
import dagger.component.AppComponent
import dagger.component.DaggerAppComponent
import data.diModules.ApplicationContextProvider


class MainApplication : Application() {

  val mainComponent: AppComponent by lazy {
    DaggerAppComponent.builder()
      .applicationContextProvider(ApplicationContextProvider(this))
      .build()
  }
}