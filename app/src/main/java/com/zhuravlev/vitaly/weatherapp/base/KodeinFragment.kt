package com.zhuravlev.vitaly.weatherapp.base

import android.content.Context
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.SupervisorJob
import org.kodein.di.KodeinAware
import org.kodein.di.direct
import org.kodein.di.generic.instance
import kotlin.coroutines.CoroutineContext
import kotlin.properties.Delegates

abstract class KodeinFragment : Fragment(), CoroutineScope by MainScope() {

    private var viewModelFactory: KodeinViewModelFactory by Delegates.notNull()

    val kActivity get() = requireActivity() as KodeinActivity

    override val coroutineContext: CoroutineContext = Dispatchers.IO + SupervisorJob()

    @CallSuper
    override fun onAttach(context: Context) {
        super.onAttach(context)
        val kodein = (activity as KodeinAware).kodein
        viewModelFactory = kodein.direct.instance()
    }

    protected fun <T : KodeinViewModel> provide(viewModelClass: Class<T>): T {
        return ViewModelProvider(this, viewModelFactory).get(viewModelClass)
    }

    protected fun <T : KodeinViewModel> provideGlobal(viewModelClass: Class<T>): T {
        return ViewModelProvider(requireActivity(), viewModelFactory).get(viewModelClass)
    }

    fun <T> LiveData<T>.observe(observer: (item: T?) -> Unit) =
        observe(getSuitableLifecycleOwner(), Observer(observer))

    private fun getSuitableLifecycleOwner() =
        if (view != null) viewLifecycleOwner else this
}
