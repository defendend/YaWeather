package com.yandex.yaweather.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.yandex.yaweather.R
import com.yandex.yaweather.data.network.CityItem
import com.yandex.yaweather.handler.CityScreenAction


object TempData {
  val db = mutableListOf<CityItem>()
}


@Composable
fun CitySelectionScreen(cityItems: State<MutableList<CityItem>>, action: (CityScreenAction) -> Unit) {
  var query by remember { mutableStateOf("") }
  var filteredCities by remember { mutableStateOf(getCities()) }

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
      items(filteredCities) { city ->
        CityItem(city)
      }
    }
  }
}

fun getCities(): List<CityItem> {
  return TempData.db
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherSearchBar(query: String, cityItems: State<MutableList<CityItem>>, action: (CityScreenAction) -> Unit, onQueryChange: (String) -> Unit) {
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
            Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
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
        .clip(MaterialTheme.shapes.medium).padding(start = 8.dp, end = 8.dp)
        .background(MaterialTheme.colorScheme.surface)
        .padding(bottom = 7.dp)
        .shadow(elevation = 20.dp)
    ) {
      if (query.isEmpty() && recentSearches.isNotEmpty()) {
        Text("Recent searches", modifier = Modifier.padding(8.dp))
        LazyColumn {
          items(recentSearches) { city ->
            Text(
              text = city,
              modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(16.dp)
                .clip(MaterialTheme.shapes.small)
                .clickable {
                  onQueryChange(city)
                  active = false
                }
            )
          }
        }
      } else if (query.isNotEmpty()) {
        Text(
          text = "Search result for \"$query\"",
          modifier = Modifier.padding(8.dp)
        )
        LazyColumn(
          modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
        ) {
          val cities = cityItems.value
          items(cities) { result ->
            Text(
              text = result.fullName?: "",
              modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(16.dp)
                .clip(MaterialTheme.shapes.small)
                .clickable {
                  TempData.db.add(result)
                  if (!recentSearches.contains(result.name)) {
                    recentSearches.add(0, result.name?: "")
                  }
                  onQueryChange("")
                  active = false
                }
            )
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
    Column(modifier = Modifier
      .align(Alignment.TopStart)
      .padding(10.dp)) {
      Text(text = city.name?: "", fontSize = 30.sp, color = Color.White)
      Text("12:13 PM", color = Color.White)
    }
    Text(
      text = "$10°",
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


