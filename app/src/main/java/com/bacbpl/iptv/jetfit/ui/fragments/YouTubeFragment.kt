//package  com.bacbpl.iptv.jetfit.ui.fragments
//
//import android.content.Context
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.net.Uri
//import android.os.Bundle
//import android.util.Log
//import android.view.ViewGroup
//import android.widget.Toast
//import androidx.core.content.ContextCompat
//import androidx.leanback.app.BrowseSupportFragment
//import androidx.leanback.app.VerticalGridSupportFragment
//import androidx.leanback.widget.*
//import com.bacbpl.iptv.R
//
//class YouTubeFragment :
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
//        title = "YOUTUBE"
//
//        val gridPresenter = VerticalGridPresenter(FocusHighlight.ZOOM_FACTOR_MEDIUM).apply {
//            numberOfColumns = 1
//        }
//        setGridPresenter(gridPresenter)
//
//        val adapter = ArrayObjectAdapter(YouTubePresenter())
//        adapter.add("YOUTUBE")
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
//    private inner class YouTubePresenter : Presenter() {
//
//        override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
//            val card = ImageCardView(parent.context).apply {
//                isFocusable = true
//                isFocusableInTouchMode = true
//                setMainImageDimensions(600, 340)
//                titleText = "YouTube"
//                contentText = "Launch YouTube TV App"
//                mainImage = ContextCompat.getDrawable(
//                    parent.context,
//                    R.drawable.youtub // Make sure this matches your drawable name
//                )
//            }
//            return ViewHolder(card)
//        }
//
//        override fun onBindViewHolder(vh: ViewHolder, item: Any) {
//            (vh.view as? ImageCardView)?.apply {
//                setOnClickListener {
//                    launchYouTube(requireContext())
//                }
//                setOnLongClickListener {
//                    Toast.makeText(
//                        context,
//                        "Opens YouTube TV app",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    true
//                }
//            }
//        }
//
//        override fun onUnbindViewHolder(vh: ViewHolder) {}
//    }
//
//    // ---------------- YOUTUBE LAUNCH (ALL FALLBACKS) ----------------
//
//    private fun launchYouTube(context: Context) {
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
//            "com.google.android.youtube.tv",       // YouTube TV (main)
//            "com.google.android.youtube.tvkids",   // YouTube Kids TV
//            "com.google.android.apps.youtube.kids", // YouTube Kids
//            "com.google.android.youtube",          // YouTube mobile
//            "com.google.android.youtube.go",       // YouTube Go
//            "com.google.android.youtube.leanback"  // YouTube Leanback
//        )
//
//        for (pkg in packageNames) {
//            try {
//                val launchIntent = pm.getLaunchIntentForPackage(pkg)
//                if (launchIntent != null) {
//                    launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                    context.startActivity(launchIntent)
//                    Log.d("YOUTUBE_LAUNCH", "Normal launch successful: $pkg")
//                    return true
//                }
//            } catch (e: Exception) {
//                Log.e("YOUTUBE_LAUNCH", "Normal launch failed for $pkg", e)
//            }
//        }
//        return false
//    }
//
//    private fun tryLeanbackLaunch(pm: PackageManager, context: Context): Boolean {
//        val leanbackPackageNames = listOf(
//            "com.google.android.youtube.tv",
//            "com.google.android.youtube.tvkids",
//            "com.google.android.youtube.leanback"
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
//                    Log.d("YOUTUBE_LAUNCH", "Leanback launch successful: $pkg")
//                    return true
//                }
//            } catch (e: Exception) {
//                Log.e("YOUTUBE_LAUNCH", "Leanback launch failed for $pkg", e)
//            }
//        }
//        return false
//    }
//
//    private fun tryDeepLinks(context: Context): Boolean {
//        val deepLinks = listOf(
//            "youtube://",                                   // YouTube scheme
//            "vnd.youtube://",                               // YouTube vnd scheme
//            "https://www.youtube.com",                      // Web URL
//            "https://youtube.com/tv",                       // YouTube TV web
//            "market://details?id=com.google.android.youtube.tv" // Play Store
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
//                    Log.d("YOUTUBE_LAUNCH", "Deep link successful: $link")
//                    return true
//                }
//            } catch (e: Exception) {
//                Log.e("YOUTUBE_LAUNCH", "Deep link failed: $link", e)
//            }
//        }
//        return false
//    }
//
//    private fun tryPackageNamesList(context: Context): Boolean {
//        val actions = listOf(
//            Intent.ACTION_MAIN to Intent.CATEGORY_LAUNCHER,
//            Intent.ACTION_VIEW to Intent.CATEGORY_BROWSABLE
//        )
//
//        val packages = listOf(
//            "com.google.android.youtube.tv",
//            "com.google.android.youtube",
//            "com.google.android.apps.youtube.kids"
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
//                        Log.d("YOUTUBE_LAUNCH", "Package intent successful: $pkg with $action")
//                        return true
//                    }
//                } catch (e: Exception) {
//                    Log.e("YOUTUBE_LAUNCH", "Package intent failed: $pkg", e)
//                }
//            }
//        }
//        return false
//    }
//
//    private fun tryClassNamesList(context: Context): Boolean {
//        val classMappings = listOf(
//            // YouTube TV variants
//            Pair("com.google.android.youtube.tv", "com.google.android.apps.youtube.tv.activity.MainActivity"),
//            Pair("com.google.android.youtube.tv", "com.google.android.apps.youtube.tv.activity.ShellActivity"),
//            Pair("com.google.android.youtube.tv", "com.google.android.youtube.tv.TVActivity"),
//
//            // YouTube mobile variants
//            Pair("com.google.android.youtube", "com.google.android.apps.youtube.app.WatchWhileActivity"),
//            Pair("com.google.android.youtube", "com.google.android.apps.youtube.app.application.Shell\$HomeActivity"),
//
//            // YouTube Kids
//            Pair("com.google.android.youtube.tvkids", "com.google.android.apps.youtube.tvkids.activity.MainActivity"),
//            Pair("com.google.android.apps.youtube.kids", "com.google.android.apps.youtube.kids.home.HomeActivity")
//        )
//
//        for ((pkg, className) in classMappings) {
//            try {
//                val intent = Intent().apply {
//                    setClassName(pkg, className)
//                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                }
//                context.startActivity(intent)
//                Log.d("YOUTUBE_LAUNCH", "Class launch successful: $pkg/$className")
//                return true
//            } catch (e: Exception) {
//                Log.e("YOUTUBE_LAUNCH", "Class launch failed: $pkg/$className", e)
//            }
//        }
//        return false
//    }
//
//    private fun showNotInstalledError(context: Context) {
//        Toast.makeText(
//            context,
//            "YouTube TV app not found.\n\nPlease install from:\n• Google Play Store\n• YouTube website\n• Device manufacturer's app store",
//            Toast.LENGTH_LONG
//        ).show()
//
//        // Open Play Store as last resort
//        try {
//            val intent = Intent(Intent.ACTION_VIEW).apply {
//                data = Uri.parse("market://details?id=com.google.android.youtube.tv")
//                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            }
//            context.startActivity(intent)
//        } catch (e: Exception) {
//            Log.e("YOUTUBE_LAUNCH", "Failed to open Play Store", e)
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
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.app.VerticalGridSupportFragment
import androidx.leanback.widget.*
import com.bacbpl.iptv.R
import com.bacbpl.iptv.jetfit.ui.presenters.BaseImageCardPresenter

class YouTubeFragment :
    VerticalGridSupportFragment(),
    BrowseSupportFragment.MainFragmentAdapterProvider {

    private val mainFragmentAdapter =
        BrowseSupportFragment.MainFragmentAdapter(this)

    override fun getMainFragmentAdapter():
            BrowseSupportFragment.MainFragmentAdapter<*> = mainFragmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = "YOUTUBE"

        val gridPresenter = VerticalGridPresenter(FocusHighlight.ZOOM_FACTOR_MEDIUM).apply {
            numberOfColumns = 1
        }
        setGridPresenter(gridPresenter)

        // IMPORTANT: এখানে BaseImageCardPresenter-based Presenter ব্যবহার করুন
        val adapter = ArrayObjectAdapter(YouTubePresenter())
        adapter.add("YOUTUBE") // Dummy data

        this.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        view?.setBackgroundColor(
            ContextCompat.getColor(requireContext(), R.color.default_background)
        )
    }

    // ============ BaseImageCardPresenter-based PRESENTER ============
    private inner class YouTubePresenter : BaseImageCardPresenter<String>(600, 340) {
        override fun bindData(container: FrameLayout, imageView: ImageView, data: String) {
            try {
                // Set YouTube icon
                val drawable = ContextCompat.getDrawable(container.context, R.drawable.youtub)
                imageView.setImageDrawable(drawable)

                // Click listener
                container.setOnClickListener {
                    launchYouTube(container.context)
                }

                // Long click listener
                container.setOnLongClickListener {
                    Toast.makeText(
                        container.context,
                        "Opens YouTube TV app",
                        Toast.LENGTH_SHORT
                    ).show()
                    true
                }
            } catch (e: Exception) {
                Log.e("YouTubeFragment", "Error binding data", e)
            }
        }
    }

    // ---------------- YOUTUBE LAUNCH (ALL FALLBACKS) ----------------
    private fun launchYouTube(context: Context) {
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
            "com.google.android.youtube.tv",       // YouTube TV (main)
            "com.google.android.youtube.tvkids",   // YouTube Kids TV
            "com.google.android.apps.youtube.kids", // YouTube Kids
            "com.google.android.youtube",          // YouTube mobile
            "com.google.android.youtube.go",       // YouTube Go
            "com.google.android.youtube.leanback"  // YouTube Leanback
        )

        for (pkg in packageNames) {
            try {
                val launchIntent = pm.getLaunchIntentForPackage(pkg)
                if (launchIntent != null) {
                    launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(launchIntent)
                    Log.d("YOUTUBE_LAUNCH", "Normal launch successful: $pkg")
                    return true
                }
            } catch (e: Exception) {
                Log.e("YOUTUBE_LAUNCH", "Normal launch failed for $pkg", e)
            }
        }
        return false
    }

    private fun tryLeanbackLaunch(pm: PackageManager, context: Context): Boolean {
        val leanbackPackageNames = listOf(
            "com.google.android.youtube.tv",
            "com.google.android.youtube.tvkids",
            "com.google.android.youtube.leanback"
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
                    Log.d("YOUTUBE_LAUNCH", "Leanback launch successful: $pkg")
                    return true
                }
            } catch (e: Exception) {
                Log.e("YOUTUBE_LAUNCH", "Leanback launch failed for $pkg", e)
            }
        }
        return false
    }

    private fun tryDeepLinks(context: Context): Boolean {
        val deepLinks = listOf(
            "youtube://",                                   // YouTube scheme
            "vnd.youtube://",                               // YouTube vnd scheme
            "https://www.youtube.com",                      // Web URL
            "https://youtube.com/tv",                       // YouTube TV web
            "market://details?id=com.google.android.youtube.tv" // Play Store
        )

        for (link in deepLinks) {
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link)).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }

                if (intent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(intent)
                    Log.d("YOUTUBE_LAUNCH", "Deep link successful: $link")
                    return true
                }
            } catch (e: Exception) {
                Log.e("YOUTUBE_LAUNCH", "Deep link failed: $link", e)
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
            "com.google.android.youtube.tv",
            "com.google.android.youtube",
            "com.google.android.apps.youtube.kids"
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
                        Log.d("YOUTUBE_LAUNCH", "Package intent successful: $pkg with $action")
                        return true
                    }
                } catch (e: Exception) {
                    Log.e("YOUTUBE_LAUNCH", "Package intent failed: $pkg", e)
                }
            }
        }
        return false
    }

    private fun tryClassNamesList(context: Context): Boolean {
        val classMappings = listOf(
            // YouTube TV variants
            Pair("com.google.android.youtube.tv", "com.google.android.apps.youtube.tv.activity.MainActivity"),
            Pair("com.google.android.youtube.tv", "com.google.android.apps.youtube.tv.activity.ShellActivity"),
            Pair("com.google.android.youtube.tv", "com.google.android.youtube.tv.TVActivity"),

            // YouTube mobile variants
            Pair("com.google.android.youtube", "com.google.android.apps.youtube.app.WatchWhileActivity"),
            Pair("com.google.android.youtube", "com.google.android.apps.youtube.app.application.Shell\$HomeActivity"),

            // YouTube Kids
            Pair("com.google.android.youtube.tvkids", "com.google.android.apps.youtube.tvkids.activity.MainActivity"),
            Pair("com.google.android.apps.youtube.kids", "com.google.android.apps.youtube.kids.home.HomeActivity")
        )

        for ((pkg, className) in classMappings) {
            try {
                val intent = Intent().apply {
                    setClassName(pkg, className)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(intent)
                Log.d("YOUTUBE_LAUNCH", "Class launch successful: $pkg/$className")
                return true
            } catch (e: Exception) {
                Log.e("YOUTUBE_LAUNCH", "Class launch failed: $pkg/$className", e)
            }
        }
        return false
    }

    private fun showNotInstalledError(context: Context) {
        Toast.makeText(
            context,
            "YouTube TV app not found.\n\nPlease install from:\n• Google Play Store\n• YouTube website\n• Device manufacturer's app store",
            Toast.LENGTH_LONG
        ).show()

        // Open Play Store as last resort
        try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("market://details?id=com.google.android.youtube.tv")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Log.e("YOUTUBE_LAUNCH", "Failed to open Play Store", e)
        }
    }
}