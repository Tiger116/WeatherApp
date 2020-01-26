package com.zhuravlev.vitaly.weatherapp.ui.main_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zhuravlev.vitaly.weatherapp.base.KodeinViewModel
import org.kodein.di.Kodein

class MainScreenViewModel(kodein: Kodein) : KodeinViewModel(kodein) {
    private val isLoadingInternal = MutableLiveData<Boolean>()
    val isLoading = isLoadingInternal as LiveData<Boolean>

}
