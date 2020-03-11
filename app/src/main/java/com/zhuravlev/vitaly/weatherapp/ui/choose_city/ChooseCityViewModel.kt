package com.zhuravlev.vitaly.weatherapp.ui.choose_city

import com.google.gson.Gson
import com.zhuravlev.vitaly.weatherapp.base.kodein.KodeinViewModel
import com.zhuravlev.vitaly.weatherapp.model.weather.structures.Coordinate
import org.kodein.di.Kodein

class ChooseCityViewModel(kodein: Kodein) : KodeinViewModel(kodein) {

    private var coordinate: Coordinate? = null

    fun setCoordinate(latitude: Double, longitude: Double, name: String? = null) {
        coordinate = Coordinate(latitude, longitude, name)
    }

    fun getCoordinateJson(): String = Gson().toJson(coordinate)
}
