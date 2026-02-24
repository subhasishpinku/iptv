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
//class AmazonVideoFragment :
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
//        title = "AMAZON PRIME VIDEO"
//
//        val gridPresenter = VerticalGridPresenter(FocusHighlight.ZOOM_FACTOR_MEDIUM).apply {
//            numberOfColumns = 1
//        }
//        setGridPresenter(gridPresenter)
//
//        val adapter = ArrayObjectAdapter(AmazonVideoPresenter())
//        adapter.add("AMAZON PRIME VIDEO")
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
//    private inner class AmazonVideoPresenter : Presenter() {
//
//        override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
//            val card = ImageCardView(parent.context).apply {
//                isFocusable = true
//                isFocusableInTouchMode = true
//                setMainImageDimensions(600, 340)
//                titleText = "Amazon Prime Video"
//                contentText = "Launch Amazon Prime Video TV App"
//                mainImage = ContextCompat.getDrawable(
//                    parent.context,
//                    R.drawable.amzone_prime // Use your Amazon icon
//                )
//            }
//            return ViewHolder(card)
//        }
//
//        override fun onBindViewHolder(vh: ViewHolder, item: Any) {
//            (vh.view as? ImageCardView)?.apply {
//                setOnClickListener {
//                    launchAmazonVideo(requireContext())
//                }
//                setOnLongClickListener {
//                    Toast.makeText(
//                        context,
//                        "Opens Amazon Prime Video TV app",
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
//    // ---------------- AMAZON PRIME VIDEO LAUNCH (ALL FALLBACKS) ----------------
//
//    private fun launchAmazonVideo(context: Context) {
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
//            "com.amazon.amazonvideo.livingroom",   // Prime Video TV
//            "com.amazon.avod",                     // Prime Video mobile
//            "com.amazon.firetv.primevideo",        // Fire TV
//            "com.amazon.avod.thirdpartyclient",    // Third party
//            "com.amazon.amazonvideo"               // Generic
//        )
//
//        for (pkg in packageNames) {
//            try {
//                val launchIntent = pm.getLaunchIntentForPackage(pkg)
//                if (launchIntent != null) {
//                    launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                    context.startActivity(launchIntent)
//                    Log.d("AMAZON_LAUNCH", "Normal launch successful: $pkg")
//                    return true
//                }
//            } catch (e: Exception) {
//                Log.e("AMAZON_LAUNCH", "Normal launch failed for $pkg", e)
//            }
//        }
//        return false
//    }
//
//    private fun tryLeanbackLaunch(pm: PackageManager, context: Context): Boolean {
//        val leanbackPackageNames = listOf(
//            "com.amazon.amazonvideo.livingroom",
//            "com.amazon.firetv.primevideo"
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
//                    Log.d("AMAZON_LAUNCH", "Leanback launch successful: $pkg")
//                    return true
//                }
//            } catch (e: Exception) {
//                Log.e("AMAZON_LAUNCH", "Leanback launch failed for $pkg", e)
//            }
//        }
//        return false
//    }
//
//    private fun tryDeepLinks(context: Context): Boolean {
//        val deepLinks = listOf(
//            "amazonvideo://",                              // Amazon Video scheme
//            "primevideo://",                               // Prime Video scheme
//            "https://www.primevideo.com",                  // Web URL
//            "https://app.primevideo.com",                  // App URL
//            "market://details?id=com.amazon.amazonvideo.livingroom" // Play Store
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
//                    Log.d("AMAZON_LAUNCH", "Deep link successful: $link")
//                    return true
//                }
//            } catch (e: Exception) {
//                Log.e("AMAZON_LAUNCH", "Deep link failed: $link", e)
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
//            "com.amazon.amazonvideo.livingroom",
//            "com.amazon.avod",
//            "com.amazon.firetv.primevideo"
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
//                        Log.d("AMAZON_LAUNCH", "Package intent successful: $pkg with $action")
//                        return true
//                    }
//                } catch (e: Exception) {
//                    Log.e("AMAZON_LAUNCH", "Package intent failed: $pkg", e)
//                }
//            }
//        }
//        return false
//    }
//
//    private fun tryClassNamesList(context: Context): Boolean {
//        val classMappings = mapOf(
//            "com.amazon.amazonvideo.livingroom" to listOf(
//                "com.amazon.amazonvideo.livingroom.MainActivity",
//                "com.amazon.amazonvideo.livingroom.splash.SplashActivity",
//                "com.amazon.amazonvideo.livingroom.HomeActivity"
//            ),
//            "com.amazon.avod" to listOf(
//                "com.amazon.avod.MainActivity",
//                "com.amazon.avod.ui.HomeActivity"
//            ),
//            "com.amazon.firetv.primevideo" to listOf(
//                "com.amazon.firetv.primevideo.MainActivity"
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
//                    Log.d("AMAZON_LAUNCH", "Class launch successful: $pkg/$className")
//                    return true
//                } catch (e: Exception) {
//                    Log.e("AMAZON_LAUNCH", "Class launch failed: $pkg/$className", e)
//                }
//            }
//        }
//        return false
//    }
//
//    private fun showNotInstalledError(context: Context) {
//        Toast.makeText(
//            context,
//            "Amazon Prime Video app not found.\n\nPlease install from:\n• Amazon Appstore\n• Google Play Store\n• Prime Video website",
//            Toast.LENGTH_LONG
//        ).show()
//
//        // Open Play Store as last resort
//        try {
//            val intent = Intent(Intent.ACTION_VIEW).apply {
//                data = Uri.parse("market://details?id=com.amazon.amazonvideo.livingroom")
//                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            }
//            context.startActivity(intent)
//        } catch (e: Exception) {
//            Log.e("AMAZON_LAUNCH", "Failed to open Play Store", e)
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

class AmazonVideoFragment :
    VerticalGridSupportFragment(),
    BrowseSupportFragment.MainFragmentAdapterProvider {

    private val mainFragmentAdapter =
        BrowseSupportFragment.MainFragmentAdapter(this)

    override fun getMainFragmentAdapter():
            BrowseSupportFragment.MainFragmentAdapter<*> = mainFragmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = "AMAZON PRIME VIDEO"

        val gridPresenter = VerticalGridPresenter(FocusHighlight.ZOOM_FACTOR_MEDIUM).apply {
            numberOfColumns = 1
        }
        setGridPresenter(gridPresenter)

        val adapter = ArrayObjectAdapter(AmazonVideoPresenter())
        adapter.add("AMAZON PRIME VIDEO") // Dummy data

        this.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        view?.setBackgroundColor(
            ContextCompat.getColor(requireContext(), R.color.default_background)
        )
    }

    // ============ আপডেটেড PRESENTER ============
    private inner class AmazonVideoPresenter : BaseImageCardPresenter<String>(600, 340) {
        override fun bindData(container: FrameLayout, imageView: ImageView, data: String) {
            // Set Amazon icon
            imageView.setImageDrawable(
                ContextCompat.getDrawable(container.context, R.drawable.amzone_prime)
            )

            // Click listener
            container.setOnClickListener {
                launchAmazonVideo(container.context)
            }

            // Long click listener
            container.setOnLongClickListener {
                Toast.makeText(
                    container.context,
                    "Opens Amazon Prime Video TV app",
                    Toast.LENGTH_SHORT
                ).show()
                true
            }
        }
    }

    // ---------------- AMAZON PRIME VIDEO LAUNCH (ALL FALLBACKS) ----------------
    // নিচের সব methods অপরিবর্তিত থাকবে
    private fun launchAmazonVideo(context: Context) {
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
            "com.amazon.amazonvideo.livingroom",   // Prime Video TV
            "com.amazon.avod",                     // Prime Video mobile
            "com.amazon.firetv.primevideo",        // Fire TV
            "com.amazon.avod.thirdpartyclient",    // Third party
            "com.amazon.amazonvideo"               // Generic
        )

        for (pkg in packageNames) {
            try {
                val launchIntent = pm.getLaunchIntentForPackage(pkg)
                if (launchIntent != null) {
                    launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(launchIntent)
                    Log.d("AMAZON_LAUNCH", "Normal launch successful: $pkg")
                    return true
                }
            } catch (e: Exception) {
                Log.e("AMAZON_LAUNCH", "Normal launch failed for $pkg", e)
            }
        }
        return false
    }

    private fun tryLeanbackLaunch(pm: PackageManager, context: Context): Boolean {
        val leanbackPackageNames = listOf(
            "com.amazon.amazonvideo.livingroom",
            "com.amazon.firetv.primevideo"
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
                    Log.d("AMAZON_LAUNCH", "Leanback launch successful: $pkg")
                    return true
                }
            } catch (e: Exception) {
                Log.e("AMAZON_LAUNCH", "Leanback launch failed for $pkg", e)
            }
        }
        return false
    }

    private fun tryDeepLinks(context: Context): Boolean {
        val deepLinks = listOf(
            "amazonvideo://",                              // Amazon Video scheme
            "primevideo://",                               // Prime Video scheme
            "https://www.primevideo.com",                  // Web URL
            "https://app.primevideo.com",                  // App URL
            "market://details?id=com.amazon.amazonvideo.livingroom" // Play Store
        )

        for (link in deepLinks) {
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link)).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }

                if (intent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(intent)
                    Log.d("AMAZON_LAUNCH", "Deep link successful: $link")
                    return true
                }
            } catch (e: Exception) {
                Log.e("AMAZON_LAUNCH", "Deep link failed: $link", e)
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
            "com.amazon.amazonvideo.livingroom",
            "com.amazon.avod",
            "com.amazon.firetv.primevideo"
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
                        Log.d("AMAZON_LAUNCH", "Package intent successful: $pkg with $action")
                        return true
                    }
                } catch (e: Exception) {
                    Log.e("AMAZON_LAUNCH", "Package intent failed: $pkg", e)
                }
            }
        }
        return false
    }

    private fun tryClassNamesList(context: Context): Boolean {
        val classMappings = mapOf(
            "com.amazon.amazonvideo.livingroom" to listOf(
                "com.amazon.amazonvideo.livingroom.MainActivity",
                "com.amazon.amazonvideo.livingroom.splash.SplashActivity",
                "com.amazon.amazonvideo.livingroom.HomeActivity"
            ),
            "com.amazon.avod" to listOf(
                "com.amazon.avod.MainActivity",
                "com.amazon.avod.ui.HomeActivity"
            ),
            "com.amazon.firetv.primevideo" to listOf(
                "com.amazon.firetv.primevideo.MainActivity"
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
                    Log.d("AMAZON_LAUNCH", "Class launch successful: $pkg/$className")
                    return true
                } catch (e: Exception) {
                    Log.e("AMAZON_LAUNCH", "Class launch failed: $pkg/$className", e)
                }
            }
        }
        return false
    }

    private fun showNotInstalledError(context: Context) {
        Toast.makeText(
            context,
            "Amazon Prime Video app not found.\n\nPlease install from:\n• Amazon Appstore\n• Google Play Store\n• Prime Video website",
            Toast.LENGTH_LONG
        ).show()

        // Open Play Store as last resort
        try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("market://details?id=com.amazon.amazonvideo.livingroom")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Log.e("AMAZON_LAUNCH", "Failed to open Play Store", e)
        }
    }
}