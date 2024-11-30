package com.yandex.yaweather.data

import android.content.Context
import javax.inject.Inject


class LocalStorage @Inject constructor(context: Context) : SharedPreference(context) {
    var isDark by booleans(false)
    var token by strings("Nellas")
}