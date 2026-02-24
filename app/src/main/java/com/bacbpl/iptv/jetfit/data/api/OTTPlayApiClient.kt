package com.bacbpl.iptv.jetfit.data.api


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object OTTPlayApiClient {
    private const val BASE_URL = "https://subs-api.ottplay.com/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: OTTPlayApiService by lazy {
        retrofit.create(OTTPlayApiService::class.java)
    }
}