package com.zhuravlev.vitaly.weatherapp.ui.application

import com.zhuravlev.vitaly.weatherapp.base.kodein.KodeinApplication
import com.zhuravlev.vitaly.weatherapp.model.modelModule
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware

class App : KodeinApplication(), KodeinAware {
    override val rootModule = Kodein.Module("Root") {
        import(modelModule)
    }
}