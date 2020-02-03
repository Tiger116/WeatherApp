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
    @SerializedName("dt")
    val dateTimestamp: Long,
    @SerializedName("sys")
    val systemInfo: SystemInfo,
    val timezone: Int,
    @SerializedName("id")
    val cityId: Int,
    @SerializedName("name")
    val placeName: String,
    @SerializedName("cod")
    val code: Int
)