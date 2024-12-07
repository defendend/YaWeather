package com.yandex.yaweather.handler

sealed class SplashScreenAction {
  data object OpenMainScreen: SplashScreenAction()
}