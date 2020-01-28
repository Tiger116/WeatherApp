package com.zhuravlev.vitaly.weatherapp.model.weather

import com.zhuravlev.vitaly.weatherapp.model.network.retrofit.retrofitModule
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import retrofit2.Retrofit

val weatherModule = Kodein.Module("Weather") {
    importOnce(retrofitModule)

    bind() from provider { instance<Retrofit>().create(WeatherAPI::class.java) }
    bind() from provider { WeatherRepository(instance()) }
}