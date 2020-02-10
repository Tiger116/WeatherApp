package com.zhuravlev.vitaly.weatherapp.model.places

import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

val placesModule = Kodein.Module("Places") {
    bind() from provider {
        PlacesInteractor(instance())
    }
}
