package com.bacbpl.iptv.jetfit.models.tmdb


import com.google.gson.annotations.SerializedName

data class TmdbTvResponse(
    val page: Int,
    val results: List<TmdbTvSeries>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)