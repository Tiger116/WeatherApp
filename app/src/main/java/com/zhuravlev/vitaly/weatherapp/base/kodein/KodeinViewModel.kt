package com.zhuravlev.vitaly.weatherapp.base.kodein

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import kotlin.coroutines.CoroutineContext

abstract class KodeinViewModel(override val kodein: Kodein) : ViewModel(), CoroutineScope,
    KodeinAware {

    override val coroutineContext: CoroutineContext = Dispatchers.IO + SupervisorJob()

    override fun onCleared() = coroutineContext.cancel()

    fun <T> mutableLiveData(initValue: T) = MutableLiveData<T>().apply { value = initValue }

    fun <T> MutableLiveData<T>.immutable() = this as LiveData<T>
}