package com.yandex.yaweather.ui.screens

import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.yandex.yaweather.handler.CityScreenAction
import com.yandex.yaweather.utils.weatherBackground
import com.yandex.yaweather.viewModel.CitySelectionUIState
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Composable
fun CityItem(
  citySelectionUIState: CitySelectionUIState, index: Int, action: (CityScreenAction) -> Unit, modifier: Modifier
) {
  Box(modifier = Modifier
    .then(modifier)
    .padding(horizontal = 16.dp, vertical = 8.dp)
    .clip(RoundedCornerShape(16.dp))
    .fillMaxWidth()
    .height(120.dp)
    .clickable {
      action(CityScreenAction.OpenSelectedCityScreen(index))
    }) {
    Image(
      painter = rememberDrawablePainter(
        drawable = getDrawable(
          LocalContext.current, weatherBackground(citySelectionUIState.weatherUiState.weatherId)
        )
      ),
      contentDescription = "Loading animation",
      contentScale = ContentScale.Crop,
      modifier = Modifier
        .fillMaxSize()
        .clip(RoundedCornerShape(16.dp))
    )

    Box(
      modifier = Modifier
        .padding(horizontal = 16.dp, vertical = 8.dp)
        .clip(RoundedCornerShape(16.dp))
        .fillMaxWidth()
        .height(120.dp)
    )

    Column(
      modifier = Modifier
        .align(Alignment.TopStart)
        .padding(16.dp)
    ) {
      val currentUtcTime = ZonedDateTime.now(ZoneOffset.UTC)
      val adjustedTime = citySelectionUIState.cityItem.timeZone?.toLong()?.let { currentUtcTime.plusHours(it) }
      val formattedTime = adjustedTime?.format(DateTimeFormatter.ofPattern("HH:mm"))

      Text(
        text = citySelectionUIState.cityItem.name ?: "",
        fontSize = 26.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )
      Text(
        text = formattedTime ?: "", fontSize = 16.sp, color = Color.White.copy(alpha = 0.8f)
      )
    }
    Text(
      text = "${citySelectionUIState.weatherUiState.temperature}°",
      modifier = Modifier
        .align(Alignment.TopEnd)
        .padding(16.dp),
      fontSize = 36.sp,
      fontWeight = FontWeight.Bold,
      color = Color.White
    )

    Text(
      text = citySelectionUIState.weatherUiState.description,
      modifier = Modifier
        .align(Alignment.BottomStart)
        .padding(16.dp),
      fontSize = 14.sp,
      color = Color.White.copy(alpha = 0.8f)
    )

    Text(
      text = "ощущается: ${citySelectionUIState.weatherUiState.feelsLike}°",
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(16.dp),
      fontSize = 14.sp,
      color = Color.White.copy(alpha = 0.8f)
    )
  }
}
