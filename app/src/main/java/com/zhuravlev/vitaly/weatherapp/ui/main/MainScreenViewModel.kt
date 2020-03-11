package com.zhuravlev.vitaly.weatherapp.ui.main

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.zhuravlev.vitaly.weatherapp.R
import com.zhuravlev.vitaly.weatherapp.base.Constants
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
        Transformations.map(currentWeatherLiveData) {
            if (it is Atom.Success)
                it.content
            else null
        }

    val dateLiveData =
        Transformations.map(currentWeatherLiveData) {
            if (it is Atom.Success) {
                val timestamp = Date(TimeUnit.SECONDS.toMillis(it.content.dateTimestamp))
                SimpleDateFormat("dd MMMM HH:mm", Locale.getDefault()).format(timestamp)
            } else null
        }

    fun getCurrentWeather(coordinate: Coordinate = Coordinate(59.89, 30.26)) {
        currentWeatherJob?.cancel()
        currentWeatherJob = launch {
            try {
                currentWeatherInternal.postValue(Atom.Loading())
                val currentWeather = weatherRepository.getCurrentWeatherByLocation(
                    coordinate.latitude,
                    coordinate.longitude
                )
                currentWeatherInternal.postValue(
                    Atom.Success(
                        if (coordinate.name.isNullOrBlank()) currentWeather
                        else currentWeather.copy(placeName = coordinate.name)
                    )
                )
            } catch (exception: Exception) {
                exception.printStackTrace()
                currentWeatherInternal.postValue(Atom.Error(exception))
            }
        }
    }

    fun getWindDegreeDescription(degree: Double, context: Context): String {
        return when (degree) {
            in 292.5..337.5 -> context.getString(R.string.north_westerly_wind_description)
            in 247.5..292.5 -> context.getString(R.string.westerly_wind_description)
            in 202.5..247.5 -> context.getString(R.string.south_westerly_wind_description)
            in 157.5..202.5 -> context.getString(R.string.south_wind_description)
            in 122.5..157.5 -> context.getString(R.string.south_easterly_wind_description)
            in 67.5..122.5 -> context.getString(R.string.easterly_wind_description)
            in 22.5..67.5 -> context.getString(R.string.north_easterly_wind_description)
            else -> context.getString(R.string.northerly_wind_description)
        }
    }

    fun getPressureDescription(pressure: Int, context: Context): String {
        val pressureInMmHg: Int = (pressure * Constants.MM_HG_IN_HPA).toInt()
        return "$pressureInMmHg ${context.getString(R.string.pressure_value_mask)}"
    }
}
