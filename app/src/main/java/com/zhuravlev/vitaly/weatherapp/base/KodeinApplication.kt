package com.zhuravlev.vitaly.weatherapp.base


import android.app.Application
import android.system.Os.bind

import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton
import java.util.Collections.singleton

abstract class KodeinApplication : Application(), KodeinAware {
    abstract val rootModule: Kodein.Module

    final override val kodein = Kodein.lazy {
        bind() from singleton { KodeinViewModelFactory(kodein) }
        import(androidXModule(this@KodeinApplication))
        importOnce(rootModule)
    }
}