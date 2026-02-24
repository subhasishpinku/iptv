package com.bacbpl.iptv.jetfit.models.ottplay

import com.google.gson.annotations.SerializedName

data class OTTPlayResponse(
    val success: Boolean,
    val widgets: List<OTTPlayWidget>
)

data class OTTPlayWidget(
    val name: String,
    val type: String,
    val id: String,
    val data: List<OTTPlayItem>
)

data class OTTPlayItem(
    val name: String,
    val format: String,
    val language: String,
    @SerializedName("release_year")
    val releaseYear: Int,
    @SerializedName("ott_provider_name")
    val ottProviderName: String,
    @SerializedName("poster_image")
    val posterImage: String,
    @SerializedName("backdrop_image")
    val backdropImage: String?,
    val id: String,
    @SerializedName("ottplay_rating")
    val ottplayRating: Double,
    val genre: String,
    val casts: List<CastCrew>?,
    val crews: List<CastCrew>?,
    @SerializedName("ottplay_url")
    val ottplayUrl: String
)

data class CastCrew(
    val name: String,
    val poster: String?
)