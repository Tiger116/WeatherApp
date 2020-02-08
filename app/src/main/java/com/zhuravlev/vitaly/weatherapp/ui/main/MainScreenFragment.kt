package com.zhuravlev.vitaly.weatherapp.ui.main

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.zhuravlev.vitaly.weatherapp.R
import com.zhuravlev.vitaly.weatherapp.base.Constants
import com.zhuravlev.vitaly.weatherapp.base.atom.Atom
import com.zhuravlev.vitaly.weatherapp.base.kodein.KodeinFragment
import com.zhuravlev.vitaly.weatherapp.base.snackbar.Snackbar
import com.zhuravlev.vitaly.weatherapp.databinding.FragmentMainScreenBinding
import com.zhuravlev.vitaly.weatherapp.model.weather.structures.Coordinate
import com.zhuravlev.vitaly.weatherapp.ui.choose_city.ChooseCityFragment

class MainScreenFragment : KodeinFragment() {
    private val viewModel: MainScreenViewModel by lazy {
        provide(MainScreenViewModel::class.java)
    }
    private lateinit var binding: FragmentMainScreenBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainScreenBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.swipeRefreshLayout.setColorSchemeResources(
            R.color.colorPrimary,
            R.color.colorPrimaryDark
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.swipeRefreshLayout.setOnRefreshListener {
            updateCurrentWeather()
        }
        viewModel.currentWeatherLiveData.observe {
            when (it) {
                is Atom.Success -> {
                    val currentWeather = it.content

                    // Set toolbar title
                    kodeinActivity.supportActionBar?.title = currentWeather.placeName
                }
                is Atom.Error -> {
                    val exception = it.throwable
                    Snackbar().showMessage(
                        requireContext(),
                        exception.message ?: getString(R.string.undefined_error_message)
                    )
                    kodeinActivity.supportActionBar?.title = getString(R.string.main_screen_label)
                }
                null -> {
                    Snackbar().showMessage(
                        requireContext(),
                        getString(R.string.undefined_error_message)
                    )
                    kodeinActivity.supportActionBar?.title = getString(R.string.main_screen_label)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        updateCurrentWeather()
    }

    private fun updateCurrentWeather() {
        arguments?.getString(ChooseCityFragment.SAVED_COORDINATE_PREFERENCE)?.let {
            val coordinate = Gson().fromJson<Coordinate>(it, Coordinate::class.java)
            viewModel.getCurrentWeather(coordinate)
        } ?: getSavedLocationFromSharedPrefs()
    }

    private fun getSavedLocationFromSharedPrefs() {
        val sharedPreferences =
            context?.getSharedPreferences(Constants.APP_PREFERENCES, Context.MODE_PRIVATE)
        sharedPreferences?.run {
            if (getBoolean(ChooseCityFragment.IS_MY_LOCATION_PREFERENCE, false)) {
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermissions(
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        ChooseCityFragment.LOCATION_PERMISSION_REQUEST
                    )
                } else {
                    getCurrentLocationWeather()
                }
            } else {
                getString(ChooseCityFragment.SAVED_COORDINATE_PREFERENCE, null)?.let {
                    val coordinate = Gson().fromJson<Coordinate>(it, Coordinate::class.java)
                    viewModel.getCurrentWeather(coordinate)
                } ?: viewModel.getCurrentWeather()
            }
        } ?: viewModel.getCurrentWeather()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            ChooseCityFragment.LOCATION_PERMISSION_REQUEST -> {
                val everyPermissionGranted = grantResults
                    .all { it == PackageManager.PERMISSION_GRANTED }
                if (everyPermissionGranted) {
                    getCurrentLocationWeather()
                }
            }
        }
    }

    private fun getCurrentLocationWeather() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val locationManager =
                context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            location?.let {
                viewModel.getCurrentWeather(Coordinate(it.longitude, it.latitude))
            } ?: locationManager.requestSingleUpdate(
                LocationManager.NETWORK_PROVIDER,
                object : LocationListener {
                    override fun onLocationChanged(location: Location?) {
                        location?.let {
                            viewModel.getCurrentWeather(Coordinate(it.longitude, it.latitude))
                        }
                    }

                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
                    override fun onProviderEnabled(provider: String?) {}
                    override fun onProviderDisabled(provider: String?) {}
                }, null
            )
        } else viewModel.getCurrentWeather()
    }
}
