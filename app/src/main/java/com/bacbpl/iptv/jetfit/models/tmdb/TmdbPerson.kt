package com.bacbpl.iptv.jetfit.models.tmdb

import com.google.gson.annotations.SerializedName

data class TmdbPerson(
    val id: Int,
    val name: String,
    @SerializedName("profile_path")
    val profilePath: String?,
    val popularity: Double
)

data class TmdbPersonResponse(
    val page: Int,
    val results: List<TmdbPerson>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)