package com.yandex.yaweather.data.diModules

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class LocationModule(private val context: Context) {
  @Provides
  fun provideLocationService(): LocationService {
    return LocationService(context)
  }
}