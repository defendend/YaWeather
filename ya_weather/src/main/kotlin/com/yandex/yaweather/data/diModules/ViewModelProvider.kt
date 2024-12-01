package com.yandex.yaweather.data.diModules

import com.yandex.yaweather.repository.OpenWeatherRepository
import com.yandex.yaweather.viewModel.YaWeatherViewModel
import dagger.Module
import dagger.Provides

@Module
class ViewModelProvider {
  @Provides
  fun provideViewModule(openWeatherRepository: OpenWeatherRepository) : YaWeatherViewModel
  {
    return YaWeatherViewModel(openWeatherRepository)
  }
}