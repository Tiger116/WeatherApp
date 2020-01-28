package com.zhuravlev.vitaly.weatherapp.model.weather.structures

import com.google.gson.annotations.SerializedName

data class Coordinate(
    @SerializedName("lon")
    val longitude: Double,
    @SerializedName("lat")
    val latitude: Double
)
