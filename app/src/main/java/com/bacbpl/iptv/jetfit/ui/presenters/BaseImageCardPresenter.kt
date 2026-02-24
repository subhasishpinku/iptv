package com.bacbpl.iptv.jetfit.ui.presenters

import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.leanback.widget.Presenter

abstract class BaseImageCardPresenter<T>(
    private val cardWidth: Int,
    private val cardHeight: Int
) : Presenter() {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val container = FrameLayout(parent.context)
        container.layoutParams = ViewGroup.LayoutParams(cardWidth, cardHeight)
        container.isFocusable = true
        container.isFocusableInTouchMode = true

        val imageView = ImageView(parent.context)
        imageView.layoutParams = FrameLayout.LayoutParams(cardWidth, cardHeight)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.id = android.R.id.icon
        container.addView(imageView)

        return ViewHolder(container)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
        val container = viewHolder.view as FrameLayout
        val imageView = container.findViewById<ImageView>(android.R.id.icon)

        @Suppress("UNCHECKED_CAST")
        bindData(container, imageView, item as T)

        container.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                v.scaleX = 1.05f
                v.scaleY = 1.05f
                v.elevation = 10f
            } else {
                v.scaleX = 1f
                v.scaleY = 1f
                v.elevation = 0f
            }
        }
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder) {
        val container = viewHolder.view as FrameLayout
        container.setOnFocusChangeListener(null)
    }

    abstract fun bindData(container: FrameLayout, imageView: ImageView, data: T)
}