package com.yandex.yaweather.data.diModules

import com.yandex.yaweather.repository.HourlyWeatherRepository
import com.yandex.yaweather.repository.HourlyWeatherRepositoryImp
import dagger.Binds
import dagger.Module

@Module
interface MeteostatRepositoryProvider {

  @Binds
  fun bindMeteostatRepository(meteostatRepositoryImp: HourlyWeatherRepositoryImp) : HourlyWeatherRepository
}