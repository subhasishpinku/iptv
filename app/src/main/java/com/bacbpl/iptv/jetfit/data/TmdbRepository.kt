package com.bacbpl.iptv.jetfit.repository

import com.bacbpl.iptv.jetfit.AppConstants
import com.bacbpl.iptv.jetfit.models.tmdb.TmdbMovie
import com.bacbpl.iptv.jetfit.models.tmdb.TmdbMovieResponse
import com.bacbpl.iptv.jetfit.models.tmdb.TmdbPerson
import com.bacbpl.iptv.jetfit.models.tmdb.TmdbPersonResponse
import com.bacbpl.iptv.jetfit.models.tmdb.TmdbTvResponse
import com.bacbpl.iptv.jetfit.models.tmdb.TmdbTvSeries
import com.bacbpl.iptv.jetfit.network.TmdbClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TmdbRepository {

    private val apiKey = AppConstants.API_TMDB

    // ============ MOVIE METHODS ============

    fun getNowPlayingMovies(
        page: Int = 1,
        onSuccess: (List<TmdbMovie>) -> Unit,
        onError: (String) -> Unit
    ) {
        TmdbClient.apiService.getNowPlayingMovies(apiKey, page = page)
            .enqueue(object : Callback<TmdbMovieResponse> {
                override fun onResponse(
                    call: Call<TmdbMovieResponse>,
                    response: Response<TmdbMovieResponse>
                ) {
                    if (response.isSuccessful) {
                        onSuccess(response.body()?.results ?: emptyList())
                    } else {
                        onError("Error: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<TmdbMovieResponse>, t: Throwable) {
                    onError(t.message ?: "Network error")
                }
            })
    }

    fun getUpcomingMovies(
        page: Int = 1,
        onSuccess: (List<TmdbMovie>) -> Unit,
        onError: (String) -> Unit
    ) {
        TmdbClient.apiService.getUpcomingMovies(apiKey, page = page)
            .enqueue(object : Callback<TmdbMovieResponse> {
                override fun onResponse(
                    call: Call<TmdbMovieResponse>,
                    response: Response<TmdbMovieResponse>
                ) {
                    if (response.isSuccessful) {
                        onSuccess(response.body()?.results ?: emptyList())
                    } else {
                        onError("Error: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<TmdbMovieResponse>, t: Throwable) {
                    onError(t.message ?: "Network error")
                }
            })
    }

    fun getPopularMovies(
        page: Int = 1,
        onSuccess: (List<TmdbMovie>) -> Unit,
        onError: (String) -> Unit
    ) {
        TmdbClient.apiService.getPopularMovies(apiKey, page = page)
            .enqueue(object : Callback<TmdbMovieResponse> {
                override fun onResponse(
                    call: Call<TmdbMovieResponse>,
                    response: Response<TmdbMovieResponse>
                ) {
                    if (response.isSuccessful) {
                        onSuccess(response.body()?.results ?: emptyList())
                    } else {
                        onError("Error: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<TmdbMovieResponse>, t: Throwable) {
                    onError(t.message ?: "Network error")
                }
            })
    }

    fun getTopRatedMovies(
        page: Int = 1,
        onSuccess: (List<TmdbMovie>) -> Unit,
        onError: (String) -> Unit
    ) {
        TmdbClient.apiService.getTopRatedMovies(apiKey, page = page)
            .enqueue(object : Callback<TmdbMovieResponse> {
                override fun onResponse(
                    call: Call<TmdbMovieResponse>,
                    response: Response<TmdbMovieResponse>
                ) {
                    if (response.isSuccessful) {
                        onSuccess(response.body()?.results ?: emptyList())
                    } else {
                        onError("Error: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<TmdbMovieResponse>, t: Throwable) {
                    onError(t.message ?: "Network error")
                }
            })
    }

    fun getMovieDetails(
        movieId: Int,
        onSuccess: (TmdbMovie) -> Unit,
        onError: (String) -> Unit
    ) {
        TmdbClient.apiService.getMovieDetails(movieId, apiKey, appendToResponse = "videos,credits")
            .enqueue(object : Callback<TmdbMovie> {
                override fun onResponse(
                    call: Call<TmdbMovie>,
                    response: Response<TmdbMovie>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { onSuccess(it) }
                    } else {
                        onError("Error: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<TmdbMovie>, t: Throwable) {
                    onError(t.message ?: "Network error")
                }
            })
    }

    // ============ TV SERIES METHODS ============

    fun getAiringToday(
        page: Int = 1,
        onSuccess: (List<TmdbTvSeries>) -> Unit,
        onError: (String) -> Unit
    ) {
        TmdbClient.apiService.getAiringToday(apiKey, page = page)
            .enqueue(object : Callback<TmdbTvResponse> {
                override fun onResponse(
                    call: Call<TmdbTvResponse>,
                    response: Response<TmdbTvResponse>
                ) {
                    if (response.isSuccessful) {
                        onSuccess(response.body()?.results ?: emptyList())
                    } else {
                        onError("Error: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<TmdbTvResponse>, t: Throwable) {
                    onError(t.message ?: "Network error")
                }
            })
    }

    fun getOnTheAir(
        page: Int = 1,
        onSuccess: (List<TmdbTvSeries>) -> Unit,
        onError: (String) -> Unit
    ) {
        TmdbClient.apiService.getOnTheAir(apiKey, page = page)
            .enqueue(object : Callback<TmdbTvResponse> {
                override fun onResponse(
                    call: Call<TmdbTvResponse>,
                    response: Response<TmdbTvResponse>
                ) {
                    if (response.isSuccessful) {
                        onSuccess(response.body()?.results ?: emptyList())
                    } else {
                        onError("Error: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<TmdbTvResponse>, t: Throwable) {
                    onError(t.message ?: "Network error")
                }
            })
    }

    fun getPopularTv(
        page: Int = 1,
        onSuccess: (List<TmdbTvSeries>) -> Unit,
        onError: (String) -> Unit
    ) {
        TmdbClient.apiService.getPopularTv(apiKey, page = page)
            .enqueue(object : Callback<TmdbTvResponse> {
                override fun onResponse(
                    call: Call<TmdbTvResponse>,
                    response: Response<TmdbTvResponse>
                ) {
                    if (response.isSuccessful) {
                        onSuccess(response.body()?.results ?: emptyList())
                    } else {
                        onError("Error: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<TmdbTvResponse>, t: Throwable) {
                    onError(t.message ?: "Network error")
                }
            })
    }

    fun getTopRatedTv(
        page: Int = 1,
        onSuccess: (List<TmdbTvSeries>) -> Unit,
        onError: (String) -> Unit
    ) {
        TmdbClient.apiService.getTopRatedTv(apiKey, page = page)
            .enqueue(object : Callback<TmdbTvResponse> {
                override fun onResponse(
                    call: Call<TmdbTvResponse>,
                    response: Response<TmdbTvResponse>
                ) {
                    if (response.isSuccessful) {
                        onSuccess(response.body()?.results ?: emptyList())
                    } else {
                        onError("Error: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<TmdbTvResponse>, t: Throwable) {
                    onError(t.message ?: "Network error")
                }
            })
    }

    // FIXED: This is the method that was causing the error
    fun getTvSeriesDetails(
        seriesId: Int,
        onSuccess: (TmdbTvSeries) -> Unit,
        onError: (String) -> Unit
    ) {
        TmdbClient.apiService.getTvSeriesDetails(seriesId, apiKey, appendToResponse = "videos,credits")
            .enqueue(object : Callback<TmdbTvSeries> {
                override fun onResponse(
                    call: Call<TmdbTvSeries>,
                    response: Response<TmdbTvSeries>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            onSuccess(it)
                        }
                    } else {
                        onError("Error: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<TmdbTvSeries>, t: Throwable) {
                    onError(t.message ?: "Network error")
                }
            })
    }

    // ============ PEOPLE METHODS ============

    fun getPopularPeople(
        page: Int = 1,
        onSuccess: (List<TmdbPerson>) -> Unit,
        onError: (String) -> Unit
    ) {
        TmdbClient.apiService.getPopularPeople(apiKey, page = page)
            .enqueue(object : Callback<TmdbPersonResponse> {
                override fun onResponse(
                    call: Call<TmdbPersonResponse>,
                    response: Response<TmdbPersonResponse>
                ) {
                    if (response.isSuccessful) {
                        onSuccess(response.body()?.results ?: emptyList())
                    } else {
                        onError("Error: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<TmdbPersonResponse>, t: Throwable) {
                    onError(t.message ?: "Network error")
                }
            })
    }
}