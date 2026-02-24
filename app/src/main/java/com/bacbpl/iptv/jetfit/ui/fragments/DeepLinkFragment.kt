package com.bacbpl.iptv.jetfit.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.app.VerticalGridSupportFragment
import androidx.leanback.widget.*
import com.bacbpl.iptv.R
import com.bacbpl.iptv.jetfit.ui.activities.OTTplayDeepLinkActivity
import com.bacbpl.iptv.jetfit.ui.presenters.BaseImageCardPresenter
import com.bumptech.glide.Glide

class DeepLinkFragment(private val category: String) :
    VerticalGridSupportFragment(),
    BrowseSupportFragment.MainFragmentAdapterProvider {

    private val mainFragmentAdapter = BrowseSupportFragment.MainFragmentAdapter(this)
    private lateinit var mAdapter: ArrayObjectAdapter

    companion object {
        private const val TAG = "DeepLinkFragment"

        // OTTplay API configuration
        private const val OTTPLAY_BASE_URL = "https://www.ottplay.com"
        private const val PARTNER_SOURCE = "bacbpl"
        private const val PARTNER_APP_NAME = "BACBPL IPTV"

        // Sample token for demo (in production, get from your auth system)
        private const val SAMPLE_TOKEN = "3uPhNCSL4TSacPe5jVkWZea1ujTyF3NXTRXrP00sVG56MIcfSlb2MHLPnat9EKVo9vrbW5J4z5NI3hyWLJRp3CWFhoMpKPIKkZCgl7IzRdio1twKYhkOub7glxnFcZdP3m6zceHY5YcoYl4hn7RRKXJhhgbwNPAXqQmwD7E5p58icnV0bFdQEnvIBh9QYCbr1Qc3ue5tAYgMmIJTFfV3s19bvUMCAGj2bpDtWBSxXKOlngjLFgU2RCI2cKz8byC"
//        private const val SAMPLE_BACK_URL = "https://bacbpl.iptv.com/back"
        private const val SAMPLE_BACK_URL =  "http://192.168.1.11:8080/"
    }

    override fun getMainFragmentAdapter(): BrowseSupportFragment.MainFragmentAdapter<*> = mainFragmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = category.ifEmpty { "OTTplay DEEP LINK" }

        // গ্রিড প্রেজেন্টার সেটআপ
        gridPresenter = VerticalGridPresenter(FocusHighlight.ZOOM_FACTOR_MEDIUM).apply {
            numberOfColumns = 6 // ২ কলাম
        }

        mAdapter = ArrayObjectAdapter(DeepLinkItemPresenter())
        adapter = mAdapter

        loadDeepLinkItems()
    }

    override fun onStart() {
        super.onStart()
        view?.setBackgroundColor(
            ContextCompat.getColor(requireContext(), R.color.default_background)
        )
    }

    private fun loadDeepLinkItems() {
        // OTTplay কন্টেন্ট আইটেম (ডিপ লিংক সহ)
        val items = listOf(
            DeepLinkItem(
                id = "8a038239b9903",
                title = "BB5 - 2017",
                description = "Action Thriller Movie",
                imageUrl = "https://images.ottplay.com/posters/bb5-2017-poster.jpg",
                contentType = "movie",
                contentPath = "/movie/bb5-2017/8a038239b9903",
                token = SAMPLE_TOKEN,
                backUrl = SAMPLE_BACK_URL,
                bgColor = "#FF6B6B"
            ),
            DeepLinkItem(
                id = "7b038239b9904",
                title = "The Family Man",
                description = "Web Series - Season 2",
                imageUrl = "https://images.ottplay.com/posters/family-man-s2-poster.jpg",
                contentType = "show",
                contentPath = "/show/the-family-man-2021/7b038239b9904",
                token = SAMPLE_TOKEN,
                backUrl = SAMPLE_BACK_URL,
                bgColor = "#4ECDC4"
            ),
            DeepLinkItem(
                id = "6c038239b9905",
                title = "RRR",
                description = "Blockbuster Movie",
                imageUrl = "https://images.ottplay.com/posters/rrr-poster.jpg",
                contentType = "movie",
                contentPath = "/movie/rrr-2022/6c038239b9905",
                token = SAMPLE_TOKEN,
                backUrl = SAMPLE_BACK_URL,
                bgColor = "#FFD93D"
            ),
            DeepLinkItem(
                id = "5d038239b9906",
                title = "Mirzapur",
                description = "Crime Thriller Series",
                imageUrl = "https://images.ottplay.com/posters/mirzapur-poster.jpg",
                contentType = "show",
                contentPath = "/show/mirzapur-2020/5d038239b9906",
                token = SAMPLE_TOKEN,
                backUrl = SAMPLE_BACK_URL,
                bgColor = "#6BCB77"
            ),
            DeepLinkItem(
                id = "4e038239b9907",
                title = "KGF Chapter 2",
                description = "Action Movie",
                imageUrl = "https://images.ottplay.com/posters/kgf2-poster.jpg",
                contentType = "movie",
                contentPath = "/movie/kgf-chapter-2-2022/4e038239b9907",
                token = SAMPLE_TOKEN,
                backUrl = SAMPLE_BACK_URL,
                bgColor = "#FF9F1C"
            ),
            DeepLinkItem(
                id = "3f038239b9908",
                title = "Panchayat",
                description = "Comedy Drama Series",
                imageUrl = "https://images.ottplay.com/posters/panchayat-poster.jpg",
                contentType = "show",
                contentPath = "/show/panchayat-2021/3f038239b9908",
                token = SAMPLE_TOKEN,
                backUrl = SAMPLE_BACK_URL,
                bgColor = "#F4A261"
            )
        )

        items.forEach { item ->
            mAdapter.add(item)
        }
        Log.d(TAG, "Loaded ${items.size} deep link items")
    }

    // Deep Link Item Presenter
    private inner class DeepLinkItemPresenter : BaseImageCardPresenter<DeepLinkItem>(450, 350) {
        override fun bindData(container: FrameLayout, imageView: ImageView, data: DeepLinkItem) {
            try {
                // Container styling
                container.elevation = 8f
                container.setBackgroundColor(android.graphics.Color.parseColor(data.bgColor))

                // ইমেজ লোড করুন
                Glide.with(container.context)
                    .load(data.imageUrl)
                    .centerCrop()
                    .placeholder(R.drawable.ic_ottplay_placeholder)
                    .error(R.drawable.ic_ottplay_placeholder)
                    .into(imageView)

                // কন্টেন্ট টাইপ ব্যাজ
                val typeBadge = container.findViewById<TextView>(R.id.type_badge) ?:
                TextView(container.context).apply {
                    id = R.id.type_badge
                    layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        gravity = android.view.Gravity.TOP or android.view.Gravity.START
                        topMargin = 8
                        leftMargin = 8
                    }
                    setPadding(8, 4, 8, 4)
                    textSize = 12f
                    setTextColor(android.graphics.Color.WHITE)
                    setBackgroundColor(android.graphics.Color.parseColor("#FF4081"))
                    container.addView(this)
                }
                typeBadge.text = data.contentType.uppercase()

                // OTTplay ব্যাজ
                val ottplayBadge = container.findViewById<TextView>(R.id.ottplay_badge) ?:
                TextView(container.context).apply {
                    id = R.id.ottplay_badge
                    text = "OTTplay"
                    layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        gravity = android.view.Gravity.TOP or android.view.Gravity.END
                        topMargin = 8
                        rightMargin = 8
                    }
                    setPadding(8, 4, 8, 4)
                    textSize = 12f
                    setTextColor(android.graphics.Color.WHITE)
                    setBackgroundColor(android.graphics.Color.parseColor("#6200EE"))
                    container.addView(this)
                }

                // Bottom info container
                val infoContainer = FrameLayout(container.context).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        gravity = android.view.Gravity.BOTTOM
                    }
                    setBackgroundColor(android.graphics.Color.parseColor("#AA000000"))
                    container.addView(this)
                }

                // Title
                val titleView = TextView(container.context).apply {
                    text = data.title
                    setTextColor(android.graphics.Color.WHITE)
                    textSize = 16f
                    isAllCaps = false
                    typeface = android.graphics.Typeface.DEFAULT_BOLD
                    setPadding(12, 8, 12, 4)
                    layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                    )
                }
                infoContainer.addView(titleView)

                // Description
                val descView = TextView(container.context).apply {
                    text = data.description
                    setTextColor(android.graphics.Color.parseColor("#CCCCCC"))
                    textSize = 12f
                    setPadding(12, 0, 12, 8)
                    layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                    )
                }
                infoContainer.addView(descView)

                // Click listener - Open OTTplay deep link
                container.setOnClickListener {
                    openOTTplayDeepLink(data)
                }

                // Focus effects
                container.setOnFocusChangeListener { v, hasFocus ->
                    if (hasFocus) {
                        v.scaleX = 1.05f
                        v.scaleY = 1.05f
                        v.elevation = 15f
                    } else {
                        v.scaleX = 1f
                        v.scaleY = 1f
                        v.elevation = 8f
                    }
                }

            } catch (e: Exception) {
                Log.e(TAG, "Error binding data: ${e.message}")
            }
        }

        private fun openOTTplayDeepLink(item: DeepLinkItem) {
            try {
                // ডিপ লিংক তৈরি করুন
                val deepLink = createOTTplayDeepLink(item)

                Log.d(TAG, "Opening deep link: $deepLink")

                // ডিপ লিংক অ্যাক্টিভিটি খুলুন
                val intent = Intent(requireContext(), OTTplayDeepLinkActivity::class.java).apply {
                    data = Uri.parse(deepLink)
                }
                startActivity(intent)

            } catch (e: Exception) {
                Log.e(TAG, "Error opening deep link: ${e.message}")
                Toast.makeText(context, "Cannot open content", Toast.LENGTH_SHORT).show()
            }
        }

        private fun createOTTplayDeepLink(item: DeepLinkItem): String {
            return if (item.token.isNotEmpty()) {
                // Mobile/TV deep link with parameters
                "$OTTPLAY_BASE_URL${item.contentPath}?" +
                        "token=${item.token}" +
                        "&backUrl=${Uri.encode(item.backUrl)}" +
                        "&source=$PARTNER_SOURCE" +
                        "&appName=${Uri.encode(PARTNER_APP_NAME)}"
            } else {
                // Fallback to basic URL
                "$OTTPLAY_BASE_URL${item.contentPath}"
            }
        }
    }

    // Deep Link Item Data Class
    data class DeepLinkItem(
        val id: String,
        val title: String,
        val description: String,
        val imageUrl: String,
        val contentType: String, // "movie" or "show"
        val contentPath: String,
        val token: String,
        val backUrl: String,
        val bgColor: String
    )
}