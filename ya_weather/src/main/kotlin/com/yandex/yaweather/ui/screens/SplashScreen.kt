package com.yandex.yaweather.ui.screens

import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.yandex.yaweather.R
import com.yandex.yaweather.handler.SplashScreenAction
import kotlinx.coroutines.delay
import java.time.LocalTime
import kotlin.random.Random


@Composable
fun SplashScreen(isDataLoaded: Boolean, action: (SplashScreenAction) -> Unit) {
  var timerCompleted by remember { mutableStateOf(false) }
  var isVisible by remember { mutableStateOf(true) }

  LaunchedEffect(Unit) {
    delay(3000)
    timerCompleted = true
  }

  AnimatedVisibility(
    visible = isVisible,
    exit = fadeOut(animationSpec = tween(durationMillis = 500))
  )
  {
    val currentTime = LocalTime.now()
    val isNight = currentTime.isAfter(LocalTime.of(22, 0)) || currentTime.isBefore(LocalTime.of(6, 0))

    val gradientBrush = if (isNight) {
      Brush.verticalGradient(
        colors = listOf(
          Color(0xFF001f3d),
          Color(0xFF003b6f),
          Color(0xFF0a4e8e),
          Color(0xFF4f8bbd),
          Color(0xFF87CEFA)
        )
      )
    } else {
      Brush.verticalGradient(
        colors = listOf(
          Color(0xFF4682B4),
          Color(0xFF5A9BD4),
          Color(0xFF87CEFA),
          Color(0xFFAEDCF3),
          Color(0xFFB0E0E6)
        )
      )
    }

    Box(
      modifier = Modifier
        .fillMaxWidth()
        .background(brush = gradientBrush)
    ) {
      if (isNight) {
        AnimatedMoon(Modifier.align(Alignment.Center))
        MovingCloud()
        NightSkyAnimation()
      } else {
        AnimatedSun()
        MovingCloud()
      }
    }
    if (isDataLoaded && timerCompleted) {
      LaunchedEffect(Unit) {
        isVisible = false
        delay(500)
        action(SplashScreenAction.OpenMainScreen)
      }
    }
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(MaterialTheme.colorScheme.primary)
    systemUiController.setNavigationBarColor(MaterialTheme.colorScheme.primary)
  }
}


@Composable
fun AnimatedSun() {
  val infiniteTransition = rememberInfiniteTransition(label = "")

  val rotation by infiniteTransition.animateFloat(
    initialValue = 0f, targetValue = 360f, animationSpec = infiniteRepeatable(
      animation = keyframes {
        durationMillis = 2500
        0f at 0 with LinearOutSlowInEasing
        180f at 2500 with FastOutSlowInEasing
        360f at 5000 with LinearOutSlowInEasing
      }, repeatMode = RepeatMode.Restart
    ), label = stringResource(R.string.splash_screen_rotation)
  )


  val scale by infiniteTransition.animateFloat(
    initialValue = 0.7f, targetValue = 1f, animationSpec = infiniteRepeatable(
      animation = tween(durationMillis = 1250, easing = LinearEasing), repeatMode = RepeatMode.Reverse
    ), label = stringResource(R.string.splash_screen_scale)
  )

  Box(
    modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
  ) {
    Image(
      painter = painterResource(id = R.drawable.ic_sun),
      contentDescription = stringResource(R.string.splash_screen_sun_icon),
      modifier = Modifier
        .size(150.dp)
        .scale(scale)
        .rotate(rotation)
    )
  }
}


@Composable
fun MovingCloud() {

  Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
    DrawAnimatedCloud(-350f, 290f, 2, 8000)
    DrawAnimatedCloud(-350f, 320f, 1, 5000)
  }

}

@Composable
private fun DrawAnimatedCloud(initialValue: Float, targetValue: Float, cloudId: Int, speedMillis: Int) {
  val infiniteTransition = rememberInfiniteTransition(label = "")
  val img = when (cloudId) {
    1 -> R.drawable.cloud_svgrepo_com
    2 -> R.drawable.cloud_2
    else -> R.drawable.cloud_svgrepo_com
  }
  val animatedOffset by infiniteTransition.animateFloat(
    initialValue = initialValue, targetValue = targetValue, animationSpec = infiniteRepeatable(
      animation = tween(durationMillis = speedMillis, easing = LinearEasing), repeatMode = RepeatMode.Restart
    ), label = stringResource(R.string.splash_screen_offset)
  )
  Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
    Image(
      painter = painterResource(id = img),
      contentDescription = stringResource(R.string.splash_screen_cloud_icon),
      modifier = Modifier
        .size(100.dp)
        .offset(animatedOffset.dp, 0.dp)
    )
  }
}


@Composable
fun AnimatedMoon(modifier: Modifier) {
  val infiniteTransition = rememberInfiniteTransition(label = "")
  val scale by infiniteTransition.animateFloat(
    initialValue = 0.7f,
    targetValue = 1f,
    animationSpec = infiniteRepeatable(
      animation = tween(2000, easing = LinearEasing),
      repeatMode = RepeatMode.Reverse
    ), label = ""
  )

    Image(
      painter = painterResource(id = R.drawable.img),
      contentDescription = stringResource(R.string.splash_screen_sun_icon),
      modifier = modifier
        .size(150.dp)
        .scale(scale)
    )
}


@Composable
fun NightSkyAnimation() {
  val infiniteTransition = rememberInfiniteTransition(label = "")

  val starAlpha = infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 1f,
    animationSpec = infiniteRepeatable(
      animation = tween(
        durationMillis = 2000,
        easing = LinearEasing
      ),
      repeatMode = RepeatMode.Reverse
    ), label = ""
  )

  val starCount = 100

  Canvas(modifier = Modifier.fillMaxSize()) {
    val width = size.width
    val height = size.height

    repeat(starCount) {
      val x = Random.nextFloat() * width
      val y = Random.nextFloat() * height
      val starSize = Random.nextFloat() * 3 + 1

      drawCircle(
        color = Color.White.copy(alpha = starAlpha.value),
        radius = starSize,
        center = Offset(x, y)
      )
    }
  }
}

