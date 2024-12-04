package com.yandex.yaweather.ui.screens

import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.yandex.yaweather.R
import com.yandex.yaweather.data.network.CityItem
import com.yandex.yaweather.handler.CityScreenAction
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.ZoneOffset


object TempData {
  val db = mutableListOf<CityItem>()
}


@Composable
fun CitySelectionScreen(cityItems: State<MutableList<CityItem>>, action: (CityScreenAction) -> Unit) {
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
      items(TempData.db) { city ->
        CityItem(city)
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
        .clip(MaterialTheme.shapes.medium)
        .padding(start = 8.dp, end = 8.dp)
        .background(MaterialTheme.colorScheme.surface)
        .padding(bottom = 7.dp)
        .shadow(elevation = 20.dp)
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
              colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
              ),
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
                  modifier = Modifier.size(24.dp)
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
                  TempData.db.add(result)
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
                  .background(MaterialTheme.colorScheme.primaryContainer)
                  .padding(16.dp)
              ) {
                Text(
                  text = result.fullName ?: "",
                  style = MaterialTheme.typography.bodyLarge,
                  color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                  text = "Coordinates: ${result.lat}, ${result.lon}",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onPrimaryContainer
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
fun CityItem(city: CityItem) {
  Box(
    modifier = Modifier
      .padding(horizontal = 16.dp, vertical = 5.dp)
      .clip(RoundedCornerShape(10.dp))
      .fillMaxWidth()
      .height(100.dp)
      .background(color = Color.Black)
  ) {
    Image(
      painter = rememberDrawablePainter(
        drawable = getDrawable(
          LocalContext.current,
          R.drawable.rain_gif
        )
      ),
      contentDescription = "Loading animation",
      contentScale = ContentScale.FillWidth,
    )
    Column(
      modifier = Modifier
        .align(Alignment.TopStart)
        .padding(10.dp)
    ) {
      val currentUtcTime = ZonedDateTime.now(ZoneOffset.UTC)
      val adjustedTime = city.timeZone?.toLong()?.let { currentUtcTime.plusHours(it) }
      val formattedTime = adjustedTime?.format(DateTimeFormatter.ofPattern("HH:mm"))
      Text(text = city.name ?: "", fontSize = 30.sp, color = Color.White)
      Text(formattedTime.toString(), color = Color.White)
    }
    Text(
      text = "10°",
      modifier = Modifier
        .align(Alignment.TopEnd)
        .padding(10.dp),
      fontSize = 40.sp,
      color = Color.White
    )
    Text(
      "мужитский дождь",
      modifier = Modifier
        .align(Alignment.BottomStart)
        .padding(10.dp),
      color = Color.White
    )
    Text(
      "H:${city.lat}° L:${city.lon}°",
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(10.dp),
      color = Color.White
    )
  }
}


