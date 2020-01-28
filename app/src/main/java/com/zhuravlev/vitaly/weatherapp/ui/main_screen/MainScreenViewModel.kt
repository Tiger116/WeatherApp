package com.zhuravlev.vitaly.weatherapp.ui.main_screen

import androidx.lifecycle.MutableLiveData
import com.zhuravlev.vitaly.weatherapp.base.kodein.KodeinViewModel
import com.zhuravlev.vitaly.weatherapp.model.weather.WeatherRepository
import com.zhuravlev.vitaly.weatherapp.model.weather.structures.Coordinate
import com.zhuravlev.vitaly.weatherapp.model.weather.structures.CurrentWeather
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.generic.instance

class MainScreenViewModel(kodein: Kodein) : KodeinViewModel(kodein) {
//    private val weatherRepository: WeatherRepository by instance<WeatherRepository>()

    private val isLoadingInternal = MutableLiveData<Boolean>()
    val isLoadingLiveData = isLoadingInternal.immutable()

    private val currentWeatherInternal = MutableLiveData<CurrentWeather>()
    val currentWeatherLiveData = currentWeatherInternal.immutable()

    fun getCurrentWeather() {
        isLoadingInternal.postValue(true)
        launch {
            try {
                val location = Coordinate(30.392231, 59.87092)
//                val currentWeather = weatherRepository.getCurrentWeatherByLocation(
//                    location.latitude,
//                    location.longitude
//                )
//                currentWeatherInternal.postValue(currentWeather)
            } catch (exception: Exception) {
                exception.printStackTrace()
            } finally {
                isLoadingInternal.postValue(false)
            }
        }
    }

}
