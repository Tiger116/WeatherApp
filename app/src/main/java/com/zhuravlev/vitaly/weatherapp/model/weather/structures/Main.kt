package com.zhuravlev.vitaly.weatherapp.model.weather.structures

import com.google.gson.annotations.SerializedName

data class Main(
    @SerializedName("temp")
    val temperature: Double,
    val feelsLike: Double,
    @SerializedName("temp_min")
    val temperatureMin: Double,
    @SerializedName("temp_max")
    val temperatureMax: Double,
    val pressure: Int,
    val humidity: Int
)