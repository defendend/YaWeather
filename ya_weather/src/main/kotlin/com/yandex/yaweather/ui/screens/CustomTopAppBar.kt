package com.yandex.yaweather.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.yandex.yaweather.R

@ExperimentalMaterial3Api
@Composable
fun CustomTopAppBar(
  onMenuClick: () -> Unit, onSettingsClick: (Unit) -> Unit, onShareClick: (Unit) -> Unit
) {
  TopAppBar(modifier = Modifier
    .alpha(0.5f)
    .background(color = Color.Gray),
    title = { Text(text = "") },
    navigationIcon = {
      Row {
        IconButton(
          onClick = onMenuClick,
          colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.inversePrimary)
        ) {
          Icon(
            imageVector = Icons.Default.Menu, contentDescription = stringResource(R.string.menu)
          )
        }
        IconButton(
          onClick = { onShareClick.invoke(Unit) },
          colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.inversePrimary)
        ) {
          Icon(
            imageVector = Icons.Default.Share, contentDescription = stringResource(R.string.share)
          )
        }

      }
    },
    actions = {
      IconButton(
        onClick = { onSettingsClick.invoke(Unit) },
        colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.inversePrimary)
      ) {
        Icon(
          imageVector = Icons.Default.Settings, contentDescription = stringResource(R.string.settings)
        )
      }
    })
}