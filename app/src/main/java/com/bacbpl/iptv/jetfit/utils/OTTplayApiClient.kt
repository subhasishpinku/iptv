package com.bacbpl.iptv.jetfit.utils

import com.bacbpl.iptv.jetfit.ui.fragments.DeepLinkFragment.ApiResponse
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import kotlin.jvm.java

class OTTplayApiClient {
    private val client = OkHttpClient()
    private val gson = Gson()
    private val baseUrl = "https://api2.ottplay.com/api/partnerfeed-service/v1/partner"

    fun getWidgetList(seoUrl: String): ApiResponse {
        val request = Request.Builder()
            .url("$baseUrl/widget-list?seo_url=$seoUrl&t=${System.currentTimeMillis()}")
            .addHeader("User-Agent", "BACBPL-IPTV/1.0")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val responseBody = response.body?.string()
            return gson.fromJson(responseBody, ApiResponse::class.java)
        }
    }
}