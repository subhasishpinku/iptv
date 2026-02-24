package com.bacbpl.iptv.jetfit.ui.fragments

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
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
import com.bacbpl.iptv.jetfit.ui.presenters.BaseImageCardPresenter

class OTTAllFragment(private val category: String) :
    VerticalGridSupportFragment(),
    BrowseSupportFragment.MainFragmentAdapterProvider {

    private val mainFragmentAdapter = BrowseSupportFragment.MainFragmentAdapter(this)
    private lateinit var mAdapter: ArrayObjectAdapter

    // OTT অ্যাপের তালিকা
    private val ottApps = listOf(
        OTTApp("Amazon Prime Video", R.drawable.amzone_prime, "amazon"),
        OTTApp("Netflix", R.drawable.netflix, "netflix"),
        OTTApp("Hotstar", R.drawable.jiohotstar, "hotstar"),
        OTTApp("YouTube", R.drawable.youtub, "youtube"),
        OTTApp("SonyLIV", R.drawable.sonyliv, "sonyliv"),
        OTTApp("ZEE5", R.drawable.zee5, "zee5"),
        OTTApp("JioCinema", R.drawable.jiocinema, "jiocinema"),
        OTTApp("Disney+ Hotstar", R.drawable.jiohotstar, "disney")
    )

    override fun getMainFragmentAdapter(): BrowseSupportFragment.MainFragmentAdapter<*> = mainFragmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = category.ifEmpty { "ALL OTT APPS" }

        // গ্রিড প্রেজেন্টার সেটআপ
        gridPresenter = VerticalGridPresenter(FocusHighlight.ZOOM_FACTOR_MEDIUM).apply {
            numberOfColumns = 6 // ৩ কলামে OTT অ্যাপ দেখাবে
        }

        mAdapter = ArrayObjectAdapter(OTTAppPresenter())
        adapter = mAdapter

        // OTT অ্যাপগুলো অ্যাড করুন
        ottApps.forEach { app ->
            mAdapter.add(app)
        }
    }

    override fun onStart() {
        super.onStart()
        view?.setBackgroundColor(
            ContextCompat.getColor(requireContext(), R.color.default_background)
        )
    }

    // OTT অ্যাপের জন্য Presenter
    private inner class OTTAppPresenter : BaseImageCardPresenter<OTTApp>(400, 350) {
        override fun bindData(container: FrameLayout, imageView: ImageView, data: OTTApp) {
            try {
                // অ্যাপ আইকন সেট করুন
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(container.context, data.iconResId)
                )
                imageView.scaleType = ImageView.ScaleType.FIT_CENTER
                imageView.setPadding(20, 20, 20, 20)

                // ক্লিক লিসেনার - অ্যাপ খুলবে
                container.setOnClickListener {
                    launchOTTApp(container.context, data)
                }

                // লং ক্লিক লিসেনার
                container.setOnLongClickListener {
                    Toast.makeText(
                        container.context,
                        "Open ${data.name} app",
                        Toast.LENGTH_SHORT
                    ).show()
                    true
                }

                // অ্যাপের নাম দেখানোর জন্য TextView
                val titleView = container.findViewById<TextView>(R.id.ott_title) ?:
                TextView(container.context).apply {
                    id = R.id.ott_title
                    text = data.name
                    setTextColor(android.graphics.Color.WHITE)
                    setBackgroundColor(android.graphics.Color.parseColor("#80000000"))
                    setPadding(8, 8, 8, 8)
                    textSize = 14f
                    gravity = android.view.Gravity.CENTER
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
                Log.e("OTTAllFragment", "Error binding data: ${e.message}")
            }
        }
    }

    // OTT অ্যাপ লঞ্চ করার ফাংশন
    private fun launchOTTApp(context: Context, app: OTTApp) {
        when (app.type) {
            "amazon" -> launchAmazonVideo(context)
            "netflix" -> launchNetflix(context)
            "hotstar", "disney" -> launchHotstar(context)
            "youtube" -> launchYouTube(context)
            else -> {
                // ডিফল্ট: প্লে স্টোর খুলবে
                openPlayStore(context, getPlayStorePackage(app.type))
            }
        }
    }

    // প্লে স্টোর থেকে অ্যাপ ইনস্টল করার লিংক
    private fun openPlayStore(context: Context, packageName: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: Exception) {
            // প্লে স্টোর না থাকলে ওয়েব ব্রাউজার খুলবে
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName"))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    private fun getPlayStorePackage(type: String): String {
        return when (type) {
            "amazon" -> "com.amazon.amazonvideo.livingroom"
            "netflix" -> "com.netflix.ninja"
            "hotstar", "disney" -> "in.jio.hotstar"
            "youtube" -> "com.google.android.youtube.tv"
            "sonyliv" -> "com.sonyliv"
            "zee5" -> "com.graymatrix.did"
            "jiocinema" -> "com.jio.jiocinema"
            else -> "com.amazon.amazonvideo.livingroom"
        }
    }

    // নিচের মেথডগুলো আপনার existing কোড থেকে নেওয়া
    private fun launchAmazonVideo(context: Context) {
        val pm = context.packageManager
        if (tryNormalLaunch(pm, context,
                listOf("com.amazon.amazonvideo.livingroom", "com.amazon.avod", "com.amazon.firetv.primevideo"))) return
        if (tryDeepLinks(context, listOf("amazonvideo://", "primevideo://", "https://www.primevideo.com"))) return
        openPlayStore(context, "com.amazon.amazonvideo.livingroom")
    }

    private fun launchNetflix(context: Context) {
        val pm = context.packageManager
        if (tryNormalLaunch(pm, context,
                listOf("com.netflix.ninja", "com.netflix.mediaclient", "com.netflix.tv"))) return
        if (tryDeepLinks(context, listOf("netflix://", "nflx://", "https://www.netflix.com"))) return
        openPlayStore(context, "com.netflix.ninja")
    }

    private fun launchHotstar(context: Context) {
        val pm = context.packageManager
        if (tryNormalLaunch(pm, context,
                listOf("in.jio.hotstar", "com.jio.hotstar.tv", "com.disney.hotstar.tv"))) return
        if (tryDeepLinks(context, listOf("hotstar://", "disneyplus://", "https://www.hotstar.com"))) return
        openPlayStore(context, "in.jio.hotstar")
    }

    private fun launchYouTube(context: Context) {
        val pm = context.packageManager
        if (tryNormalLaunch(pm, context,
                listOf("com.google.android.youtube.tv", "com.google.android.youtube", "com.google.android.youtube.leanback"))) return
        if (tryDeepLinks(context, listOf("youtube://", "vnd.youtube://", "https://www.youtube.com/tv"))) return
        openPlayStore(context, "com.google.android.youtube.tv")
    }

    private fun tryNormalLaunch(pm: PackageManager, context: Context, packageNames: List<String>): Boolean {
        for (pkg in packageNames) {
            try {
                val launchIntent = pm.getLaunchIntentForPackage(pkg)
                if (launchIntent != null) {
                    launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(launchIntent)
                    Log.d("OTT_LAUNCH", "Launched: $pkg")
                    return true
                }
            } catch (e: Exception) {
                Log.e("OTT_LAUNCH", "Failed: $pkg", e)
            }
        }
        return false
    }

    private fun tryDeepLinks(context: Context, deepLinks: List<String>): Boolean {
        for (link in deepLinks) {
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                if (intent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(intent)
                    Log.d("OTT_LAUNCH", "Deep link: $link")
                    return true
                }
            } catch (e: Exception) {
                Log.e("OTT_LAUNCH", "Deep link failed: $link", e)
            }
        }
        return false
    }

    // OTT অ্যাপের ডেটা ক্লাস
    data class OTTApp(
        val name: String,
        val iconResId: Int,
        val type: String
    )
}