package com.yandex.yaweather.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WeatherScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray)
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize()
        ) {
            // City and Temperature
            Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Лондон, Англия",
                    fontSize = 24.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "10°",
                    fontSize = 64.sp,
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Макс. 14° Мин. 11°",
                    fontSize = 16.sp,
                    color = Color.LightGray
                )
            }

            // Hourly Forecast
            HourlyForecast()

            // 10-day Forecast
            TenDayForecast()
        }
    }
}

@Composable
fun HourlyForecast() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.DarkGray, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Text(
            text = "ПОЧАСОВОЙ ПРОГНОЗ",
            fontSize = 16.sp,
            color = Color.LightGray,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            repeat(6) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "11°", color = Color.White, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "${it + 1} h", color = Color.LightGray, fontSize = 14.sp)
                }
            }
        }
    }
}

@Composable
fun TenDayForecast() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.DarkGray, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Text(
            text = "10-ДНЕВНЫЙ ПРОГНОЗ",
            fontSize = 16.sp,
            color = Color.LightGray,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        repeat(7) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            ) {
                Text(text = "Сб", color = Color.White, fontSize = 16.sp)
                Text(text = "11°", color = Color.LightGray, fontSize = 14.sp)
                Text(text = "14°", color = Color.White, fontSize = 16.sp)
            }
        }
    }
}