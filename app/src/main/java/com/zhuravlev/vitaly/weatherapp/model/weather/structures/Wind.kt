package com.zhuravlev.vitaly.weatherapp.model.weather.structures

import com.google.gson.annotations.SerializedName

data class Wind(
    val speed: Double,
    @SerializedName("deg")
    val degrees: Double
)