package com.bacbpl.iptv.jetfit.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
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

class SportsFragment(private val category: String) :
    VerticalGridSupportFragment(),
    BrowseSupportFragment.MainFragmentAdapterProvider {

    private val mainFragmentAdapter = BrowseSupportFragment.MainFragmentAdapter(this)
    private lateinit var mAdapter: ArrayObjectAdapter

    companion object {
        private const val TAG = "SportsFragment"
    }

    override fun getMainFragmentAdapter(): BrowseSupportFragment.MainFragmentAdapter<*> = mainFragmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = category.ifEmpty { "SPORTS" }

        // গ্রিড প্রেজেন্টার সেটআপ
        gridPresenter = VerticalGridPresenter(FocusHighlight.ZOOM_FACTOR_MEDIUM).apply {
            numberOfColumns = 6 // স্পোর্টসের জন্য ২ কলাম
        }

        mAdapter = ArrayObjectAdapter(SportsItemPresenter())
        adapter = mAdapter

        loadSportsData()
    }

    override fun onStart() {
        super.onStart()
        view?.setBackgroundColor(
            ContextCompat.getColor(requireContext(), R.color.default_background)
        )
    }

    private fun loadSportsData() {
        // ডেমো স্পোর্টস ডাটা - আপনার API কল এখানে যোগ করুন
        val sportsList = listOf(
            SportsItem(
                title = "IPL 2024 Schedule",
                description = "Complete schedule and match timings",
                imageUrl = "https://images.ottplay.com/images/sports/ipl-2024.jpg",
                category = "Cricket",
                matchInfo = "CSK vs MI - Tonight 7:30 PM"
            ),
            SportsItem(
                title = "FIFA World Cup Qualifiers",
                description = "India vs Qatar match preview",
                imageUrl = "https://images.ottplay.com/images/sports/football.jpg",
                category = "Football",
                matchInfo = "Live streaming on Sports18"
            ),
            SportsItem(
                title = "Wimbledon 2024",
                description = "Highlights and match results",
                imageUrl = "https://images.ottplay.com/images/sports/tennis.jpg",
                category = "Tennis",
                matchInfo = "Final match - Sunday"
            ),
            SportsItem(
                title = "Pro Kabaddi League",
                description = "Patna Pirates vs Bengal Warriors",
                imageUrl = "https://images.ottplay.com/images/sports/kabaddi.jpg",
                category = "Kabaddi",
                matchInfo = "Live Now"
            ),
            SportsItem(
                title = "Olympics 2024 Paris",
                description = "India's medal tally and updates",
                imageUrl = "https://images.ottplay.com/images/sports/olympics.jpg",
                category = "Multi-Sport",
                matchInfo = "Day 5 updates"
            ),
            SportsItem(
                title = "BWF World Championships",
                description = "Badminton matches today",
                imageUrl = "https://images.ottplay.com/images/sports/badminton.jpg",
                category = "Badminton",
                matchInfo = "Semifinals today"
            ),
            SportsItem(
                title = "NBA Playoffs",
                description = "Lakers vs Warriors Game 7",
                imageUrl = "https://images.ottplay.com/images/sports/basketball.jpg",
                category = "Basketball",
                matchInfo = "Live on NBA League Pass"
            ),
            SportsItem(
                title = "Formula 1 Monaco GP",
                description = "Race highlights and standings",
                imageUrl = "https://images.ottplay.com/images/sports/f1.jpg",
                category = "Motorsports",
                matchInfo = "Verstappen wins"
            )
        )

        sportsList.forEach { sports ->
            mAdapter.add(sports)
        }
    }

    // স্পোর্টস আইটেমের জন্য Presenter
    private inner class SportsItemPresenter : BaseImageCardPresenter<SportsItem>(400, 350) {
        override fun bindData(container: FrameLayout, imageView: ImageView, data: SportsItem) {
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
                    // স্পোর্টস ডিটেইলস দেখানোর কোড
                    openSportsDetails(data)
                }

                // ক্যাটাগরি ব্যাজ (উপরের দিকে)
                val categoryView = container.findViewById<TextView>(R.id.sports_category) ?:
                TextView(container.context).apply {
                    id = R.id.sports_category
                    setTextColor(android.graphics.Color.WHITE)
                    setBackgroundColor(android.graphics.Color.parseColor("#FF4081"))
                    setPadding(8, 4, 8, 4)
                    textSize = 12f
                    layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        gravity = android.view.Gravity.TOP or android.view.Gravity.START
                        topMargin = 8
                        leftMargin = 8
                    }
                    container.addView(this)
                }

                categoryView.text = data.category

                // টাইটেল (নিচের দিকে)
                val titleView = container.findViewById<TextView>(R.id.sports_title) ?:
                TextView(container.context).apply {
                    id = R.id.sports_title
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

                // ম্যাচ তথ্য (টাইটেলের নিচে)
                val matchInfoView = container.findViewById<TextView>(R.id.sports_match_info) ?:
                TextView(container.context).apply {
                    id = R.id.sports_match_info
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

                matchInfoView.text = data.matchInfo

                // Live ব্যাজ (যদি লাইভ হয়)
                if (data.matchInfo.contains("Live", ignoreCase = true)) {
                    val liveBadge = container.findViewById<TextView>(R.id.sports_live) ?:
                    TextView(container.context).apply {
                        id = R.id.sports_live
                        text = "LIVE"
                        setTextColor(android.graphics.Color.WHITE)
                        setBackgroundColor(android.graphics.Color.RED)
                        setPadding(8, 4, 8, 4)
                        textSize = 10f
                        layoutParams = FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.WRAP_CONTENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT
                        ).apply {
                            gravity = android.view.Gravity.TOP or android.view.Gravity.END
                            topMargin = 8
                            rightMargin = 8
                        }
                        container.addView(this)
                    }
                    liveBadge.visibility = android.view.View.VISIBLE
                }

            } catch (e: Exception) {
                Log.e(TAG, "Error binding data: ${e.message}")
            }
        }

        private fun openSportsDetails(sportsItem: SportsItem) {
            // স্পোর্টস ডিটেইলস দেখানোর জন্য ইচ্ছেমতো কোড
            // যেমন: ডিটেইলস ফ্র্যাগমেন্ট খোলা বা ওয়েবভিউ দেখানো
            Log.d(TAG, "Opening details for: ${sportsItem.title}")

            // উদাহরণ: ওয়েবভিউতে গুগল সার্চ খোলা
            // val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=${sportsItem.title}"))
            // startActivity(intent)
        }
    }

    // স্পোর্টস আইটেমের জন্য ডেটা ক্লাস
    data class SportsItem(
        val title: String,
        val description: String,
        val imageUrl: String,
        val category: String,
        val matchInfo: String
    )
}