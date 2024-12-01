package com.yandex.yaweather.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


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
          // Фильтруем города по запросу
          filteredCities = getCities().filter { city ->
            city.name.contains(query, ignoreCase = true)
          }
        }
      )
    }
  ) { paddingValues ->
    LazyColumn(
      modifier = Modifier.fillMaxSize().padding(16.dp),
      contentPadding = paddingValues
    ) {
      items(filteredCities) { city ->
        CityItem(city) // Отображаем информацию о городе
      }
    }
  }
}

// Функция для генерации списка городов
fun getCities(): List<City> {
  return List(20) {
    City("Tashkent", 100, 30, "rain", 30, 30) // Пример данных о городе
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherSearchBar(query: String, onQueryChange: (String) -> Unit) {
  var active by remember { mutableStateOf(false) }

  Column(
    modifier = Modifier.fillMaxWidth()
  ) {
    SearchBar(
      query = query,
      onQueryChange = onQueryChange,
      onSearch = { /* Обработка поиска */ },
      active = active,
      onActiveChange = { active = it },
      placeholder = { Text("Search cities...") },
      modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp)
        .clip(MaterialTheme.shapes.medium)
        .background(MaterialTheme.colorScheme.surface)
    ) {
      if (query.isNotEmpty()) {
        Text(
          text = "Search result for \"$query\"",
          modifier = Modifier.padding(8.dp)
        )
        LazyColumn(
          modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
        ) {
          items(listOf("Хуйинск", "Залупинск", "Мухосранск")) { result ->
            Text(
              text = result,
              modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(16.dp)
                .clip(MaterialTheme.shapes.small)
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

