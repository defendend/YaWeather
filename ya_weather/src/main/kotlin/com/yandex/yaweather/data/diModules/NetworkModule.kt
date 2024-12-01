package data.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.yandex.yaweather.data.diModules.CityRetrofitQualifier
import com.yandex.yaweather.data.diModules.WeatherRetrofitQualifier
import com.yandex.yaweather.data.network.CityApi
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

  @WeatherRetrofitQualifier
  @Provides
  fun providesWeatherRetrofit(okHttpClient: OkHttpClient): Retrofit =
    Retrofit.Builder()
      .baseUrl("https://api.openweathermap.org/")
      .client(okHttpClient)
      .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
      .build()

  @Provides
  fun providesHomeApi(@WeatherRetrofitQualifier retrofit: Retrofit): WeatherApi =
    retrofit.create(WeatherApi::class.java)

  @CityRetrofitQualifier
  @Provides
  fun providesCityRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
    .baseUrl("http://htmlweb.ru/")
    .client(okHttpClient)
    .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
    .build()

  @Provides
  fun providerCityApi(@CityRetrofitQualifier retrofit: Retrofit): CityApi = retrofit.create(CityApi::class.java)
}