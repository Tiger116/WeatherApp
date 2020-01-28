package com.zhuravlev.vitaly.weatherapp.model.weather

import com.zhuravlev.vitaly.weatherapp.base.Constants
import com.zhuravlev.vitaly.weatherapp.model.weather.structures.CurrentWeather

class WeatherRepository(
    private val weatherAPI: WeatherAPI
) {
    suspend fun getCurrentWeatherByLocation(lat: Double, lon: Double): CurrentWeather =
        weatherAPI.getCurrentWeatherByLocation(lat, lon, Constants.OPEN_WEATHER_API_KEY)
}