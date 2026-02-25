package com.bacbpl.iptv.jetfit.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bacbpl.iptv.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

class DoubleSlideshowManager(
    // Main Background Slideshow
    private val mainImage1: ImageView,
    private val mainImage2: ImageView,
    private val mainIndicator: LinearLayout,

    // Top Slider Slideshow
    private val topImage1: ImageView,
    private val topImage2: ImageView,
    private val topIndicator: LinearLayout,
    private val topTitle: TextView,
    private val topSubtitle: TextView
) {

    private var mainCurrentIndex = 0
    private var topCurrentIndex = 0
    private var isMainFirstActive = true
    private var isTopFirstActive = true

    private val handler = Handler(Looper.getMainLooper())
    private var isRunning = false

    // Data classes for slideshow items
    data class SlideshowItem(
        val imageUrl: String,
        val title: String,
        val subtitle: String
    )

    // Main background images
    private val mainBackgrounds = listOf(
        "https://images.unsplash.com/photo-1536440136628-849c177e76a1?w=1920",
        "https://images.unsplash.com/photo-1598899134737-88c22d78c0de?w=1920",
        "https://images.unsplash.com/photo-1485846234645-a62644f84728?w=1920",
        "https://images.unsplash.com/photo-1574375927938-d5a98e8ffe8e?w=1920"
    )

    // Top slider items with titles and subtitles
    private val topSliderItems = listOf(
        SlideshowItem(
            "https://images.unsplash.com/photo-1578022760304-7f5c5f1e70c8?w=1920",
            "New Releases",
            "Watch Latest Movies"
        ),
        SlideshowItem(
            "https://images.unsplash.com/photo-1593359677879-a4bb92f829d1?w=1920",
            "Popular Shows",
            "Top Rated This Week"
        ),
        SlideshowItem(
            "https://images.unsplash.com/photo-1522869635100-9f4c5e86aa37?w=1920",
            "Recommended",
            "Just For You"
        ),
        SlideshowItem(
            "https://images.unsplash.com/photo-1517604931442-7e0c8ed2963c?w=1920",
            "Trending Now",
            "Most Watched"
        )
    )

    init {
        setupIndicators()
    }

    // Main indicator dots (বড়)
    private fun setupIndicators() {
        // Setup main indicator
        mainIndicator.removeAllViews()
        for (i in mainBackgrounds.indices) {
            val dot = View(mainIndicator.context)
            val params = LinearLayout.LayoutParams(14, 14) // 14dp size
            params.setMargins(6, 0, 6, 0)
            dot.layoutParams = params
            dot.setBackgroundResource(if (i == 0) R.drawable.dot_active else R.drawable.dot_inactive)
            mainIndicator.addView(dot)
        }

        // Setup top indicator (ছোট)
        topIndicator.removeAllViews()
        for (i in topSliderItems.indices) {
            val dot = View(topIndicator.context)
            val params = LinearLayout.LayoutParams(10, 10) // 10dp size
            params.setMargins(8, 0, 8, 0)
            dot.layoutParams = params
            dot.setBackgroundResource(if (i == 0) R.drawable.dot_active else R.drawable.dot_inactive)
            topIndicator.addView(dot)
        }
    }

    private val slideshowRunnable = object : Runnable {
        override fun run() {
            if (isRunning) {
                changeMainBackground()
                changeTopSlider()
                handler.postDelayed(this, 6000) // Change every 6 seconds
            }
        }
    }

    fun start() {
        isRunning = true
        loadInitialImages()
        handler.postDelayed(slideshowRunnable, 6000)
    }

    fun stop() {
        isRunning = false
        handler.removeCallbacks(slideshowRunnable)
        mainImage1.animate().cancel()
        mainImage2.animate().cancel()
        topImage1.animate().cancel()
        topImage2.animate().cancel()
    }

    private fun loadInitialImages() {
        // Load main background
        mainImage1.scaleX = 1f
        mainImage1.scaleY = 1f
        mainImage2.scaleX = 1f
        mainImage2.scaleY = 1f

        loadImage(mainImage1, getMainUrl(0), true)
        loadImage(mainImage2, getMainUrl(1), false)

        // Load top slider
        topImage1.scaleX = 1f
        topImage1.scaleY = 1f
        topImage2.scaleX = 1f
        topImage2.scaleY = 1f

        loadTopSliderItem(topImage1, getTopItem(0), true)
        loadTopSliderItem(topImage2, getTopItem(1), false)

        updateMainIndicator(0)
        updateTopIndicator(0)
    }

    private fun changeMainBackground() {
        val fadeOut = if (isMainFirstActive) mainImage1 else mainImage2
        val fadeIn = if (isMainFirstActive) mainImage2 else mainImage1

        fadeIn.scaleX = 1f
        fadeIn.scaleY = 1f

        val nextIndex = (mainCurrentIndex + 1) % mainBackgrounds.size
        loadImage(fadeIn, getMainUrl(nextIndex), true)

        // Crossfade animation
        fadeIn.alpha = 0f
        fadeIn.visibility = ImageView.VISIBLE

        fadeIn.animate()
            .alpha(1f)
            .setDuration(1500)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setListener(null)

        fadeOut.animate()
            .alpha(0f)
            .setDuration(1500)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    fadeOut.visibility = ImageView.GONE
                    fadeOut.animate().cancel()
                    fadeOut.scaleX = 1f
                    fadeOut.scaleY = 1f
                }
            })

        isMainFirstActive = !isMainFirstActive
        mainCurrentIndex = nextIndex
        updateMainIndicator(mainCurrentIndex)
    }

    private fun changeTopSlider() {
        val fadeOut = if (isTopFirstActive) topImage1 else topImage2
        val fadeIn = if (isTopFirstActive) topImage2 else topImage1

        fadeIn.scaleX = 1f
        fadeIn.scaleY = 1f

        val nextIndex = (topCurrentIndex + 1) % topSliderItems.size
        loadTopSliderItem(fadeIn, getTopItem(nextIndex), true)

        // Update titles with animation
        topTitle.animate()
            .alpha(0f)
            .setDuration(750)
            .withEndAction {
                topTitle.text = getTopItem(nextIndex).title
                topTitle.animate().alpha(1f).setDuration(750).start()
            }

        topSubtitle.animate()
            .alpha(0f)
            .setDuration(750)
            .withEndAction {
                topSubtitle.text = getTopItem(nextIndex).subtitle
                topSubtitle.animate().alpha(1f).setDuration(750).start()
            }

        // Crossfade animation
        fadeIn.alpha = 0f
        fadeIn.visibility = ImageView.VISIBLE

        fadeIn.animate()
            .alpha(1f)
            .setDuration(1500)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setListener(null)

        fadeOut.animate()
            .alpha(0f)
            .setDuration(1500)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    fadeOut.visibility = ImageView.GONE
                    fadeOut.animate().cancel()
                    fadeOut.scaleX = 1f
                    fadeOut.scaleY = 1f
                }
            })

        isTopFirstActive = !isTopFirstActive
        topCurrentIndex = nextIndex
        updateTopIndicator(topCurrentIndex)
    }

    private fun getMainUrl(index: Int): String {
        return mainBackgrounds[index % mainBackgrounds.size]
    }

    private fun getTopItem(index: Int): SlideshowItem {
        return topSliderItems[index % topSliderItems.size]
    }

    private fun loadImage(imageView: ImageView, url: String, withZoom: Boolean) {
        Glide.with(imageView.context)
            .load(url)
            .apply(
                RequestOptions()
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(android.R.color.black)
                    .error(android.R.color.black)
            )
            .into(imageView)

        if (withZoom) {
            addZoomEffect(imageView, 8000)
        }
    }

    private fun loadTopSliderItem(imageView: ImageView, item: SlideshowItem, withZoom: Boolean) {
        Glide.with(imageView.context)
            .load(item.imageUrl)
            .apply(
                RequestOptions()
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(android.R.color.black)
                    .error(android.R.color.black)
            )
            .into(imageView)

        if (withZoom) {
            addZoomEffect(imageView, 6000)
        }
    }

    private fun addZoomEffect(imageView: ImageView, duration: Long) {
        imageView.animate().cancel()
        imageView.scaleX = 1f
        imageView.scaleY = 1f

        imageView.animate()
            .scaleX(1.15f)
            .scaleY(1.15f)
            .setDuration(duration)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()
    }

    private fun updateMainIndicator(index: Int) {
        for (i in 0 until mainIndicator.childCount) {
            val dot = mainIndicator.getChildAt(i)
            dot.setBackgroundResource(if (i == index) R.drawable.dot_active else R.drawable.dot_inactive)
        }
    }

    private fun updateTopIndicator(index: Int) {
        for (i in 0 until topIndicator.childCount) {
            val dot = topIndicator.getChildAt(i)
            dot.setBackgroundResource(if (i == index) R.drawable.dot_active else R.drawable.dot_inactive)
        }
    }
}