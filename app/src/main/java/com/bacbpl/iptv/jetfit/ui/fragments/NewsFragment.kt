package com.bacbpl.iptv.jetfit.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.app.VerticalGridSupportFragment
import androidx.leanback.widget.*
import com.bacbpl.iptv.R
import com.bacbpl.iptv.jetfit.ui.presenters.BaseImageCardPresenter
import com.bumptech.glide.Glide

class NewsFragment(private val category: String) :
    VerticalGridSupportFragment(),
    BrowseSupportFragment.MainFragmentAdapterProvider {

    private val mainFragmentAdapter = BrowseSupportFragment.MainFragmentAdapter(this)
    private lateinit var mAdapter: ArrayObjectAdapter

    companion object {
        private const val TAG = "NewsFragment"
    }

    override fun getMainFragmentAdapter(): BrowseSupportFragment.MainFragmentAdapter<*> = mainFragmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = category.ifEmpty { "NEWS" }

        // গ্রিড প্রেজেন্টার সেটআপ
        gridPresenter = VerticalGridPresenter(FocusHighlight.ZOOM_FACTOR_MEDIUM).apply {
            numberOfColumns = 6
        }

        mAdapter = ArrayObjectAdapter(NewsItemPresenter())
        adapter = mAdapter

        loadNewsData()
    }

    // onStart() এ শুধু ব্যাকগ্রাউন্ড সেট করুন, super.onStart() কল রাখুন
    override fun onStart() {
        try {
            super.onStart()
            // শুধু ব্যাকগ্রাউন্ড সেট করুন
            view?.let {
                it.setBackgroundColor(
                    ContextCompat.getColor(requireContext(), R.color.default_background)
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in onStart: ${e.message}")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        try {
            super.onViewCreated(view, savedInstanceState)
            // ব্যাকগ্রাউন্ড সেট করুন
            view.setBackgroundColor(
                ContextCompat.getColor(requireContext(), R.color.default_background)
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error in onViewCreated: ${e.message}")
        }
    }

    private fun loadNewsData() {
        // ডেমো নিউজ ডাটা
        val newsList = listOf(
            NewsItem(
                title = "New Movies Released This Week",
                description = "Check out the latest blockbusters",
                imageUrl = "https://images.ottplay.com/images/news-1.jpg",
                source = "OTTplay"
            ),
            NewsItem(
                title = "Streaming Platform Updates",
                description = "New features coming to your favorite apps",
                imageUrl = "https://images.ottplay.com/images/news-2.jpg",
                source = "TechCrunch"
            ),
            NewsItem(
                title = "Upcoming Web Series",
                description = "Most anticipated shows of 2024",
                imageUrl = "https://images.ottplay.com/images/news-3.jpg",
                source = "Variety"
            ),
            NewsItem(
                title = "Awards Season Highlights",
                description = "Best performances to watch",
                imageUrl = "https://images.ottplay.com/images/news-4.jpg",
                source = "Hollywood Reporter"
            ),
            NewsItem(
                title = "Celebrity Interviews",
                description = "Exclusive conversations with your favorite stars",
                imageUrl = "https://images.ottplay.com/images/news-5.jpg",
                source = "ET Online"
            ),
            NewsItem(
                title = "Box Office Updates",
                description = "Weekly box office collection report",
                imageUrl = "https://images.ottplay.com/images/news-6.jpg",
                source = "Box Office India"
            )
        )

        newsList.forEach { news ->
            mAdapter.add(news)
        }
    }

    // নিউজ আইটেমের জন্য Presenter
    private inner class NewsItemPresenter : BaseImageCardPresenter<NewsItem>(400, 300) {
        override fun bindData(container: FrameLayout, imageView: ImageView, data: NewsItem) {
            try {
                // ইমেজ লোড করুন
                Glide.with(container.context)
                    .load(data.imageUrl)
                    .centerCrop()
                    .placeholder(android.R.color.darker_gray)
                    .error(android.R.color.holo_red_dark)
                    .into(imageView)

                // ক্লিক লিসেনার
                container.setOnClickListener {
                    Log.d(TAG, "Clicked: ${data.title}")
                }

                // টাইটেল দেখানোর জন্য TextView
                val titleView = container.findViewById<TextView>(R.id.news_title) ?:
                TextView(container.context).apply {
                    id = R.id.news_title
                    setTextColor(android.graphics.Color.WHITE)
                    setBackgroundColor(android.graphics.Color.parseColor("#AA000000"))
                    setPadding(12, 8, 12, 8)
                    textSize = 16f
                    layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        gravity = android.view.Gravity.BOTTOM
                    }
                    container.addView(this)
                }

                titleView.text = data.title

                // সোর্স দেখানোর জন্য TextView
                val sourceView = container.findViewById<TextView>(R.id.news_source) ?:
                TextView(container.context).apply {
                    id = R.id.news_source
                    setTextColor(android.graphics.Color.parseColor("#CCCCCC"))
                    setPadding(12, 0, 12, 8)
                    textSize = 12f
                    layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        gravity = android.view.Gravity.BOTTOM
                        bottomMargin = 30
                    }
                    container.addView(this)
                }

                sourceView.text = data.source

            } catch (e: Exception) {
                Log.e(TAG, "Error binding data: ${e.message}")
            }
        }
    }

    // নিউজ আইটেমের জন্য ডেটা ক্লাস
    data class NewsItem(
        val title: String,
        val description: String,
        val imageUrl: String,
        val source: String
    )
}