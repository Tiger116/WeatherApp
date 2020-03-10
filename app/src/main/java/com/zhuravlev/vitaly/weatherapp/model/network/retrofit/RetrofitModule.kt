package com.zhuravlev.vitaly.weatherapp.model.network.retrofit

import com.zhuravlev.vitaly.weatherapp.base.Constants
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import org.kodein.di.generic.with
import retrofit2.Retrofit

const val BASE_URL = "baseURL"
const val APP_ID = "appId"
val retrofitModule = Kodein.Module("Retrofit") {

    constant(BASE_URL) with Constants.OPEN_WEATHER_BASE_URL
    constant(APP_ID) with Constants.OPEN_WEATHER_API_KEY

    bind<Retrofit>() with singleton {
        RetrofitFactory.create(instance(BASE_URL), instance(APP_ID))
    }
}
