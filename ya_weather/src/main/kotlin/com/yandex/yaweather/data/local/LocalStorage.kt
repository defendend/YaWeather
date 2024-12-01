package com.yandex.yaweather.data.local

import android.content.Context
import javax.inject.Inject


class LocalStorage @Inject constructor(context: Context) : SharedPreference(context) {
    var isDark by booleans(false)
    var token by strings("Nellas")
}