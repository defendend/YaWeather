package com.yandex.yaweather.data.diModules

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class ApplicationContextProvider(private val application: Application) {

  @Provides
  fun provideContext(): Context {
    return application.applicationContext
  }
}