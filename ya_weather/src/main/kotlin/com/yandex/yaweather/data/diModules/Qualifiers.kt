package com.yandex.yaweather.data.diModules

import jakarta.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class WeatherRetrofitQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CityRetrofitQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class HourlyWeatherRetrofitQualifier
