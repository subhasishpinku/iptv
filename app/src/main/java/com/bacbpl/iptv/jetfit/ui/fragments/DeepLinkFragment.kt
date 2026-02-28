//package com.bacbpl.iptv.jetfit.ui.fragments
//
//import android.content.Intent
//import android.net.Uri
//import android.os.Bundle
//import android.util.Log
//import android.view.ViewGroup
//import android.widget.FrameLayout
//import android.widget.ImageView
//import android.widget.TextView
//import android.widget.Toast
//import androidx.core.content.ContextCompat
//import androidx.leanback.app.BrowseSupportFragment
//import androidx.leanback.app.VerticalGridSupportFragment
//import androidx.leanback.widget.*
//import com.bacbpl.iptv.R
//import com.bacbpl.iptv.jetfit.ui.activities.OTTplayDeepLinkActivity
//import com.bacbpl.iptv.jetfit.ui.presenters.BaseImageCardPresenter
//import com.bumptech.glide.Glide
//
//class DeepLinkFragment(private val category: String) :
//    VerticalGridSupportFragment(),
//    BrowseSupportFragment.MainFragmentAdapterProvider {
//
//    private val mainFragmentAdapter = BrowseSupportFragment.MainFragmentAdapter(this)
//    private lateinit var mAdapter: ArrayObjectAdapter
//
//    companion object {
//        private const val TAG = "DeepLinkFragment"
//
//        // OTTplay API configuration
//        private const val OTTPLAY_BASE_URL = "https://www.ottplay.com"
//        private const val PARTNER_SOURCE = "bacbpl"
//        private const val PARTNER_APP_NAME = "BACBPL IPTV"
//
//        // Sample token for demo (in production, get from your auth system)
//        private const val SAMPLE_TOKEN = "3uPhNCSL4TSacPe5jVkWZea1ujTyF3NXTRXrP00sVG56MIcfSlb2MHLPnat9EKVo9vrbW5J4z5NI3hyWLJRp3CWFhoMpKPIKkZCgl7IzRdio1twKYhkOub7glxnFcZdP3m6zceHY5YcoYl4hn7RRKXJhhgbwNPAXqQmwD7E5p58icnV0bFdQEnvIBh9QYCbr1Qc3ue5tAYgMmIJTFfV3s19bvUMCAGj2bpDtWBSxXKOlngjLFgU2RCI2cKz8byC"
////        private const val SAMPLE_BACK_URL = "https://bacbpl.iptv.com/back"
//        private const val SAMPLE_BACK_URL =  "https://www.ottplay.com/movie/bb5-2017/8a038239b9903?token=145688999&source=bacbpl&backUrl=http:localhost:5000/home&appName=BACBPL"
//    }
//
//    override fun getMainFragmentAdapter(): BrowseSupportFragment.MainFragmentAdapter<*> = mainFragmentAdapter
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        title = category.ifEmpty { "OTTplay DEEP LINK" }
//
//        // গ্রিড প্রেজেন্টার সেটআপ
//        gridPresenter = VerticalGridPresenter(FocusHighlight.ZOOM_FACTOR_MEDIUM).apply {
//            numberOfColumns = 6 // ২ কলাম
//        }
//
//        mAdapter = ArrayObjectAdapter(DeepLinkItemPresenter())
//        adapter = mAdapter
//
//        loadDeepLinkItems()
//    }
//
//
//    override fun onStart() {
//        try {
//            super.onStart()
//            view?.setBackgroundColor(
//                ContextCompat.getColor(requireContext(), R.color.default_background)
//            )
//        } catch (e: Exception) {
//            Log.e(TAG, "Error in onStart: ${e.message}")
//        }
//    }
//    private fun loadDeepLinkItems() {
//        // OTTplay কন্টেন্ট আইটেম (ডিপ লিংক সহ)
//        val items = listOf(
//            DeepLinkItem(
//                id = "8a038239b9903",
//                title = "BB5 - 2017",
//                description = "Action Thriller Movie",
//                imageUrl = "https://images.ottplay.com/posters/bb5-2017-poster.jpg",
//                contentType = "movie",
//                contentPath = "/movie/bb5-2017/8a038239b9903",
//                token = SAMPLE_TOKEN,
//                backUrl = SAMPLE_BACK_URL,
//                bgColor = "#FF6B6B"
//            ),
//            DeepLinkItem(
//                id = "7b038239b9904",
//                title = "The Family Man",
//                description = "Web Series - Season 2",
//                imageUrl = "https://images.ottplay.com/posters/family-man-s2-poster.jpg",
//                contentType = "show",
//                contentPath = "/show/the-family-man-2021/7b038239b9904",
//                token = SAMPLE_TOKEN,
//                backUrl = SAMPLE_BACK_URL,
//                bgColor = "#4ECDC4"
//            ),
//            DeepLinkItem(
//                id = "6c038239b9905",
//                title = "RRR",
//                description = "Blockbuster Movie",
//                imageUrl = "https://images.ottplay.com/posters/rrr-poster.jpg",
//                contentType = "movie",
//                contentPath = "/movie/rrr-2022/6c038239b9905",
//                token = SAMPLE_TOKEN,
//                backUrl = SAMPLE_BACK_URL,
//                bgColor = "#FFD93D"
//            ),
//            DeepLinkItem(
//                id = "5d038239b9906",
//                title = "Mirzapur",
//                description = "Crime Thriller Series",
//                imageUrl = "https://images.ottplay.com/posters/mirzapur-poster.jpg",
//                contentType = "show",
//                contentPath = "/show/mirzapur-2020/5d038239b9906",
//                token = SAMPLE_TOKEN,
//                backUrl = SAMPLE_BACK_URL,
//                bgColor = "#6BCB77"
//            ),
//            DeepLinkItem(
//                id = "4e038239b9907",
//                title = "KGF Chapter 2",
//                description = "Action Movie",
//                imageUrl = "https://images.ottplay.com/posters/kgf2-poster.jpg",
//                contentType = "movie",
//                contentPath = "/movie/kgf-chapter-2-2022/4e038239b9907",
//                token = SAMPLE_TOKEN,
//                backUrl = SAMPLE_BACK_URL,
//                bgColor = "#FF9F1C"
//            ),
//            DeepLinkItem(
//                id = "3f038239b9908",
//                title = "Panchayat",
//                description = "Comedy Drama Series",
//                imageUrl = "https://images.ottplay.com/posters/panchayat-poster.jpg",
//                contentType = "show",
//                contentPath = "/show/panchayat-2021/3f038239b9908",
//                token = SAMPLE_TOKEN,
//                backUrl = SAMPLE_BACK_URL,
//                bgColor = "#F4A261"
//            )
//        )
//
//        items.forEach { item ->
//            mAdapter.add(item)
//        }
//        Log.d(TAG, "Loaded ${items.size} deep link items")
//    }
//
//    // Deep Link Item Presenter
//    private inner class DeepLinkItemPresenter : BaseImageCardPresenter<DeepLinkItem>(450, 350) {
//        override fun bindData(container: FrameLayout, imageView: ImageView, data: DeepLinkItem) {
//            try {
//                // Container styling
//                container.elevation = 8f
//                container.setBackgroundColor(android.graphics.Color.parseColor(data.bgColor))
//
//                // ইমেজ লোড করুন
//                Glide.with(container.context)
//                    .load(data.imageUrl)
//                    .centerCrop()
//                    .placeholder(R.drawable.ic_ottplay_placeholder)
//                    .error(R.drawable.ic_ottplay_placeholder)
//                    .into(imageView)
//
//                // কন্টেন্ট টাইপ ব্যাজ
//                val typeBadge = container.findViewById<TextView>(R.id.type_badge) ?:
//                TextView(container.context).apply {
//                    id = R.id.type_badge
//                    layoutParams = FrameLayout.LayoutParams(
//                        FrameLayout.LayoutParams.WRAP_CONTENT,
//                        FrameLayout.LayoutParams.WRAP_CONTENT
//                    ).apply {
//                        gravity = android.view.Gravity.TOP or android.view.Gravity.START
//                        topMargin = 8
//                        leftMargin = 8
//                    }
//                    setPadding(8, 4, 8, 4)
//                    textSize = 12f
//                    setTextColor(android.graphics.Color.WHITE)
//                    setBackgroundColor(android.graphics.Color.parseColor("#FF4081"))
//                    container.addView(this)
//                }
//                typeBadge.text = data.contentType.uppercase()
//
//                // OTTplay ব্যাজ
//                val ottplayBadge = container.findViewById<TextView>(R.id.ottplay_badge) ?:
//                TextView(container.context).apply {
//                    id = R.id.ottplay_badge
//                    text = "OTTplay"
//                    layoutParams = FrameLayout.LayoutParams(
//                        FrameLayout.LayoutParams.WRAP_CONTENT,
//                        FrameLayout.LayoutParams.WRAP_CONTENT
//                    ).apply {
//                        gravity = android.view.Gravity.TOP or android.view.Gravity.END
//                        topMargin = 8
//                        rightMargin = 8
//                    }
//                    setPadding(8, 4, 8, 4)
//                    textSize = 12f
//                    setTextColor(android.graphics.Color.WHITE)
//                    setBackgroundColor(android.graphics.Color.parseColor("#6200EE"))
//                    container.addView(this)
//                }
//
//                // Bottom info container
//                val infoContainer = FrameLayout(container.context).apply {
//                    layoutParams = FrameLayout.LayoutParams(
//                        FrameLayout.LayoutParams.MATCH_PARENT,
//                        FrameLayout.LayoutParams.WRAP_CONTENT
//                    ).apply {
//                        gravity = android.view.Gravity.BOTTOM
//                    }
//                    setBackgroundColor(android.graphics.Color.parseColor("#AA000000"))
//                    container.addView(this)
//                }
//
//                // Title
//                val titleView = TextView(container.context).apply {
//                    text = data.title
//                    setTextColor(android.graphics.Color.WHITE)
//                    textSize = 16f
//                    isAllCaps = false
//                    typeface = android.graphics.Typeface.DEFAULT_BOLD
//                    setPadding(12, 8, 12, 4)
//                    layoutParams = FrameLayout.LayoutParams(
//                        FrameLayout.LayoutParams.MATCH_PARENT,
//                        FrameLayout.LayoutParams.WRAP_CONTENT
//                    )
//                }
//                infoContainer.addView(titleView)
//
//                // Description
//                val descView = TextView(container.context).apply {
//                    text = data.description
//                    setTextColor(android.graphics.Color.parseColor("#CCCCCC"))
//                    textSize = 12f
//                    setPadding(12, 0, 12, 8)
//                    layoutParams = FrameLayout.LayoutParams(
//                        FrameLayout.LayoutParams.MATCH_PARENT,
//                        FrameLayout.LayoutParams.WRAP_CONTENT
//                    )
//                }
//                infoContainer.addView(descView)
//
//                // Click listener - Open OTTplay deep link
//                container.setOnClickListener {
//                    openOTTplayDeepLink(data)
//                }
//
//                // Focus effects
//                container.setOnFocusChangeListener { v, hasFocus ->
//                    if (hasFocus) {
//                        v.scaleX = 1.05f
//                        v.scaleY = 1.05f
//                        v.elevation = 15f
//                    } else {
//                        v.scaleX = 1f
//                        v.scaleY = 1f
//                        v.elevation = 8f
//                    }
//                }
//
//            } catch (e: Exception) {
//                Log.e(TAG, "Error binding data: ${e.message}")
//            }
//        }
//
//        private fun openOTTplayDeepLink(item: DeepLinkItem) {
//            try {
//                // ডিপ লিংক তৈরি করুন
//                val deepLink = createOTTplayDeepLink(item)
//
//                Log.d(TAG, "Opening deep link: $deepLink")
//
//                // ডিপ লিংক অ্যাক্টিভিটি খুলুন
//                val intent = Intent(requireContext(), OTTplayDeepLinkActivity::class.java).apply {
//                    data = Uri.parse(deepLink)
//                }
//                startActivity(intent)
//
//            } catch (e: Exception) {
//                Log.e(TAG, "Error opening deep link: ${e.message}")
//                Toast.makeText(context, "Cannot open content", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//        private fun createOTTplayDeepLink(item: DeepLinkItem): String {
//            return if (item.token.isNotEmpty()) {
//                // Mobile/TV deep link with parameters
//                "$OTTPLAY_BASE_URL${item.contentPath}?" +
//                        "token=${item.token}" +
//                        "&backUrl=${Uri.encode(item.backUrl)}" +
//                        "&source=$PARTNER_SOURCE" +
//                        "&appName=${Uri.encode(PARTNER_APP_NAME)}"
//            } else {
//                // Fallback to basic URL
//                "$OTTPLAY_BASE_URL${item.contentPath}"
//            }
//        }
//    }
//
//    // Deep Link Item Data Class
//    data class DeepLinkItem(
//        val id: String,
//        val title: String,
//        val description: String,
//        val imageUrl: String,
//        val contentType: String, // "movie" or "show"
//        val contentPath: String,
//        val token: String,
//        val backUrl: String,
//        val bgColor: String
//    )
//}
package com.bacbpl.iptv.jetfit.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.app.VerticalGridSupportFragment
import androidx.leanback.widget.*
import com.bacbpl.iptv.R
import com.bacbpl.iptv.ui.activities.OTTplayDeepLinkActivity
import com.bacbpl.iptv.jetfit.ui.presenters.BaseImageCardPresenter
import com.bacbpl.iptv.jetfit.utils.OTTplayApiClient
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URLEncoder

class DeepLinkFragment(private val category: String) :
    VerticalGridSupportFragment(),
    BrowseSupportFragment.MainFragmentAdapterProvider {

    private val mainFragmentAdapter = BrowseSupportFragment.MainFragmentAdapter(this)
    private lateinit var mAdapter: ArrayObjectAdapter

    // OTTplay API configuration
    private val apiClient = OTTplayApiClient()

    companion object {
        private const val TAG = "DeepLinkFragment"
        private const val OTTPLAY_BASE_URL = "https://www.ottplay.com"
        private const val PARTNER_SOURCE = "bacbpl"
        private const val PARTNER_APP_NAME = "BACBPL IPTV"

        // Use your actual local IP and port
        private const val BACK_URL = "http://192.168.1.11:8080/cpanel"

        // You need to get a valid token from your authentication system
        private const val SAMPLE_TOKEN = "123456789"
    }

    override fun getMainFragmentAdapter(): BrowseSupportFragment.MainFragmentAdapter<*> = mainFragmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = when(category) {
            "latest" -> "Latest Releases"
            "sonyliv" -> "NEW ON SonyLIV"
            "zee5" -> "NEW ON ZEE5"
            "stage" -> "NEW ON Stage"
            else -> "OTTplay Content"
        }

        // Grid setup
        gridPresenter = VerticalGridPresenter(FocusHighlight.ZOOM_FACTOR_MEDIUM).apply {
            numberOfColumns = 6
        }

        mAdapter = ArrayObjectAdapter(DeepLinkItemPresenter())
        adapter = mAdapter

        // Load content
        loadOTTplayContent()
    }

    override fun onStart() {
        try {
            super.onStart()
            view?.setBackgroundColor(
                ContextCompat.getColor(requireContext(), R.color.default_background)
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error in onStart: ${e.message}")
        }
    }

    private fun loadOTTplayContent() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                // Show loading state
                showLoading()

                // Fetch data from API
                val response = withContext(Dispatchers.IO) {
                    apiClient.getWidgetList("partner/bhorer-alo-cable/ed11a21fc3727")
                }

                if (response.success) {
                    populateContent(response.widgets)
                } else {
                    // Fallback to sample data if API fails
                    loadSampleData()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading content: ${e.message}")
                loadSampleData()
            } finally {
                hideLoading()
            }
        }
    }

    private fun loadSampleData() {
        // Sample data with the exact URL format you provided
        val sampleItems = listOf(
            DeepLinkItem(
                id = "622438dd309f2",
                title = "Behen Hogi Teri (2017)",
                description = "Hindi • Comedy/Romance • 2017",
                imageUrl = "https://images.ottplay.com/posters/behen-hogi-teri-2017-poster.jpg",
                contentType = "movie",
                contentPath = "/movie/behen-hogi-teri-2017/622438dd309f2",
                rating = 6.5,
                ottProvider = "ZEE5"
            ),
            DeepLinkItem(
                id = "8a038239b9903",
                title = "BB5 - 2017",
                description = "Bengali • Action/Thriller • 2017",
                imageUrl = "https://images.ottplay.com/posters/bb5-2017-poster.jpg",
                contentType = "movie",
                contentPath = "/movie/bb5-2017/8a038239b9903",
                rating = 7.2,
                ottProvider = "ZEE5"
            )
        )

        sampleItems.forEach { item ->
            mAdapter.add(item)
        }
        Log.d(TAG, "Loaded ${mAdapter.size()} sample items")
    }

    private fun populateContent(widgets: List<Widget>) {
        mAdapter.clear()

        // Filter widgets based on category
        val filteredWidgets = when(category) {
            "latest" -> widgets.filter { it.type == "latest_release" }
            "sonyliv" -> widgets.filter { it.id == "5f456c2aff9ccd034434e6fd" }
            "zee5" -> widgets.filter { it.id == "5f456c2aff9ccd034434e700" }
            "stage" -> widgets.filter { it.id == "648c5261bfe063001d8e02b7" }
            else -> widgets
        }

        filteredWidgets.forEach { widget ->
            widget.data.forEach { item ->
                val deepLinkItem = DeepLinkItem(
                    id = item.id,
                    title = item.name,
                    description = "${item.language} • ${item.genre} • ${item.release_year}",
                    imageUrl = item.poster_image,
                    contentType = item.format,
                    contentPath = item.ottplay_url.replace(OTTPLAY_BASE_URL, ""),
                    rating = item.ottplay_rating,
                    ottProvider = item.ott_provider_name
                )
                mAdapter.add(deepLinkItem)
            }
        }

        Log.d(TAG, "Loaded ${mAdapter.size()} items")
    }

    private fun showLoading() {
        // Implement loading state
    }

    private fun hideLoading() {
        // Hide loading state
    }

    // Deep Link Item Presenter
    private inner class DeepLinkItemPresenter : BaseImageCardPresenter<DeepLinkItem>(450, 350) {
        override fun bindData(container: FrameLayout, imageView: ImageView, data: DeepLinkItem) {
            try {
                // Container styling
                container.elevation = 8f
                container.setBackgroundColor(android.graphics.Color.parseColor("#1E1E1E"))

                // In DeepLinkItemPresenter.bindData method, update Glide loading:
                Glide.with(container.context)
                    .load(data.imageUrl)
                    .centerCrop()
                    .placeholder(R.drawable.ic_ottplay_placeholder)
                    .error(R.drawable.ic_ottplay_placeholder)
                    .thumbnail(0.25f) // Add thumbnail for faster loading
                    .skipMemoryCache(false) // Enable memory caching
                    .diskCacheStrategy(DiskCacheStrategy.ALL) // Cache on disk
                    .into(imageView)

                // Content type badge
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
                    setBackgroundColor(getTypeColor(data.contentType))
                    container.addView(this)
                }
                typeBadge.text = data.contentType.uppercase()

                // OTT Provider badge
                val providerBadge = container.findViewById<TextView>(R.id.provider_badge) ?:
                TextView(container.context).apply {
                    id = R.id.provider_badge
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
                    setBackgroundColor(getProviderColor(data.ottProvider))
                    container.addView(this)
                }
                providerBadge.text = data.ottProvider

                // Rating badge if available
                if (data.rating > 0) {
                    val ratingBadge = container.findViewById<TextView>(R.id.rating_badge) ?:
                    TextView(container.context).apply {
                        id = R.id.rating_badge
                        layoutParams = FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.WRAP_CONTENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT
                        ).apply {
                            gravity = android.view.Gravity.TOP or android.view.Gravity.CENTER_HORIZONTAL
                            topMargin = 8
                        }
                        setPadding(8, 4, 8, 4)
                        textSize = 12f
                        setTextColor(android.graphics.Color.WHITE)
                        setBackgroundColor(android.graphics.Color.parseColor("#FFA500"))
                        container.addView(this)
                    }
                    ratingBadge.text = "★ ${data.rating}"
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
                    textSize = 14f
                    isAllCaps = false
                    typeface = android.graphics.Typeface.DEFAULT_BOLD
                    maxLines = 1
                    setPadding(12, 8, 12, 2)
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
                    textSize = 11f
                    maxLines = 2
                    setPadding(12, 0, 12, 8)
                    layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                    )
                }
                infoContainer.addView(descView)

                // Click listener - Open with proper deep link format
                container.setOnClickListener {
                    openOTTplayDeepLink(data)
                }

                // Focus effects
                container.setOnFocusChangeListener { v, hasFocus ->
                    if (hasFocus) {
                        v.scaleX = 1.05f
                        v.scaleY = 1.05f
                        v.elevation = 15f
                        v.bringToFront()
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

        private fun getTypeColor(type: String): Int {
            return when(type.lowercase()) {
                "movie" -> android.graphics.Color.parseColor("#FF4081")
                "show" -> android.graphics.Color.parseColor("#4CAF50")
                else -> android.graphics.Color.parseColor("#9C27B0")
            }
        }

        private fun getProviderColor(provider: String): Int {
            return when(provider) {
                "ZEE5" -> android.graphics.Color.parseColor("#6A1B9A")
                "SonyLIV" -> android.graphics.Color.parseColor("#1565C0")
                "Stage" -> android.graphics.Color.parseColor("#C2185B")
                "CHAUPAL" -> android.graphics.Color.parseColor("#2E7D32")
                else -> android.graphics.Color.parseColor("#455A64")
            }
        }

        private fun openOTTplayDeepLink(item: DeepLinkItem) {
            try {
                // Construct the deep link exactly as in your example:
                // https://www.ottplay.com/movie/behen-hogi-teri-2017/622438dd309f2?token=123456789&backUrl=http://192.168.1.11:8080:cpanel&source=BACBPL%20IPTV&appName=bacbpl

                val deepLink = buildDeepLink(item)

                Log.d(TAG, "Opening deep link: $deepLink")

                // Use the custom activity to handle the deep link
                val intent = Intent(requireContext(), OTTplayDeepLinkActivity::class.java).apply {
                    data = Uri.parse(deepLink)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }

                // Alternative: Try to open directly in browser if custom activity fails
                try {
                    startActivity(intent)
                } catch (e: Exception) {
                    Log.e(TAG, "Custom activity failed, trying browser: ${e.message}")
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(deepLink))
                    startActivity(browserIntent)
                }

            } catch (e: Exception) {
                Log.e(TAG, "Error opening deep link: ${e.message}")
                Toast.makeText(context, "Cannot open content: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

        private fun buildDeepLink(item: DeepLinkItem): String {
            return try {
                // URL encode the parameters properly
                val encodedBackUrl = URLEncoder.encode(BACK_URL, "UTF-8")
                val encodedAppName = URLEncoder.encode(PARTNER_APP_NAME, "UTF-8")
                val encodedSource = URLEncoder.encode(PARTNER_SOURCE, "UTF-8")

                // Build the URL exactly as in your example
                "$OTTPLAY_BASE_URL${item.contentPath}?" +
                        "token=$SAMPLE_TOKEN" +
                        "&backUrl=$encodedBackUrl" +
                        "&source=$encodedSource" +
                        "&appName=$encodedAppName"
            } catch (e: Exception) {
                Log.e(TAG, "Error building deep link: ${e.message}")
                // Fallback to basic URL
                "$OTTPLAY_BASE_URL${item.contentPath}"
            }
        }
    }

    // Data classes matching API response
    data class ApiResponse(
        val success: Boolean,
        val widgets: List<Widget>
    )

    data class Widget(
        val name: String,
        val type: String,
        val id: String,
        val data: List<ContentItem>
    )

    data class ContentItem(
        val name: String,
        val format: String,
        val language: String,
        val release_year: Int,
        val ott_provider_name: String,
        val poster_image: String,
        val backdrop_image: String,
        val id: String,
        val ottplay_rating: Double,
        val genre: String,
        val ottplay_url: String,
        val casts: List<Cast>?,
        val crews: List<Crew>?
    )

    data class Cast(
        val name: String,
        val poster: String?
    )

    data class Crew(
        val name: String,
        val poster: String?
    )

    // Deep Link Item Data Class
    data class DeepLinkItem(
        val id: String,
        val title: String,
        val description: String,
        val imageUrl: String,
        val contentType: String,
        val contentPath: String,
        val rating: Double,
        val ottProvider: String
    )
}