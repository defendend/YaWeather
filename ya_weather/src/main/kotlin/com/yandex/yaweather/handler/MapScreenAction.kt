package com.yandex.yaweather.handler

import com.google.android.gms.maps.model.LatLng

sealed class MapScreenAction {
  data class UpdateMarkerPositionAction(val latLng: LatLng) : MapScreenAction()

}