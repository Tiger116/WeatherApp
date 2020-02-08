package com.zhuravlev.vitaly.weatherapp.ui.choose_city

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
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.zhuravlev.vitaly.weatherapp.R
import com.zhuravlev.vitaly.weatherapp.base.Constants
import com.zhuravlev.vitaly.weatherapp.base.kodein.KodeinFragment
import com.zhuravlev.vitaly.weatherapp.databinding.FragmentChooseCityBinding
import com.zhuravlev.vitaly.weatherapp.model.weather.structures.Coordinate

class ChooseCityFragment : KodeinFragment() {
    companion object {
        const val LOCATION_PERMISSION_REQUEST = 100
        const val SAVED_COORDINATE_PREFERENCE = "savedCoordinate"
        const val IS_MY_LOCATION_PREFERENCE = "isMyLocation"
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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.isRefreshing = false
        }
        binding.myLocationItem.setOnClickListener {
            requestPermissionsAndGetLocation()
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
            val location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            location?.let {
                saveAndBack(Coordinate(it.longitude, it.latitude), true)
            } ?: locationManager.requestSingleUpdate(
                LocationManager.NETWORK_PROVIDER,
                object : LocationListener {
                    override fun onLocationChanged(location: Location?) {
                        location?.let { saveAndBack(Coordinate(it.latitude, it.longitude), true) }
                    }

                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
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
