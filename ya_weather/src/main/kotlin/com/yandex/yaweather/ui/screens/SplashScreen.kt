package com.yandex.yaweather.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.yandex.yaweather.R

@Composable
fun SplashScreen() {
  Box(modifier = Modifier.fillMaxWidth().background(Color(0xFF7BD3F7))) {
    AnimatedSun()
    MovingCloud()
  }
}

@Composable
fun AnimatedSun() {
  val infiniteTransition = rememberInfiniteTransition(label = "")

  val rotation by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 360f,
    animationSpec = infiniteRepeatable(
      animation = keyframes {
        durationMillis = 2500
        0f at 0 with LinearOutSlowInEasing
        180f at 2500 with FastOutSlowInEasing
        360f at 5000 with LinearOutSlowInEasing
      },
      repeatMode = RepeatMode.Restart
    ), label = "rotation"
  )


  val scale by infiniteTransition.animateFloat(
    initialValue = 1f,
    targetValue = 1.2f,
    animationSpec = infiniteRepeatable(
      animation = tween(durationMillis = 1250, easing = LinearEasing),
      repeatMode = RepeatMode.Reverse
    ), label = ""
  )


  Box(
    modifier = Modifier.fillMaxSize(),
    contentAlignment = Alignment.Center
  ) {
    Image(
      painter = painterResource(id = R.drawable.ic_sun),
      contentDescription = "Sun Icon",
      modifier = Modifier
        .size(150.dp)
        .scale(scale)
        .rotate(rotation)
    )
  }
}


@Composable
fun MovingCloud() {

  Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center)
  {
    DrawAnimatedCloud(-400f, 250f, 1)
    DrawAnimatedCloud(-400f, 200f, 3)
    DrawAnimatedCloud(-400f, 350f, 4)
    DrawAnimatedCloud(-400f, 4000f, 6)
  }

}

@Composable
private fun DrawAnimatedCloud(initialValue: Float, targetValue:Float, cloudId: Int){
  val infiniteTransition = rememberInfiniteTransition(label = "")
  val img = when (cloudId) {
    1 -> R.drawable.cloud1
    2 -> R.drawable.cloud2
    3 -> R.drawable.cloud3
    4 -> R.drawable.cloud4
    5 -> R.drawable.cloud5
    else -> R.drawable.plane
  }
  val animatedOffset by infiniteTransition.animateFloat(
    initialValue = initialValue,
    targetValue = targetValue,
    animationSpec = infiniteRepeatable(
      animation = tween(durationMillis = 6500, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = ""
  )
  Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
    Image(
      painter = painterResource(id = img),
      contentDescription = "Cloud Icon",
      modifier = Modifier
        .size(140.dp)
        .offset(animatedOffset.dp, 0.dp)
    )
  }

}