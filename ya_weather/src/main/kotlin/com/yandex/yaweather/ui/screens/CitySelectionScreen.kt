package com.yandex.yaweather.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
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
  Scaffold(
    topBar = {
      WeatherSearchBar()
    }
  ) { paddingValues ->
    LazyColumn(contentPadding = paddingValues) {
      item {City("Tashkent", 100, 30, "rain" ,30, 30)}
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherSearchBar() {
  var query by remember { mutableStateOf("") }
  var active by remember { mutableStateOf(false) }

  SearchBar(
    query = query,
    onQueryChange = { query = it },
    onSearch = { /* Handle search */ },
    active = active,
    onActiveChange = { active = it },
    placeholder = { Text("Search...") },
    modifier = Modifier
      .fillMaxWidth()
      .padding(10.dp)
  ) {
    Text("Search result for \"$query\"")
  }
}

@Composable
fun City(CityName: String, cityTimeMinutes: Int, temperatureCelsius: Int, weather: String, h: Int, l: Int) {
  cityTimeMinutes / (1000 * 60) % 60
  val hours = cityTimeMinutes / (1000 * 60 * 60) % 24
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
      Text(text = CityName, fontSize = 30.sp, color = Color.White)
      Text("12:13 PM", color = Color.White)
    }
    Text(text = "$temperatureCelsius°", modifier = Modifier
      .align(Alignment.TopEnd)
      .padding(10.dp), fontSize = 40.sp, color = Color.White)
    Text(weather, modifier = Modifier
      .align(Alignment.BottomStart)
      .padding(10.dp), color = Color.White)
    Text("H:$h° L:$l°", modifier = Modifier
      .align(Alignment.BottomEnd)
      .padding(10.dp), color = Color.White)
  }
}