package com.zhuravlev.vitaly.weatherapp.model.places.structures

import com.google.android.gms.maps.model.LatLng

data class FetchPlaceResponse(
    val location: LatLng
)