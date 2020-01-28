package com.zhuravlev.vitaly.weatherapp.base.kodein

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

abstract class KodeinActivity : AppCompatActivity(), CoroutineScope by MainScope(), KodeinAware {

    final override val kodein by closestKodein()

    private val viewModelFactory: KodeinViewModelFactory by instance()

    protected fun <T : KodeinViewModel> provide(viewModelClass: Class<T>): T {
        return ViewModelProvider(this, viewModelFactory).get(viewModelClass)
    }
}