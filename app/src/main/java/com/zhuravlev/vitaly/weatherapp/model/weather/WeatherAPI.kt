package com.zhuravlev.vitaly.weatherapp.model.weather

import com.zhuravlev.vitaly.weatherapp.model.weather.structures.CurrentWeather
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {

    @GET("/data/2.5/weather")
    suspend fun getCurrentWeatherByLocation(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): CurrentWeather
}