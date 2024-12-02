package com.yandex.yaweather

import androidx.activity.ComponentActivity
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay


@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      SplashScreen()
    }
  }
  @Composable
  fun SplashScreen() {
    LaunchedEffect(Unit) {
      delay(3000)
      startActivity(Intent(this@SplashActivity, MainActivity::class.java))
      finish()
      overridePendingTransition(R.anim.circle_in, R.anim.circle_out)
    }
    Box(modifier = Modifier.fillMaxWidth().background(
      brush = Brush.verticalGradient(
      colors = listOf(
        Color(0xFF4682B4),
        Color(0xFF5A9BD4),
        Color(0xFF87CEFA),
        Color(0xFFAEDCF3),
        Color(0xFFB0E0E6)
      )
      )
    )) {
      AnimatedSun()
      MovingCloud()
    }
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
    initialValue = 0.7f,
    targetValue = 1f,
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
    DrawAnimatedCloud(-350f, 290f, 2, 8000)
    DrawAnimatedCloud(-350f, 320f, 1, 5000)
  }

}

@Composable
private fun DrawAnimatedCloud(initialValue: Float, targetValue:Float, cloudId: Int, speedMillis: Int){
  val infiniteTransition = rememberInfiniteTransition(label = "")
  val img = when (cloudId) {
    1 -> R.drawable.cloud_svgrepo_com
    2 -> R.drawable.cloud_2
    else -> R.drawable.cloud_svgrepo_com
  }
  val animatedOffset by infiniteTransition.animateFloat(
    initialValue = initialValue,
    targetValue = targetValue,
    animationSpec = infiniteRepeatable(
      animation = tween(durationMillis = speedMillis, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = ""
  )
  Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
    Image(
      painter = painterResource(id = img),
      contentDescription = "Cloud Icon",
      modifier = Modifier
        .size(100.dp)
        .offset(animatedOffset.dp, 0.dp)
    )
  }
}
