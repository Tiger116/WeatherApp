package com.zhuravlev.vitaly.weatherapp.base.kodein


import android.app.Application

import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

abstract class KodeinApplication : Application(), KodeinAware {
    abstract val rootModule: Kodein.Module

    final override val kodein = Kodein.lazy {
        bind() from singleton {
            KodeinViewModelFactory(
                kodein
            )
        }

        import(androidXModule(this@KodeinApplication))
        importOnce(rootModule)
    }
}