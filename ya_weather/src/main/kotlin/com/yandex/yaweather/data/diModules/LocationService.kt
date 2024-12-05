package com.yandex.yaweather.data.diModules

import android.Manifest.permission
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.tasks.Task
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationService @Inject constructor(
  private val context: Context,
  private val fusedLocationClient: FusedLocationProviderClient
) {
  fun getLastKnownLocation(): Task<Location>? {
    return if (ActivityCompat.checkSelfPermission(
        context, permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    ) {
      fusedLocationClient.lastLocation
    } else {
      null
    }
  }
}