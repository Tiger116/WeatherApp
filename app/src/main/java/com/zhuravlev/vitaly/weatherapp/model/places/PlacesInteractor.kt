package com.zhuravlev.vitaly.weatherapp.model.places

import android.content.Context
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.zhuravlev.vitaly.weatherapp.base.extension.wrapFetchPlaceTask
import com.zhuravlev.vitaly.weatherapp.base.extension.wrapPredictionTask
import com.zhuravlev.vitaly.weatherapp.model.places.structures.FetchPlaceResponse
import com.zhuravlev.vitaly.weatherapp.model.places.structures.SearchResponse

class PlacesInteractor(val context: Context) {

    private val token: AutocompleteSessionToken = AutocompleteSessionToken.newInstance()
    private val client = Places.createClient(context)

    private fun configurePredictionRequest(query: String) =
        FindAutocompletePredictionsRequest.builder().apply {
            setTypeFilter(TypeFilter.CITIES)
            setSessionToken(token)
            setQuery(query)
            build()
        }

    suspend fun searchResultByText(query: String): List<SearchResponse> {
        val response = client.wrapPredictionTask(configurePredictionRequest(query).build())
        return response.autocompletePredictions.map { result ->
            SearchResponse(result.getFullText(null).toString(), result.placeId)
        }
    }

    suspend fun getSearchResultByPlaceId(placeId: String): LatLng? {
        val response = client.wrapFetchPlaceTask(configureFetchPlaceRequest(placeId))
        return response.place.latLng
    }

    private fun configureFetchPlaceRequest(placeId: String): FetchPlaceRequest {
        val placeFields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
        return FetchPlaceRequest.newInstance(placeId, placeFields)
    }
}