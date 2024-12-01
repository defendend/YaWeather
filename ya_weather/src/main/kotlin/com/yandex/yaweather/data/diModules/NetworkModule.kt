package data.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.yandex.yaweather.data.network.WeatherApi
import dagger.Module
import dagger.Provides
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType

import okhttp3.OkHttpClient
import retrofit2.Retrofit

@Module
class NetworkProvider {
  private val json = Json { ignoreUnknownKeys = true }

  @Provides
  fun providesOkHttpClient(): OkHttpClient = OkHttpClient.Builder().build()

  @Provides
  fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit =
    Retrofit.Builder()
      .baseUrl("https://api.openweathermap.org/")
      .client(okHttpClient)
      .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
      .build()

  @Provides
  fun providesHomeApi(retrofit: Retrofit): WeatherApi = retrofit.create(WeatherApi::class.java)
}