package com.yandex.yaweather.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.yandex.yaweather.R
import com.yandex.yaweather.data.diModules.FavoriteCitiesService
import com.yandex.yaweather.data.network.CityItem
import com.yandex.yaweather.handler.CityScreenAction
import com.yandex.yaweather.handler.CityScreenAction.SearchCityAction
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class, FlowPreview::class)
@Composable
fun WeatherSearchBar(
  query: String,
  cityItems: State<MutableList<CityItem>>,
  favoriteCitiesService: FavoriteCitiesService,
  action: (CityScreenAction) -> Unit,
  onQueryChange: (String) -> Unit
) {
  var active by remember { mutableStateOf(false) }
  val recentSearches = remember { mutableStateListOf<String>() }
  val queryFlow = remember { MutableStateFlow(query) }
  LaunchedEffect(queryFlow.value) {
    queryFlow
      .debounce(300)
      .distinctUntilChanged()
      .collect { debouncedQuery ->
        if (debouncedQuery.isNotEmpty()) {
          action(SearchCityAction(debouncedQuery))
        }
      }
  }

  LaunchedEffect(active) {
    if (!active) {
      onQueryChange("")
    }
  }

  Column(modifier = Modifier.fillMaxWidth()) {
    SearchBar(
      query = query,
      onQueryChange = { newQuery ->
        onQueryChange(newQuery)
        queryFlow.value = newQuery
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
            queryFlow.value = ""
          }) {
            Icon(Filled.ArrowBack, contentDescription = "Назад")
          }
        } else {
          Icon(
            imageVector = Icons.Default.Search, contentDescription = "Поиск"
          )
        }
      },
      placeholder = { Text(if (query.isEmpty()) "${LocalContext.current.resources.getString(R.string.search_cities)}..." else "") },
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
        Text(LocalContext.current.resources.getString(R.string.recent_searches), modifier = Modifier.padding(8.dp))
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
                }, elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
              )
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(16.dp), verticalAlignment = Alignment.CenterVertically
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
                  text = city, style = MaterialTheme.typography.bodyLarge
                )
              }
            }
          }
        }

      } else if (query.isNotEmpty()) {
        Text(
          text = "${LocalContext.current.resources.getString(R.string.search_result_for)} \"$query\"",
          modifier = Modifier.padding(8.dp),
          style = MaterialTheme.typography.titleMedium
        )

        LazyColumn(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
        ) {
          val cities = cityItems.value.filter { it.lat != null && it.lon != null }
          items(cities) { result ->
            Card(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .clickable {
                  action(CityScreenAction.AddToFavoriteCityList(result))
                  favoriteCitiesService.addCity(result)

                  if (!recentSearches.contains(result.name)) {
                    recentSearches.add(0, result.name ?: "")
                  }
                  onQueryChange("")
                  active = false
                  cityItems.value.clear()
                }, shape = MaterialTheme.shapes.medium, elevation = CardDefaults.cardElevation(
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
