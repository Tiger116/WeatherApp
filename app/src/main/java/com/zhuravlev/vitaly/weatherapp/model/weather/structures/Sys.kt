package com.zhuravlev.vitaly.weatherapp.model.weather.structures

data class Sys(
    val type: Int,
    val id: Int,
    val message: Double,
    val country: String,
    val sunrise: Long,
    val sunset: Long
)