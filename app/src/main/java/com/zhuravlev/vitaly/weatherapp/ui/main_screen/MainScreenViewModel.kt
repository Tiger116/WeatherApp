package com.zhuravlev.vitaly.weatherapp.ui.main_screen

import androidx.lifecycle.MutableLiveData
import com.zhuravlev.vitaly.weatherapp.base.atom.Atom
import com.zhuravlev.vitaly.weatherapp.base.kodein.KodeinViewModel
import com.zhuravlev.vitaly.weatherapp.model.weather.WeatherRepository
import com.zhuravlev.vitaly.weatherapp.model.weather.structures.Coordinate
import com.zhuravlev.vitaly.weatherapp.model.weather.structures.CurrentWeather
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.generic.instance

class MainScreenViewModel(kodein: Kodein) : KodeinViewModel(kodein) {
    private val weatherRepository: WeatherRepository by instance()

    private var currentWeatherJob: Job? = null

    private val isLoadingInternal = MutableLiveData<Boolean>()
    val isLoadingLiveData = isLoadingInternal.immutable()

    private val currentWeatherInternal = MutableLiveData<Atom<CurrentWeather>>()
    val currentWeatherLiveData = currentWeatherInternal.immutable()

    fun getCurrentWeather() {
        currentWeatherJob?.cancel()
        currentWeatherJob = launch {
            try {
                isLoadingInternal.postValue(true)
                val location = Coordinate(30.392231, 59.87092)
                val currentWeather = weatherRepository.getCurrentWeatherByLocation(
                    location.latitude,
                    location.longitude
                )
                currentWeatherInternal.postValue(Atom.Success(currentWeather))
            } catch (exception: Exception) {
                exception.printStackTrace()
                currentWeatherInternal.postValue(Atom.Error(exception))
            } finally {
                isLoadingInternal.postValue(false)
            }
        }
    }
}
