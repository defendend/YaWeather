package com.yandex.yaweather.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yandex.yaweather.data.diModules.FavoriteCitiesService
import com.yandex.yaweather.data.network.CityItem
import com.yandex.yaweather.handler.CityScreenAction
import com.yandex.yaweather.utils.dragContainer
import com.yandex.yaweather.utils.draggableItems
import com.yandex.yaweather.utils.rememberDragDropState
import com.yandex.yaweather.viewModel.CitySelectionUIState

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CitySelectionScreen(
  cityItems: State<MutableList<CityItem>>,
  favoriteCitiesService: FavoriteCitiesService,
  favoriteCityItems: MutableList<CitySelectionUIState>,
  action: (CityScreenAction) -> Unit
) {
  var query by remember { mutableStateOf("") }
  val stateList = rememberLazyListState()
  val dragDropState = rememberDragDropState(lazyListState = stateList,
    draggableItemsNum = favoriteCityItems.size,
    onMove = { fromIndex, toIndex ->
      action(CityScreenAction.MoveCity(fromIndex, toIndex))
      favoriteCitiesService.moveCity(fromIndex, toIndex)
    })

  Scaffold(topBar = {
    WeatherSearchBar(
      query = query,
      cityItems = cityItems,
      action = action,
      favoriteCitiesService = favoriteCitiesService,
      onQueryChange = {
        query = it
      },
    )
  }) {
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(top = 70.dp)
        .dragContainer(dragDropState),
      state = stateList,
    ) {
      draggableItems(
        items = favoriteCityItems, dragDropState = dragDropState
      ) { modifier, item ->
        CityItem(
          item, favoriteCityItems.indexOf(item), action, modifier
        )
      }
    }
  }
}