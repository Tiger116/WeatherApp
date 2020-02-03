package com.zhuravlev.vitaly.weatherapp.model.weather.structures

import com.google.gson.annotations.SerializedName

data class CurrentWeather(
    @SerializedName("coord")
    val coordinate: Coordinate,
    val weather: List<Weather>,
    val base: String,
    val main: Main,
    val visibility: Int,
    val wind: Wind,
    val clouds: Clouds,
    val dt: Long,
    val sys: Sys,
    val timezone: Int,
    val id: Int,
    val name: String,
    val cod: Int
)