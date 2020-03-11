package com.zhuravlev.vitaly.weatherapp.ui.choose_city

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.gson.Gson
import com.zhuravlev.vitaly.weatherapp.R
import com.zhuravlev.vitaly.weatherapp.base.Constants
import com.zhuravlev.vitaly.weatherapp.base.extension.hideKeyboard
import com.zhuravlev.vitaly.weatherapp.base.extension.onDelayedClick
import com.zhuravlev.vitaly.weatherapp.base.extension.showKeyboard
import com.zhuravlev.vitaly.weatherapp.base.kodein.KodeinFragment
import com.zhuravlev.vitaly.weatherapp.databinding.FragmentChooseCityBinding
import com.zhuravlev.vitaly.weatherapp.model.weather.structures.Coordinate

class ChooseCityFragment : KodeinFragment() {
    companion object {
        const val LOCATION_PERMISSION_REQUEST = 100
        const val AUTOCOMPLETE_REQUEST = 101
        const val SAVED_COORDINATE_PREFERENCE = "savedCoordinate"
        const val IS_MY_LOCATION_PREFERENCE = "isMyLocation"
        private const val googleApiKey = "AIzaSyCqERM0OGg0RSvzT1-BNhGPRTVaAsaxXGA"
    }

    private val viewModel: ChooseCityViewModel by lazy {
        provide(ChooseCityViewModel::class.java)
    }
    private lateinit var binding: FragmentChooseCityBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChooseCityBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.swipeRefreshLayout.setColorSchemeResources(
            R.color.colorPrimary,
            R.color.colorPrimaryDark
        )

        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), googleApiKey)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.isRefreshing = false
        }
        binding.myLocationItem.onDelayedClick {
            requestPermissionsAndGetLocation()
        }
//        binding.searchEditText.addTextChangedListener(object : TextWatcher {
////            override fun afterTextChanged(s: Editable?) {
////                s?.let { searchText ->
////                    viewModel.searchResultByText(searchText.toString())
////                }
////            }
////
////            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
////
////            override fun onTextChanged(
////                address: CharSequence?,
////                start: Int,
////                before: Int,
////                count: Int
////            ) {
////            }
////        })
        binding.searchEditText.onDelayedClick {
            context?.let { context ->
                val intent = Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.FULLSCREEN,
                        listOf(Place.Field.LAT_LNG, Place.Field.ADDRESS)
                    ).setTypeFilter(TypeFilter.CITIES)
                    .build(context)
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST)
            }
        }
        binding.searchEditText.showKeyboard()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST -> {
                val everyPermissionGranted = grantResults
                    .all { it == PackageManager.PERMISSION_GRANTED }
                if (everyPermissionGranted) {
                    getCurrentLocation()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AUTOCOMPLETE_REQUEST) {
            when (resultCode) {
                AutocompleteActivity.RESULT_OK -> {
                    data?.let { data ->
                        val place = Autocomplete.getPlaceFromIntent(data)
                        place.latLng?.let { saveAndBack(Coordinate(it.longitude, it.latitude)) }
                    }
                }
                AutocompleteActivity.RESULT_ERROR -> {

                }
                AutocompleteActivity.RESULT_CANCELED -> {

                }
                else -> {

                }
            }
        }
    }

    private fun requestPermissionsAndGetLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST
            )
        } else {
            getCurrentLocation()
        }
    }

    private fun getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val locationManager =
                context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val location =
                locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            location?.let {
                saveAndBack(Coordinate(it.longitude, it.latitude), true)
            } ?: locationManager.requestSingleUpdate(
                LocationManager.NETWORK_PROVIDER,
                object : LocationListener {
                    override fun onLocationChanged(location: Location?) {
                        location?.let {
                            saveAndBack(
                                Coordinate(it.latitude, it.longitude),
                                true
                            )
                        }
                    }

                    override fun onStatusChanged(
                        provider: String?,
                        status: Int,
                        extras: Bundle?
                    ) {
                    }

                    override fun onProviderEnabled(provider: String?) {}
                    override fun onProviderDisabled(provider: String?) {}
                }, null
            )

        }
    }

    private fun saveAndBack(coordinate: Coordinate, isMyLocation: Boolean = false) {
        val coordinateString = Gson().toJson(coordinate)
        context?.run {
            val sharedPreferences =
                getSharedPreferences(Constants.APP_PREFERENCES, Context.MODE_PRIVATE)
            sharedPreferences.edit()
                .putString(SAVED_COORDINATE_PREFERENCE, coordinateString)
                .putBoolean(IS_MY_LOCATION_PREFERENCE, isMyLocation)
                .apply()
        }
        findNavController().navigate(
            R.id.action_chooseCityFragment_to_mainScreenFragment,
            Bundle().apply {
                putString(
                    SAVED_COORDINATE_PREFERENCE, coordinateString
                )
            })
    }
}
