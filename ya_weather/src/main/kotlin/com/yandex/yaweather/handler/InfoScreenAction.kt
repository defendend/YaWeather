package com.yandex.yaweather.handler

sealed class InfoScreenAction {
  data object CloseScreenAction : InfoScreenAction()
}