package com.bacbpl.iptv.jetfit.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.app.VerticalGridSupportFragment
import androidx.leanback.widget.*
import com.bacbpl.iptv.R
import com.bacbpl.iptv.jetfit.data.api.OTTPlayApiClient
import com.bacbpl.iptv.jetfit.models.ottplay.OTTPlayItem
import com.bacbpl.iptv.jetfit.models.ottplay.OTTPlayResponse
import com.bacbpl.iptv.jetfit.ui.presenters.BaseImageCardPresenter
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OTTPlayFragment(private val category: String) :
    VerticalGridSupportFragment(),
    BrowseSupportFragment.MainFragmentAdapterProvider {

    private val mainFragmentAdapter = BrowseSupportFragment.MainFragmentAdapter(this)
    private lateinit var mAdapter: ArrayObjectAdapter

    override fun getMainFragmentAdapter(): BrowseSupportFragment.MainFragmentAdapter<*> = mainFragmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = category.ifEmpty { "OTTplay" }

        gridPresenter = VerticalGridPresenter(FocusHighlight.ZOOM_FACTOR_MEDIUM).apply {
            numberOfColumns = 6
        }

        mAdapter = ArrayObjectAdapter(OTTPlayItemPresenter())
        adapter = mAdapter

        loadDataFromAPI()
    }

    private fun loadDataFromAPI() {
        val seoUrl = "partner/bacbpl-test/a344dab0cc8"
        val t = "955414"

        // OTTPlayApiClient ব্যবহার করুন
        OTTPlayApiClient.apiService.getOTTPlayWidgets(seoUrl, t).enqueue(object : Callback<OTTPlayResponse> {
            override fun onResponse(call: Call<OTTPlayResponse>, response: Response<OTTPlayResponse>) {
                if (response.isSuccessful) {
                    val widgets = response.body()?.widgets
                    if (!widgets.isNullOrEmpty()) {
                        widgets.forEach { widget ->
                            widget.data.forEach { item ->
                                mAdapter.add(item)
                            }
                        }
                        Log.d("OTTPlayFragment", "Loaded ${mAdapter.size()} items")
                    } else {
                        Log.e("OTTPlayFragment", "No data found")
                    }
                } else {
                    Log.e("OTTPlayFragment", "API Error: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<OTTPlayResponse>, t: Throwable) {
                Log.e("OTTPlayFragment", "Network Failure: ${t.message}")
            }
        })
    }

    private inner class OTTPlayItemPresenter : BaseImageCardPresenter<OTTPlayItem>(300, 450) {
        override fun bindData(container: FrameLayout, imageView: ImageView, data: OTTPlayItem) {
            try {
                // Glide দিয়ে ইমেজ লোড
                Glide.with(container.context)
                    .load(data.posterImage)
                    .centerCrop()
                    .placeholder(android.R.color.darker_gray)
                    .error(android.R.color.holo_red_dark)
                    .into(imageView)

                // ক্লিক করলে OTTplay ওয়েবসাইট খুলবে
                container.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(data.ottplayUrl))
                    startActivity(intent)
                }

                // টাইটেল দেখানোর জন্য TextView
                val titleView = container.findViewById<TextView>(android.R.id.title) ?:
                TextView(container.context).apply {
                    id = android.R.id.title
                    setTextColor(android.graphics.Color.WHITE)
                    setBackgroundColor(android.graphics.Color.parseColor("#80000000"))
                    setPadding(8, 8, 8, 8)
                    layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        gravity = android.view.Gravity.BOTTOM
                    }
                    container.addView(this)
                }

                titleView.text = data.name
            } catch (e: Exception) {
                Log.e("OTTPlayFragment", "Error binding data: ${e.message}")
            }
        }
    }
}