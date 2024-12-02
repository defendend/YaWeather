package com.yandex.yaweather


import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider

object WeatherWidget : GlanceAppWidget() {

  val countKey = intPreferencesKey("count")

  @Composable
  override fun Content() {
    val count = currentState(key = countKey) ?: 0
    Column(
      modifier = GlanceModifier.fillMaxSize().background(color = Color.Cyan),
      verticalAlignment = Alignment.Vertical.CenterVertically,
      horizontalAlignment = Alignment.Horizontal.CenterHorizontally
    ) {
      Row {
        Text(text = "Tashkent", modifier = GlanceModifier.padding(horizontal = 10.dp))
        Text(
          text = count.toString(),
          style = TextStyle(fontWeight = FontWeight.Medium, color = ColorProvider(Color.White), fontSize = 16.sp)
        )
      }
      Button(text = "Inc", onClick = actionRunCallback(IncrementActionCallBack::class.java))
    }
  }
}

class SimpleWeatherWidgetReceiver : GlanceAppWidgetReceiver() {
  override val glanceAppWidget: GlanceAppWidget
    get() = WeatherWidget

}

class IncrementActionCallBack : ActionCallback {
  override suspend fun onAction(
    context: Context,
    glanceId: GlanceId,
    parameters: ActionParameters
  ) {
    updateAppWidgetState(context, glanceId) { prefs ->
      val currentCount = prefs[WeatherWidget.countKey]
      if (currentCount != null) {
        prefs[WeatherWidget.countKey] = currentCount + 1
      } else {
        prefs[WeatherWidget.countKey] = 1
      }
    }
    WeatherWidget.update(context, glanceId)
  }
}