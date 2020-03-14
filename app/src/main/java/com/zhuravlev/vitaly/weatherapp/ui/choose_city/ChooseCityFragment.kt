package com.zhuravlev.vitaly.weatherapp.ui.choose_city

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Base64
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
import com.zhuravlev.vitaly.weatherapp.BuildConfig
import com.zhuravlev.vitaly.weatherapp.R
import com.zhuravlev.vitaly.weatherapp.base.Constants
import com.zhuravlev.vitaly.weatherapp.base.extension.onDelayedClick
import com.zhuravlev.vitaly.weatherapp.base.kodein.KodeinFragment
import com.zhuravlev.vitaly.weatherapp.base.snackbar.Snackbar
import com.zhuravlev.vitaly.weatherapp.databinding.FragmentChooseCityBinding

class ChooseCityFragment : KodeinFragment() {
    companion object {
        const val LOCATION_PERMISSION_REQUEST = 100
        const val AUTOCOMPLETE_REQUEST = 101
        const val SAVED_COORDINATE_PREFERENCE = "savedCoordinate"
        const val IS_MY_LOCATION_PREFERENCE = "isMyLocation"
    }

    private val viewModel: ChooseCityViewModel by lazy {
        provide(ChooseCityViewModel::class.java)
    }
    private lateinit var binding: FragmentChooseCityBinding

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location?) {
            location?.let {
                viewModel.setCoordinate(it.latitude, it.longitude)
                saveAndBack(true)
            }
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        override fun onProviderEnabled(provider: String?) {}
        override fun onProviderDisabled(provider: String?) {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChooseCityBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        if (!Places.isInitialized()) {
            val bytes = Base64.decode(BuildConfig.GOOGLE_API_KEY.toByteArray(), Base64.DEFAULT)
            Places.initialize(requireContext(), String(bytes))
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.myLocationItem.onDelayedClick {
            requestPermissionsAndGetLocation()
        }
        binding.searchTextView.onDelayedClick {
            context?.let { context ->
                val intent = Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.FULLSCREEN,
                    listOf(Place.Field.LAT_LNG, Place.Field.NAME)
                ).setTypeFilter(TypeFilter.CITIES)
                    .build(context)
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST)
            }
        }
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
                    data?.let { intent ->
                        val place = Autocomplete.getPlaceFromIntent(intent)
                        place.latLng?.let {
                            viewModel.setCoordinate(it.latitude, it.longitude, place.name)
                            saveAndBack()
                        }
                    }
                }
                AutocompleteActivity.RESULT_ERROR -> {
                    Snackbar().showMessage(
                        requireContext(),
                        getString(R.string.places_search_error)
                    )
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
                context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager?

            val location =
                locationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

            location?.let {
                viewModel.setCoordinate(it.latitude, it.longitude)
                saveAndBack(true)
            } ?: locationManager?.requestSingleUpdate(
                LocationManager.NETWORK_PROVIDER,
                locationListener,
                null
            )
        }
    }

    private fun saveAndBack(isMyLocation: Boolean = false) {
        val coordinateJson = viewModel.getCoordinateJson()

        context?.let { saveCoordinateToSharedPrefs(it, coordinateJson, isMyLocation) }

        val args = Bundle().apply {
            putString(SAVED_COORDINATE_PREFERENCE, coordinateJson)
        }
        findNavController().navigate(R.id.action_chooseCityFragment_to_mainScreenFragment, args)
    }

    private fun saveCoordinateToSharedPrefs(
        context: Context,
        coordinateString: String,
        isMyLocation: Boolean = false
    ) {
        val sharedPreferences =
            context.getSharedPreferences(Constants.APP_PREFERENCES, Context.MODE_PRIVATE)
        sharedPreferences.edit()
            .putString(SAVED_COORDINATE_PREFERENCE, coordinateString)
            .putBoolean(IS_MY_LOCATION_PREFERENCE, isMyLocation)
            .apply()
    }
}
