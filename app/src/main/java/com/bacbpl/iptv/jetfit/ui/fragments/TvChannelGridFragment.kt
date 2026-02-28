
//import android.os.Bundle
//import androidx.leanback.app.VerticalGridSupportFragment
//import androidx.leanback.widget.ArrayObjectAdapter
//import androidx.leanback.widget.FocusHighlight
//import androidx.leanback.widget.VerticalGridPresenter
//import  com.bacbpl.iptv.jetfit.R
//import  com.bacbpl.iptv.jetfit.models.TvChannelItem
//import  com.bacbpl.iptv.jetfit.ui.presenters.TvChannelPresenter
//
//class TvChannelGridFragment(private val titleText: String) :
//    VerticalGridSupportFragment() {
//
//    private lateinit var mAdapter: ArrayObjectAdapter
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        title = titleText
//
//        gridPresenter = VerticalGridPresenter(FocusHighlight.ZOOM_FACTOR_MEDIUM).apply {
//            numberOfColumns = 4
//        }
//
//        mAdapter = ArrayObjectAdapter(TvChannelPresenter())
//        adapter = mAdapter
//
//        loadChannels()
//    }
//
//    private fun loadChannels() {
//        mAdapter.add(
//            TvChannelItem(
//                "News Channel",
//                R.drawable.baseline_connected_tv_24,
//                "https://test-stream.m3u8"
//            )
//        )
//
//        mAdapter.add(
//            TvChannelItem(
//                "Sports TV",
//                R.drawable.baseline_connected_tv_24,
//                "https://test-stream.m3u8"
//            )
//        )
//
//        mAdapter.add(
//            TvChannelItem(
//                "Movie TV",
//                R.drawable.baseline_connected_tv_24,
//                "https://test-stream.m3u8"
//            )
//        )
//    }
//}
package  com.bacbpl.iptv.jetfit.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.app.VerticalGridSupportFragment

import androidx.leanback.widget.*
import com.bacbpl.iptv.R
import com.bacbpl.iptv.jetfit.models.TvChannelItem
import  com.bacbpl.iptv.jetfit.ui.activities.TvPlayerActivity
import  com.bacbpl.iptv.jetfit.ui.presenters.TvChannelPresenter

class TvChannelGridFragment :
    VerticalGridSupportFragment(),
    BrowseSupportFragment.MainFragmentAdapterProvider {

    private lateinit var mAdapter: ArrayObjectAdapter

    // 🔑 REQUIRED by BrowseSupportFragment
    private val mMainFragmentAdapter =
        BrowseSupportFragment.MainFragmentAdapter(this)

    override fun getMainFragmentAdapter(): BrowseSupportFragment.MainFragmentAdapter<*> {
        return mMainFragmentAdapter
    }

    companion object {
        private const val ARG_TITLE = "title"

        fun newInstance(title: String): TvChannelGridFragment {
            val fragment = TvChannelGridFragment()
            val bundle = Bundle()
            bundle.putString(ARG_TITLE, title)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = arguments?.getString(ARG_TITLE) ?: ""

        gridPresenter = VerticalGridPresenter(FocusHighlight.ZOOM_FACTOR_MEDIUM).apply {
            numberOfColumns = 6
        }

        mAdapter = ArrayObjectAdapter(TvChannelPresenter())
        adapter = mAdapter

        loadChannels()

        // ▶ CLICK → PLAYER
        setOnItemViewClickedListener { _, item, _, _ ->
            val channel = item as TvChannelItem
            val intent = Intent(requireContext(), TvPlayerActivity::class.java)
            intent.putExtra(TvPlayerActivity.EXTRA_URL, channel.streamUrl)
            startActivity(intent)
        }
    }
    private fun loadChannels() {
        mAdapter.add(TvChannelItem(
            "Amar Bangla",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTYxgzAohREFZtBKn-T6cIiMMRQ0SZORhHoYA&s",
            "http://115.187.41.216:8080/hls/amarbangla/index.m3u8"
        ))

        mAdapter.add(TvChannelItem(
            "Amar Digital",
            "https://yt3.googleusercontent.com/ytc/AIdro_mF09sq2C17-S7RNo_0Bg4jfZHAPF9JtLHc1YDgzxvWPA=s900-c-k-c0x00ffffff-no-rj",
            "http://115.187.41.216:8080/hls/amardigital/index.m3u8"
        ))

        mAdapter.add(
            TvChannelItem(
                "Montv Bangla",
                "https://jiotvimages.cdn.jio.com/dare_images/images/channel/c455ca0e9fe90ef63458716120b5abd1.png",
                "http://115.187.41.216:8080/hls/montvbangla/index.m3u8"
            )
        )

        mAdapter.add(TvChannelItem(
            "Bhakti Bangla",
            "https://static.wikia.nocookie.net/etv-gspn-bangla/images/f/fe/Bangla_Bhakti_logo_%282020%29.png/revision/latest?cb=20230510105504",
            "http://115.187.41.216:8080/hls/bhaktibangla/index.m3u8"
        ))

        mAdapter.add(TvChannelItem(
            "Salam Bangla",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRR0UPvb6_mWHmiqn49ztVC4pmroDSl06d-0g&s",
            "http://115.187.41.216:8080/hls/salambangla/index.m3u8"
        ))

        mAdapter.add(TvChannelItem(
            "Digital Fashion",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR5fZsVfjAkwpyK_oetMMtvZAFBCdMnCtzbbA&s",
            "http://115.187.41.216:8080/hls/digitalfashion/index.m3u8"
        ))

        mAdapter.add(TvChannelItem(
            "Sananda TV",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSIVSW63OS-YIryP3InB1Bt3QrDxYPYAK9u0A&s",
            "http://115.187.41.216:8080/hls/sanandatv/index.m3u8"
        ))

        mAdapter.add(TvChannelItem(
            "Channel Vision",
            "https://d3jnkp3lrs2hd5.cloudfront.net/images/320x180/b38cd969-3a15-4c04-ac64-2df2b662729a.jpg",
            "rtmp://channelvision.livebox.co.in:1935/channelvision/live"
        ))

        mAdapter.add(TvChannelItem(
            "Hksk Live",
            "https://play-lh.googleusercontent.com/S2gWoKZx5ftE24G3p_KMOneYRoipV5h4BEWOTS00RLhfrrk_4RnfGwHFplECSkoE2uTIXD8R-gDAKRoxlEyd4I4",
            "rtmp://hksk.dataplayer.in:1935/live/channel1"
        ))

        mAdapter.add(TvChannelItem(
            "Bakti TV",
            "https://play-lh.googleusercontent.com/FMhz8Kw3hZxsVtBkvKRy9ZGQ_EJImRsuTjAV_T4PW7ZTBjKHwhVGdlsJSgdyeTGW15A=w526-h296-rw",
            "rtmp://185.193.19.32:1935/static/baktitv"
        ))

        mAdapter.add(TvChannelItem(
            "Stream 103",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSuHnZag5qznWdoJQH2hE2aIU16s5Wb4gRdvw&s",
            "http://103.67.97.46:80/0.ts"
        ))

        mAdapter.add(TvChannelItem(
            "BBC News",
            "https://m.media-amazon.com/images/M/MV5BNGYwNDlmZDgtMDg1Yi00N2JmLTk0NzQtNWVkN2NiMTQxY2RlXkEyXkFqcGc@._V1_.jpg",
            "https://cdn4.skygo.mn/live/disk1/BBC_News/HLSv3-FTA/BBC_News.m3u8"
        ))

        mAdapter.add(TvChannelItem(
            "Ekhon Kolkata",
            "https://i.ytimg.com/vi/JnC6n7ddxMU/hqdefault.jpg",
            "rtmp://live.dataplayer.in:1935/live/ekhonkolkata"
        ))

        mAdapter.add(TvChannelItem(
            "35mm Live",
            "https://daex9l847wg3n.cloudfront.net/shemoutputimages/35-MM/63b6e6d7988cd4d98f45946d/large_16_9_1723016573.jpg",
            "rtmp://legitpro.co.in/live6/35mm"
        ))

        mAdapter.add(TvChannelItem(
            "Inception",
            "http://192.168.1.11:8080/banners/inception/Inception-LeonardoDiCaprio-ChristopherNolan-HollywoodSciFiMoviePoster_66029b94-50ae-494c-b11d-60a3d91268b5.jpg",
            "http://192.168.1.8:8080/vod/vod_inception/inception.mp4"
        ))
        mAdapter.add(TvChannelItem(
            "Dhurandar",
            "http://192.168.1.8:8080/banners/dhurandar/dhurandhar1763462432_2.jpeg",
            "http://192.168.1.11:8080/vod/vod_dhurandar/dh1.mp4"
        ))
    }
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
        }
    }
}
