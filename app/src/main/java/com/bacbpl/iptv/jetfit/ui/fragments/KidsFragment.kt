package com.bacbpl.iptv.jetfit.ui.fragments

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
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
import com.bacbpl.iptv.jetfit.models.TvChannelItem
import com.bacbpl.iptv.jetfit.ui.activities.TvPlayerActivity
import com.bacbpl.iptv.jetfit.ui.presenters.BaseImageCardPresenter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class KidsFragment(private val category: String) : BaseVerticalGridFragment(category) {
    private val mainFragmentAdapter = BrowseSupportFragment.MainFragmentAdapter(this)
    private lateinit var mAdapter: ArrayObjectAdapter

    companion object {
        private const val TAG = "KidsFragment"

        // Kids-friendly colors
        private val COLORS = arrayOf(
            "#FF6B6B", "#4ECDC4", "#FFD93D", "#6BCB77",
            "#FF9F1C", "#F4A261", "#E9C46A", "#2A9D8F",
            "#9C89B8", "#F08080", "#81A1C1", "#B5838D"
        )
    }

    override fun getMainFragmentAdapter(): BrowseSupportFragment.MainFragmentAdapter<*> = mainFragmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = category.ifEmpty { "🎈 KIDS ZONE 🎈" }

        // গ্রিড প্রেজেন্টার সেটআপ
        gridPresenter = VerticalGridPresenter(FocusHighlight.ZOOM_FACTOR_LARGE).apply {
            numberOfColumns = 3 // ৩ কলাম (বাচ্চাদের জন্য বড় কার্ড)
        }

        mAdapter = ArrayObjectAdapter(KidsChannelPresenter())
        adapter = mAdapter

        loadKidsChannels()
    }

    override fun onStart() {
        try {
            super.onStart()

            // Colorful gradient background
            val gradientDrawable = GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                intArrayOf(
                    Color.parseColor("#667eea"),
                    Color.parseColor("#764ba2"),
                    Color.parseColor("#f093fb"),
                    Color.parseColor("#f5576c")
                )
            )
            view?.background = gradientDrawable

            Log.d(TAG, "Gradient background applied successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Error in onStart: ${e.message}")
            // Fallback to default color if gradient fails
            view?.setBackgroundColor(
                ContextCompat.getColor(requireContext(), R.color.default_background)
            )
        }
    }


    private fun loadKidsChannels() {
        val channels = listOf(
            KidsChannel(
                name = "🎨 Cartoon Network",
                logoUrl = "https://upload.wikimedia.org/wikipedia/commons/8/80/Cartoon_Network_2010_logo.svg",
                streamUrl = "https://example.com/cartoon-network.m3u8",
                ageGroup = "6-12",
                rating = 4.8,
                color = "#FF6B6B"  // color যোগ করুন
            ),
            KidsChannel(
                name = "🌟 Nickelodeon",
                logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7a/Nickelodeon_2009_logo.svg/1200px-Nickelodeon_2009_logo.svg.png",
                streamUrl = "https://example.com/nickelodeon.m3u8",
                ageGroup = "6-14",
                rating = 4.7,
                color = "#4ECDC4"
            ),
            KidsChannel(
                name = "✨ Disney Channel",
                logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/f/f4/Disney_Channel_logo.svg/1200px-Disney_Channel_logo.svg.png",
                streamUrl = "https://example.com/disney.m3u8",
                ageGroup = "6-16",
                rating = 4.9,
                color = "#FFD93D"
            ),
            KidsChannel(
                name = "🐼 Pogo",
                logoUrl = "https://static.toiimg.com/photo/51409680.cms",
                streamUrl = "https://example.com/pogo.m3u8",
                ageGroup = "4-12",
                rating = 4.6,
                color = "#6BCB77"
            ),
            KidsChannel(
                name = "🎵 Hungama TV",
                logoUrl = "https://www.hungama.com/assets/images/logo.png",
                streamUrl = "https://example.com/hungama.m3u8",
                ageGroup = "5-14",
                rating = 4.5,
                color = "#FF9F1C"
            ),
            KidsChannel(
                name = "🐻 Nick Jr.",
                logoUrl = "https://upload.wikimedia.org/wikipedia/en/thumb/a/a1/Nick_Jr._logo_2009.svg/1200px-Nick_Jr._logo_2009.svg.png",
                streamUrl = "https://example.com/nickjr.m3u8",
                ageGroup = "2-7",
                rating = 4.8,
                color = "#F4A261"
            ),
            KidsChannel(
                name = "🍼 BabyTV",
                logoUrl = "https://yt3.googleusercontent.com/ytc/AIdro_lJBL8o5hVloLvLkZ2rQwY9C9X9X9X9X9X9X9X9X9X9X9X=s900-c-k-c0x00ffffff-no-rj",
                streamUrl = "https://example.com/babytv.m3u8",
                ageGroup = "0-4",
                rating = 4.9,
                color = "#E9C46A"
            ),
            KidsChannel(
                name = "🦖 Discovery Kids",
                logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3b/Discovery_Kids_Logo.svg/1200px-Discovery_Kids_Logo.svg.png",
                streamUrl = "https://example.com/discovery-kids.m3u8",
                ageGroup = "6-12",
                rating = 4.7,
                color = "#2A9D8F"
            ),
            KidsChannel(
                name = "🎪 Sony YAY!",
                logoUrl = "https://static.toiimg.com/photo/69264410.cms",
                streamUrl = "https://example.com/sonyyay.m3u8",
                ageGroup = "4-12",
                rating = 4.6,
                color = "#9C89B8"
            ),
            KidsChannel(
                name = "🌈 Super Hungama",
                logoUrl = "https://www.hungama.com/assets/images/super-hungama-logo.png",
                streamUrl = "https://example.com/superhungama.m3u8",
                ageGroup = "6-14",
                rating = 4.5,
                color = "#F08080"
            ),
            KidsChannel(
                name = "🦸‍♂️ Marvel HQ",
                logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0f/Marvel_HQ_logo.svg/1200px-Marvel_HQ_logo.svg.png",
                streamUrl = "https://example.com/marvelhq.m3u8",
                ageGroup = "7-16",
                rating = 4.9,
                color = "#81A1C1"
            ),
            KidsChannel(
                name = "🧸 CBeebies",
                logoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/2/2d/CBeebies_logo.svg/1200px-CBeebies_logo.svg.png",
                streamUrl = "https://example.com/cbeebies.m3u8",
                ageGroup = "2-6",
                rating = 4.8,
                color = "#B5838D"
            )
        )

        channels.forEach { channel ->
            mAdapter.add(channel)
        }
        Log.d(TAG, "Loaded ${channels.size} kids channels")
    }

    // Kids Channel Presenter
    private inner class KidsChannelPresenter : BaseImageCardPresenter<KidsChannel>(450, 350) {
        override fun bindData(container: FrameLayout, imageView: ImageView, data: KidsChannel) {
            try {
                // Container styling
                container.elevation = 8f
                container.background = createCardBackground(data.color)

                // লোগো লোড করুন (rounded corners সহ)
                Glide.with(container.context)
                    .load(data.logoUrl)
                    .transform(CenterCrop(), RoundedCorners(24))
                    .placeholder(R.drawable.ic_kids_placeholder)
                    .error(R.drawable.ic_kids_placeholder)
                    .into(imageView)

                // Rating badge (স্টার রেটিং)
                val ratingBadge = container.findViewById<TextView>(R.id.rating_badge) ?:
                TextView(container.context).apply {
                    id = R.id.rating_badge
                    layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        gravity = android.view.Gravity.TOP or android.view.Gravity.START
                        topMargin = 8
                        leftMargin = 8
                    }
                    setPadding(8, 4, 8, 4)
                    textSize = 14f
                    setTextColor(Color.WHITE)
                    setBackgroundColor(Color.parseColor("#FFA500"))
                    container.addView(this)
                }
                ratingBadge.text = "⭐ ${data.rating}"

                // Age badge
                val ageBadge = container.findViewById<TextView>(R.id.age_badge) ?:
                TextView(container.context).apply {
                    id = R.id.age_badge
                    layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        gravity = android.view.Gravity.TOP or android.view.Gravity.END
                        topMargin = 8
                        rightMargin = 8
                    }
                    setPadding(8, 4, 8, 4)
                    textSize = 14f
                    setTextColor(Color.WHITE)
                    setBackgroundColor(Color.parseColor("#4CAF50"))
                    container.addView(this)
                }
                ageBadge.text = data.ageGroup

                // Bottom info container
                val infoContainer = FrameLayout(container.context).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        gravity = android.view.Gravity.BOTTOM
                    }
                    setBackgroundColor(Color.parseColor("#AA000000"))
                    container.addView(this)
                }

                // Channel name
                val titleView = TextView(container.context).apply {
                    text = data.name
                    setTextColor(Color.WHITE)
                    textSize = 18f
                    isAllCaps = false
                    typeface = android.graphics.Typeface.DEFAULT_BOLD
                    setPadding(16, 12, 16, 12)
                    layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                    )
                }
                infoContainer.addView(titleView)

                // Play button indicator
                val playIndicator = TextView(container.context).apply {
                    text = "▶"
                    setTextColor(Color.WHITE)
                    textSize = 24f
                    layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        gravity = android.view.Gravity.CENTER
                    }
                    visibility = android.view.View.GONE
                    container.addView(this)
                }

                // Click listener
                container.setOnClickListener {
                    try {
                        Toast.makeText(context, "🎬 Loading ${data.name}...", Toast.LENGTH_SHORT).show()
                        val intent = Intent(requireContext(), TvPlayerActivity::class.java)
                        intent.putExtra(TvPlayerActivity.EXTRA_URL, data.streamUrl)
                        startActivity(intent)
                    } catch (e: Exception) {
                        Log.e(TAG, "Error: ${e.message}")
                        Toast.makeText(context, "😢 Cannot play this channel", Toast.LENGTH_SHORT).show()
                    }
                }

                // Focus effects
                container.setOnFocusChangeListener { v, hasFocus ->
                    if (hasFocus) {
                        v.scaleX = 1.08f
                        v.scaleY = 1.08f
                        v.elevation = 20f
                        playIndicator.visibility = android.view.View.VISIBLE
                        v.animate().rotation(2f).setDuration(200).start()
                    } else {
                        v.scaleX = 1f
                        v.scaleY = 1f
                        v.elevation = 8f
                        playIndicator.visibility = android.view.View.GONE
                        v.rotation = 0f
                    }
                }

            } catch (e: Exception) {
                Log.e(TAG, "Error binding data: ${e.message}")
            }
        }

        private fun createCardBackground(colorHex: String): GradientDrawable {
            return GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                cornerRadius = 32f
                colors = intArrayOf(
                    Color.parseColor(colorHex),
                    Color.parseColor(colorHex).let {
                        Color.argb(150, Color.red(it), Color.green(it), Color.blue(it))
                    }
                )
                orientation = GradientDrawable.Orientation.BL_TR
            }
        }
    }

    // Kids Channel Data Class
    // Kids Channel Data Class - color প্রপার্টি সহ
    data class KidsChannel(
        val name: String,
        val logoUrl: String,
        val streamUrl: String,
        val ageGroup: String,
        val rating: Double,
        val color: String = getRandomColor() // ডিফল্ট রঙ
    ) {
        companion object {
            private val COLORS = arrayOf(
                "#FF6B6B", "#4ECDC4", "#FFD93D", "#6BCB77",
                "#FF9F1C", "#F4A261", "#E9C46A", "#2A9D8F"
            )

            private fun getRandomColor(): String = COLORS.random()
        }
    }
}