package com.yandex.yaweather.ui.screens

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.yandex.yaweather.R
import com.yandex.yaweather.Theme.InfoScreenText
import com.yandex.yaweather.handler.InfoScreenAction

@Composable
fun InfoScreen(action: (InfoScreenAction) -> Unit) {
  val context = LocalContext.current
  val creatorsList = mutableListOf<Creators>(
    Creators("Мухаммадали Элибаев", "https://t.me/myxabzbzzzzz"),
    Creators("Отабек Нортожиев", "https://t.me/future_software_developer"),
    Creators("Аббосбек Нормуратов", "https://t.me/Abbosbek_Normuratov"),
    Creators("Нодир Абдивайитов", "https://t.me/imbaFFF"),
    Creators("Одилжон Бобосодиков", "https://t.me/bobo0d"),
    Creators("Нурбек Мухторов", "https://t.me/m0tik")
  )
  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(color = Color.White)
      .padding(16.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.Start,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Image(painter = painterResource(R.drawable.back),
        contentDescription = stringResource(R.string.back_button),
        modifier = Modifier
          .size(36.dp)
          .clip(shape = CircleShape)
          .clickable {
            action.invoke(InfoScreenAction.CloseScreenAction)
          })
    }
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.Center,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Image(
        painter = painterResource(R.drawable.info_image),
        contentDescription = stringResource(R.string.info_screen_image),
        modifier = Modifier.size(100.dp)
      )
    }
    Text(
      text = "YaWeather", color = InfoScreenText, fontSize = 20.sp, fontWeight = FontWeight.Bold
    )
    Text(
      text = "Информация о приложении",
      color = InfoScreenText,
      fontSize = 20.sp,
      modifier = Modifier.padding(top = 10.dp)
    )
    Text(
      text = "- Jetpack Navigation Component",
      color = InfoScreenText,
      fontSize = 16.sp,
      modifier = Modifier.padding(start = 4.dp, top = 4.dp)
    )
    Text(text = "- Dagger 2", color = InfoScreenText, fontSize = 16.sp, modifier = Modifier.padding(start = 4.dp))
    Text(text = "- Room", color = InfoScreenText, fontSize = 16.sp, modifier = Modifier.padding(start = 4.dp))
    Text(text = "- Compose", color = InfoScreenText, fontSize = 16.sp, modifier = Modifier.padding(start = 4.dp))
    Text(text = "- Retrofit", color = InfoScreenText, fontSize = 16.sp, modifier = Modifier.padding(start = 4.dp))
    Text(text = "- Coroutines", color = InfoScreenText, fontSize = 16.sp, modifier = Modifier.padding(start = 4.dp))
    Text(text = "- Google Map", color = InfoScreenText, fontSize = 16.sp, modifier = Modifier.padding(start = 4.dp))
    Text(text = "- WorkManager", color = InfoScreenText, fontSize = 16.sp, modifier = Modifier.padding(start = 4.dp))

    Text(
      text = "Использованные ресурсы",
      color = InfoScreenText,
      fontSize = 20.sp,
      modifier = Modifier.padding(top = 10.dp)
    )
    Text(
      text = "- https://openweathermap.org",
      color = InfoScreenText,
      fontSize = 16.sp,
      modifier = Modifier.padding(start = 4.dp)
    )
    Text(
      text = "Связаться с нами", color = InfoScreenText, fontSize = 20.sp, modifier = Modifier.padding(top = 10.dp)
    )
    creatorsList.forEachIndexed { index, item ->
      Creator(item.name, item.uri, context)
    }
    Text(
      text = "Спасибо за внимание!", color = InfoScreenText, fontSize = 20.sp, modifier = Modifier.padding(top = 20.dp)
    )
  }
  val systemUiController = rememberSystemUiController()
  systemUiController.setStatusBarColor(Color.White)
  systemUiController.setNavigationBarColor(Color.White)
}

private fun openTelegram(url: String, context: Context) {
  val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
    // Указываем пакет Telegram для предотвращения открытия других приложений
    setPackage("org.telegram.messenger")
  }
  try {
    context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
  } catch (e: ActivityNotFoundException) {
    e.printStackTrace()
    // Если Telegram не установлен, можно уведомить пользователя или открыть в браузере
    val fallbackIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(fallbackIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
  }
}

@Composable
private fun Creator(name: String, uri: String, context: Context) {
  Text(text = name,
    color = Color.Blue,
    fontSize = 16.sp,
    modifier = Modifier
      .clip(shape = RoundedCornerShape(10.dp))
      .padding(start = 4.dp)
      .clickable {
        openTelegram(uri, context)
      }
      .padding(2.dp),
    textDecoration = TextDecoration.Underline)
}

private data class Creators(val name: String, val uri: String)