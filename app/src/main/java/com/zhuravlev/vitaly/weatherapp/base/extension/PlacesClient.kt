package com.zhuravlev.vitaly.weatherapp.base.extension

import com.google.android.libraries.places.api.net.*
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

suspend fun PlacesClient.wrapPredictionTask(
    request: FindAutocompletePredictionsRequest
): FindAutocompletePredictionsResponse = suspendCancellableCoroutine { continuation ->
    this.findAutocompletePredictions(request)
        .addOnSuccessListener { response ->
            continuation.resume(response)
        }.addOnFailureListener {
            continuation.resumeWithException(RuntimeException(it))
        }
}

suspend fun PlacesClient.wrapFetchPlaceTask(request: FetchPlaceRequest): FetchPlaceResponse =
    suspendCancellableCoroutine { continuation ->
        this.fetchPlace(request).addOnSuccessListener { placeIdResult ->
            continuation.resume(placeIdResult)
        }.addOnFailureListener {
            continuation.resumeWithException(RuntimeException(it))
        }
    }