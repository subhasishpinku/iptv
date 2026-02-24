//package  com.bacbpl.iptv.jetfit.utils
//
//import android.os.AsyncTask
//import android.text.TextUtils
//import info.movito.themoviedbapi.TmdbApi
//import info.movito.themoviedbapi.model.MovieDb
//import info.movito.themoviedbapi.model.people.Person
//import info.movito.themoviedbapi.model.tv.TvSeries
//import  com.bacbpl.iptv.jetfit.AppConstants
//import com.bacbpl.iptv.jetfit.models.RowObjectAdapter
//
//class LoadMoreItems(private val rowObject: RowObjectAdapter, private val tmdb: TmdbApi,
//                    private val cListener: ListListener) : AsyncTask<Void, Void, Void>() {
//    val list = ArrayList<Any>()
//
//    init {
//        execute()
//    }
//
//    override fun onPostExecute(result: Void?) {
//        super.onPostExecute(result)
//        cListener.onResult(list)
//    }
//
//    override fun doInBackground(vararg params: Void?): Void? {
//        rowObject.page += 1
//        when (rowObject.type) {
//            AppConstants.TypeRows.MOVIE_NOW -> {
//                val movieNowPlaying =
//                    tmdb.movies.getNowPlayingMovies("en-US", rowObject.page, "")
//                for (item: MovieDb in movieNowPlaying.results) {
//                    if (!TextUtils.isEmpty(item.title))
//                        list.add(item)
//                }
//            }
//            AppConstants.TypeRows.MOVIE_UPCOMING -> {
//                val movieUpcoming = tmdb.movies.getUpcoming("en-US", rowObject.page, "")
//                for (item: MovieDb in movieUpcoming.results) {
//                    if (!TextUtils.isEmpty(item.title))
//                        list.add(item)
//                }
//            }
//            AppConstants.TypeRows.MOVIE_POPULAR -> {
//                val moviePopular = tmdb.movies.getPopularMovies("en-US", rowObject.page)
//                for (item: MovieDb in moviePopular.results) {
//                    if (!TextUtils.isEmpty(item.title))
//                        list.add(item)
//                }
//            }
//            AppConstants.TypeRows.MOVIE_TOP -> {
//                val movieTopRated = tmdb.movies.getTopRatedMovies("en-US", rowObject.page)
//                for (item: MovieDb in movieTopRated.results) {
//                    if (!TextUtils.isEmpty(item.title))
//                        list.add(item)
//                }
//            }
//            AppConstants.TypeRows.SERIAL_TODAY -> {
//                val serialToday = tmdb.tvSeries.getAiringToday("en-US", rowObject.page, null)
//                for (item: TvSeries in serialToday.results) {
//                    if (!TextUtils.isEmpty(item.name))
//                        list.add(item)
//                }
//            }
//            AppConstants.TypeRows.SERIAL_ONTV -> {
//                val serialTv = tmdb.tvSeries.getOnTheAir("en-US", rowObject.page)
//                for (item: TvSeries in serialTv.results) {
//                    if (!TextUtils.isEmpty(item.name))
//                        list.add(item)
//                }
//            }
//            AppConstants.TypeRows.SERIAL_POPULAR -> {
//                val serialPopular = tmdb.tvSeries.getPopular("en-US", rowObject.page)
//                for (item: TvSeries in serialPopular.results) {
//                    if (!TextUtils.isEmpty(item.name))
//                        list.add(item)
//                }
//            }
//            AppConstants.TypeRows.SERIAL_TOP -> {
//                val serialTopRated = tmdb.tvSeries.getTopRated("en-US", rowObject.page)
//                for (item: TvSeries in serialTopRated.results) {
//                    if (!TextUtils.isEmpty(item.name))
//                        list.add(item)
//                }
//            }
//            AppConstants.TypeRows.PEOPLE -> {
//                val people = tmdb.people.getPersonPopular(rowObject.page)
//                for (item: Person in people.results) {
//                    if (!TextUtils.isEmpty(item.name))
//                        list.add(item)
//                }
//            }
//        }
//        return null
//    }
//}

// Create LoadMoreItems.kt using Retrofit

package com.bacbpl.iptv.jetfit.utils

import android.os.AsyncTask
import com.bacbpl.iptv.jetfit.AppConstants
import com.bacbpl.iptv.jetfit.models.RowObjectAdapter
import com.bacbpl.iptv.jetfit.models.tmdb.TmdbMovie
import com.bacbpl.iptv.jetfit.models.tmdb.TmdbPerson
import com.bacbpl.iptv.jetfit.models.tmdb.TmdbTvSeries
import com.bacbpl.iptv.jetfit.repository.TmdbRepository

class LoadMoreItems(
    private val rowObject: RowObjectAdapter,
    private val listener: ListListener
) : AsyncTask<Void, Void, ArrayList<Any>>() {

    private val repository = TmdbRepository()
    private val resultList = ArrayList<Any>()

    override fun doInBackground(vararg params: Void?): ArrayList<Any> {
        when (rowObject.type) {
            AppConstants.TypeRows.MOVIE_NOW -> {
                repository.getNowPlayingMovies(
                    page = rowObject.page + 1,
                    onSuccess = { resultList.addAll(it) },
                    onError = {}
                )
            }
            AppConstants.TypeRows.MOVIE_POPULAR -> {
                repository.getPopularMovies(
                    page = rowObject.page + 1,
                    onSuccess = { resultList.addAll(it) },
                    onError = {}
                )
            }
            AppConstants.TypeRows.MOVIE_TOP -> {
                repository.getTopRatedMovies(
                    page = rowObject.page + 1,
                    onSuccess = { resultList.addAll(it) },
                    onError = {}
                )
            }
            AppConstants.TypeRows.MOVIE_UPCOMING -> {
                repository.getUpcomingMovies(
                    page = rowObject.page + 1,
                    onSuccess = { resultList.addAll(it) },
                    onError = {}
                )
            }
            AppConstants.TypeRows.SERIAL_TODAY -> {
                repository.getAiringToday(
                    page = rowObject.page + 1,
                    onSuccess = { resultList.addAll(it) },
                    onError = {}
                )
            }
            AppConstants.TypeRows.SERIAL_ONTV -> {
                repository.getOnTheAir(
                    page = rowObject.page + 1,
                    onSuccess = { resultList.addAll(it) },
                    onError = {}
                )
            }
            AppConstants.TypeRows.SERIAL_POPULAR -> {
                repository.getPopularTv(
                    page = rowObject.page + 1,
                    onSuccess = { resultList.addAll(it) },
                    onError = {}
                )
            }
            AppConstants.TypeRows.SERIAL_TOP -> {
                repository.getTopRatedTv(
                    page = rowObject.page + 1,
                    onSuccess = { resultList.addAll(it) },
                    onError = {}
                )
            }
            AppConstants.TypeRows.PEOPLE -> {
                repository.getPopularPeople(
                    page = rowObject.page + 1,
                    onSuccess = { resultList.addAll(it) },
                    onError = {}
                )
            }
        }
        return resultList
    }

    override fun onPostExecute(result: ArrayList<Any>) {
        super.onPostExecute(result)
        listener.onResult(result)
    }
}