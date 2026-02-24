package com.bacbpl.iptv.jetfit.models.tmdb

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class TmdbVideoResponse(
    val results: List<TmdbVideo>
) : Serializable

data class TmdbVideo(
    val id: String,
    val key: String,
    val name: String,
    val site: String,
    val type: String,
    @SerializedName("published_at")
    val publishedAt: String
) : Serializable