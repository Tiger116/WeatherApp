package com.zhuravlev.vitaly.weatherapp.ui.application

import com.zhuravlev.vitaly.weatherapp.BuildConfig
import com.zhuravlev.vitaly.weatherapp.base.KodeinApplication
import com.zhuravlev.vitaly.weatherapp.model.modelModule
import kotlinx.coroutines.DEBUG_PROPERTY_NAME
import kotlinx.coroutines.DEBUG_PROPERTY_VALUE_ON
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware

class App : KodeinApplication(), KodeinAware {
    init {
        if (BuildConfig.DEBUG) {
            System.setProperty(DEBUG_PROPERTY_NAME, DEBUG_PROPERTY_VALUE_ON)
        }
    }

    override val rootModule = Kodein.Module("Root") {
        import(modelModule)
    }
}