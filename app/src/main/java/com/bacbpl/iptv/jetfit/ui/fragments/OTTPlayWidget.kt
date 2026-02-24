package com.bacbpl.iptv.jetfit.ui.fragments

// OTTPlayWidget.kt
data class OTTPlayResponse(val success: Boolean, val widgets: List<OTTPlayWidget>)
data class OTTPlayWidget(val name: String, val type: String, val data: List<OTTPlayItem>)
data class OTTPlayItem(
    val name: String,
    val format: String,
    val language: String,
    val release_year: Int,
    val ott_provider_name: String,
    val poster_image: String,
    val backdrop_image: String?,
    val id: String,
    val ottplay_rating: Double,
    val genre: String,
    val casts: List<CastCrew>?,
    val crews: List<CastCrew>?,
    val ottplay_url: String
)
data class CastCrew(val name: String, val poster: String?)