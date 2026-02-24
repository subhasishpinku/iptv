package com.bacbpl.iptv.jetfit.ui.fragments

import android.content.Intent
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
import com.bacbpl.iptv.jetfit.ui.activities.TvPlayersActivity
import com.bacbpl.iptv.jetfit.ui.presenters.BaseImageCardPresenter
import com.bumptech.glide.Glide

class DevotionalFragment(private val category: String) :
    VerticalGridSupportFragment(),
    BrowseSupportFragment.MainFragmentAdapterProvider {

    private val mainFragmentAdapter = BrowseSupportFragment.MainFragmentAdapter(this)
    private lateinit var mAdapter: ArrayObjectAdapter

    companion object {
        private const val TAG = "DevotionalFragment"
    }

    override fun getMainFragmentAdapter(): BrowseSupportFragment.MainFragmentAdapter<*> = mainFragmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = category.ifEmpty { "DEVOTIONAL CHANNELS" }

        // গ্রিড প্রেজেন্টার সেটআপ
        gridPresenter = VerticalGridPresenter(FocusHighlight.ZOOM_FACTOR_MEDIUM).apply {
            numberOfColumns = 6 // ৩ কলামে চ্যানেল দেখাবে
        }

        mAdapter = ArrayObjectAdapter(DevotionalChannelPresenter())
        adapter = mAdapter

        loadDevotionalChannels()
    }

    override fun onStart() {
        super.onStart()
        view?.setBackgroundColor(
            ContextCompat.getColor(requireContext(), R.color.default_background)
        )
    }

    private fun loadDevotionalChannels() {
        // ধর্মীয় চ্যানেলের তালিকা
        val channels = listOf(
            TvChannelItem(
                "Sanskar TV",
                "https://www.sanskargroup.in/images/logo.png",
                "https://sanskarlive.sanskargroup.in/hls/test.m3u8"
            ),
            TvChannelItem(
                "Aastha TV",
                "https://www.aastha.tv/images/logo.png",
                "https://aasthalive.astream.in/aastha_bharat/aastha_master.m3u8"
            ),
            TvChannelItem(
                "Peace of Mind",
                "https://play-lh.googleusercontent.com/FMhz8Kw3hZxsVtBkvKRy9ZGQ_EJImRsuTjAV_T4PW7ZTBjKHwhVGdlsJSgdyeTGW15A=w526-h296-rw",
                "rtmp://185.193.19.32:1935/static/baktitv"
            ),
            TvChannelItem(
                "Ishwar TV",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRR0UPvb6_mWHmiqn49ztVC4pmroDSl06d-0g&s",
                "https://ishwartv.astream.in/ishwartv/ishwartv_master.m3u8"
            ),
            TvChannelItem(
                "Bhakti TV",
                "https://static.wikia.nocookie.net/etv-gspn-bangla/images/f/fe/Bangla_Bhakti_logo_%282020%29.png/revision/latest?cb=20230510105504",
                "http://115.187.41.216:8080/hls/bhaktibangla/index.m3u8"
            ),
            TvChannelItem(
                "Divya TV",
                "https://yt3.googleusercontent.com/ytc/AIdro_mF09sq2C17-S7RNo_0Bg4jfZHAPF9JtLHc1YDgzxvWPA=s900-c-k-c0x00ffffff-no-rj",
                "https://divyatv.astream.in/divyatv/divyatv_master.m3u8"
            ),
            TvChannelItem(
                "Tulsi TV",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTYxgzAohREFZtBKn-T6cIiMMRQ0SZORhHoYA&s",
                "https://tulsitv.astream.in/tulsitv/tulsitv_master.m3u8"
            ),
            TvChannelItem(
                "Sadhna TV",
                "https://www.sadhnatv.org/images/logo.png",
                "https://sadhnalive.sadhnatv.org/hls/test.m3u8"
            ),
            TvChannelItem(
                "Darshan 24",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSIVSW63OS-YIryP3InB1Bt3QrDxYPYAK9u0A&s",
                "https://darshan24.astream.in/darshan24/darshan24_master.m3u8"
            ),
            TvChannelItem(
                "Shubh TV",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR5fZsVfjAkwpyK_oetMMtvZAFBCdMnCtzbbA&s",
                "https://shubhtv.astream.in/shubhtv/shubhtv_master.m3u8"
            ),
            TvChannelItem(
                "Vedic TV",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSuHnZag5qznWdoJQH2hE2aIU16s5Wb4gRdvw&s",
                "https://vedictv.astream.in/vedictv/vedictv_master.m3u8"
            ),
            TvChannelItem(
                "Gita TV",
                "https://m.media-amazon.com/images/M/MV5BNGYwNDlmZDgtMDg1Yi00N2JmLTk0NzQtNWVkN2NiMTQxY2RlXkEyXkFqcGc@._V1_.jpg",
                "https://gitatv.astream.in/gitatv/gitatv_master.m3u8"
            )
        )

        channels.forEach { channel ->
            mAdapter.add(channel)
        }
        Log.d(TAG, "Loaded ${channels.size} devotional channels")
    }

    // Devotional Channel Presenter
    private inner class DevotionalChannelPresenter : BaseImageCardPresenter<TvChannelItem>(350, 250) {
        override fun bindData(container: FrameLayout, imageView: ImageView, data: TvChannelItem) {
            try {
                // চ্যানেল লোগো লোড করুন
                Glide.with(container.context)
                    .load(data.logo)
                    .centerCrop()
                    .placeholder(android.R.color.darker_gray)
                    .error(R.drawable.ic_baseline_connected_tv_24) // ডিফল্ট ইমেজ
                    .into(imageView)

                // ধর্মীয় ব্যাজ যোগ করুন (ঐচ্ছিক)
                val badgeView = container.findViewById<TextView>(R.id.devotional_badge) ?:
                TextView(container.context).apply {
                    id = R.id.devotional_badge
                    text = "🕉" // ওম চিহ্ন
                    setTextColor(android.graphics.Color.parseColor("#FF9933")) // সাফরন রং
                    textSize = 20f
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

                // চ্যানেলের নাম দেখানোর জন্য TextView
                val titleView = container.findViewById<TextView>(R.id.channel_title) ?:
                TextView(container.context).apply {
                    id = R.id.channel_title
                    setTextColor(android.graphics.Color.WHITE)
                    setBackgroundColor(android.graphics.Color.parseColor("#AA000000"))
                    setPadding(8, 8, 8, 8)
                    textSize = 14f
                    maxLines = 1
                    ellipsize = android.text.TextUtils.TruncateAt.END
                    layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        gravity = android.view.Gravity.BOTTOM
                    }
                    container.addView(this)
                }
                titleView.text = data.name

                // ক্লিক লিসেনার - প্লেয়ার খুলবে
                container.setOnClickListener {
                    try {
                        val intent = Intent(requireContext(), TvPlayersActivity::class.java)
                        intent.putExtra(TvPlayerActivity.EXTRA_URL, data.streamUrl)
                        startActivity(intent)
                    } catch (e: Exception) {
                        Log.e(TAG, "Error opening player: ${e.message}")
                        Toast.makeText(context, "Cannot play this channel", Toast.LENGTH_SHORT).show()
                    }
                }

                // ফোকাস ইফেক্ট
                container.setOnFocusChangeListener { v, hasFocus ->
                    if (hasFocus) {
                        v.scaleX = 1.05f
                        v.scaleY = 1.05f
                        v.elevation = 10f
                        titleView.setBackgroundColor(android.graphics.Color.parseColor("#FFFF9933")) // সাফরন
                    } else {
                        v.scaleX = 1f
                        v.scaleY = 1f
                        v.elevation = 0f
                        titleView.setBackgroundColor(android.graphics.Color.parseColor("#AA000000"))
                    }
                }

            } catch (e: Exception) {
                Log.e(TAG, "Error binding data: ${e.message}")
            }
        }
    }
}