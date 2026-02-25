package  com.bacbpl.iptv.jetfit.ui.fragments

//import android.annotation.SuppressLint
//import android.content.Context
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.graphics.Bitmap
//import android.graphics.Color
//import android.graphics.drawable.BitmapDrawable
//import android.graphics.drawable.Drawable
//import android.net.Uri
//import android.os.Bundle
//import android.os.Handler
//import android.text.TextUtils
//import android.util.Log
//import android.util.SparseArray
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.*
//import androidx.leanback.app.DetailsSupportFragment
//import androidx.leanback.app.DetailsSupportFragmentBackgroundController
//import androidx.leanback.widget.*
//import com.bumptech.glide.Glide
//import com.bumptech.glide.request.target.CustomTarget
//import com.bumptech.glide.request.transition.Transition
//import info.movito.themoviedbapi.model.MovieDb
//import  com.bacbpl.iptv.jetfit.AppConstants
//
//import androidx.leanback.widget.Presenter
//import androidx.leanback.widget.DetailsOverviewLogoPresenter
//import at.huber.youtubeExtractor.VideoMeta
//import at.huber.youtubeExtractor.YouTubeExtractor
//import at.huber.youtubeExtractor.YtFile
//import com.bacbpl.iptv.R
//import  com.bacbpl.iptv.jetfit.models.ObjectSerializable
//import com.bacbpl.iptv.jetfit.models.tmdb.TmdbMovie
//import com.bacbpl.iptv.jetfit.models.tmdb.TmdbTvSeries
//import com.bacbpl.iptv.jetfit.ui.activities.DetailsActivity
//import info.movito.themoviedbapi.TmdbApi
//import info.movito.themoviedbapi.TmdbMovies
//import info.movito.themoviedbapi.TmdbTV
//import info.movito.themoviedbapi.TmdbTvSeasons
//import info.movito.themoviedbapi.model.tv.TvSeries
//import  com.bacbpl.iptv.jetfit.ui.presenters.CustomActionPresenterSelector
//import  com.bacbpl.iptv.jetfit.ui.presenters.FullWidthOverviewPresenter
//import  com.bacbpl.iptv.jetfit.utils.CompletedListener
//import com.bacbpl.iptv.jetfit.utils.DoAsync
//import  com.bacbpl.iptv.jetfit.utils.UtilsView

//
//class DetailsFragment : DetailsSupportFragment() {
//    private var mRowsAdapter: ArrayObjectAdapter? = null
//    lateinit var overviewRowPresenter: FullWidthOverviewPresenter
//    lateinit var overviewRow: DetailsOverviewRow
//    private var mLogoView: ImageView? = null
//    private var tmdb: TmdbApi? = null
//    private var obj: Any? = null
//    private var lastSelectedItem: Any? = null
//
//    private var trailer = ""
//
//    var canUpToTrailer = false
//
//    private val ACTION_IMDB = 1
//    private val ACTION_TVDB = 2
//    private val ACTION_WEB = 3
//    private lateinit var detailsBackground: DetailsSupportFragmentBackgroundController;
//    private val tmdbRepository = com.bacbpl.iptv.jetfit.repository.TmdbRepository()
//
//    // Your existing variables...
//    var loadTrailer = false
//    private var trailerUrl = ""
//    fun newInstance(card: ObjectSerializable): DetailsFragment {
//        val fragment = DetailsFragment()
//        val args = Bundle()
//        args.putSerializable(AppConstants.ITEM, card)
//        fragment.arguments = args
//        return fragment
//    }
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        val ps = ClassPresenterSelector()
//        overviewRowPresenter = FullWidthOverviewPresenter(DetailsDescriptionPresenter(),
//            object : DetailsOverviewLogoPresenter() {
//                override fun onBindViewHolder(viewHolder: Presenter.ViewHolder, item: Any) {
//                    super.onBindViewHolder(viewHolder, item)
//                    mLogoView = viewHolder.view as ImageView
//                    mLogoView?.elevation = 14f
//                    mLogoView?.setBackgroundColor(Color.BLACK)
//                    mLogoView?.transitionName = AppConstants.TRANSITION_POSTER
//                }
//
//            })
//
//        // Your existing setup code...
//
//        // When you need to play a trailer
//        detailsBackground = DetailsSupportFragmentBackgroundController(this)
//
//        overviewRowPresenter.onActionClickedListener =
//            OnActionClickedListener { action ->
//                Toast.makeText(activity, trailer, Toast.LENGTH_SHORT).show()
////                (activity as DetailsActivity).youtubeFragment.mYouTubePlayer
////                    ?.loadVideo("yy96yJjkvjo", 0f)
////                val youtubeLink = "http://youtube.com/watch?v=yy96yJjkvjo"
////                if (action.id == ACTION_BUY.toLong()) {
////                    // on the UI thread, we can modify actions adapter directly
////                    val actions = overviewRow.actionsAdapter as SparseArrayObjectAdapter
////                    actions.clear(ACTION_BUY)
////                }
////                else if (action.id == ACTION_RENT.toLong()) {
////                    view?.findViewById<FrameLayout>(R.id.details_fragment_root)?.visibility= View.GONE
////                }
//
////                if (loadTrailer && activity is DetailsActivity) {
////                    (activity as DetailsActivity).videoFragment.pause(true)
////                }
//
//                // KEEP YOUR EXISTING CODE
////                val intent = Intent(Intent.ACTION_VIEW)
////                intent.data = Uri.parse(trailer)
////
////                //  DO NOT REMOVE, JUST WRAP WITH TRY-CATCH
////                try {
////                    intent.setPackage("com.google.android.youtube.tv")
////                    startActivity(intent)
////                } catch (e: Exception) {
////                    // fallback if YouTube TV not available
////                    startActivity(
////                        Intent(Intent.ACTION_VIEW, Uri.parse(trailer))
////                    )
////                }
//                playTrailer(trailer)
////                if (loadTrailer && !trailer.isNullOrEmpty()) {
////                    playTrailer(trailer)
////                }
//            }
//        ps.addClassPresenter(DetailsOverviewRow::class.java, overviewRowPresenter)
//        ps.addClassPresenter(ListRow::class.java, ListRowPresenter())
//
//        mRowsAdapter = ArrayObjectAdapter(ps)
//        adapter = mRowsAdapter
//
//        (activity as DetailsActivity).bgScroll.visibility = View.VISIBLE
//        (activity as DetailsActivity).bgScroll.isFocusable = false
//        (activity as DetailsActivity).bgScroll.isFocusableInTouchMode = false
//        (activity as DetailsActivity).bgScroll.clearFocus()
//
//        UtilsView().animationBlink((activity as DetailsActivity).drawerBottomArrow)
//        UtilsView().animationBlink((activity as DetailsActivity).drawerTopText)
//        UtilsView().animationBlink((activity as DetailsActivity).drawerTopArrow)
//        (activity as DetailsActivity).drawerTop.translationY = -(activity as DetailsActivity).resources.getDimensionPixelOffset(R.dimen.lb_details_v2_logo_margin_top).toFloat()
//
//        Handler().postDelayed({
//            (activity as DetailsActivity).drawerTopText.visibility = View.VISIBLE
//            (activity as DetailsActivity).drawerBottomArrow.visibility = View.VISIBLE
//            (activity as DetailsActivity).drawerBottom.visibility = View.VISIBLE
//            UtilsView().showBottomDrawer((activity as DetailsActivity).drawerBottomArrow,500)
//            UtilsView().showBottomDrawer((activity as DetailsActivity).drawerBottom,500)
//        }, 800)
//
//        setOnItemViewSelectedListener { itemViewHolder, item, rowViewHolder, row ->
//            if (overviewRowPresenter.initialState == FullWidthDetailsOverviewRowPresenter.STATE_HALF &&
//                !(activity as DetailsActivity).detailsHiden) {
//                if (loadTrailer) {
//                    canUpToTrailer = false
//                    (activity as DetailsActivity).videoFragment.pause(false)
//                    UtilsView().hideTopDrawer((activity as DetailsActivity).drawerTop,300)
//                }
//                (activity as DetailsActivity).bgScroll.smoothScrollTo(0, 540)
//                (activity as DetailsActivity).dpadController?.enableDown(mRowsAdapter!!.size() > 1)
//                (activity as DetailsActivity).dpadController?.enableRight(false)
//                (activity as DetailsActivity).dpadController?.enableLeft(false)
//                UtilsView().animScaleY((activity as DetailsActivity).drawerBottomArrow, mRowsAdapter!!.size() > 1)
//            } else if (overviewRowPresenter.initialState == FullWidthDetailsOverviewRowPresenter.STATE_FULL &&
//                !(activity as DetailsActivity).detailsHiden) {
//                if (loadTrailer) {
//                    canUpToTrailer = true
//                    (activity as DetailsActivity).videoFragment.play(false)
//                    UtilsView().showTopDrawer((activity as DetailsActivity).drawerTop,300)
//                }
//                (activity as DetailsActivity).bgScroll.smoothScrollTo(0, 0)
//                (activity as DetailsActivity).dpadController?.enableDown(true)
//                if (lastSelectedItem != null && lastSelectedItem is Action) {
//                    dpadOnAction(lastSelectedItem!! as Action)
//                }
//                UtilsView().animScaleY((activity as DetailsActivity).drawerBottomArrow, true)
//            }
//            if (item is Action) {
//                if (loadTrailer) {
//                    canUpToTrailer = true
//                    (activity as DetailsActivity).videoFragment.play(false)
//                }
//                lastSelectedItem = item
//                dpadOnAction(item)
//            }
//        }
//
//        val item = arguments?.getSerializable(AppConstants.ITEM) as ObjectSerializable
//
//        overviewRow = DetailsOverviewRow(item.obj)
//        val adapter = SparseArrayObjectAdapter()
//        adapter.presenterSelector = CustomActionPresenterSelector()
//        overviewRow.actionsAdapter = adapter
//        mRowsAdapter!!.add(0, overviewRow)
//
//        if (item.obj != null) {
//            setItem(item.obj!!)
//            loadDeatails(item)
//        }
//    }
//    // In DetailsFragment.kt - Replace the commented loadDeatails function
//
//    private fun loadDeatails(item: ObjectSerializable) {
//        when (val obj = item.obj) {
//            is TmdbMovie -> {
//                tmdbRepository.getMovieDetails(
//                    movieId = obj.id,
//                    onSuccess = { movie ->
//                        activity?.runOnUiThread {
//                            this.obj = movie
//                            overviewRowPresenter.setItem(movie)
//                            setActions(movie)
//
//                            // Load trailer if available
//                            if (!movie.videoResults?.results.isNullOrEmpty()) {
//                                val video = movie.videoResults?.results?.firstOrNull {
//                                    it.site.equals("youtube", true)
//                                }
//                                video?.let {
//                                    trailer = "http://youtube.com/watch?v=${it.key}"
//                                    loadYoutubeUrl(trailer, false)
//                                } ?: run {
//                                    (activity as? DetailsActivity)?.progressMain?.visibility = View.GONE
//                                }
//                            } else {
//                                (activity as? DetailsActivity)?.progressMain?.visibility = View.GONE
//                            }
//                        }
//                    },
//                    onError = { error ->
//                        Log.e("DetailsFragment", "Error loading movie: $error")
//                        activity?.runOnUiThread {
//                            (activity as? DetailsActivity)?.progressMain?.visibility = View.GONE
//                        }
//                    }
//                )
//            }
//            is TmdbTvSeries -> {
//                tmdbRepository.getTvSeriesDetails(
//                    seriesId = obj.id,
//                    onSuccess = { series ->
//                        activity?.runOnUiThread {
//                            this.obj = series
//                            overviewRowPresenter.setItem(series)
//                            setActions(series)
//
//                            // Load trailer if available
//                            if (!series.videoResults?.results.isNullOrEmpty()) {
//                                val video = series.videoResults?.results?.firstOrNull {
//                                    it.site.equals("youtube", true)
//                                }
//                                video?.let {
//                                    trailer = "http://youtube.com/watch?v=${it.key}"
//                                    loadYoutubeUrl(trailer, false)
//                                } ?: run {
//                                    (activity as? DetailsActivity)?.progressMain?.visibility = View.GONE
//                                }
//                            } else {
//                                (activity as? DetailsActivity)?.progressMain?.visibility = View.GONE
//                            }
//                        }
//                    },
//                    onError = { error ->
//                        Log.e("DetailsFragment", "Error loading series: $error")
//                        activity?.runOnUiThread {
//                            (activity as? DetailsActivity)?.progressMain?.visibility = View.GONE
//                        }
//                    }
//                )
//            }
//            else -> {
//                activity?.runOnUiThread {
//                    (activity as? DetailsActivity)?.progressMain?.visibility = View.GONE
//                }
//            }
//        }
//    }
////    private fun loadDeatails(item: ObjectSerializable) {
////        when (val obj = item.obj) {
////            is TmdbMovie -> {
////                // ID দিয়ে movie details load করুন
////                val movieId = obj.id
////                tmdbRepository.getMovieDetails(
////                    movieId = movieId,
////                    onSuccess = { movie ->
////                        activity?.runOnUiThread {
////                            this.obj = movie
////                            overviewRowPresenter.setItem(movie)
////                            setActions(movie)
////                            // বাকি কোড...
////                        }
////                    },
////                    onError = { error ->
////                        Log.e("DetailsFragment", "Error loading movie: $error")
////                    }
////                )
////            }
////            is TmdbTvSeries -> {
////                // ID দিয়ে TV series details load করুন
////                val tvId = obj.id
////                // similar implementation for TV
////            }
////        }
////    }
////    private fun loadDeatails(item: ObjectSerializable) {
////        DoAsync(object : CompletedListener {
////            override fun onCompleted() {
////                if (obj != null) {
////                    overviewRowPresenter.setItem(obj!!)
////                    setActions(obj!!)
////                    if (trailer.isEmpty())
////                        (activity as DetailsActivity).progressMain.visibility = View.GONE
////                    else loadYoutubeUrl(trailer, false)
////                }
////            }
////        }) {
////            if (tmdb == null)
////                tmdb = TmdbApi(AppConstants.API_TMDB)
////            if (item.obj is MovieDb) {
////                obj = tmdb!!.movies.getMovie(
////                    (item.obj as MovieDb).id,
////                    "en-US",
////                    TmdbMovies.MovieMethod.videos
////                )
////                if ((obj as MovieDb).videos.isEmpty()) {
////                    val vid = tmdb!!.movies.getVideos((item.obj as MovieDb).id, "en-US")
////
////                    if (vid.isNotEmpty()) {
////                        for (video in vid) {
////                            if (video.site.equals("youtube", true)) {
////                                trailer = "http://youtube.com/watch?v=${video.key}"
////                                println("trailer $trailer");
////                                break
////                            }
////                        }
////                    }
////                } else if ((obj as MovieDb).videos[0].site.equals("youtube", true)) {
////                    trailer = "http://youtube.com/watch?v=${(obj as MovieDb).videos[0].key}"
////                    println("trailer $trailer");
////
////                }
////
////            } else if (item.obj is TvSeries) {
////                val c = tmdb!!.tvSeries.getSeries(
////                    (item.obj as TvSeries).id,
////                    "en",
////                    TmdbTV.TvMethod.external_ids,
////                    TmdbTV.TvMethod.content_ratings,
////                    TmdbTV.TvMethod.videos
////                )
////                val lastSeason = tmdb!!.tvSeasons.getSeason(
////                    c.id,
////                    c.seasons.last().seasonNumber,
////                    "en",
////                    TmdbTvSeasons.SeasonMethod.external_ids
////                )
////                c.seasons[c.seasons.size - 1] = lastSeason
////                if (c.videos.isEmpty()) {
////                    val vid = tmdb!!.tvSeries.getSeries(
////                        (item.obj as TvSeries).id,
////                        "en-US",
////                        TmdbTV.TvMethod.videos
////                    )
////                    if (vid.videos.isNotEmpty()) {
////                        for (video in vid.videos) {
////                            if (video.site.equals("youtube", true)) {
////                                trailer = "http://youtube.com/watch?v=${video.key}"
////                                break
////                            }
////                        }
////                    }
////                } else if (c.videos[0].site.equals("youtube", true)) {
////                    trailer = "http://youtube.com/watch?v=${c.videos[0].key}"
////                }
////                obj = c
////            }
////        }
////    }
//
//    private fun loadYoutubeUrl(youtubeLink: String, orig: Boolean) {
//        println("youtubeLink $youtubeLink")
//        val yt = @SuppressLint("StaticFieldLeak")
//        object : YouTubeExtractor(requireContext()) {
//            override fun onExtractionComplete(
//                ytFiles: SparseArray<YtFile>?,
//                videoMeta: VideoMeta?
//            ) {
//                val s = when {
//                    ytFiles?.get(22)?.url != null -> ytFiles.get(22)?.url
//                    ytFiles?.get(135)?.url != null -> ytFiles.get(135)?.url
//                    ytFiles?.get(18)?.url != null -> ytFiles.get(18)?.url
//                    else -> null
//                }
//
//                activity?.runOnUiThread {
//                    if (s != null) {
//                        try {
//                            (activity as? DetailsActivity)?.videoFragment?.playYoutube(youtubeLink)
//                            (activity as? DetailsActivity)?.drawerTop?.visibility = View.VISIBLE
//                            (activity as? DetailsActivity)?.progressMain?.visibility = View.GONE
//                            (activity as? DetailsActivity)?.dpadController?.enableUp(true)
//
//                            Handler().postDelayed({
//                                if (!(activity as? DetailsActivity)?.detailsFragment?.isHidden!! &&
//                                    overviewRowPresenter.initialState == FullWidthDetailsOverviewRowPresenter.STATE_FULL) {
//                                    UtilsView().showTopDrawer((activity as DetailsActivity).drawerTop, 300)
//                                }
//                            }, 600)
//                            loadTrailer = true
//                        } catch (e: Exception) {
//                            Log.e("loadYoutubeUrl", "Error playing video", e)
//                        }
//                    } else if (!orig) {
//                        loadTrailerOrig()
//                    } else {
//                        (activity as? DetailsActivity)?.progressMain?.visibility = View.GONE
//                        (activity as? DetailsActivity)?.dpadController?.enableUp(false)
//                        Log.e("loadYoutubeUrl", "null result from $youtubeLink")
//                    }
//                }
//            }
//        }
//        yt.extract(youtubeLink, true, true)
//    }
//
//
//    private fun loadTrailerOrig() {
//        DoAsync(object : CompletedListener {
//            override fun onCompleted() {
//                loadYoutubeUrl(trailer, true)
//            }
//        }) {
//            if (tmdb == null)
//                tmdb = TmdbApi(AppConstants.API_TMDB)
//            if (obj is MovieDb) {
//                val vid = tmdb!!.movies.getVideos((obj as MovieDb).id, "en-US")
//                if (vid.isNotEmpty()) {
//                    for (video in vid) {
//                        if (video.site.equals("youtube", true)){
//                            trailer = "http://youtube.com/watch?v=${video.key}"
//                            break
//                        }
//                    }
//                }
//            } else if (obj is TvSeries) {
//                val vid = tmdb!!.tvSeries.getSeries((obj as TvSeries).id, "en-US", TmdbTV.TvMethod.videos)
//                if (vid.videos.isNotEmpty()) {
//                    for (video in vid.videos) {
//                        if (video.site.equals("youtube", true)){
//                            trailer = "http://youtube.com/watch?v=${video.key}"
//                            break
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    private fun dpadOnAction(item: Action) {
//        for (i in 0 until overviewRow.actionsAdapter.size()) {
//            if (item.label1 == (overviewRow.actionsAdapter[i] as Action).label1) {
//                (activity as DetailsActivity).dpadController?.enableRight(i < overviewRow.actionsAdapter.size() - 1)
//                (activity as DetailsActivity).dpadController?.enableLeft(i > 0)
//                break
//            }
//        }
//    }
//
//    private fun setActions(obj: Any) {
//        if (obj is MovieDb) {
//            if (!TextUtils.isEmpty(obj.homepage)) {
//                val actions = overviewRow.actionsAdapter as SparseArrayObjectAdapter
//                actions.set(
//                    ACTION_WEB, Action(
//                        ACTION_WEB.toLong(), "HOMEPAGE"
//                    )
//                )
//            }
//            if (!TextUtils.isEmpty(obj.imdbID)) {
//                val actions = overviewRow.actionsAdapter as SparseArrayObjectAdapter
//                actions.set(
//                    ACTION_IMDB, Action(
//                        ACTION_IMDB.toLong(), "IMDB"
//                    )
//                )
//            }
//        } else if (obj is TvSeries) {
//            if (!TextUtils.isEmpty(obj.homepage)) {
//                val actions = overviewRow.actionsAdapter as SparseArrayObjectAdapter
//                actions.set(
//                    ACTION_WEB, Action(
//                        ACTION_WEB.toLong(), "HOMEPAGE"
//                    )
//                )
//            }
//            if (obj.externalIds != null) {
//                if (!TextUtils.isEmpty(obj.externalIds.imdbId)) {
//                    val actions = overviewRow.actionsAdapter as SparseArrayObjectAdapter
//                    actions.set(
//                        ACTION_IMDB, Action(
//                            ACTION_IMDB.toLong(), "IMDB"
//                        )
//                    )
//                }
//                if (!TextUtils.isEmpty(obj.externalIds.tvdbId)) {
//                    val actions = overviewRow.actionsAdapter as SparseArrayObjectAdapter
//                    actions.set(
//                        ACTION_TVDB, Action(
//                            ACTION_TVDB.toLong(), "TVDB"
//                        )
//                    )
//                }
//            }
//        }
//        requestActions()
//    }
//
//    fun requestActions() {
//        canUpToTrailer = true
//        overviewRowPresenter.actionsView.requestFocus()
//    }
//
//    private fun setItem(item: Any) {
//        if (item is MovieDb) {
//            loadPoster("https://image.tmdb.org/t/p/original" + item.posterPath)
//            (activity as DetailsActivity).bgContainerView.visibility = View.VISIBLE
//            loadBackPoster("https://image.tmdb.org/t/p/original" + item.backdropPath)
//        } else if (item is TvSeries) {
//            loadPoster("https://image.tmdb.org/t/p/original" + item.posterPath)
//            (activity as DetailsActivity).bgContainerView.visibility = View.VISIBLE
//            loadBackPoster("https://image.tmdb.org/t/p/original" + item.backdropPath)
//        }
//    }
//
//    fun hideInterface() {
//        titleView?.animate()?.translationY(-titleView.height.toFloat())?.setDuration(400)?.start()
//        val frame = activity?.findViewById<FrameLayout>(R.id.details_fragment_root)
//        frame?.animate()?.translationY(400f)?.setDuration(400)?.start()
//        frame?.animate()?.alpha(0f)?.setDuration(400)?.start()
//    }
//
//    fun showInterface() {
//        titleView?.animate()?.translationY(0f)?.setDuration(400)?.start()
//        val frame = activity?.findViewById<FrameLayout>(R.id.details_fragment_root)
//        frame?.animate()?.translationY(0f)?.setDuration(400)?.start()
//        frame?.animate()?.alpha(1f)?.setDuration(400)?.start()
//    }
//
//    private fun loadPoster(posterPathFull: String?) {
//        val thumb = posterPathFull?.replace("/original/","/w185_and_h278_bestv2/")
//        val thumbnailRequest = Glide.with(this).asBitmap().load(thumb)
//        Glide.with(this)
//            .asBitmap()
//            .thumbnail(thumbnailRequest)
//            .load(posterPathFull)
//            .override(300, 450)
//            .centerCrop()
//            .into(object : CustomTarget<Bitmap>(){
//                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
//                    overviewRow.imageDrawable = BitmapDrawable(resources, resource)
//                    activity?.startPostponedEnterTransition()
//                }
//                override fun onLoadCleared(placeholder: Drawable?) {
//                }
//            })
//    }
//
//    private fun loadBackPoster(backdropPath: String?) {
//        val thumb = backdropPath?.replace("/original/","/w500_and_h282_face/")
//        val thumbnailRequest = Glide.with(this).asBitmap().load(thumb)
//        Glide.with(this)
//            .asBitmap()
//            .load(backdropPath)
//            .thumbnail(thumbnailRequest)
//            .fitCenter()
//            .into((activity as DetailsActivity).bgView)
//    }
//
//    inner class DetailsDescriptionPresenter : Presenter() {
//        override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
//            val v = LayoutInflater.from(parent.context)
//                .inflate(R.layout.lb_details_description, parent, false)
//            return ViewHolder(v)
//        }
//
//        override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
//            overviewRowPresenter.setItem(item)
//        }
//
//        override fun onUnbindViewHolder(viewHolder: ViewHolder) {
//
//        }
//    }
//    // Function to handle trailer playback
//    private fun playTrailer(trailer: String) {
//        // Pause any existing video if in DetailsActivity
//        Log.e("Trailer", trailer)
//
//        try {
//            if (activity is DetailsActivity) {
//                (activity as DetailsActivity).videoFragment.pause(true)
//            }
//        } catch (e: Exception) {
//            Log.e("Trailer", "Failed to pause video fragment", e)
//        }
//
//        // Launch YouTube with comprehensive fallbacks
//        launchYouTubeTrailer(requireContext(), trailer)
//    }
//
//    // Comprehensive YouTube trailer launcher
//    private fun launchYouTubeTrailer(context: Context, trailerUrl: String) {
//        Log.d("YouTubeTrailer", "Attempting to play: $trailerUrl")
//
//        // Normalize the URL first
//        val normalizedUrl = normalizeYouTubeUrl(trailerUrl)
//        Log.d("YouTubeTrailer", "Normalized URL: $normalizedUrl")
//
//        // Try multiple YouTube packages in order of preference
//        val success = tryYouTubePackages(context, normalizedUrl)
//
//        if (!success) {
//            // Try all package combinations with different intent setups
//            val success2 = tryAllYouTubeVariants(context, normalizedUrl)
//
//            if (!success2) {
//                // Try opening in browser
//                openInBrowser(context, normalizedUrl)
//            }
//        }
//    }
//
//    private fun normalizeYouTubeUrl(url: String): String {
//        return when {
//            // Convert short URLs
//            url.contains("youtu.be/") -> {
//                url.replace("youtu.be/", "https://www.youtube.com/watch?v=")
//            }
//            // Convert vnd.youtube:// URLs
//            url.startsWith("vnd.youtube://") -> {
//                val videoId = url.replace("vnd.youtube://", "")
//                "https://www.youtube.com/watch?v=$videoId"
//            }
//            // Convert youtube:// URLs
//            url.startsWith("youtube://") -> {
//                val videoId = url.replace("youtube://", "")
//                "https://www.youtube.com/watch?v=$videoId"
//            }
//            // Ensure proper scheme for web URLs
//            url.startsWith("www.youtube.com") -> {
//                "https://$url"
//            }
//            url.startsWith("youtube.com") -> {
//                "https://$url"
//            }
//            // Already good URL
//            else -> url
//        }
//    }
//
//    private fun tryYouTubePackages(context: Context, url: String): Boolean {
//        val youtubePackages = listOf(
//            Pair("com.google.android.youtube.tv", "YouTube TV (Android TV)"),
//            Pair("com.google.android.youtube", "YouTube Mobile"),
//            Pair("com.google.android.youtube.tvkids", "YouTube Kids TV"),
//            Pair("com.google.android.youtube.leanback", "YouTube Leanback"),
//            Pair("com.google.android.youtube.go", "YouTube Go")
//        )
//
//        for ((packageName, description) in youtubePackages) {
//            try {
//                // Check if package is installed
//                val pm = context.packageManager
//                val appInstalled = try {
//                    pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
//                    true
//                } catch (e: PackageManager.NameNotFoundException) {
//                    false
//                }
//
//                if (appInstalled) {
//                    Log.d("YouTubeTrailer", "Found: $description")
//
//                    // Try different intent approaches for this package
//                    val success = tryIntentForPackage(context, packageName, url)
//                    if (success) {
//                        Log.d("YouTubeTrailer", "Successfully opened with: $description")
//                        return true
//                    }
//                } else {
//                    Log.d("YouTubeTrailer", "$description not installed")
//                }
//            } catch (e: Exception) {
//                Log.e("YouTubeTrailer", "Error checking $description", e)
//            }
//        }
//
//        return false
//    }
//
//    private fun tryIntentForPackage(context: Context, packageName: String, url: String): Boolean {
//        // Try different intent configurations
//        val intentConfigs = listOf(
//            // Standard VIEW intent
//            {
//                Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
//                    setPackage(packageName)
//                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                }
//            },
//            // With LEANBACK launcher category (for TV)
//            {
//                Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
//                    setPackage(packageName)
//                    addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER)
//                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                }
//            },
//            // Without setting package (let system choose)
//            {
//                Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
//                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                }
//            }
//        )
//
//        for (intentBuilder in intentConfigs) {
//            try {
//                val intent = intentBuilder()
//
//                // Check if intent can be resolved
//                if (intent.resolveActivity(context.packageManager) != null) {
//                    context.startActivity(intent)
//                    return true
//                }
//            } catch (e: Exception) {
//                Log.e("YouTubeTrailer", "Intent failed for $packageName", e)
//                continue
//            }
//        }
//
//        return false
//    }
//
//    private fun tryAllYouTubeVariants(context: Context, url: String): Boolean {
//        // Try different YouTube app variants with specific activities
//        val variants = listOf(
//            Triple("com.google.android.youtube.tv", "com.google.android.apps.youtube.tv.activity.MainActivity", "YouTube TV Main"),
//            Triple("com.google.android.youtube.tv", "com.google.android.apps.youtube.tv.activity.ShellActivity", "YouTube TV Shell"),
//            Triple("com.google.android.youtube", "com.google.android.apps.youtube.app.WatchWhileActivity", "YouTube Mobile Watch"),
//            Triple("com.google.android.youtube", "com.google.android.apps.youtube.app.application.Shell\$HomeActivity", "YouTube Mobile Home")
//        )
//
//        for ((packageName, className, description) in variants) {
//            try {
//                val intent = Intent().apply {
//                    setClassName(packageName, className)
//                    data = Uri.parse(url)
//                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                }
//
//                if (intent.resolveActivity(context.packageManager) != null) {
//                    context.startActivity(intent)
//                    Log.d("YouTubeTrailer", "Opened with variant: $description")
//                    return true
//                }
//            } catch (e: Exception) {
//                Log.e("YouTubeTrailer", "Variant failed: $description", e)
//            }
//        }
//
//        return false
//    }
//
//    private fun openInBrowser(context: Context, url: String) {
//        try {
//            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
//                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            }
//
//            // Try different browsers in order
//            val browsers = listOf(
//                "com.android.chrome",          // Chrome
//                "org.mozilla.firefox",         // Firefox
//                "com.microsoft.emmx",          // Edge
//                "com.opera.browser",           // Opera
//                "com.brave.browser",           // Brave
//                "com.sec.android.app.sbrowser" // Samsung Internet
//            )
//
//            var browserOpened = false
//
//            for (browserPackage in browsers) {
//                try {
//                    browserIntent.setPackage(browserPackage)
//                    if (browserIntent.resolveActivity(context.packageManager) != null) {
//                        context.startActivity(browserIntent)
//                        browserOpened = true
//                        Log.d("YouTubeTrailer", "Opened in browser: $browserPackage")
//                        break
//                    }
//                } catch (e: Exception) {
//                    continue
//                }
//            }
//
//            // If no specific browser worked, let system choose
//            if (!browserOpened) {
//                browserIntent.setPackage(null)
//                context.startActivity(browserIntent)
//                Log.d("YouTubeTrailer", "Opened in default browser")
//            }
//
//        } catch (e: Exception) {
//            Log.e("YouTubeTrailer", "Failed to open in browser", e)
//            showErrorMessage(context)
//        }
//    }
//
//    private fun showErrorMessage(context: Context) {
//        // Show user-friendly error message
//        Toast.makeText(
//            context,
//            "Unable to play trailer. Please install YouTube app from the Play Store.",
//            Toast.LENGTH_LONG
//        ).show()
//
//        // Offer to open Play Store
//        try {
//            val playStoreIntent = Intent(Intent.ACTION_VIEW).apply {
//                data = Uri.parse("market://details?id=com.google.android.youtube.tv")
//                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            }
//            context.startActivity(playStoreIntent)
//        } catch (e: Exception) {
//            Log.e("YouTubeTrailer", "Failed to open Play Store", e)
//
//            // Last resort: open YouTube website
//            try {
//                val webIntent = Intent(
//                    Intent.ACTION_VIEW,
//                    Uri.parse("https://www.youtube.com")
//                ).apply {
//                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                }
//                context.startActivity(webIntent)
//            } catch (e2: Exception) {
//                Log.e("YouTubeTrailer", "Failed to open YouTube website", e2)
//            }
//        }
//    }
//
//    // Helper function to extract YouTube video ID from URL
//    private fun extractYouTubeVideoId(url: String): String? {
//        val patterns = listOf(
//            "v=([a-zA-Z0-9_-]{11})",                     // v=VIDEO_ID
//            "youtu\\.be/([a-zA-Z0-9_-]{11})",            // youtu.be/VIDEO_ID
//            "embed/([a-zA-Z0-9_-]{11})",                  // embed/VIDEO_ID
//            "youtube://([a-zA-Z0-9_-]{11})",             // youtube://VIDEO_ID
//            "vnd\\.youtube://([a-zA-Z0-9_-]{11})"        // vnd.youtube://VIDEO_ID
//        )
//
//        for (pattern in patterns) {
//            val regex = pattern.toRegex()
//            val match = regex.find(url)
//            if (match != null) {
//                return match.groupValues[1]
//            }
//        }
//
//        return null
//    }
//
//    // Simplified version if you want minimal changes to your existing code:
//    private fun launchYouTubeSimple(trailer: String) {
//        val intent = Intent(Intent.ACTION_VIEW)
//        intent.data = Uri.parse(trailer)
//
//        try {
//            // Try YouTube TV first
//            intent.setPackage("com.google.android.youtube.tv")
//            startActivity(intent)
//        } catch (e: Exception) {
//            // If YouTube TV fails, try without package (system will choose)
//            try {
//                intent.setPackage(null)
//                startActivity(intent)
//            } catch (e2: Exception) {
//                // If that fails, show error and open Play Store
//                Toast.makeText(
//                    requireContext(),
//                    "YouTube app not found. Opening Play Store...",
//                    Toast.LENGTH_LONG
//                ).show()
//
//                try {
//                    val playStoreIntent = Intent(
//                        Intent.ACTION_VIEW,
//                        Uri.parse("market://details?id=com.google.android.youtube.tv")
//                    )
//                    startActivity(playStoreIntent)
//                } catch (e3: Exception) {
//                    Toast.makeText(
//                        requireContext(),
//                        "Cannot open trailer. Please install YouTube app.",
//                        Toast.LENGTH_LONG
//                    ).show()
//                }
//            }
//        }
//    }
//}


import android.animation.Animator
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.util.SparseArray
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.leanback.app.DetailsSupportFragment
import androidx.leanback.app.DetailsSupportFragmentBackgroundController
import androidx.leanback.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.bacbpl.iptv.R
import com.bacbpl.iptv.jetfit.AppConstants
import com.bacbpl.iptv.jetfit.models.ObjectSerializable
import com.bacbpl.iptv.jetfit.models.tmdb.TmdbMovie
import com.bacbpl.iptv.jetfit.models.tmdb.TmdbTvSeries
import com.bacbpl.iptv.jetfit.repository.TmdbRepository
import com.bacbpl.iptv.jetfit.ui.activities.DetailsActivity
import com.bacbpl.iptv.jetfit.ui.presenters.CustomActionPresenterSelector
import com.bacbpl.iptv.jetfit.ui.presenters.FullWidthOverviewPresenter
import com.bacbpl.iptv.jetfit.utils.UtilsView

class DetailsFragment : DetailsSupportFragment() {
    private var mRowsAdapter: ArrayObjectAdapter? = null
    lateinit var overviewRowPresenter: FullWidthOverviewPresenter
    lateinit var overviewRow: DetailsOverviewRow
    private var mLogoView: ImageView? = null
    private var tmdbRepository = TmdbRepository()
    private var obj: Any? = null
    private var lastSelectedItem: Any? = null

    private var trailer = ""

    var canUpToTrailer = false

    private val ACTION_IMDB = 1
    private val ACTION_TVDB = 2
    private val ACTION_WEB = 3
    private lateinit var detailsBackground: DetailsSupportFragmentBackgroundController

    var loadTrailer = false
    private var trailerUrl = ""

    // Header views
    private lateinit var headerView: View
    private lateinit var tvTitle: TextView
    private lateinit var tvPart: TextView
    private lateinit var tvRating: TextView
    private lateinit var tvDate: TextView
    private lateinit var tvStreams: TextView
    private lateinit var btnPlay: Button
    private lateinit var btnTrailer: Button

    fun newInstance(card: ObjectSerializable): DetailsFragment {
        val fragment = DetailsFragment()
        val args = Bundle()
        args.putSerializable(AppConstants.ITEM, card)
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        addHeaderView()

        return view
    }

    private fun addHeaderView() {
        try {
            val rootView = view?.findViewById<ViewGroup>(android.R.id.content)

            headerView = LayoutInflater.from(context).inflate(R.layout.details_header, null)

            tvTitle = headerView.findViewById(R.id.tv_title)
            tvPart = headerView.findViewById(R.id.tv_part)
            tvRating = headerView.findViewById(R.id.tv_rating)
            tvDate = headerView.findViewById(R.id.tv_date)
            tvStreams = headerView.findViewById(R.id.tv_streams)
            btnPlay = headerView.findViewById(R.id.btn_play)
            btnTrailer = headerView.findViewById(R.id.btn_trailer)

            btnPlay.setOnClickListener {
                playVideo()
            }

            btnTrailer.setOnClickListener {
                playTrailer(trailer)
            }

            val params = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
            params.gravity = Gravity.BOTTOM

            (rootView as? ViewGroup)?.addView(headerView, params)
        } catch (e: Exception) {
            Log.e("DetailsFragment", "Error adding header view", e)
        }
    }

    fun updateHeaderData(
        title: String,
        part: String,
        rating: String,
        date: String,
        streams: String
    ) {
        try {
            tvTitle.text = title
            tvPart.text = part
            tvRating.text = rating
            tvDate.text = date
            tvStreams.text = streams
        } catch (e: Exception) {
            Log.e("DetailsFragment", "Error updating header", e)
        }
    }

    private fun playVideo() {
        Toast.makeText(context, "Playing video...", Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val ps = ClassPresenterSelector()
        overviewRowPresenter = FullWidthOverviewPresenter(DetailsDescriptionPresenter(),
            object : DetailsOverviewLogoPresenter() {
                override fun onBindViewHolder(viewHolder: Presenter.ViewHolder, item: Any) {
                    super.onBindViewHolder(viewHolder, item)
                    mLogoView = viewHolder.view as ImageView
                    mLogoView?.elevation = 14f
                    mLogoView?.setBackgroundColor(Color.BLACK)
                    mLogoView?.transitionName = AppConstants.TRANSITION_POSTER
                }
            })

        detailsBackground = DetailsSupportFragmentBackgroundController(this)

        overviewRowPresenter.onActionClickedListener =
            OnActionClickedListener { action ->
                playTrailer(trailer)
            }

        ps.addClassPresenter(DetailsOverviewRow::class.java, overviewRowPresenter)
        ps.addClassPresenter(ListRow::class.java, ListRowPresenter())

        mRowsAdapter = ArrayObjectAdapter(ps)
        adapter = mRowsAdapter

        (activity as DetailsActivity).bgScroll.visibility = View.VISIBLE
        (activity as DetailsActivity).bgScroll.isFocusable = false
        (activity as DetailsActivity).bgScroll.isFocusableInTouchMode = false
        (activity as DetailsActivity).bgScroll.clearFocus()

        UtilsView().animationBlink((activity as DetailsActivity).drawerBottomArrow)
        UtilsView().animationBlink((activity as DetailsActivity).drawerTopText)
        UtilsView().animationBlink((activity as DetailsActivity).drawerTopArrow)
        (activity as DetailsActivity).drawerTop.translationY = -(activity as DetailsActivity).resources.getDimensionPixelOffset(R.dimen.lb_details_v2_logo_margin_top).toFloat()

        Handler().postDelayed({
            (activity as DetailsActivity).drawerTopText.visibility = View.VISIBLE
            (activity as DetailsActivity).drawerBottomArrow.visibility = View.VISIBLE
            (activity as DetailsActivity).drawerBottom.visibility = View.VISIBLE
            UtilsView().showBottomDrawer((activity as DetailsActivity).drawerBottomArrow,500)
            UtilsView().showBottomDrawer((activity as DetailsActivity).drawerBottom,500)
        }, 800)

        setOnItemViewSelectedListener { itemViewHolder, item, rowViewHolder, row ->
            if (overviewRowPresenter.initialState == FullWidthDetailsOverviewRowPresenter.STATE_HALF &&
                !(activity as DetailsActivity).detailsHiden) {
                if (loadTrailer) {
                    canUpToTrailer = false
                    (activity as DetailsActivity).videoFragment.pause(false)
                    UtilsView().hideTopDrawer((activity as DetailsActivity).drawerTop,300)
                }
                (activity as DetailsActivity).bgScroll.smoothScrollTo(0, 540)
                (activity as DetailsActivity).dpadController?.enableDown(mRowsAdapter!!.size() > 1)
                (activity as DetailsActivity).dpadController?.enableRight(false)
                (activity as DetailsActivity).dpadController?.enableLeft(false)
                UtilsView().animScaleY((activity as DetailsActivity).drawerBottomArrow, mRowsAdapter!!.size() > 1)
            } else if (overviewRowPresenter.initialState == FullWidthDetailsOverviewRowPresenter.STATE_FULL &&
                !(activity as DetailsActivity).detailsHiden) {
                if (loadTrailer) {
                    canUpToTrailer = true
                    (activity as DetailsActivity).videoFragment.play(false)
                    UtilsView().showTopDrawer((activity as DetailsActivity).drawerTop,300)
                }
                (activity as DetailsActivity).bgScroll.smoothScrollTo(0, 0)
                (activity as DetailsActivity).dpadController?.enableDown(true)
                if (lastSelectedItem != null && lastSelectedItem is Action) {
                    dpadOnAction(lastSelectedItem!! as Action)
                }
                UtilsView().animScaleY((activity as DetailsActivity).drawerBottomArrow, true)
            }
            if (item is Action) {
                if (loadTrailer) {
                    canUpToTrailer = true
                    (activity as DetailsActivity).videoFragment.play(false)
                }
                lastSelectedItem = item
                dpadOnAction(item)
            }
        }

        val item = arguments?.getSerializable(AppConstants.ITEM) as ObjectSerializable

        overviewRow = DetailsOverviewRow(item.obj)
        val adapter = SparseArrayObjectAdapter()
        adapter.presenterSelector = CustomActionPresenterSelector()
        overviewRow.actionsAdapter = adapter
        mRowsAdapter!!.add(0, overviewRow)

        if (item.obj != null) {
            setItem(item.obj!!)
            loadDetails(item)
        }
    }

    private fun loadDetails(item: ObjectSerializable) {
        when (val obj = item.obj) {
            is TmdbMovie -> {
                tmdbRepository.getMovieDetails(
                    movieId = obj.id,
                    onSuccess = { movie ->
                        activity?.runOnUiThread {
                            this.obj = movie
                            overviewRowPresenter.setItem(movie)
                            setActions(movie)

                            updateHeaderData(
                                title = movie.title,
                                part = "MOVIE",
                                rating = movie.voteAverage.toString(),
                                date = if (movie.releaseDate.length >= 4) movie.releaseDate.substring(0, 4) else "N/A",
                                streams = "Streams"
                            )

                            // Load trailer if available
                            if (!movie.videoResults?.results.isNullOrEmpty()) {
                                val video = movie.videoResults?.results?.firstOrNull {
                                    it.site.equals("youtube", true)
                                }
                                video?.let {
                                    trailer = "http://youtube.com/watch?v=${it.key}"
                                    loadTrailer = true
                                    (activity as? DetailsActivity)?.progressMain?.visibility = View.GONE
                                } ?: run {
                                    (activity as? DetailsActivity)?.progressMain?.visibility = View.GONE
                                }
                            } else {
                                (activity as? DetailsActivity)?.progressMain?.visibility = View.GONE
                            }
                        }
                    },
                    onError = { error ->
                        Log.e("DetailsFragment", "Error loading movie: $error")
                        activity?.runOnUiThread {
                            (activity as? DetailsActivity)?.progressMain?.visibility = View.GONE
                        }
                    }
                )
            }
            is TmdbTvSeries -> {
                tmdbRepository.getTvSeriesDetails(
                    seriesId = obj.id,
                    onSuccess = { series ->
                        activity?.runOnUiThread {
                            this.obj = series
                            overviewRowPresenter.setItem(series)
                            setActions(series)

                            val seasons = when {
                                series.numberOfSeasons != null -> series.numberOfSeasons.toString()
                                series.seasons != null && series.seasons.isNotEmpty() -> series.seasons.size.toString()
                                else -> "1"
                            }

                            val episodes = when {
                                series.numberOfEpisodes != null -> series.numberOfEpisodes.toString()
                                else -> "0"
                            }

                            updateHeaderData(
                                title = series.name,
                                part = "SEASON $seasons",
                                rating = series.voteAverage.toString(),
                                date = if (series.firstAirDate.length >= 4) series.firstAirDate.substring(0, 4) else "N/A",
                                streams = if (episodes.toInt() > 0) "$episodes Episodes" else "Streams"
                            )

                            // Load trailer if available
                            if (!series.videoResults?.results.isNullOrEmpty()) {
                                val video = series.videoResults?.results?.firstOrNull {
                                    it.site.equals("youtube", true)
                                }
                                video?.let {
                                    trailer = "http://youtube.com/watch?v=${it.key}"
                                    loadTrailer = true
                                    (activity as? DetailsActivity)?.progressMain?.visibility = View.GONE
                                } ?: run {
                                    (activity as? DetailsActivity)?.progressMain?.visibility = View.GONE
                                }
                            } else {
                                (activity as? DetailsActivity)?.progressMain?.visibility = View.GONE
                            }
                        }
                    },
                    onError = { error ->
                        Log.e("DetailsFragment", "Error loading series: $error")
                        activity?.runOnUiThread {
                            (activity as? DetailsActivity)?.progressMain?.visibility = View.GONE
                        }
                    }
                )
            }
            else -> {
                activity?.runOnUiThread {
                    (activity as? DetailsActivity)?.progressMain?.visibility = View.GONE
                }
            }
        }
    }

    // পুরানো YouTubeExtractor মেথডগুলো সরিয়ে দেওয়া হয়েছে

    private fun dpadOnAction(item: Action) {
        for (i in 0 until overviewRow.actionsAdapter.size()) {
            if (item.label1 == (overviewRow.actionsAdapter[i] as Action).label1) {
                (activity as DetailsActivity).dpadController?.enableRight(i < overviewRow.actionsAdapter.size() - 1)
                (activity as DetailsActivity).dpadController?.enableLeft(i > 0)
                break
            }
        }
    }

    private fun setActions(obj: Any) {
        // Action setup logic
    }

    fun requestActions() {
        canUpToTrailer = true
        overviewRowPresenter.actionsView.requestFocus()
    }

    private fun setItem(item: Any) {
        if (item is TmdbMovie) {
            loadPoster("https://image.tmdb.org/t/p/original" + item.posterPath)
            (activity as DetailsActivity).bgContainerView.visibility = View.VISIBLE
            loadBackPoster("https://image.tmdb.org/t/p/original" + item.backdropPath)
        } else if (item is TmdbTvSeries) {
            loadPoster("https://image.tmdb.org/t/p/original" + item.posterPath)
            (activity as DetailsActivity).bgContainerView.visibility = View.VISIBLE
            loadBackPoster("https://image.tmdb.org/t/p/original" + item.backdropPath)
        }
    }

    fun hideInterface() {
        titleView?.animate()?.translationY(-titleView.height.toFloat())?.setDuration(400)?.start()
        val frame = activity?.findViewById<FrameLayout>(R.id.details_fragment_root)
        frame?.animate()?.translationY(400f)?.setDuration(400)?.start()
        frame?.animate()?.alpha(0f)?.setDuration(400)?.start()
    }

    fun showInterface() {
        titleView?.animate()?.translationY(0f)?.setDuration(400)?.start()
        val frame = activity?.findViewById<FrameLayout>(R.id.details_fragment_root)
        frame?.animate()?.translationY(0f)?.setDuration(400)?.start()
        frame?.animate()?.alpha(1f)?.setDuration(400)?.start()
    }

    private fun loadPoster(posterPathFull: String?) {
        val thumb = posterPathFull?.replace("/original/","/w185_and_h278_bestv2/")
        val thumbnailRequest = Glide.with(this).asBitmap().load(thumb)
        Glide.with(this)
            .asBitmap()
            .thumbnail(thumbnailRequest)
            .load(posterPathFull)
            .override(300, 450)
            .centerCrop()
            .into(object : CustomTarget<Bitmap>(){
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    overviewRow.imageDrawable = BitmapDrawable(resources, resource)
                    activity?.startPostponedEnterTransition()
                }
                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
    }

    private fun loadBackPoster(backdropPath: String?) {
        val thumb = backdropPath?.replace("/original/","/w500_and_h282_face/")
        val thumbnailRequest = Glide.with(this).asBitmap().load(thumb)
        Glide.with(this)
            .asBitmap()
            .load(backdropPath)
            .thumbnail(thumbnailRequest)
            .fitCenter()
            .into((activity as DetailsActivity).bgView)
    }

    inner class DetailsDescriptionPresenter : Presenter() {
        override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.lb_details_description, parent, false)
            return ViewHolder(v)
        }

        override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
            overviewRowPresenter.setItem(item)
        }

        override fun onUnbindViewHolder(viewHolder: ViewHolder) {

        }
    }

    private fun playTrailer(trailer: String) {
        Log.e("Trailer", trailer)

        try {
            if (activity is DetailsActivity) {
                (activity as DetailsActivity).videoFragment.pause(true)
                (activity as DetailsActivity).videoFragment.playYoutube(trailer)
            }
        } catch (e: Exception) {
            Log.e("Trailer", "Failed to play trailer", e)
            Toast.makeText(context, "Cannot play trailer", Toast.LENGTH_SHORT).show()
        }
    }
}