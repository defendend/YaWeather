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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.yandex.yaweather.R

object TempData {
  val db = mutableListOf<City>(
      City("Tashkent", 10, 30, "rain", 30, 30),
      City("Moscow", 5, 25, "cloud", 20, 25),
      City("London", 8, 28, "clear", 15, 18),
      City("New York", 2, 15, "snow", 10, 12),
      City("Tokyo", 12, 32, "rain", 40, 35),
      City("Paris", 6, 22, "cloud", 25, 20),
      City("Berlin", 7, 27, "clear", 18, 22),
      City("Sydney", 15, 35, "rain", 50, 40),
      City("Toronto", -5, 10, "snow", 12, 15),
      City("Dubai", 20, 40, "clear", 10, 10),
      City("Beijing", 9, 29, "cloud", 30, 28),
      City("Madrid", 14, 33, "clear", 35, 32),
      City("Seoul", 3, 18, "snow", 5, 8),
      City("Rome", 11, 31, "rain", 20, 18),
      City("Istanbul", 10, 28, "cloud", 15, 20),
      City("Cairo", 18, 38, "clear", 5, 5),
      City("Bangkok", 22, 35, "rain", 60, 45),
      City("Johannesburg", 8, 25, "cloud", 22, 18),
      City("Buenos Aires", 12, 26, "clear", 28, 24),
      City("Moscow", -3, 14, "snow", 7, 9)
    )
}
@Composable
fun CitySelectionScreen() {
  var query by remember { mutableStateOf("") }
  var filteredCities by remember { mutableStateOf(getCities()) }

  Scaffold(
    topBar = {
      WeatherSearchBar(
        query = query,
        onQueryChange = {
          query = it
          filteredCities = getCities().filter { city ->
            city.name.contains(query, ignoreCase = true)
          }
        },
      )
    }
  ) { paddingValues ->
    LazyColumn(
      modifier = Modifier.fillMaxSize().padding(16.dp),
      contentPadding = paddingValues
    ) {
      items(filteredCities) { city ->
        CityItem(city)
      }
    }
  }
}

fun getCities(): List<City> {
  return TempData.db
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherSearchBar(query: String, onQueryChange: (String) -> Unit) {
  var active by remember { mutableStateOf(false) }
  val recentSearches = remember { mutableStateListOf<String>() }

  Column(modifier = Modifier.fillMaxWidth()) {
    SearchBar(
      query = query,
      onQueryChange = { newQuery ->
        onQueryChange(newQuery)
        if (newQuery.isEmpty()) active = false
      },
      onSearch = { /* Обработка поиска */ },
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
        .padding(10.dp)
        .clip(MaterialTheme.shapes.medium)
        .background(MaterialTheme.colorScheme.surface)
    ) {
      if (query.isEmpty() && recentSearches.isNotEmpty()) {
        Text("Recent searches", modifier = Modifier.padding(8.dp))
        LazyColumn {
          items(recentSearches) { city ->
            Text(
              text = city,
              modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(16.dp)
                .clip(MaterialTheme.shapes.small)
                .clickable {
                  onQueryChange(city) // Подстановка из истории
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
          val cities = listOf(
            City("Залупинск", -2, 10, "snow", 15, 12),
            City("Пердюевка", 5, 18, "cloud", 20, 14),
            City("Забулдыгино", 7, 20, "rain", 25, 18),
            City("Гадюкино", 10, 25, "clear", 10, 8),
            City("Зашкварово", 8, 22, "cloud", 30, 20),
            City("Чебуреченск", 12, 28, "rain", 35, 22),
            City("Кукуевка", 3, 15, "snow", 18, 10),
            City("Дно", -5, 8, "cloud", 12, 9),
            City("Жопинск", 0, 10, "rain", 22, 15),
            City("Грязюкино", 4, 18, "clear", 16, 14),
            City("Пупыркино", 9, 24, "cloud", 20, 17),
            City("Трындино", 11, 29, "rain", 25, 19),
            City("Карачиха", -1, 12, "snow", 14, 11),
            City("Заборье", 6, 19, "clear", 18, 13),
            City("Нахреново", 2, 16, "rain", 20, 12),
            City("Глухомань", -3, 8, "snow", 10, 8)
          )
          items(cities.filter { it.name.contains(query, ignoreCase = true) }) { result ->
            Text(
              text = result.name,
              modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(16.dp)
                .clip(MaterialTheme.shapes.small)
                .clickable {
                  TempData.db.add(result)
                  if (!recentSearches.contains(result.name)) {
                    recentSearches.add(0, result.name)
                  }
                  onQueryChange("") // Очистка строки поиска
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
fun CityItem(city: City) {
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
          when(city.weather) {
            "clear" -> R.drawable.clear_sky
            "rain" -> R.drawable.rain_gif
            "snow" -> R.drawable.snow_gif
            "cloud" -> R.drawable.cloudy_gif
            else -> R.drawable.clear_sky

          }
        )
      ),
      contentDescription = "Loading animation",
      contentScale = ContentScale.FillWidth,
    )
    Column(modifier = Modifier
      .align(Alignment.TopStart)
      .padding(10.dp)) {
      Text(text = city.name, fontSize = 30.sp, color = Color.White)
      Text("12:13 PM", color = Color.White)
    }
    Text(
      text = "${city.temperatureCelsius}°",
      modifier = Modifier
        .align(Alignment.TopEnd)
        .padding(10.dp),
      fontSize = 40.sp,
      color = Color.White
    )
    Text(
      city.weather,
      modifier = Modifier
        .align(Alignment.BottomStart)
        .padding(10.dp),
      color = Color.White
    )
    Text(
      "H:${city.h}° L:${city.l}°",
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(10.dp),
      color = Color.White
    )
  }
}

data class City(
  val name: String,
  val temperatureCelsius: Int,
  val humidity: Int,
  val weather: String,
  val h: Int,
  val l: Int
)


