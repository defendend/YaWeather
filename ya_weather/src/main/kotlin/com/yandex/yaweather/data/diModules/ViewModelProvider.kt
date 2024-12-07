package com.yandex.yaweather.data.diModules

import com.yandex.yaweather.repository.CityFinderRepository
import com.yandex.yaweather.repository.HourlyWeatherRepository
import com.yandex.yaweather.repository.OpenWeatherRepository
import com.yandex.yaweather.viewModel.YaWeatherViewModel
import dagger.Module
import dagger.Provides

@Module
class ViewModelProvider {
  @Provides
  fun provideViewModule(openWeatherRepository: OpenWeatherRepository, cityFinderRepository: CityFinderRepository, hourlyWeatherRepository: HourlyWeatherRepository) : YaWeatherViewModel
  {
    return YaWeatherViewModel(openWeatherRepository, cityFinderRepository, hourlyWeatherRepository)
  }
}