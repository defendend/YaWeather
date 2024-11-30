package data.network

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class ApiModule {
    @Provides
    fun providesHomeApi(retrofit: Retrofit): WeatherApi = retrofit.create(WeatherApi::class.java)
}