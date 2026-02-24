//package com.bacbpl.iptv.jetfit.ui.fragments
//
//import android.content.Context
//import android.graphics.Color
//import android.os.Handler
//import android.text.SpannableString
//import android.text.Spanned
//import android.text.TextUtils
//import android.util.Log
//import android.view.View
//import android.widget.ProgressBar
//import androidx.leanback.app.RowsSupportFragment
//import androidx.leanback.widget.*
//import com.bacbpl.iptv.R
//import com.bacbpl.iptv.jetfit.ui.activities.DetailsActivity
//import com.bacbpl.iptv.jetfit.ui.presenters.CardPresenter
//import com.bacbpl.iptv.jetfit.ui.presenters.CustomListRowPresenter
//import com.bacbpl.iptv.jetfit.*
//import com.bacbpl.iptv.jetfit.models.ObjectSerializable
//import com.bacbpl.iptv.jetfit.models.RowObjectAdapter
//import com.bacbpl.iptv.jetfit.models.tmdb.TmdbMovie
//import com.bacbpl.iptv.jetfit.models.tmdb.TmdbTvSeries
//import com.bacbpl.iptv.jetfit.repository.TmdbRepository
//import com.bacbpl.iptv.jetfit.utils.*
//import java.util.Locale.getDefault
//
//class MainRowFragment(mContext: Context, private val category: String) : RowsSupportFragment() {
//    val arrayAdapter: ArrayList<RowObjectAdapter>
//    val mRowsAdapter: ArrayObjectAdapter
//
//    // Remove old tmdb
//    // private lateinit var tmdb: TmdbApi
//
//    // Add new repository
//    private val tmdbRepository = TmdbRepository()
//
//    private var mPb: ProgressBar? = null
//
//    private var array: Array<String>
//    private var nowPlaying: String
//    private var upcoming: String
//    private var popular: String
//    private var topRated: String
//    private var airingToday: String
//    private var onTv: String
//
//    init {
//        val presenter = CustomListRowPresenter()
//        mRowsAdapter = ArrayObjectAdapter(presenter)
//        arrayAdapter = ArrayList()
//
//        onItemViewClickedListener = OnItemViewClickedListener { itemViewHolder, item, rowViewHolder, row ->
//            DetailsActivity().start(activity, ObjectSerializable(item), (itemViewHolder.view as ImageCardView).mainImageView)
//        }
//
//        onItemViewSelectedListener = OnItemViewSelectedListener { itemViewHolder, item, rowViewHolder, row ->
//            val curPos = arrayAdapter[selectedPosition].list.indexOf(item)
//            if (curPos == arrayAdapter[selectedPosition].list.size() - 7 || curPos == arrayAdapter[selectedPosition].list.size() - 2) {
//                loadMoreItems(arrayAdapter[selectedPosition])
//            }
//
//            when (item) {
//                is TmdbMovie -> {
//                    updateMoviePresenter(presenter, item)
//                }
//                is TmdbTvSeries -> {
//                    updateTvPresenter(presenter, item)
//                }
//            }
//        }
//
//        adapter = mRowsAdapter
//        array = mContext.resources.getStringArray(R.array.category_title)
//        nowPlaying = category + AppConstants.DOT_DELIMETERSPACE + mContext.getString(R.string.now_playing)
//        upcoming = category + AppConstants.DOT_DELIMETERSPACE + mContext.getString(R.string.upcoming)
//        popular = category + AppConstants.DOT_DELIMETERSPACE + mContext.getString(R.string.popular)
//        topRated = category + AppConstants.DOT_DELIMETERSPACE + mContext.getString(R.string.top_rated)
//        airingToday = category + AppConstants.DOT_DELIMETERSPACE + mContext.getString(R.string.airing_today)
//        onTv = category + AppConstants.DOT_DELIMETERSPACE + mContext.getString(R.string.on_tv)
//
//        mPb = activity?.findViewById(R.id.pb)
//        mPb?.visibility = View.VISIBLE
//
//        loadData()
//    }
//
//    private fun loadData() {
//        when (category.lowercase(getDefault())) {
//            array[0].lowercase(getDefault()) -> {
//                // Movies
//                loadMovies()
//            }
//            array[1].lowercase(getDefault()) -> {
//                // TV Series
//                loadTvSeries()
//            }
//            array[2].lowercase(getDefault()) -> {
//                // People - handle if needed
//                mPb?.visibility = View.GONE
//            }
//            array[3].lowercase(getDefault()) -> {
//                // TV Channels - handle if needed
//                mPb?.visibility = View.GONE
//            }
//            else -> {
//                mPb?.visibility = View.GONE
//            }
//        }
//    }
//
//    private fun loadMovies() {
//        val presenter = CardPresenter()
//        val loadingStates = Array(4) { false }
//
//        // Now Playing
//        tmdbRepository.getNowPlayingMovies(
//            onSuccess = { movies ->
//                val listMovieNowPlaying = RowObjectAdapter()
//                listMovieNowPlaying.list = ArrayObjectAdapter(presenter)
//                listMovieNowPlaying.page = 1
//                listMovieNowPlaying.type = AppConstants.TypeRows.MOVIE_NOW
//                listMovieNowPlaying.header = nowPlaying
//                movies.forEach { movie ->
//                    if (!TextUtils.isEmpty(movie.title)) {
//                        listMovieNowPlaying.list.add(movie)
//                    }
//                }
//                arrayAdapter.add(listMovieNowPlaying)
//                loadingStates[0] = true
//                checkAllMoviesLoaded(loadingStates)
//            },
//            onError = { error ->
//                Log.e("MainRowFragment", "Error loading now playing: $error")
//                loadingStates[0] = true
//                checkAllMoviesLoaded(loadingStates)
//            }
//        )
//
//        // Upcoming
//        tmdbRepository.getUpcomingMovies(
//            onSuccess = { movies ->
//                val listMovieUpcoming = RowObjectAdapter()
//                listMovieUpcoming.list = ArrayObjectAdapter(presenter)
//                listMovieUpcoming.page = 1
//                listMovieUpcoming.type = AppConstants.TypeRows.MOVIE_UPCOMING
//                listMovieUpcoming.header = upcoming
//                movies.forEach { movie ->
//                    if (!TextUtils.isEmpty(movie.title)) {
//                        listMovieUpcoming.list.add(movie)
//                    }
//                }
//                arrayAdapter.add(listMovieUpcoming)
//                loadingStates[1] = true
//                checkAllMoviesLoaded(loadingStates)
//            },
//            onError = { error ->
//                Log.e("MainRowFragment", "Error loading upcoming: $error")
//                loadingStates[1] = true
//                checkAllMoviesLoaded(loadingStates)
//            }
//        )
//
//        // Popular
//        tmdbRepository.getPopularMovies(
//            onSuccess = { movies ->
//                val listMoviePopular = RowObjectAdapter()
//                listMoviePopular.list = ArrayObjectAdapter(presenter)
//                listMoviePopular.page = 1
//                listMoviePopular.type = AppConstants.TypeRows.MOVIE_POPULAR
//                listMoviePopular.header = popular
//                movies.forEach { movie ->
//                    if (!TextUtils.isEmpty(movie.title)) {
//                        listMoviePopular.list.add(movie)
//                    }
//                }
//                arrayAdapter.add(listMoviePopular)
//                loadingStates[2] = true
//                checkAllMoviesLoaded(loadingStates)
//            },
//            onError = { error ->
//                Log.e("MainRowFragment", "Error loading popular: $error")
//                loadingStates[2] = true
//                checkAllMoviesLoaded(loadingStates)
//            }
//        )
//
//        // Top Rated
//        tmdbRepository.getTopRatedMovies(
//            onSuccess = { movies ->
//                val listMovieTopRated = RowObjectAdapter()
//                listMovieTopRated.list = ArrayObjectAdapter(presenter)
//                listMovieTopRated.page = 1
//                listMovieTopRated.type = AppConstants.TypeRows.MOVIE_TOP
//                listMovieTopRated.header = topRated
//                movies.forEach { movie ->
//                    if (!TextUtils.isEmpty(movie.title)) {
//                        listMovieTopRated.list.add(movie)
//                    }
//                }
//                arrayAdapter.add(listMovieTopRated)
//                loadingStates[3] = true
//                checkAllMoviesLoaded(loadingStates)
//            },
//            onError = { error ->
//                Log.e("MainRowFragment", "Error loading top rated: $error")
//                loadingStates[3] = true
//                checkAllMoviesLoaded(loadingStates)
//            }
//        )
//    }
//
//    private fun loadTvSeries() {
//        val presenter = CardPresenter()
//        val loadingStates = Array(4) { false }
//
//        // Airing Today
//        tmdbRepository.getAiringToday(
//            onSuccess = { series ->
//                val listToday = RowObjectAdapter()
//                listToday.list = ArrayObjectAdapter(presenter)
//                listToday.page = 1
//                listToday.type = AppConstants.TypeRows.SERIAL_TODAY
//                listToday.header = airingToday
//                series.forEach { tv ->
//                    if (!TextUtils.isEmpty(tv.name)) {
//                        listToday.list.add(tv)
//                    }
//                }
//                arrayAdapter.add(listToday)
//                loadingStates[0] = true
//                checkAllTvLoaded(loadingStates)
//            },
//            onError = { error ->
//                Log.e("MainRowFragment", "Error loading airing today: $error")
//                loadingStates[0] = true
//                checkAllTvLoaded(loadingStates)
//            }
//        )
//
//        // On TV
//        tmdbRepository.getOnTheAir(
//            onSuccess = { series ->
//                val listTv = RowObjectAdapter()
//                listTv.list = ArrayObjectAdapter(presenter)
//                listTv.page = 1
//                listTv.type = AppConstants.TypeRows.SERIAL_ONTV
//                listTv.header = onTv
//                series.forEach { tv ->
//                    if (!TextUtils.isEmpty(tv.name)) {
//                        listTv.list.add(tv)
//                    }
//                }
//                arrayAdapter.add(listTv)
//                loadingStates[1] = true
//                checkAllTvLoaded(loadingStates)
//            },
//            onError = { error ->
//                Log.e("MainRowFragment", "Error loading on tv: $error")
//                loadingStates[1] = true
//                checkAllTvLoaded(loadingStates)
//            }
//        )
//
//        // Popular
//        tmdbRepository.getPopularTv(
//            onSuccess = { series ->
//                val listPopular = RowObjectAdapter()
//                listPopular.list = ArrayObjectAdapter(presenter)
//                listPopular.page = 1
//                listPopular.type = AppConstants.TypeRows.SERIAL_POPULAR
//                listPopular.header = popular
//                series.forEach { tv ->
//                    if (!TextUtils.isEmpty(tv.name)) {
//                        listPopular.list.add(tv)
//                    }
//                }
//                arrayAdapter.add(listPopular)
//                loadingStates[2] = true
//                checkAllTvLoaded(loadingStates)
//            },
//            onError = { error ->
//                Log.e("MainRowFragment", "Error loading popular tv: $error")
//                loadingStates[2] = true
//                checkAllTvLoaded(loadingStates)
//            }
//        )
//
//        // Top Rated
//        tmdbRepository.getTopRatedTv(
//            onSuccess = { series ->
//                val listTopRated = RowObjectAdapter()
//                listTopRated.list = ArrayObjectAdapter(presenter)
//                listTopRated.page = 1
//                listTopRated.type = AppConstants.TypeRows.SERIAL_TOP
//                listTopRated.header = topRated
//                series.forEach { tv ->
//                    if (!TextUtils.isEmpty(tv.name)) {
//                        listTopRated.list.add(tv)
//                    }
//                }
//                arrayAdapter.add(listTopRated)
//                loadingStates[3] = true
//                checkAllTvLoaded(loadingStates)
//            },
//            onError = { error ->
//                Log.e("MainRowFragment", "Error loading top rated tv: $error")
//                loadingStates[3] = true
//                checkAllTvLoaded(loadingStates)
//            }
//        )
//    }
//
//    private fun checkAllMoviesLoaded(loadingStates: Array<Boolean>) {
//        if (loadingStates.all { it }) {
//            Handler().postDelayed({
//                updateAdapter()
//            }, 500)
//        }
//    }
//
//    private fun checkAllTvLoaded(loadingStates: Array<Boolean>) {
//        if (loadingStates.all { it }) {
//            Handler().postDelayed({
//                updateAdapter()
//            }, 500)
//        }
//    }
//
//    private fun updateAdapter() {
//        mPb?.visibility = View.GONE
//        mRowsAdapter.clear()
//        for (obj in arrayAdapter) {
//            mRowsAdapter.add(
//                ListRow(
//                    HeaderItem(obj.header),
//                    obj.list
//                )
//            )
//        }
//    }
//
//    private fun loadMoreItems(rowObject: RowObjectAdapter) {
//        // Implement load more functionality with Retrofit
//        // You'll need to create LoadMoreItems class that uses TmdbRepository
//    }
//
//    private fun updateMoviePresenter(presenter: CustomListRowPresenter, movie: TmdbMovie) {
//        var title = movie.title
//        if (movie.title != movie.originalTitle)
//            title = UtilsText().delimeterStrings(
//                AppConstants.DOT_DELIMETERSPACE,
//                movie.title,
//                movie.originalTitle)
//
//        presenter.setDesc(
//            title,
//            SpannableString(
//                UtilsText().delimeterStrings(
//                    AppConstants.COMMA,
//                    UtilsText().safesplit(movie.releaseDate, "-").first(),
//                    AppConstants.ELLIPSIS
//                )
//            )
//        )
//    }
//
//    private fun updateTvPresenter(presenter: CustomListRowPresenter, series: TmdbTvSeries) {
//        var title = series.name
//        if (series.name != series.originalName)
//            title = UtilsText().delimeterStrings(
//                AppConstants.DOT_DELIMETERSPACE,
//                series.name,
//                series.originalName)
//
//        presenter.setDesc(
//            title,
//            SpannableString(UtilsText().safesplit(series.firstAirDate, "-").first()+" ${AppConstants.ELLIPSIS}")
//        )
//    }
//
//    private fun setPresenterDesc(presenter: CustomListRowPresenter, item: Any) {
//        when (item) {
//            is TmdbMovie -> {
//                val genres = item.genres?.let { UtilsText().itemArrToStr(it) } ?: AppConstants.ELLIPSIS
//                val contries = item.productionCountries?.let { UtilsText().itemArrToStr(it) } ?: ""
//                var title = item.title
//                if (item.title != item.originalTitle)
//                    title = UtilsText().delimeterStrings(
//                        AppConstants.DOT_DELIMETERSPACE,
//                        item.title,
//                        item.originalTitle)
//
//                val subtitle = UtilsText().safesplit(item.releaseDate, "-").first() + ", " +
//                        contries + "  " + genres.replace(",", " ")
//                val spanSubtitle = SpannableString(subtitle)
//
//                if (item.genres != null) {
//                    for (g in item.genres) {
//                        spanSubtitle.setSpan(
//                            RoundedColorSpan(Color.rgb(55, 71, 79), Color.rgb(238, 238, 238), 5f, 10, 10),
//                            subtitle.split(" " + g.name)[0].length + 1,
//                            subtitle.split(" " + g.name)[0].length + 1 + g.name.length,
//                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
//                        )
//                    }
//                }
//
//                if (presenter.curTitle == title) {
//                    presenter.setRating(item.voteAverage.toFloat())
//                    presenter.setDesc(title, spanSubtitle)
//                }
//            }
//
//            is TmdbTvSeries -> {
//                val genres = item.genres?.let { UtilsText().itemArrToStr(it) } ?: AppConstants.ELLIPSIS
//                val contries = item.originCountry?.let { UtilsText().itemArrToStr(it) } ?: ""
//                var title = item.name
//                if (item.name != item.originalName)
//                    title = UtilsText().delimeterStrings(
//                        AppConstants.DOT_DELIMETERSPACE,
//                        item.name,
//                        item.originalName)
//
//                if (presenter.curTitle == title) {
//                    val subtitle = UtilsText().safesplit(item.firstAirDate, "-").first() + "  " +
//                            contries + "  " + genres.replace(",", " ")
//                    val spanSubtitle = SpannableString(subtitle)
//
//                    presenter.setRating(item.voteAverage.toFloat())
//                    presenter.setDesc(title, spanSubtitle)
//                }
//            }
//        }
//    }
//
//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        selectedPosition = 0
//        mRowsAdapter.clear()
//
//        mPb = activity?.findViewById(R.id.pb)
//        mPb?.visibility = View.VISIBLE
//
//        // Data is already loading in init, just wait for it
//        Handler().postDelayed({
//            setSelectedPosition(0, false)
//            updateAdapter()
//        }, 1000)
//    }
//}

package com.bacbpl.iptv.jetfit.ui.fragments

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.leanback.app.RowsSupportFragment
import androidx.leanback.widget.*
import com.bacbpl.iptv.R
import com.bacbpl.iptv.jetfit.ui.activities.DetailsActivity
import com.bacbpl.iptv.jetfit.ui.presenters.CardPresenter
import com.bacbpl.iptv.jetfit.ui.presenters.CustomListRowPresenter
import com.bacbpl.iptv.jetfit.*
import com.bacbpl.iptv.jetfit.models.ObjectSerializable
import com.bacbpl.iptv.jetfit.models.RowObjectAdapter
import com.bacbpl.iptv.jetfit.models.tmdb.TmdbMovie
import com.bacbpl.iptv.jetfit.models.tmdb.TmdbTvSeries
import com.bacbpl.iptv.jetfit.repository.TmdbRepository
import com.bacbpl.iptv.jetfit.ui.presenters.CustomImageCardView
import com.bacbpl.iptv.jetfit.utils.*
import java.util.Locale.getDefault

class MainRowFragment(mContext: Context, private val category: String) : RowsSupportFragment() {
    val arrayAdapter: ArrayList<RowObjectAdapter>
    val mRowsAdapter: ArrayObjectAdapter

    // Add new repository
    private val tmdbRepository = TmdbRepository()

    private var mPb: ProgressBar? = null

    private lateinit var array: Array<String>
    private lateinit var nowPlaying: String
    private lateinit var upcoming: String
    private lateinit var popular: String
    private lateinit var topRated: String
    private lateinit var airingToday: String
    private lateinit var onTv: String

//    init {
//        val presenter = CustomListRowPresenter()
//        mRowsAdapter = ArrayObjectAdapter(presenter)
//        arrayAdapter = ArrayList()
//
//        // Set click listener
//        onItemViewClickedListener = OnItemViewClickedListener { itemViewHolder, item, rowViewHolder, row ->
//            val imageView = try {
//                when (val view = itemViewHolder.view) {
//                    is CustomImageCardView -> view.getImageView()
//                    is ImageCardView -> view.mainImageView
//                    else -> {
//                        var result: ImageView? = null
//                        fun searchForImageView(v: View) {
//                            if (result != null) return
//                            when (v) {
//                                is ImageView -> result = v
//                                is ViewGroup -> {
//                                    for (i in 0 until v.childCount) {
//                                        searchForImageView(v.getChildAt(i))
//                                    }
//                                }
//                            }
//                        }
//                        searchForImageView(view)
//                        result ?: ImageView(view.context).apply {
//                            layoutParams = ViewGroup.LayoutParams(1, 1)
//                        }
//                    }
//                }
//            } catch (e: Exception) {
//                Log.e("MainRowFragment", "Error getting ImageView", e)
//                ImageView(itemViewHolder.view.context).apply {
//                    layoutParams = ViewGroup.LayoutParams(1, 1)
//                }
//            }
//
//            // শুধু ID এবং type পাঠান
//            when (item) {
//                is TmdbMovie -> {
//                    val bundle = Bundle()
//                    bundle.putInt("id", item.id)
//                    bundle.putString("type", "movie")
//                    bundle.putString("title", item.title)
//                    bundle.putString("poster_path", item.posterPath)
//                    bundle.putString("backdrop_path", item.backdropPath)
//
//                    val intent = Intent(activity, DetailsActivity::class.java)
//                    intent.putExtra(AppConstants.ITEM_BUNDLE, bundle)
//                    activity?.startActivity(intent)
//                }
//                is TmdbTvSeries -> {
//                    val bundle = Bundle()
//                    bundle.putInt("id", item.id)
//                    bundle.putString("type", "tv")
//                    bundle.putString("title", item.name)
//                    bundle.putString("poster_path", item.posterPath)
//                    bundle.putString("backdrop_path", item.backdropPath)
//
//                    val intent = Intent(activity, DetailsActivity::class.java)
//                    intent.putExtra(AppConstants.ITEM_BUNDLE, bundle)
//                    activity?.startActivity(intent)
//                }
//            }
//
//            // পুরানো পদ্ধতি comment করে দিন
//            // DetailsActivity().start(activity, ObjectSerializable(item), imageView)
//        }
////        onItemViewClickedListener = OnItemViewClickedListener { itemViewHolder, item, rowViewHolder, row ->
////            val imageView = try {
////                when (val view = itemViewHolder.view) {
////                    is CustomImageCardView -> view.getImageView()
////                    is ImageCardView -> view.mainImageView
////                    else -> {
////                        var result: ImageView? = null
////                        fun searchForImageView(v: View) {
////                            if (result != null) return
////                            when (v) {
////                                is ImageView -> result = v
////                                is ViewGroup -> {
////                                    for (i in 0 until v.childCount) {
////                                        searchForImageView(v.getChildAt(i))
////                                    }
////                                }
////                            }
////                        }
////                        searchForImageView(view)
////                        result ?: ImageView(view.context).apply {
////                            layoutParams = ViewGroup.LayoutParams(1, 1)
////                        }
////                    }
////                }
////            } catch (e: Exception) {
////                Log.e("MainRowFragment", "Error getting ImageView", e)
////                ImageView(itemViewHolder.view.context).apply {
////                    layoutParams = ViewGroup.LayoutParams(1, 1)
////                }
////            }
////
////            DetailsActivity().start(activity, ObjectSerializable(item), imageView)
////        }
//
//        // Set selection listener
//        onItemViewSelectedListener = OnItemViewSelectedListener { itemViewHolder, item, rowViewHolder, row ->
//            val curPos = if (selectedPosition < arrayAdapter.size) {
//                arrayAdapter[selectedPosition].list.indexOf(item)
//            } else 0
//
//            if (selectedPosition < arrayAdapter.size &&
//                (curPos == arrayAdapter[selectedPosition].list.size() - 7 ||
//                        curPos == arrayAdapter[selectedPosition].list.size() - 2)) {
//                loadMoreItems(arrayAdapter[selectedPosition])
//            }
//
//            when (item) {
//                is TmdbMovie -> {
//                    updateMoviePresenter(presenter, item)
//                }
//                is TmdbTvSeries -> {
//                    updateTvPresenter(presenter, item)
//                }
//            }
//        }
//
//        adapter = mRowsAdapter
//
//        // Initialize strings
//        array = mContext.resources.getStringArray(R.array.category_title)
//        nowPlaying = category + AppConstants.DOT_DELIMETERSPACE + mContext.getString(R.string.now_playing)
//        upcoming = category + AppConstants.DOT_DELIMETERSPACE + mContext.getString(R.string.upcoming)
//        popular = category + AppConstants.DOT_DELIMETERSPACE + mContext.getString(R.string.popular)
//        topRated = category + AppConstants.DOT_DELIMETERSPACE + mContext.getString(R.string.top_rated)
//        airingToday = category + AppConstants.DOT_DELIMETERSPACE + mContext.getString(R.string.airing_today)
//        onTv = category + AppConstants.DOT_DELIMETERSPACE + mContext.getString(R.string.on_tv)
//
//        mPb = activity?.findViewById(R.id.pb)
//        mPb?.visibility = View.VISIBLE
//
//        loadData()
//    }
    init {
        val presenter = CustomListRowPresenter()
        mRowsAdapter = ArrayObjectAdapter(presenter)
        arrayAdapter = ArrayList()

        // Set click listener
        onItemViewClickedListener = OnItemViewClickedListener { itemViewHolder, item, rowViewHolder, row ->
            val imageView = try {
                when (val view = itemViewHolder.view) {
                    is CustomImageCardView -> view.getImageView()
                    is ImageCardView -> view.mainImageView
                    else -> {
                        var result: ImageView? = null
                        fun searchForImageView(v: View) {
                            if (result != null) return
                            when (v) {
                                is ImageView -> result = v
                                is ViewGroup -> {
                                    for (i in 0 until v.childCount) {
                                        searchForImageView(v.getChildAt(i))
                                    }
                                }
                            }
                        }
                        searchForImageView(view)
                        result ?: ImageView(view.context).apply {
                            layoutParams = ViewGroup.LayoutParams(1, 1)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("MainRowFragment", "Error getting ImageView", e)
                ImageView(itemViewHolder.view.context).apply {
                    layoutParams = ViewGroup.LayoutParams(1, 1)
                }
            }

            // নতুন কোড - ID এবং type পাঠান
            when (item) {
                is TmdbMovie -> {
                    val bundle = Bundle().apply {
                        putInt("id", item.id)
                        putString("type", "movie")
                        putString("title", item.title)
                        putString("poster_path", item.posterPath)
                        putString("backdrop_path", item.backdropPath)
                    }
                    val intent = Intent(activity, DetailsActivity::class.java)
                    intent.putExtra(AppConstants.ITEM_BUNDLE, bundle)
                    activity?.startActivity(intent)
                }
                is TmdbTvSeries -> {
                    val bundle = Bundle().apply {
                        putInt("id", item.id)
                        putString("type", "tv")
                        putString("title", item.name)
                        putString("poster_path", item.posterPath)
                        putString("backdrop_path", item.backdropPath)
                    }
                    val intent = Intent(activity, DetailsActivity::class.java)
                    intent.putExtra(AppConstants.ITEM_BUNDLE, bundle)
                    activity?.startActivity(intent)
                }
            }
        }

        // Set selection listener
        onItemViewSelectedListener = OnItemViewSelectedListener { itemViewHolder, item, rowViewHolder, row ->
            val curPos = if (selectedPosition < arrayAdapter.size) {
                arrayAdapter[selectedPosition].list.indexOf(item)
            } else 0

            if (selectedPosition < arrayAdapter.size &&
                (curPos == arrayAdapter[selectedPosition].list.size() - 7 ||
                        curPos == arrayAdapter[selectedPosition].list.size() - 2)) {
                loadMoreItems(arrayAdapter[selectedPosition])
            }

            when (item) {
                is TmdbMovie -> {
                    updateMoviePresenter(presenter, item)
                }
                is TmdbTvSeries -> {
                    updateTvPresenter(presenter, item)
                }
            }
        }

        adapter = mRowsAdapter

        // Initialize strings
        array = mContext.resources.getStringArray(R.array.category_title)
        nowPlaying = category + AppConstants.DOT_DELIMETERSPACE + mContext.getString(R.string.now_playing)
        upcoming = category + AppConstants.DOT_DELIMETERSPACE + mContext.getString(R.string.upcoming)
        popular = category + AppConstants.DOT_DELIMETERSPACE + mContext.getString(R.string.popular)
        topRated = category + AppConstants.DOT_DELIMETERSPACE + mContext.getString(R.string.top_rated)
        airingToday = category + AppConstants.DOT_DELIMETERSPACE + mContext.getString(R.string.airing_today)
        onTv = category + AppConstants.DOT_DELIMETERSPACE + mContext.getString(R.string.on_tv)

        mPb = activity?.findViewById(R.id.pb)
        mPb?.visibility = View.VISIBLE

        loadData()
    }
    private fun loadData() {
        when (category.lowercase(getDefault())) {
            array[0].lowercase(getDefault()) -> {
                // Movies
                loadMovies()
            }
            array[1].lowercase(getDefault()) -> {
                // TV Series
                loadTvSeries()
            }
            array[2].lowercase(getDefault()) -> {
                // People - handle if needed
                mPb?.visibility = View.GONE
            }
            array[3].lowercase(getDefault()) -> {
                // TV Channels - handle if needed
                mPb?.visibility = View.GONE
            }
            else -> {
                mPb?.visibility = View.GONE
            }
        }
    }

    private fun loadMovies() {
        val presenter = CardPresenter()
        val loadingStates = Array(4) { false }

        // Now Playing
        tmdbRepository.getNowPlayingMovies(
            onSuccess = { movies ->
                activity?.runOnUiThread {
                    val listMovieNowPlaying = RowObjectAdapter()
                    listMovieNowPlaying.list = ArrayObjectAdapter(presenter)
                    listMovieNowPlaying.page = 1
                    listMovieNowPlaying.type = AppConstants.TypeRows.MOVIE_NOW
                    listMovieNowPlaying.header = nowPlaying
                    movies.forEach { movie ->
                        if (!TextUtils.isEmpty(movie.title)) {
                            listMovieNowPlaying.list.add(movie)
                        }
                    }
                    arrayAdapter.add(listMovieNowPlaying)
                    loadingStates[0] = true
                    checkAllMoviesLoaded(loadingStates)
                }
            },
            onError = { error ->
                activity?.runOnUiThread {
                    Log.e("MainRowFragment", "Error loading now playing: $error")
                    loadingStates[0] = true
                    checkAllMoviesLoaded(loadingStates)
                }
            }
        )

        // Upcoming
        tmdbRepository.getUpcomingMovies(
            onSuccess = { movies ->
                activity?.runOnUiThread {
                    val listMovieUpcoming = RowObjectAdapter()
                    listMovieUpcoming.list = ArrayObjectAdapter(presenter)
                    listMovieUpcoming.page = 1
                    listMovieUpcoming.type = AppConstants.TypeRows.MOVIE_UPCOMING
                    listMovieUpcoming.header = upcoming
                    movies.forEach { movie ->
                        if (!TextUtils.isEmpty(movie.title)) {
                            listMovieUpcoming.list.add(movie)
                        }
                    }
                    arrayAdapter.add(listMovieUpcoming)
                    loadingStates[1] = true
                    checkAllMoviesLoaded(loadingStates)
                }
            },
            onError = { error ->
                activity?.runOnUiThread {
                    Log.e("MainRowFragment", "Error loading upcoming: $error")
                    loadingStates[1] = true
                    checkAllMoviesLoaded(loadingStates)
                }
            }
        )

        // Popular
        tmdbRepository.getPopularMovies(
            onSuccess = { movies ->
                activity?.runOnUiThread {
                    val listMoviePopular = RowObjectAdapter()
                    listMoviePopular.list = ArrayObjectAdapter(presenter)
                    listMoviePopular.page = 1
                    listMoviePopular.type = AppConstants.TypeRows.MOVIE_POPULAR
                    listMoviePopular.header = popular
                    movies.forEach { movie ->
                        if (!TextUtils.isEmpty(movie.title)) {
                            listMoviePopular.list.add(movie)
                        }
                    }
                    arrayAdapter.add(listMoviePopular)
                    loadingStates[2] = true
                    checkAllMoviesLoaded(loadingStates)
                }
            },
            onError = { error ->
                activity?.runOnUiThread {
                    Log.e("MainRowFragment", "Error loading popular: $error")
                    loadingStates[2] = true
                    checkAllMoviesLoaded(loadingStates)
                }
            }
        )

        // Top Rated
        tmdbRepository.getTopRatedMovies(
            onSuccess = { movies ->
                activity?.runOnUiThread {
                    val listMovieTopRated = RowObjectAdapter()
                    listMovieTopRated.list = ArrayObjectAdapter(presenter)
                    listMovieTopRated.page = 1
                    listMovieTopRated.type = AppConstants.TypeRows.MOVIE_TOP
                    listMovieTopRated.header = topRated
                    movies.forEach { movie ->
                        if (!TextUtils.isEmpty(movie.title)) {
                            listMovieTopRated.list.add(movie)
                        }
                    }
                    arrayAdapter.add(listMovieTopRated)
                    loadingStates[3] = true
                    checkAllMoviesLoaded(loadingStates)
                }
            },
            onError = { error ->
                activity?.runOnUiThread {
                    Log.e("MainRowFragment", "Error loading top rated: $error")
                    loadingStates[3] = true
                    checkAllMoviesLoaded(loadingStates)
                }
            }
        )
    }

    private fun loadTvSeries() {
        val presenter = CardPresenter()
        val loadingStates = Array(4) { false }

        // Airing Today
        tmdbRepository.getAiringToday(
            onSuccess = { series ->
                activity?.runOnUiThread {
                    val listToday = RowObjectAdapter()
                    listToday.list = ArrayObjectAdapter(presenter)
                    listToday.page = 1
                    listToday.type = AppConstants.TypeRows.SERIAL_TODAY
                    listToday.header = airingToday
                    series.forEach { tv ->
                        if (!TextUtils.isEmpty(tv.name)) {
                            listToday.list.add(tv)
                        }
                    }
                    arrayAdapter.add(listToday)
                    loadingStates[0] = true
                    checkAllTvLoaded(loadingStates)
                }
            },
            onError = { error ->
                activity?.runOnUiThread {
                    Log.e("MainRowFragment", "Error loading airing today: $error")
                    loadingStates[0] = true
                    checkAllTvLoaded(loadingStates)
                }
            }
        )

        // On TV
        tmdbRepository.getOnTheAir(
            onSuccess = { series ->
                activity?.runOnUiThread {
                    val listTv = RowObjectAdapter()
                    listTv.list = ArrayObjectAdapter(presenter)
                    listTv.page = 1
                    listTv.type = AppConstants.TypeRows.SERIAL_ONTV
                    listTv.header = onTv
                    series.forEach { tv ->
                        if (!TextUtils.isEmpty(tv.name)) {
                            listTv.list.add(tv)
                        }
                    }
                    arrayAdapter.add(listTv)
                    loadingStates[1] = true
                    checkAllTvLoaded(loadingStates)
                }
            },
            onError = { error ->
                activity?.runOnUiThread {
                    Log.e("MainRowFragment", "Error loading on tv: $error")
                    loadingStates[1] = true
                    checkAllTvLoaded(loadingStates)
                }
            }
        )

        // Popular
        tmdbRepository.getPopularTv(
            onSuccess = { series ->
                activity?.runOnUiThread {
                    val listPopular = RowObjectAdapter()
                    listPopular.list = ArrayObjectAdapter(presenter)
                    listPopular.page = 1
                    listPopular.type = AppConstants.TypeRows.SERIAL_POPULAR
                    listPopular.header = popular
                    series.forEach { tv ->
                        if (!TextUtils.isEmpty(tv.name)) {
                            listPopular.list.add(tv)
                        }
                    }
                    arrayAdapter.add(listPopular)
                    loadingStates[2] = true
                    checkAllTvLoaded(loadingStates)
                }
            },
            onError = { error ->
                activity?.runOnUiThread {
                    Log.e("MainRowFragment", "Error loading popular tv: $error")
                    loadingStates[2] = true
                    checkAllTvLoaded(loadingStates)
                }
            }
        )

        // Top Rated
        tmdbRepository.getTopRatedTv(
            onSuccess = { series ->
                activity?.runOnUiThread {
                    val listTopRated = RowObjectAdapter()
                    listTopRated.list = ArrayObjectAdapter(presenter)
                    listTopRated.page = 1
                    listTopRated.type = AppConstants.TypeRows.SERIAL_TOP
                    listTopRated.header = topRated
                    series.forEach { tv ->
                        if (!TextUtils.isEmpty(tv.name)) {
                            listTopRated.list.add(tv)
                        }
                    }
                    arrayAdapter.add(listTopRated)
                    loadingStates[3] = true
                    checkAllTvLoaded(loadingStates)
                }
            },
            onError = { error ->
                activity?.runOnUiThread {
                    Log.e("MainRowFragment", "Error loading top rated tv: $error")
                    loadingStates[3] = true
                    checkAllTvLoaded(loadingStates)
                }
            }
        )
    }

    private fun checkAllMoviesLoaded(loadingStates: Array<Boolean>) {
        if (loadingStates.all { it }) {
            activity?.runOnUiThread {
                Handler().postDelayed({
                    updateAdapter()
                }, 500)
            }
        }
    }

    private fun checkAllTvLoaded(loadingStates: Array<Boolean>) {
        if (loadingStates.all { it }) {
            activity?.runOnUiThread {
                Handler().postDelayed({
                    updateAdapter()
                }, 500)
            }
        }
    }

    private fun updateAdapter() {
        mPb?.visibility = View.GONE
        mRowsAdapter.clear()
        for (obj in arrayAdapter) {
            mRowsAdapter.add(
                ListRow(
                    HeaderItem(obj.header),
                    obj.list
                )
            )
        }
    }

    private fun loadMoreItems(rowObject: RowObjectAdapter) {
        // Implement load more functionality
    }

    private fun updateMoviePresenter(presenter: CustomListRowPresenter, movie: TmdbMovie) {
        var title = movie.title
        if (movie.title != movie.originalTitle)
            title = UtilsText().delimeterStrings(
                AppConstants.DOT_DELIMETERSPACE,
                movie.title,
                movie.originalTitle)

        presenter.setDesc(
            title,
            SpannableString(
                UtilsText().delimeterStrings(
                    AppConstants.COMMA,
                    UtilsText().safesplit(movie.releaseDate, "-").first(),
                    AppConstants.ELLIPSIS
                )
            )
        )
    }

    private fun updateTvPresenter(presenter: CustomListRowPresenter, series: TmdbTvSeries) {
        var title = series.name
        if (series.name != series.originalName)
            title = UtilsText().delimeterStrings(
                AppConstants.DOT_DELIMETERSPACE,
                series.name,
                series.originalName)

        presenter.setDesc(
            title,
            SpannableString(UtilsText().safesplit(series.firstAirDate, "-").first()+" ${AppConstants.ELLIPSIS}")
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        selectedPosition = 0
    }
}