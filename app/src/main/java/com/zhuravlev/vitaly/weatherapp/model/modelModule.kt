package com.zhuravlev.vitaly.weatherapp.model

import com.zhuravlev.vitaly.weatherapp.model.places.placesModule
import com.zhuravlev.vitaly.weatherapp.model.weather.weatherModule
import org.kodein.di.Kodein

val modelModule = Kodein.Module("Model") {
    importOnce(weatherModule)
    importOnce(placesModule)
}