package com.yandex.yaweather.ui.screens

import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.AutoMirrored.Filled
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.yandex.yaweather.R
import com.yandex.yaweather.data.network.CityItem
import com.yandex.yaweather.handler.CityScreenAction
import com.yandex.yaweather.viewModel.CitySelectionUIState
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.ZoneOffset

@Composable
fun CitySelectionScreen(
  cityItems: State<MutableList<CityItem>>,
  favoriteCityItems: MutableList<CitySelectionUIState>,
  action: (CityScreenAction) -> Unit
) {
  var query by remember { mutableStateOf("") }

  Scaffold(
    topBar = {
      WeatherSearchBar(
        query = query,
        cityItems = cityItems,
        action = action,
        onQueryChange = {
          query = it
        },
      )
    }
  ) { paddingValues ->
    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      contentPadding = paddingValues
    ) {
      items(favoriteCityItems) { cityUIState ->
        CityItem(cityUIState, action)
      }
    }
  }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherSearchBar(
  query: String,
  cityItems: State<MutableList<CityItem>>,
  action: (CityScreenAction) -> Unit,
  onQueryChange: (String) -> Unit
) {
  var active by remember { mutableStateOf(false) }
  val recentSearches = remember { mutableStateListOf<String>() }

  Column(modifier = Modifier.fillMaxWidth()) {
    SearchBar(
      query = query,
      onQueryChange = { newQuery ->
        onQueryChange(newQuery)
        if (newQuery.isEmpty()) active = false
      },
      onSearch = {
        action(CityScreenAction.SearchCityAction(query))
      },
      active = active,
      onActiveChange = { active = it },
      leadingIcon = {
        if (active) {
          IconButton(onClick = {
            active = false
            onQueryChange("")
          }) {
            Icon(Filled.ArrowBack, contentDescription = "Назад")
          }
        } else {
          Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Поиск"
          )
        }
      },
      placeholder = { Text(if (query.isEmpty()) "Search cities..." else "") },
      modifier = Modifier
        .fillMaxWidth()
        .padding(start = 8.dp, end = 8.dp)
        .clip(MaterialTheme.shapes.extraSmall)
        .shadow(elevation = 20.dp)
        .background(MaterialTheme.colorScheme.surface)
        .padding(bottom = 7.dp, top = 10.dp)
        .clip(MaterialTheme.shapes.large)


    ) {
      if (query.isEmpty() && recentSearches.isNotEmpty()) {
        Text("Recent searches", modifier = Modifier.padding(8.dp))
        LazyColumn(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
        ) {
          items(recentSearches) { city ->
            Card(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .clip(MaterialTheme.shapes.medium)
                .clickable {
                  onQueryChange(city)
                  active = false
                },
              elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
              )
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Image(
                  painter = painterResource(id = R.drawable.history_icon),
                  contentDescription = "history_icon",
                  modifier = Modifier.size(24.dp),
                  colorFilter = ColorFilter.tint(
                    if (isSystemInDarkTheme()) Color.White else Color.Black
                  )
                )

                Spacer(modifier = Modifier.width(12.dp))
                Text(
                  text = city,
                  style = MaterialTheme.typography.bodyLarge
                )
              }
            }
          }
        }

      } else if (query.isNotEmpty()) {
        Text(
          text = "Search result for \"$query\"",
          modifier = Modifier.padding(8.dp),
          style = MaterialTheme.typography.titleMedium
        )

        LazyColumn(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
        ) {
          val cities = cityItems.value
          items(cities) { result ->
            Card(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .clickable {
                  action(CityScreenAction.AddToFavoriteCityList(result))
                  if (!recentSearches.contains(result.name)) {
                    recentSearches.add(0, result.name ?: "")
                  }
                  onQueryChange("")
                  active = false
                  cityItems.value.clear()
                },
              shape = MaterialTheme.shapes.medium,
              elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
              )
            ) {
              Column(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(16.dp)
              ) {
                Text(
                  text = result.fullName ?: "",
                  style = MaterialTheme.typography.bodyLarge,
                )
                Text(
                  text = "Coordinates: ${result.lat}, ${result.lon}",
                  style = MaterialTheme.typography.bodySmall,
                )
              }
            }
          }
        }
      }
    }
  }
}


@Composable
fun CityItem(citySelectionUIState: CitySelectionUIState, action: (CityScreenAction) -> Unit) {
  Box(
    modifier = Modifier
      .padding(horizontal = 16.dp, vertical = 8.dp)
      .clip(RoundedCornerShape(16.dp))
      .fillMaxWidth()
      .height(120.dp)
      .clickable {
        action(CityScreenAction.OpenMainScreen)
        action(CityScreenAction.UpdateMainScreen(citySelectionUIState.cityItem))
      }
  ) {
    Image(
      painter = rememberDrawablePainter(
        drawable = getDrawable(
          LocalContext.current,
          when {
            citySelectionUIState.weatherUiState.description == "shower rain" -> R.drawable.fall_rain
            citySelectionUIState.weatherUiState.description.contains("rain", ignoreCase = true) -> R.drawable.rain_gif
            citySelectionUIState.weatherUiState.description.contains("clear", ignoreCase = true) -> R.drawable.clear_sky
            citySelectionUIState.weatherUiState.description.contains("scattered", ignoreCase = true) -> R.drawable.scaffered_clouds
            citySelectionUIState.weatherUiState.description.contains("clouds", ignoreCase = true) -> R.drawable.clouds_gif
            citySelectionUIState.weatherUiState.description.contains("thunderstorm", ignoreCase = true) -> R.drawable.thunderstorm
            citySelectionUIState.weatherUiState.description.contains("snow", ignoreCase = true) -> R.drawable.snow_gif
            citySelectionUIState.weatherUiState.description.contains("fog", ignoreCase = true) -> R.drawable.mist
            citySelectionUIState.weatherUiState.description.contains("mist", ignoreCase = true) -> R.drawable.mist
            else -> R.drawable.clear_sky
          }

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
      text = citySelectionUIState.weatherUiState.description,
      modifier = Modifier
        .align(Alignment.BottomStart)
        .padding(16.dp),
      fontSize = 14.sp,
      color = Color.White.copy(alpha = 0.8f)
    )

    Text(
      text = "H:${citySelectionUIState.weatherUiState.temperatureMax}° L:${citySelectionUIState.weatherUiState.temperatureMin}°",
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(16.dp),
      fontSize = 14.sp,
      color = Color.White.copy(alpha = 0.8f)
    )
  }
}
