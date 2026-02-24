package com.bacbpl.iptv.jetfit.utils.deeplink

import android.content.Context
import android.net.Uri
import android.util.Log
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import org.json.JSONObject
import java.io.IOException
import java.net.URLEncoder
import java.util.concurrent.TimeUnit

class DeepLinkHandler(private val context: Context) {

    companion object {
        private const val TAG = "DeepLinkHandler"
        private const val OTTPLAY_BASE_URL = "https://www.ottplay.com"
        private const val OTTPLAY_AUTH_URL = "https://www.ottplay.com/auth"
        private const val PARTNER_TOKEN_VALIDATE_URL = "https://partner.app.com/api/validate-token"
        private val JSON_MEDIA_TYPE = "application/json; charset=utf-8".toMediaType()
    }

    fun createOTTplayDeepLink(
        contentPath: String,
        token: String,
        backUrl: String,
        source: String,
        appName: String,
        isWeb: Boolean = false
    ): String {
        return if (isWeb) {
            "$OTTPLAY_AUTH_URL?seoUrl=${URLEncoder.encode("$OTTPLAY_BASE_URL$contentPath", "UTF-8")}" +
                    "&token=$token&backUrl=${URLEncoder.encode(backUrl, "UTF-8")}" +
                    "&source=$source&appName=${URLEncoder.encode(appName, "UTF-8")}"
        } else {
            "$OTTPLAY_BASE_URL$contentPath?token=$token" +
                    "&backUrl=${URLEncoder.encode(backUrl, "UTF-8")}" +
                    "&source=$source&appName=${URLEncoder.encode(appName, "UTF-8")}"
        }
    }

    fun parseDeepLink(uri: Uri): DeepLinkParams? {
        return try {
            val token = uri.getQueryParameter("token") ?: return null
            val backUrl = uri.getQueryParameter("backUrl") ?: return null
            val source = uri.getQueryParameter("source") ?: return null
            val appName = uri.getQueryParameter("appName") ?: return null
            val contentPath = uri.path ?: return null
            val pathSegments = contentPath.split("/").filter { it.isNotEmpty() }
            if (pathSegments.size < 2) return null
            val contentType = pathSegments[0]
            val contentId = pathSegments.last()

            DeepLinkParams(
                token = token,
                backUrl = backUrl,
                source = source,
                appName = appName,
                contentUrl = "$OTTPLAY_BASE_URL$contentPath",
                contentId = contentId,
                contentType = contentType
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing deep link: ${e.message}")
            null
        }
    }

    fun validatePartnerToken(token: String, callback: (PartnerTokenValidationResult) -> Unit) {
        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        val request = Request.Builder()
            .url(PARTNER_TOKEN_VALIDATE_URL)
            .header("Authorization", "Bearer $token")
            .header("Content-Type", "application/json")
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, "API call failed: ${e.message}")
                callback(PartnerTokenValidationResult.Error(e.message ?: "Network error"))
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val responseBody = response.body?.string()
                    Log.d(TAG, "API Response: $responseBody")

                    when (response.code) {
                        200 -> {
                            val json = JSONObject(responseBody ?: "{}")
                            val name = json.optString("name", "")
                            val mobile = json.optString("mobile", "")
                            val email = json.optString("email", null)
                            callback(PartnerTokenValidationResult.Valid(name, mobile, email))
                        }
                        400, 401 -> {
                            val json = JSONObject(responseBody ?: "{}")
                            val message = json.optString("message", "Invalid token")
                            callback(PartnerTokenValidationResult.Invalid(message))
                        }
                        else -> {
                            callback(PartnerTokenValidationResult.Error("Unexpected response: ${response.code}"))
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error parsing response: ${e.message}")
                    callback(PartnerTokenValidationResult.Error(e.message ?: "Parse error"))
                }
            }
        })
    }

    suspend fun validatePartnerTokenSuspend(token: String): PartnerTokenValidationResult {
        return try {
            val client = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()

            val request = Request.Builder()
                .url(PARTNER_TOKEN_VALIDATE_URL)
                .header("Authorization", "Bearer $token")
                .header("Content-Type", "application/json")
                .get()
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()

            when (response.code) {
                200 -> {
                    val json = JSONObject(responseBody ?: "{}")
                    val name = json.optString("name", "")
                    val mobile = json.optString("mobile", "")
                    val email = json.optString("email", null)
                    PartnerTokenValidationResult.Valid(name, mobile, email)
                }
                400, 401 -> {
                    val json = JSONObject(responseBody ?: "{}")
                    val message = json.optString("message", "Invalid token")
                    PartnerTokenValidationResult.Invalid(message)
                }
                else -> {
                    PartnerTokenValidationResult.Error("Unexpected response: ${response.code}")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error validating token: ${e.message}")
            PartnerTokenValidationResult.Error(e.message ?: "Unknown error")
        }
    }

    sealed class PartnerTokenValidationResult {
        data class Valid(val name: String, val mobile: String, val email: String?) : PartnerTokenValidationResult()
        data class Invalid(val message: String) : PartnerTokenValidationResult()
        data class Error(val message: String) : PartnerTokenValidationResult()
    }
}