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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.yandex.yaweather.R
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
      .background(color = MaterialTheme.colorScheme.primaryContainer)
      .padding(16.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.Start,
      verticalAlignment = Alignment.CenterVertically
    ) {
      IconButton(onClick = {
        action.invoke(InfoScreenAction.CloseScreenAction)
      }, colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.inversePrimary)) {
        Icon(
          modifier = Modifier
            .size(36.dp)
            .clip(shape = CircleShape),
          imageVector = Icons.Default.KeyboardArrowLeft,
          contentDescription = stringResource(R.string.back_button)
        )
      }
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
      text = stringResource(R.string.app_name),
      color = MaterialTheme.colorScheme.inversePrimary,
      fontSize = 20.sp,
      fontWeight = FontWeight.Bold
    )
    Text(
      text = stringResource(R.string.info_screen_about),
      color = MaterialTheme.colorScheme.inversePrimary,
      fontSize = 20.sp,
      modifier = Modifier.padding(top = 10.dp)
    )
    Text(
      text = stringResource(R.string.info_jetpack),
      color = MaterialTheme.colorScheme.inversePrimary,
      fontSize = 16.sp,
      modifier = Modifier.padding(start = 4.dp, top = 4.dp)
    )
    Text(
      text = stringResource(R.string.info_dagger2),
      color = MaterialTheme.colorScheme.inversePrimary,
      fontSize = 16.sp,
      modifier = Modifier.padding(start = 4.dp)
    )
    Text(
      text = stringResource(R.string.info_room),
      color = MaterialTheme.colorScheme.inversePrimary,
      fontSize = 16.sp,
      modifier = Modifier.padding(start = 4.dp)
    )
    Text(
      text = stringResource(R.string.info_compose),
      color = MaterialTheme.colorScheme.inversePrimary,
      fontSize = 16.sp,
      modifier = Modifier.padding(start = 4.dp)
    )
    Text(
      text = stringResource(R.string.info_retrofit),
      color = MaterialTheme.colorScheme.inversePrimary,
      fontSize = 16.sp,
      modifier = Modifier.padding(start = 4.dp)
    )
    Text(
      text = stringResource(R.string.info_coroutines),
      color = MaterialTheme.colorScheme.inversePrimary,
      fontSize = 16.sp,
      modifier = Modifier.padding(start = 4.dp)
    )
    Text(
      text = stringResource(R.string.info_map),
      color = MaterialTheme.colorScheme.inversePrimary,
      fontSize = 16.sp,
      modifier = Modifier.padding(start = 4.dp)
    )
    Text(
      text = stringResource(R.string.info_work_manager),
      color = MaterialTheme.colorScheme.inversePrimary,
      fontSize = 16.sp,
      modifier = Modifier.padding(start = 4.dp)
    )

    Text(
      text = stringResource(R.string.info_used_resources),
      color = MaterialTheme.colorScheme.inversePrimary,
      fontSize = 20.sp,
      modifier = Modifier.padding(top = 10.dp)
    )
    Text(
      text = stringResource(R.string.info_uri),
      color = MaterialTheme.colorScheme.inversePrimary,
      fontSize = 16.sp,
      modifier = Modifier.padding(start = 4.dp)
    )
    Text(
      text = stringResource(R.string.info_contact_us),
      color = MaterialTheme.colorScheme.inversePrimary,
      fontSize = 20.sp,
      modifier = Modifier.padding(top = 10.dp)
    )
    creatorsList.forEachIndexed { index, item ->
      Creator(item.name, item.uri, context)
    }
    Text(
      text = stringResource(R.string.info_thanks),
      color = MaterialTheme.colorScheme.inversePrimary,
      fontSize = 20.sp,
      modifier = Modifier.padding(top = 20.dp)
    )
  }
  val systemUiController = rememberSystemUiController()
  systemUiController.setStatusBarColor(MaterialTheme.colorScheme.primaryContainer)
  systemUiController.setNavigationBarColor(MaterialTheme.colorScheme.primaryContainer)
}

private fun openTelegram(url: String, context: Context) {
  val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
    setPackage("org.telegram.messenger")
  }
  try {
    context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
  } catch (e: ActivityNotFoundException) {
    e.printStackTrace()
    val fallbackIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(fallbackIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
  }
}

@Composable
private fun Creator(name: String, uri: String, context: Context) {
  Text(text = name,
    color = MaterialTheme.colorScheme.tertiaryContainer,
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