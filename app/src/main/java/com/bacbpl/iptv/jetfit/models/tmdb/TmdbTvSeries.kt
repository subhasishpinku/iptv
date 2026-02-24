package com.bacbpl.iptv.jetfit.models.tmdb

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class TmdbTvSeries(
    val id: Int,
    val name: String,
    @SerializedName("original_name")
    val originalName: String,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    val overview: String,
    @SerializedName("first_air_date")
    val firstAirDate: String,
    @SerializedName("vote_average")
    val voteAverage: Double,
    val genres: List<TmdbGenre>?,
    @SerializedName("origin_country")
    val originCountry: List<String>?,
    val status: String?,
    val homepage: String?,
    @SerializedName("videos")
    val videoResults: TmdbVideoResponse? = null  // Add this line
) : Serializable