package com.bacbpl.iptv.data.api

import com.bacbpl.iptv.jetfit.ui.fragments.OTTPlayResponse
import com.bacbpl.iptv.ui.activities.otpscreen.data.OtpResponse
import com.bacbpl.iptv.ui.activities.otpscreen.data.VerifyOtpRequest
import com.bacbpl.iptv.ui.activities.otpscreen.data.VerifyOtpResponse
import com.bacbpl.iptv.ui.activities.signupscreen.data.SignupResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("api/sendOtp")
    suspend fun sendOtp(@Query("mobile") mobile: String): Response<OtpResponse>

    @POST("api/verifyOtp")
    suspend fun verifyOtp(@Body request: VerifyOtpRequest): Response<VerifyOtpResponse>

    @POST("api/signup")
    suspend fun signup(
        @Query("mobile") mobile: String,
        @Query("name") name: String,
        @Query("email") email: String
    ): Response<SignupResponse>

    interface OTTPlayApiService {
        @GET("api/partnerfeed-service/v1/partner/widget-list")
        fun getOTTPlayWidgets(
            @Query("seo_url") seoUrl: String,
            @Query("t") t: String
        ): Call<OTTPlayResponse>
    }
}