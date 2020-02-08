package com.zhuravlev.vitaly.weatherapp.model.network.retrofit

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.zhuravlev.vitaly.weatherapp.model.weather.structures.UnitsFormat
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

object RetrofitFactory {
    fun create(baseUrl: String, appId: String?, unitsFormat: String? = UnitsFormat.METRIC.value): Retrofit =
        createRetrofit(createOkHttpClient(appId, unitsFormat), baseUrl)

    private fun createOkHttpClient(appId: String?, unitsFormat: String?) =
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor(createInterceptor(appId, unitsFormat))
            .build()

    private fun createRetrofit(client: OkHttpClient, baseUrl: String) =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

    private fun createInterceptor(appId: String?, unitsFormat: String?) = object : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val url = chain.request().url
                .newBuilder()
                .addQueryParameter("APPID", appId)
                .addQueryParameter("lang", getLanguage())
                .addQueryParameter("units", unitsFormat)
                .build()
            val request = chain.request()
                .newBuilder()
                .url(url)
                .build()

            return chain.proceed(request)
        }
    }

    private fun getLanguage(): String =
            Locale.getDefault().language
}
