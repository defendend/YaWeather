package com.yandex.yaweather.data.diModules

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class FavoriteCitiesModule(private val context: Context) {

  @Provides
  fun provideFavoriteCitiesService(): FavoriteCitiesService {
    return FavoriteCitiesService(context)
  }
}
