package com.zhuravlev.vitaly.weatherapp.ui.main_screen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.zhuravlev.vitaly.weatherapp.base.atom.Atom
import com.zhuravlev.vitaly.weatherapp.base.kodein.KodeinViewModel
import com.zhuravlev.vitaly.weatherapp.model.weather.WeatherRepository
import com.zhuravlev.vitaly.weatherapp.model.weather.structures.Coordinate
import com.zhuravlev.vitaly.weatherapp.model.weather.structures.CurrentWeather
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.generic.instance
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class MainScreenViewModel(kodein: Kodein) : KodeinViewModel(kodein) {
    private val weatherRepository: WeatherRepository by instance()

    private var currentWeatherJob: Job? = null

    private val currentWeatherInternal = MutableLiveData<Atom<CurrentWeather>>()
    val currentWeatherLiveData = currentWeatherInternal.immutable()

    val weatherLiveData =
        Transformations.map(currentWeatherLiveData) { atom: Atom<CurrentWeather> ->
            if (atom is Atom.Success)
                atom.content
            else null
        }

    val dateLiveData =
        Transformations.map(currentWeatherLiveData) { atom: Atom<CurrentWeather> ->
            if (atom is Atom.Success) {
                val timestamp = Date(TimeUnit.SECONDS.toMillis(atom.content.dateTimestamp))
                SimpleDateFormat("dd MMM HH:mm", Locale.getDefault()).format(timestamp)
            } else null
        }

    fun getCurrentWeather() {
        currentWeatherJob?.cancel()
        currentWeatherJob = launch {
            try {
                currentWeatherInternal.postValue(Atom.Loading())
                val location = Coordinate(30.392231, 59.87092)
                val currentWeather = weatherRepository.getCurrentWeatherByLocation(
                    location.latitude,
                    location.longitude
                )
                currentWeatherInternal.postValue(Atom.Success(currentWeather))
            } catch (exception: Exception) {
                exception.printStackTrace()
                currentWeatherInternal.postValue(Atom.Error(exception))
            }
        }
    }
}
