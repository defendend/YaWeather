package com.yandex.yaweather.data.diModules

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides

@Module
class LocationModule(private val context: Context) {
  @Provides
  fun provideFusedLocationProviderClient(): FusedLocationProviderClient {
    return LocationServices.getFusedLocationProviderClient(context)
  }

  @Provides
  fun provideLocationService(fusedLocationProviderClient: FusedLocationProviderClient): LocationService {
    return LocationService(context, fusedLocationProviderClient)
  }
}