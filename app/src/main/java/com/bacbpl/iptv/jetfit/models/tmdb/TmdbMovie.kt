package com.bacbpl.iptv.jetfit.models.tmdb

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class TmdbMovie(
    val id: Int,
    val title: String,
    @SerializedName("original_title")
    val originalTitle: String,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    val overview: String,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("vote_average")
    val voteAverage: Double,
    val genres: List<TmdbGenre>?,
    val runtime: Int?,
    val budget: Long?,
    val revenue: Long?,
    val homepage: String?,
    @SerializedName("imdb_id")
    val imdbId: String?,
    @SerializedName("production_countries")
    val productionCountries: List<TmdbProductionCountry>?,
    @SerializedName("videos")
    val videoResults: TmdbVideoResponse? = null  // Add this line
) : Serializable

data class TmdbGenre(
    val id: Int,
    val name: String
) : Serializable

data class TmdbProductionCountry(
    @SerializedName("iso_3166_1")
    val iso: String,
    val name: String
) : Serializable

