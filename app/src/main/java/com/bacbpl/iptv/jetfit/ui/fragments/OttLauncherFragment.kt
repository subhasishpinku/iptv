package  com.bacbpl.iptv.jetfit.ui.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.app.VerticalGridSupportFragment
import androidx.leanback.widget.*
import com.bacbpl.iptv.R

class OttLauncherFragment :
    VerticalGridSupportFragment(),
    BrowseSupportFragment.MainFragmentAdapterProvider {

    private val mainFragmentAdapter =
        BrowseSupportFragment.MainFragmentAdapter(this)

    override fun getMainFragmentAdapter():
            BrowseSupportFragment.MainFragmentAdapter<*> = mainFragmentAdapter

    // ---------------- OTT MODEL ----------------

    data class OttApp(
        val name: String,
        val packages: List<String>,
        val playStorePackage: String,
        val iconRes: Int
    )

    // ---------------- OTT LIST ----------------

    private val ottApps = listOf(
        OttApp(
            "Netflix",
            listOf("com.netflix.ninja"),
            "com.netflix.ninja",
            R.drawable.baseline_hotel_class_24
        ),
        OttApp(
            "Prime Video",
            listOf("com.amazon.amazonvideo.livingroom"),
            "com.amazon.amazonvideo.livingroom",
            R.drawable.baseline_hotel_class_24
        ),
        OttApp(
            "YouTube",
            listOf("com.google.android.youtube.tv"),
            "com.google.android.youtube.tv",
            R.drawable.baseline_hotel_class_24
        ),
        OttApp(
            "Hotstar",
            listOf(
                "com.jio.hotstar.tv",
                "com.jiohotstar.android.tv",
                "com.disneyplus.hotstar.tv",
                "in.startv.hotstar.tv"
            ),
            "com.jio.hotstar.tv",
            R.drawable.baseline_hotel_class_24
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = "OTT APPS"

        val presenter = VerticalGridPresenter(FocusHighlight.ZOOM_FACTOR_MEDIUM).apply {
            numberOfColumns = 4
        }
        setGridPresenter(presenter)

        val adapter = ArrayObjectAdapter(OttPresenter())
        ottApps.forEach { adapter.add(it) }
        this.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        view?.setBackgroundColor(
            ContextCompat.getColor(requireContext(), R.color.default_background)
        )
    }

    // ---------------- PRESENTER ----------------

    private inner class OttPresenter : Presenter() {

        override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
            val card = ImageCardView(parent.context).apply {
                isFocusable = true
                isFocusableInTouchMode = true
                setMainImageDimensions(320, 180)
            }
            return ViewHolder(card)
        }

        override fun onBindViewHolder(vh: ViewHolder, item: Any) {
            val app = item as OttApp
            val card = vh.view as ImageCardView

            card.titleText = app.name
            card.mainImage = ContextCompat.getDrawable(
                requireContext(),
                app.iconRes
            )

            vh.view.setOnClickListener {
                launchOrOpenPlayStore(requireContext(), app)
            }
        }

        override fun onUnbindViewHolder(vh: ViewHolder) {}
    }

    // ---------------- CORE LOGIC ----------------

    private fun launchOrOpenPlayStore(context: Context, app: OttApp) {

        val pm = context.packageManager

        // 1️⃣ Try to launch installed app
        for (pkg in app.packages) {

            pm.getLaunchIntentForPackage(pkg)?.let {
                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(it)
                Log.e("OTT", "Launched ${app.name}")
                return
            }

            val leanbackIntent = Intent(Intent.ACTION_MAIN).apply {
                addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER)
                setPackage(pkg)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            if (pm.resolveActivity(leanbackIntent, 0) != null) {
                context.startActivity(leanbackIntent)
                Log.e("OTT", "Leanback launch ${app.name}")
                return
            }
        }

        // 2️⃣ Not installed → Play Store deep link
        openPlayStore(context, app.playStorePackage)
    }

    private fun openPlayStore(context: Context, packageName: String) {
        try {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("market://details?id=$packageName")
            )
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(
                context,
                "Play Store not available",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
