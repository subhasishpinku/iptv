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
//class NetflixFragment :
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
//        title = "NETFLIX"
//
//        val gridPresenter = VerticalGridPresenter(FocusHighlight.ZOOM_FACTOR_MEDIUM).apply {
//            numberOfColumns = 1
//        }
//        setGridPresenter(gridPresenter)
//
//        val adapter = ArrayObjectAdapter(NetflixPresenter())
//        adapter.add("NETFLIX")
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
//    private inner class NetflixPresenter : Presenter() {
//
//        override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
//            val card = ImageCardView(parent.context).apply {
//                isFocusable = true
//                isFocusableInTouchMode = true
//                setMainImageDimensions(600, 340)
//                titleText = "Netflix"
//                contentText = "Launch Netflix TV App"
//                mainImage = ContextCompat.getDrawable(
//                    parent.context,
//                    R.drawable.netflix // Use your Netflix icon
//                )
//            }
//            return ViewHolder(card)
//        }
//
//        override fun onBindViewHolder(vh: ViewHolder, item: Any) {
//            (vh.view as? ImageCardView)?.apply {
//                setOnClickListener {
//                    launchNetflix(requireContext())
//                }
//                setOnLongClickListener {
//                    Toast.makeText(
//                        context,
//                        "Opens Netflix TV app",
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
//    // ---------------- NETFLIX LAUNCH (ALL FALLBACKS) ----------------
//
//    private fun launchNetflix(context: Context) {
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
//            "com.netflix.mediaclient",           // Netflix mobile
//            "com.netflix.ninja",                 // Netflix TV (main)
//            "com.netflix.ninja.tv",              // Netflix TV (alternative)
//            "com.netflix.tv",                    // Netflix TV (simplified)
//            "com.netflix"                        // Generic
//        )
//
//        for (pkg in packageNames) {
//            try {
//                val launchIntent = pm.getLaunchIntentForPackage(pkg)
//                if (launchIntent != null) {
//                    launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                    context.startActivity(launchIntent)
//                    Log.d("NETFLIX_LAUNCH", "Normal launch successful: $pkg")
//                    return true
//                }
//            } catch (e: Exception) {
//                Log.e("NETFLIX_LAUNCH", "Normal launch failed for $pkg", e)
//            }
//        }
//        return false
//    }
//
//    private fun tryLeanbackLaunch(pm: PackageManager, context: Context): Boolean {
//        val leanbackPackageNames = listOf(
//            "com.netflix.ninja",
//            "com.netflix.ninja.tv",
//            "com.netflix.tv"
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
//                    Log.d("NETFLIX_LAUNCH", "Leanback launch successful: $pkg")
//                    return true
//                }
//            } catch (e: Exception) {
//                Log.e("NETFLIX_LAUNCH", "Leanback launch failed for $pkg", e)
//            }
//        }
//        return false
//    }
//
//    private fun tryDeepLinks(context: Context): Boolean {
//        val deepLinks = listOf(
//            "netflix://",                           // Netflix scheme
//            "nflx://",                              // Netflix short scheme
//            "https://www.netflix.com",              // Web URL
//            "https://netflix.com/title",            // Netflix content
//            "market://details?id=com.netflix.ninja" // Play Store
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
//                    Log.d("NETFLIX_LAUNCH", "Deep link successful: $link")
//                    return true
//                }
//            } catch (e: Exception) {
//                Log.e("NETFLIX_LAUNCH", "Deep link failed: $link", e)
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
//            "com.netflix.ninja",
//            "com.netflix.mediaclient",
//            "com.netflix.tv"
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
//                        Log.d("NETFLIX_LAUNCH", "Package intent successful: $pkg with $action")
//                        return true
//                    }
//                } catch (e: Exception) {
//                    Log.e("NETFLIX_LAUNCH", "Package intent failed: $pkg", e)
//                }
//            }
//        }
//        return false
//    }
//
//    private fun tryClassNamesList(context: Context): Boolean {
//        val classMappings = mapOf(
//            "com.netflix.ninja" to listOf(
//                "com.netflix.ninja.MainActivity",
//                "com.netflix.ninja.ui.MainActivity",
//                "com.netflix.tv.MainActivity"
//            ),
//            "com.netflix.mediaclient" to listOf(
//                "com.netflix.mediaclient.ui.HomeActivity",
//                "com.netflix.mediaclient.ui.launch.UIWebViewActivity"
//            ),
//            "com.netflix.tv" to listOf(
//                "com.netflix.tv.MainActivity",
//                "com.netflix.tv.ui.MainActivity"
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
//                    Log.d("NETFLIX_LAUNCH", "Class launch successful: $pkg/$className")
//                    return true
//                } catch (e: Exception) {
//                    Log.e("NETFLIX_LAUNCH", "Class launch failed: $pkg/$className", e)
//                }
//            }
//        }
//        return false
//    }
//
//    private fun showNotInstalledError(context: Context) {
//        Toast.makeText(
//            context,
//            "Netflix app not found.\n\nPlease install from:\n• Google Play Store\n• Samsung TV Store\n• Netflix website",
//            Toast.LENGTH_LONG
//        ).show()
//
//        // Open Play Store as last resort
//        try {
//            val intent = Intent(Intent.ACTION_VIEW).apply {
//                data = Uri.parse("market://details?id=com.netflix.ninja")
//                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            }
//            context.startActivity(intent)
//        } catch (e: Exception) {
//            Log.e("NETFLIX_LAUNCH", "Failed to open Play Store", e)
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

class NetflixFragment :
    VerticalGridSupportFragment(),
    BrowseSupportFragment.MainFragmentAdapterProvider {

    private val mainFragmentAdapter =
        BrowseSupportFragment.MainFragmentAdapter(this)

    override fun getMainFragmentAdapter():
            BrowseSupportFragment.MainFragmentAdapter<*> = mainFragmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = "NETFLIX"

        val gridPresenter = VerticalGridPresenter(FocusHighlight.ZOOM_FACTOR_MEDIUM).apply {
            numberOfColumns = 1
        }
        setGridPresenter(gridPresenter)

        val adapter = ArrayObjectAdapter(NetflixPresenter())
        adapter.add("NETFLIX") // ডামি ডাটা

        this.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        view?.setBackgroundColor(
            ContextCompat.getColor(requireContext(), R.color.default_background)
        )
    }

    // ============ আপডেটেড PRESENTER ============
    private inner class NetflixPresenter : BaseImageCardPresenter<String>(600, 340) {
        override fun bindData(container: FrameLayout, imageView: ImageView, data: String) {
            // সেট আইকন
            imageView.setImageDrawable(
                ContextCompat.getDrawable(container.context, R.drawable.netflix)
            )

            // ক্লিক লিসেনার
            container.setOnClickListener {
                launchNetflix(container.context)
            }

            // লং ক্লিক লিসেনার (optional)
            container.setOnLongClickListener {
                Toast.makeText(
                    container.context,
                    "Opens Netflix TV app",
                    Toast.LENGTH_SHORT
                ).show()
                true
            }
        }
    }

    // ---------------- NETFLIX LAUNCH (ALL FALLBACKS) ----------------
    // নিচের সব methods অপরিবর্তিত থাকবে
    private fun launchNetflix(context: Context) {
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
            "com.netflix.mediaclient",           // Netflix mobile
            "com.netflix.ninja",                 // Netflix TV (main)
            "com.netflix.ninja.tv",              // Netflix TV (alternative)
            "com.netflix.tv",                    // Netflix TV (simplified)
            "com.netflix"                        // Generic
        )

        for (pkg in packageNames) {
            try {
                val launchIntent = pm.getLaunchIntentForPackage(pkg)
                if (launchIntent != null) {
                    launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(launchIntent)
                    Log.d("NETFLIX_LAUNCH", "Normal launch successful: $pkg")
                    return true
                }
            } catch (e: Exception) {
                Log.e("NETFLIX_LAUNCH", "Normal launch failed for $pkg", e)
            }
        }
        return false
    }

    private fun tryLeanbackLaunch(pm: PackageManager, context: Context): Boolean {
        val leanbackPackageNames = listOf(
            "com.netflix.ninja",
            "com.netflix.ninja.tv",
            "com.netflix.tv"
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
                    Log.d("NETFLIX_LAUNCH", "Leanback launch successful: $pkg")
                    return true
                }
            } catch (e: Exception) {
                Log.e("NETFLIX_LAUNCH", "Leanback launch failed for $pkg", e)
            }
        }
        return false
    }

    private fun tryDeepLinks(context: Context): Boolean {
        val deepLinks = listOf(
            "netflix://",                           // Netflix scheme
            "nflx://",                              // Netflix short scheme
            "https://www.netflix.com",              // Web URL
            "https://netflix.com/title",            // Netflix content
            "market://details?id=com.netflix.ninja" // Play Store
        )

        for (link in deepLinks) {
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link)).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }

                if (intent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(intent)
                    Log.d("NETFLIX_LAUNCH", "Deep link successful: $link")
                    return true
                }
            } catch (e: Exception) {
                Log.e("NETFLIX_LAUNCH", "Deep link failed: $link", e)
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
            "com.netflix.ninja",
            "com.netflix.mediaclient",
            "com.netflix.tv"
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
                        Log.d("NETFLIX_LAUNCH", "Package intent successful: $pkg with $action")
                        return true
                    }
                } catch (e: Exception) {
                    Log.e("NETFLIX_LAUNCH", "Package intent failed: $pkg", e)
                }
            }
        }
        return false
    }

    private fun tryClassNamesList(context: Context): Boolean {
        val classMappings = mapOf(
            "com.netflix.ninja" to listOf(
                "com.netflix.ninja.MainActivity",
                "com.netflix.ninja.ui.MainActivity",
                "com.netflix.tv.MainActivity"
            ),
            "com.netflix.mediaclient" to listOf(
                "com.netflix.mediaclient.ui.HomeActivity",
                "com.netflix.mediaclient.ui.launch.UIWebViewActivity"
            ),
            "com.netflix.tv" to listOf(
                "com.netflix.tv.MainActivity",
                "com.netflix.tv.ui.MainActivity"
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
                    Log.d("NETFLIX_LAUNCH", "Class launch successful: $pkg/$className")
                    return true
                } catch (e: Exception) {
                    Log.e("NETFLIX_LAUNCH", "Class launch failed: $pkg/$className", e)
                }
            }
        }
        return false
    }

    private fun showNotInstalledError(context: Context) {
        Toast.makeText(
            context,
            "Netflix app not found.\n\nPlease install from:\n• Google Play Store\n• Samsung TV Store\n• Netflix website",
            Toast.LENGTH_LONG
        ).show()

        // Open Play Store as last resort
        try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("market://details?id=com.netflix.ninja")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Log.e("NETFLIX_LAUNCH", "Failed to open Play Store", e)
        }
    }
}