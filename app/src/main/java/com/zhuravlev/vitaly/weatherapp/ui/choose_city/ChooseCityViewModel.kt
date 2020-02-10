package com.zhuravlev.vitaly.weatherapp.ui.choose_city

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.zhuravlev.vitaly.weatherapp.base.atom.Atom
import com.zhuravlev.vitaly.weatherapp.base.kodein.KodeinViewModel
import com.zhuravlev.vitaly.weatherapp.model.places.PlacesInteractor
import com.zhuravlev.vitaly.weatherapp.model.places.structures.SearchResponse
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.generic.instance

class ChooseCityViewModel(kodein: Kodein) : KodeinViewModel(kodein) {

    private val placesInteractor: PlacesInteractor by instance()

    private val queryResultsInternal = MutableLiveData<Atom<List<SearchResponse>>>()
    val queryResults = queryResultsInternal.immutable()

    private val placeResultInternal = MutableLiveData<Atom<LatLng?>>()
    val placeResult = placeResultInternal.immutable()

    fun searchResultByText(query: String) {
        launch {
            queryResultsInternal.postValue(Atom.Loading())

            val result = try {
                Atom.Success(
                    placesInteractor.searchResultByText(query)
                )
            } catch (exception: Exception) {
                exception.printStackTrace()
                Atom.Error<List<SearchResponse>>(exception)
            }
            queryResultsInternal.postValue(result)
        }
    }

    fun searchResultById(placeId: String) {
        launch {
            queryResultsInternal.postValue(Atom.Loading())

            val result = try {
                Atom.Success(
                    placesInteractor.getSearchResultByPlaceId(placeId)
                )
            } catch (exception: Exception) {
                exception.printStackTrace()
                Atom.Error<LatLng?>(exception)
            }
            placeResultInternal.postValue(result)
        }
    }

}
