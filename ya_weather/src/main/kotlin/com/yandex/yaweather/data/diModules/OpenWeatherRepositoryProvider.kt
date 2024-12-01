package com.yandex.yaweather.data.diModules

import com.yandex.yaweather.data.network.WeatherApi
import com.yandex.yaweather.repository.OpenWeatherRepository
import com.yandex.yaweather.repository.OpenWeatherRepositoryImp
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface OpenWeatherRepositoryProvider {

  @Binds
  fun bindOpenWeatherRepository(openWeatherRepositoryImp: OpenWeatherRepositoryImp) : OpenWeatherRepository
}