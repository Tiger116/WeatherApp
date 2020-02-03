package com.zhuravlev.vitaly.weatherapp.model.weather.structures

enum class UnitsFormat(val value: String?) {
    STANDART(null),
    METRIC("metric"),
    IMPERIAL("imperial")
}