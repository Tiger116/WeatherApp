package com.zhuravlev.vitaly.weatherapp.model.network.retrofit

import com.zhuravlev.vitaly.weatherapp.base.Constants
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import org.kodein.di.generic.with
import retrofit2.Retrofit

const val BASE_URL = "baseURL"
val retrofitModule = Kodein.Module("Retrofit") {

    constant(BASE_URL) with Constants.OPEN_WEATHER_BASE_URL

    bind<Retrofit>() with singleton {
        RetrofitFactory.create(instance(BASE_URL))
    }
}
