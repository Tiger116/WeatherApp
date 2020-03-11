package com.zhuravlev.vitaly.weatherapp.model.weather.structures

import com.google.gson.annotations.SerializedName

data class Coordinate(
    @SerializedName("lat")
    val latitude: Double,
    @SerializedName("lon")
    val longitude: Double,
    val name: String? = null
)
