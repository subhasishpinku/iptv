package com.bacbpl.iptv.jetfit.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Handler
import android.os.Looper
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import com.bacbpl.iptv.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

class BackgroundSlideshow(
    private val imageView1: ImageView,
    private val imageView2: ImageView
) {

    private var currentIndex = 0
    private var isFirstImageActive = true
    private val handler = Handler(Looper.getMainLooper())
    private var isRunning = false

    // লোকাল ড্রয়েবল ব্যবহার করুন (ইন্টারনেট ছাড়া কাজ করবে)
    private val localDrawables = listOf(
        R.drawable.bg_movies_1,
        R.drawable.bg_movies_2,
        R.drawable.bg_movies_3,
        R.drawable.bg_movies_4,
        R.drawable.bg_movies_5,
    //        R.drawable.bg_movies_6,
    //        R.drawable.bg_movies_7,
    //        R.drawable.bg_movies_8
    )

    private val slideshowRunnable = object : Runnable {
        override fun run() {
            if (isRunning) {
                changeBackground()
                handler.postDelayed(this, 8000)
            }
        }
    }

    fun start() {
        isRunning = true
        loadInitialBackground()
        handler.postDelayed(slideshowRunnable, 8000)
    }

    fun stop() {
        isRunning = false
        handler.removeCallbacks(slideshowRunnable)
        imageView1.animate().cancel()
        imageView2.animate().cancel()
    }

    private fun loadInitialBackground() {
        imageView1.scaleX = 1f
        imageView1.scaleY = 1f
        imageView2.scaleX = 1f
        imageView2.scaleY = 1f

        loadImage(imageView1, getNextDrawable(), true)
        loadImage(imageView2, getNextDrawable(), false)
    }

    private fun changeBackground() {
        val fadeOut = if (isFirstImageActive) imageView1 else imageView2
        val fadeIn = if (isFirstImageActive) imageView2 else imageView1

        fadeIn.scaleX = 1f
        fadeIn.scaleY = 1f

        loadImage(fadeIn, getNextDrawable(), true)

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

        isFirstImageActive = !isFirstImageActive
    }

    private fun getNextDrawable(): Int {
        val drawable = localDrawables[currentIndex]
        currentIndex = (currentIndex + 1) % localDrawables.size
        return drawable
    }

    private fun loadImage(imageView: ImageView, drawableRes: Int, withZoom: Boolean) {
        Glide.with(imageView.context)
            .load(drawableRes)
            .apply(
                RequestOptions()
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(android.R.color.black)
                    .error(android.R.color.black)
            )
            .into(imageView)

        if (withZoom) {
            addZoomEffect(imageView)
        }
    }

    private fun addZoomEffect(imageView: ImageView) {
        imageView.animate().cancel()
        imageView.scaleX = 1f
        imageView.scaleY = 1f

        imageView.animate()
            .scaleX(1.15f)
            .scaleY(1.15f)
            .setDuration(8000)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()
    }
}