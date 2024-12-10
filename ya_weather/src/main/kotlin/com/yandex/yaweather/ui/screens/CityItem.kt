package com.yandex.yaweather.ui.screens

import android.util.Log
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ContentAlpha
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.yandex.yaweather.Lang
import com.yandex.yaweather.R
import com.yandex.yaweather.appLanguage
import com.yandex.yaweather.handler.CityScreenAction
import com.yandex.yaweather.utils.getWeatherDescription
import com.yandex.yaweather.utils.weatherBackground
import com.yandex.yaweather.viewModel.CitySelectionUIState
import de.charlex.compose.RevealDirection
import de.charlex.compose.RevealSwipe
import de.charlex.compose.rememberRevealState
import de.charlex.compose.reset
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CityItemUi(
  citySelectionUIState: CitySelectionUIState,
  index: Int,
  action: (CityScreenAction) -> Unit,
  modifier: Modifier,
  deleteClick: (Unit) -> Unit,
  redactorClick: (String) -> Unit,
) {
  val revealSwipe = rememberRevealState()
  var isEditing by remember { mutableStateOf(false) }
  var editedCityName by remember { mutableStateOf(citySelectionUIState.cityItem.name ?: "") }

  LaunchedEffect(citySelectionUIState) {
    editedCityName = citySelectionUIState.cityItem.name ?: ""
  }

  if (isEditing) {
    AlertDialog(
      onDismissRequest = { isEditing = false },
      backgroundColor = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary,
      shape = RoundedCornerShape(16.dp),
      title = { Text("  Редактировать город") },
      text = {
        TextField(
          value = editedCityName,
          onValueChange = { editedCityName = it },
          singleLine = true,
          textStyle = androidx.compose.ui.text.TextStyle(fontSize = 20.sp),
          colors = androidx.compose.material.TextFieldDefaults.textFieldColors(
            backgroundColor = androidx.compose.material3.MaterialTheme.colorScheme.onSecondary,
            textColor = androidx.compose.material3.MaterialTheme.colorScheme.inversePrimary,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledTextColor = Color.Transparent,
            focusedLabelColor = Color.Transparent,
            unfocusedLabelColor = Color.Transparent,
          ),
          modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
        )
      },
      confirmButton = {
        Button(
          colors = androidx.compose.material.ButtonDefaults.buttonColors(
            backgroundColor = androidx.compose.material3.MaterialTheme.colorScheme.onSecondary
          ),
          onClick = {
            redactorClick(editedCityName)
            isEditing = false
          }
        ) {
          Text("Save")
        }
      },
      dismissButton = {
        Button(
          onClick = { isEditing = false },
          colors = androidx.compose.material.ButtonDefaults.buttonColors(
            backgroundColor = androidx.compose.material3.MaterialTheme.colorScheme.onSecondary
          )
        ) {
          Text("Cancel")
        }
      }
    )
  }

  Box(
    modifier = Modifier
      .then(modifier)
      .padding(horizontal = 16.dp, vertical = 8.dp)
      .clip(RoundedCornerShape(16.dp))
      .fillMaxWidth()
      .height(120.dp)
  ) {
    RevealSwipe(
      modifier = Modifier.padding(vertical = 5.dp),
      directions = setOf(RevealDirection.StartToEnd, RevealDirection.EndToStart),
      hiddenContentStart = {
        Icon(
          modifier = Modifier.padding(horizontal = 25.dp).clickable {
            redactorClick.invoke(citySelectionUIState.cityItem.name ?: "")
            isEditing = true
          },
          imageVector = Icons.Outlined.Create,
          contentDescription = null,
          tint = Color.White
        )
      },
      hiddenContentEnd = {
        Icon(
          modifier = Modifier.padding(horizontal = 25.dp).clickable {
            deleteClick.invoke(Unit)
          },
          imageVector = Icons.Outlined.Delete,
          contentDescription = null,
          tint = Color.White
        )
      },
      state = revealSwipe
    ) {
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
          .clip(RoundedCornerShape(16.dp)).clickable {
            action(CityScreenAction.OpenSelectedCityScreen(index))
          }
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
          text = if (appLanguage.value == Lang.ru) {
            citySelectionUIState.cityItem.name ?: "Not found"
          } else {
            citySelectionUIState.cityItem.engName ?: "Not found"
          },
          fontSize = 26.sp,
          fontWeight = FontWeight.Bold,
          color = Color.White,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
        Text(
          text = formattedTime ?: "",
          fontSize = 16.sp,
          color = Color.White.copy(alpha = 0.8f)
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
        text = getWeatherDescription(citySelectionUIState.weatherUiState.weatherId, LocalContext.current),
        modifier = Modifier
          .align(Alignment.BottomStart)
          .padding(16.dp),
        fontSize = 14.sp,
        color = Color.White.copy(alpha = 0.8f)
      )

      Text(
        text = "${LocalContext.current.resources.getString(R.string.feelsLike)}: ${citySelectionUIState.weatherUiState.feelsLike}°",
        modifier = Modifier
          .align(Alignment.BottomEnd)
          .padding(16.dp),
        fontSize = 14.sp,
        color = Color.White.copy(alpha = 0.8f)
      )
    }
  }

  LaunchedEffect(citySelectionUIState, index, modifier) {
    revealSwipe.reset()
  }
}
