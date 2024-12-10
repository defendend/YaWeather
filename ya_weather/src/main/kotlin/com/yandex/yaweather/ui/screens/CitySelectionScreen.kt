package com.yandex.yaweather.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.yandex.yaweather.handler.CityScreenAction.SearchCityAction
import com.yandex.yaweather.utils.dragContainer
import com.yandex.yaweather.utils.draggableItems
import com.yandex.yaweather.utils.rememberDragDropState
import com.yandex.yaweather.viewModel.CitySelectionUIState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged

@OptIn(FlowPreview::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "StateFlowValueCalledInComposition")
@Composable
fun CitySelectionScreen(
  cityItems: State<MutableList<CityItem>>,
  favoriteCitiesService: FavoriteCitiesService,
  favoriteCityItems: MutableList<CitySelectionUIState>,
  action: (CityScreenAction) -> Unit
) {
  var query by remember { mutableStateOf("") }
  val stateList = rememberLazyListState()
  val dragDropState = rememberDragDropState(
    lazyListState = stateList,
    draggableItemsNum = favoriteCityItems.size,
    onMove = { fromIndex, toIndex ->
      action(CityScreenAction.MoveCity(fromIndex, toIndex))
      favoriteCitiesService.moveCity(fromIndex, toIndex)
    },
  )

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

  Scaffold(topBar = {
    WeatherSearchBar(
      query = query,
      cityItems = cityItems,
      action = action,
      favoriteCitiesService = favoriteCitiesService,
      onQueryChange = { newQuery ->
        query = newQuery
        queryFlow.value = newQuery
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
        items = favoriteCityItems,
        dragDropState = dragDropState
      ) { modifier, item ->
        CityItemUi(
          item,
          favoriteCityItems.indexOf(item),
          action,
          modifier,
          deleteClick = {
            val index = favoriteCityItems.indexOf(item)
            action(CityScreenAction.RemoveFavoriteCity(index))
            favoriteCitiesService.removeCityAt(index)
          },
          redactorClick = { newCityName ->
            val index = favoriteCityItems.indexOf(item)
            action(CityScreenAction.EditFavoriteCityName(index, newCityName))
            favoriteCitiesService.editCityNameAt(index, newCityName)
          }

        )
      }
    }
  }
}
