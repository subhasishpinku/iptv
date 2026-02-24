package com.bacbpl.iptv.jetfit.network

import com.bacbpl.iptv.jetfit.models.tmdb.TmdbMovie
import com.bacbpl.iptv.jetfit.models.tmdb.TmdbMovieResponse
import com.bacbpl.iptv.jetfit.models.tmdb.TmdbPersonResponse
import com.bacbpl.iptv.jetfit.models.tmdb.TmdbTvResponse
import com.bacbpl.iptv.jetfit.models.tmdb.TmdbTvSeries
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApiService {

    // Movie endpoints
    @GET("movie/now_playing")
    fun getNowPlayingMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): Call<TmdbMovieResponse>

    @GET("movie/upcoming")
    fun getUpcomingMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): Call<TmdbMovieResponse>

    @GET("movie/popular")
    fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): Call<TmdbMovieResponse>

    @GET("movie/top_rated")
    fun getTopRatedMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): Call<TmdbMovieResponse>

    @GET("movie/{movie_id}")
    fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US",
        @Query("append_to_response") appendToResponse: String = "videos,credits"
    ): Call<TmdbMovie>

    // TV Series endpoints
    @GET("tv/airing_today")
    fun getAiringToday(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): Call<TmdbTvResponse>

    @GET("tv/on_the_air")
    fun getOnTheAir(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): Call<TmdbTvResponse>

    @GET("tv/popular")
    fun getPopularTv(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): Call<TmdbTvResponse>

    @GET("tv/top_rated")
    fun getTopRatedTv(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): Call<TmdbTvResponse>

    @GET("tv/{series_id}")
    fun getTvSeriesDetails(
        @Path("series_id") seriesId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US",
        @Query("append_to_response") appendToResponse: String = "videos,credits"
    ): Call<TmdbTvSeries>

    @GET("person/popular")
    fun getPopularPeople(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): Call<TmdbPersonResponse>
}