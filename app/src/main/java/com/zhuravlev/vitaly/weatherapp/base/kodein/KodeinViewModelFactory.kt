package com.zhuravlev.vitaly.weatherapp.base.kodein

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.kodein.di.Kodein

/**
 * The factory allows to create viewModels with built-in [Kodein] instance
 * */
class KodeinViewModelFactory(private val kodein: Kodein) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        require(KodeinViewModel::class.java.isAssignableFrom(modelClass)) {
            "$modelClass is not a subclass of KodeinViewModel"
        }
        require(modelClass.constructors.size == 1) {
            "KodeinViewModel must have only one constructor"
        }

        return modelClass.constructors[0].newInstance(kodein) as T
    }
}