package com.yandex.yaweather.data.diModules

import com.yandex.yaweather.repository.CityFinderRepository
import com.yandex.yaweather.repository.CityFinderRepositoryImp
import dagger.Binds
import dagger.Module

@Module
interface CityFinderProvider {

  @Binds
  fun bindCityFinderRepository(cityFinderRepositoryImp: CityFinderRepositoryImp) : CityFinderRepository
}