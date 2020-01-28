package com.zhuravlev.vitaly.weatherapp.model.weather.structures

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)