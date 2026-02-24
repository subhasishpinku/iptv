package com.bacbpl.iptv.jetfit.data.api


import com.bacbpl.iptv.jetfit.models.ottplay.OTTPlayResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OTTPlayApiService {
    @GET("api/partnerfeed-service/v1/partner/widget-list")
    fun getOTTPlayWidgets(
        @Query("seo_url") seoUrl: String,
        @Query("t") t: String
    ): Call<OTTPlayResponse>
}