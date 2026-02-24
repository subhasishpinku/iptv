//package  com.bacbpl.iptv.jetfit.ui.fragments
//
//import android.content.Context
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.net.Uri
//import android.os.Bundle
//import android.util.Log
//import android.view.ViewGroup
//import android.widget.FrameLayout
//import android.widget.ImageView
//import android.widget.Toast
//import androidx.core.content.ContextCompat
//import androidx.leanback.app.BrowseSupportFragment
//import androidx.leanback.app.VerticalGridSupportFragment
//import androidx.leanback.widget.*
//import com.bacbpl.iptv.R
//
//class HotStarFragment :
//    VerticalGridSupportFragment(),
//    BrowseSupportFragment.MainFragmentAdapterProvider {
//
//    private val mainFragmentAdapter =
//        BrowseSupportFragment.MainFragmentAdapter(this)
//
//    override fun getMainFragmentAdapter():
//            BrowseSupportFragment.MainFragmentAdapter<*> = mainFragmentAdapter
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        title = "HOTSTAR"
//
//        val gridPresenter = VerticalGridPresenter(FocusHighlight.ZOOM_FACTOR_MEDIUM).apply {
//            numberOfColumns = 1
//        }
//        setGridPresenter(gridPresenter)
//
//        val adapter = ArrayObjectAdapter(HotstarPresenter())
//        adapter.add("HOTSTAR")
//
//        this.adapter = adapter
//    }
//
//    override fun onStart() {
//        super.onStart()
//        view?.setBackgroundColor(
//            ContextCompat.getColor(requireContext(), R.color.default_background)
//        )
//    }
//
//    // ---------------- PRESENTER ----------------
//
//    //    private inner class HotstarPresenter : Presenter() {
//    //
//    //        override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
//    //            val card = ImageCardView(parent.context).apply {
//    //                isFocusable = true
//    //                isFocusableInTouchMode = true
//    //                setMainImageDimensions(600, 340)
//    //                titleText = "JioHotstar"
//    //                contentText = "Launch Hotstar TV App"
//    //                mainImage = ContextCompat.getDrawable(
//    //                    parent.context,
//    //                    R.drawable.jiohotstar
//    //                )
//    //            }
//    //            return ViewHolder(card)
//    //        }
//    //
//    //        override fun onBindViewHolder(vh: ViewHolder, item: Any) {
//    //            (vh.view as? ImageCardView)?.apply {
//    //                setOnClickListener {
//    //                    launchHotstar(requireContext())
//    //                }
//    //                setOnLongClickListener {
//    //                    Toast.makeText(
//    //                        context,
//    //                        "Opens JioHotstar/Disney+ Hotstar TV app",
//    //                        Toast.LENGTH_SHORT
//    //                    ).show()
//    //                    true
//    //                }
//    //            }
//    //        }
//    //
//    //        override fun onUnbindViewHolder(vh: ViewHolder) {}
//    //    }
//
//    private inner class HotstarPresenter : Presenter() {
//
//        override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
//            val container = FrameLayout(parent.context)
//            container.layoutParams = ViewGroup.LayoutParams(600, 340)
//            container.isFocusable = true
//            container.isFocusableInTouchMode = true
//
//            val imageView = ImageView(parent.context)
//            imageView.layoutParams = FrameLayout.LayoutParams(600, 340)
//            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
//            imageView.setImageDrawable(ContextCompat.getDrawable(parent.context, R.drawable.jiohotstar))
//            container.addView(imageView)
//
//            return ViewHolder(container)
//        }
//
//        override fun onBindViewHolder(vh: ViewHolder, item: Any) {
//            val container = vh.view as FrameLayout
//
//            container.setOnClickListener {
//                launchHotstar(requireContext())
//            }
//
//            container.setOnFocusChangeListener { v, hasFocus ->
//                if (hasFocus) {
//                    v.scaleX = 1.05f
//                    v.scaleY = 1.05f
//                    v.elevation = 10f
//                } else {
//                    v.scaleX = 1f
//                    v.scaleY = 1f
//                    v.elevation = 0f
//                }
//            }
//        }
//
//        override fun onUnbindViewHolder(vh: ViewHolder) {}
//    }
//    // ---------------- HOTSTAR LAUNCH (ALL FALLBACKS) ----------------
//
//    private fun launchHotstar(context: Context) {
//        val pm = context.packageManager
//
//        // Try all methods in order
//        if (tryNormalLaunch(pm, context)) return
//        if (tryLeanbackLaunch(pm, context)) return
//        if (tryDeepLinks(context)) return
//        if (tryPackageNamesList(context)) return
//        if (tryClassNamesList(context)) return
//
//        // All methods failed
//        showNotInstalledError(context)
//    }
//
//    private fun tryNormalLaunch(pm: PackageManager, context: Context): Boolean {
//        val packageNames = listOf(
//            "in.jio.hotstar",            // Current JioHotstar mobile
//            "com.jio.hotstar.tv",        // JioHotstar TV
//            "com.disney.hotstar.tv",     // Disney+ Hotstar TV
//            "com.disneyplus.hotstar.tv", // Disney+ Hotstar TV (alternative)
//            "com.hotstar.android",       // Hotstar mobile
//            "com.htstar.tv"              // Hotstar TV (old)
//        )
//
//        for (pkg in packageNames) {
//            try {
//                val launchIntent = pm.getLaunchIntentForPackage(pkg)
//                if (launchIntent != null) {
//                    launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                    context.startActivity(launchIntent)
//                    Log.d("HOTSTAR_LAUNCH", "Normal launch successful: $pkg")
//                    return true
//                }
//            } catch (e: Exception) {
//                Log.e("HOTSTAR_LAUNCH", "Normal launch failed for $pkg", e)
//            }
//        }
//        return false
//    }
//
//    private fun tryLeanbackLaunch(pm: PackageManager, context: Context): Boolean {
//        val leanbackPackageNames = listOf(
//            "com.jio.hotstar.tv",
//            "com.disney.hotstar.tv",
//            "com.disneyplus.hotstar.tv",
//            "com.htstar.tv"
//        )
//
//        for (pkg in leanbackPackageNames) {
//            try {
//                val leanbackIntent = Intent(Intent.ACTION_MAIN).apply {
//                    addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER)
//                    setPackage(pkg)
//                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                }
//
//                if (leanbackIntent.resolveActivity(pm) != null) {
//                    context.startActivity(leanbackIntent)
//                    Log.d("HOTSTAR_LAUNCH", "Leanback launch successful: $pkg")
//                    return true
//                }
//            } catch (e: Exception) {
//                Log.e("HOTSTAR_LAUNCH", "Leanback launch failed for $pkg", e)
//            }
//        }
//        return false
//    }
//
//    private fun tryDeepLinks(context: Context): Boolean {
//        val deepLinks = listOf(
//            "hotstar://",                    // Hotstar scheme
//            "disneyplus://",                 // Disney+ scheme
//            "jiohotstar://",                 // JioHotstar scheme
//            "https://www.hotstar.com",       // Web URL
//            "market://details?id=in.jio.hotstar"  // Play Store
//        )
//
//        for (link in deepLinks) {
//            try {
//                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link)).apply {
//                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                }
//
//                if (intent.resolveActivity(context.packageManager) != null) {
//                    context.startActivity(intent)
//                    Log.d("HOTSTAR_LAUNCH", "Deep link successful: $link")
//                    return true
//                }
//            } catch (e: Exception) {
//                Log.e("HOTSTAR_LAUNCH", "Deep link failed: $link", e)
//            }
//        }
//        return false
//    }
//
//    private fun tryPackageNamesList(context: Context): Boolean {
//        // Try specific intent actions with package names
//        val actions = listOf(
//            Intent.ACTION_MAIN to Intent.CATEGORY_LAUNCHER,
//            Intent.ACTION_VIEW to Intent.CATEGORY_BROWSABLE
//        )
//
//        val packages = listOf(
//            "in.jio.hotstar",
//            "com.jio.hotstar.tv",
//            "com.disney.hotstar.tv"
//        )
//
//        for ((action, category) in actions) {
//            for (pkg in packages) {
//                try {
//                    val intent = Intent(action).apply {
//                        addCategory(category)
//                        setPackage(pkg)
//                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                    }
//
//                    if (intent.resolveActivity(context.packageManager) != null) {
//                        context.startActivity(intent)
//                        Log.d("HOTSTAR_LAUNCH", "Package intent successful: $pkg with $action")
//                        return true
//                    }
//                } catch (e: Exception) {
//                    Log.e("HOTSTAR_LAUNCH", "Package intent failed: $pkg", e)
//                }
//            }
//        }
//        return false
//    }
//
//    private fun tryClassNamesList(context: Context): Boolean {
//        val classMappings = mapOf(
//            "in.jio.hotstar" to listOf(
//                "in.jio.hotstar.ui.splash.SplashActivity",
//                "in.jio.hotstar.MainActivity"
//            ),
//            "com.jio.hotstar.tv" to listOf(
//                "com.hotstar.tv.ui.home.HomeActivity",
//                "com.hotstar.tv.ui.main.MainActivity"
//            ),
//            "com.disney.hotstar.tv" to listOf(
//                "com.hotstar.tv.ui.home.HomeActivity",
//                "com.disney.hotstar.tv.home.HomeActivity"
//            )
//        )
//
//        for ((pkg, classes) in classMappings) {
//            for (className in classes) {
//                try {
//                    val intent = Intent().apply {
//                        setClassName(pkg, className)
//                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                    }
//                    context.startActivity(intent)
//                    Log.d("HOTSTAR_LAUNCH", "Class launch successful: $pkg/$className")
//                    return true
//                } catch (e: Exception) {
//                    Log.e("HOTSTAR_LAUNCH", "Class launch failed: $pkg/$className", e)
//                }
//            }
//        }
//        return false
//    }
//
//    private fun showNotInstalledError(context: Context) {
//        Toast.makeText(
//            context,
//            "Hotstar/JioHotstar app not found.\n\nPlease install from:\n• Jio Store (for Jio set-top box)\n• Google Play Store\n• Hotstar website",
//            Toast.LENGTH_LONG
//        ).show()
//
//        // Open Play Store as last resort
//        try {
//            val intent = Intent(Intent.ACTION_VIEW).apply {
//                data = Uri.parse("market://details?id=in.jio.hotstar")
//                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            }
//            context.startActivity(intent)
//        } catch (e: Exception) {
//            Log.e("HOTSTAR_LAUNCH", "Failed to open Play Store", e)
//        }
//    }
//}

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
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.app.VerticalGridSupportFragment
import androidx.leanback.widget.*
import com.bacbpl.iptv.R
import com.bacbpl.iptv.jetfit.ui.presenters.BaseImageCardPresenter

class HotStarFragment :
    VerticalGridSupportFragment(),
    BrowseSupportFragment.MainFragmentAdapterProvider {

    private val mainFragmentAdapter =
        BrowseSupportFragment.MainFragmentAdapter(this)

    override fun getMainFragmentAdapter():
            BrowseSupportFragment.MainFragmentAdapter<*> = mainFragmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = "HOTSTAR"

        val gridPresenter = VerticalGridPresenter(FocusHighlight.ZOOM_FACTOR_MEDIUM).apply {
            numberOfColumns = 1
        }
        setGridPresenter(gridPresenter)

        val adapter = ArrayObjectAdapter(HotstarPresenter())
        adapter.add("HOTSTAR")

        this.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        view?.setBackgroundColor(
            ContextCompat.getColor(requireContext(), R.color.default_background)
        )
    }

    // ============ আপডেটেড PRESENTER ============
    private inner class HotstarPresenter : BaseImageCardPresenter<String>(600, 340) {
        override fun bindData(container: FrameLayout, imageView: ImageView, data: String) {
            // Set Hotstar icon
            imageView.setImageDrawable(
                ContextCompat.getDrawable(container.context, R.drawable.jiohotstar)
            )

            // Click listener
            container.setOnClickListener {
                launchHotstar(container.context)
            }

            // Long click listener
            container.setOnLongClickListener {
                Toast.makeText(
                    container.context,
                    "Opens JioHotstar/Disney+ Hotstar TV app",
                    Toast.LENGTH_SHORT
                ).show()
                true
            }
        }
    }

    // ---------------- HOTSTAR LAUNCH (ALL FALLBACKS) ----------------
    private fun launchHotstar(context: Context) {
        val pm = context.packageManager

        // Try all methods in order
        if (tryNormalLaunch(pm, context)) return
        if (tryLeanbackLaunch(pm, context)) return
        if (tryDeepLinks(context)) return
        if (tryPackageNamesList(context)) return
        if (tryClassNamesList(context)) return

        // All methods failed
        showNotInstalledError(context)
    }

    private fun tryNormalLaunch(pm: PackageManager, context: Context): Boolean {
        val packageNames = listOf(
            "in.jio.hotstar",            // Current JioHotstar mobile
            "com.jio.hotstar.tv",        // JioHotstar TV
            "com.disney.hotstar.tv",     // Disney+ Hotstar TV
            "com.disneyplus.hotstar.tv", // Disney+ Hotstar TV (alternative)
            "com.hotstar.android",       // Hotstar mobile
            "com.htstar.tv"              // Hotstar TV (old)
        )

        for (pkg in packageNames) {
            try {
                val launchIntent = pm.getLaunchIntentForPackage(pkg)
                if (launchIntent != null) {
                    launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(launchIntent)
                    Log.d("HOTSTAR_LAUNCH", "Normal launch successful: $pkg")
                    return true
                }
            } catch (e: Exception) {
                Log.e("HOTSTAR_LAUNCH", "Normal launch failed for $pkg", e)
            }
        }
        return false
    }

    private fun tryLeanbackLaunch(pm: PackageManager, context: Context): Boolean {
        val leanbackPackageNames = listOf(
            "com.jio.hotstar.tv",
            "com.disney.hotstar.tv",
            "com.disneyplus.hotstar.tv",
            "com.htstar.tv"
        )

        for (pkg in leanbackPackageNames) {
            try {
                val leanbackIntent = Intent(Intent.ACTION_MAIN).apply {
                    addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER)
                    setPackage(pkg)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }

                if (leanbackIntent.resolveActivity(pm) != null) {
                    context.startActivity(leanbackIntent)
                    Log.d("HOTSTAR_LAUNCH", "Leanback launch successful: $pkg")
                    return true
                }
            } catch (e: Exception) {
                Log.e("HOTSTAR_LAUNCH", "Leanback launch failed for $pkg", e)
            }
        }
        return false
    }

    private fun tryDeepLinks(context: Context): Boolean {
        val deepLinks = listOf(
            "hotstar://",                    // Hotstar scheme
            "disneyplus://",                 // Disney+ scheme
            "jiohotstar://",                 // JioHotstar scheme
            "https://www.hotstar.com",       // Web URL
            "market://details?id=in.jio.hotstar"  // Play Store
        )

        for (link in deepLinks) {
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link)).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }

                if (intent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(intent)
                    Log.d("HOTSTAR_LAUNCH", "Deep link successful: $link")
                    return true
                }
            } catch (e: Exception) {
                Log.e("HOTSTAR_LAUNCH", "Deep link failed: $link", e)
            }
        }
        return false
    }

    private fun tryPackageNamesList(context: Context): Boolean {
        val actions = listOf(
            Intent.ACTION_MAIN to Intent.CATEGORY_LAUNCHER,
            Intent.ACTION_VIEW to Intent.CATEGORY_BROWSABLE
        )

        val packages = listOf(
            "in.jio.hotstar",
            "com.jio.hotstar.tv",
            "com.disney.hotstar.tv"
        )

        for ((action, category) in actions) {
            for (pkg in packages) {
                try {
                    val intent = Intent(action).apply {
                        addCategory(category)
                        setPackage(pkg)
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }

                    if (intent.resolveActivity(context.packageManager) != null) {
                        context.startActivity(intent)
                        Log.d("HOTSTAR_LAUNCH", "Package intent successful: $pkg with $action")
                        return true
                    }
                } catch (e: Exception) {
                    Log.e("HOTSTAR_LAUNCH", "Package intent failed: $pkg", e)
                }
            }
        }
        return false
    }

    private fun tryClassNamesList(context: Context): Boolean {
        val classMappings = mapOf(
            "in.jio.hotstar" to listOf(
                "in.jio.hotstar.ui.splash.SplashActivity",
                "in.jio.hotstar.MainActivity"
            ),
            "com.jio.hotstar.tv" to listOf(
                "com.hotstar.tv.ui.home.HomeActivity",
                "com.hotstar.tv.ui.main.MainActivity"
            ),
            "com.disney.hotstar.tv" to listOf(
                "com.hotstar.tv.ui.home.HomeActivity",
                "com.disney.hotstar.tv.home.HomeActivity"
            )
        )

        for ((pkg, classes) in classMappings) {
            for (className in classes) {
                try {
                    val intent = Intent().apply {
                        setClassName(pkg, className)
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                    context.startActivity(intent)
                    Log.d("HOTSTAR_LAUNCH", "Class launch successful: $pkg/$className")
                    return true
                } catch (e: Exception) {
                    Log.e("HOTSTAR_LAUNCH", "Class launch failed: $pkg/$className", e)
                }
            }
        }
        return false
    }

    private fun showNotInstalledError(context: Context) {
        Toast.makeText(
            context,
            "Hotstar/JioHotstar app not found.\n\nPlease install from:\n• Jio Store (for Jio set-top box)\n• Google Play Store\n• Hotstar website",
            Toast.LENGTH_LONG
        ).show()

        // Open Play Store as last resort
        try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("market://details?id=in.jio.hotstar")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Log.e("HOTSTAR_LAUNCH", "Failed to open Play Store", e)
        }
    }
}