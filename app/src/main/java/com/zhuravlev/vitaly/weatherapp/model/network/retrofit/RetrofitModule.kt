package com.zhuravlev.vitaly.weatherapp.model.network.retrofit

import com.zhuravlev.vitaly.weatherapp.base.Constants
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import retrofit2.Retrofit

val retrofitModule = Kodein.Module("RetrofitModule") {

    bind<Retrofit>() with singleton {
        RetrofitFactory.create(instance(Constants.OPEN_WEATHER_BASE_URL))
    }
}
