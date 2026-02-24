//package  com.bacbpl.iptv.jetfit.ui.presenters
//
//import android.view.ViewGroup
//import androidx.leanback.widget.ImageCardView
//import androidx.leanback.widget.Presenter
//import com.bacbpl.iptv.jetfit.models.TvChannelItem
//import com.bumptech.glide.Glide
//
//class TvChannelPresenter : Presenter() {
//
//    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
//        val view = ImageCardView(parent.context).apply {
//            isFocusable = true
//            isFocusableInTouchMode = true
//            setMainImageDimensions(300, 180)
//        }
//        return ViewHolder(view)
//    }
//
//    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
//        val channel = item as TvChannelItem
//        val card = viewHolder.view as ImageCardView
//
//        // Set the channel title
//        card.titleText = channel.name
//
//        // Load logo image
//        if (channel.logo.startsWith("http")) {
//            // If logo is a URL, load with Glide
//            Glide.with(card.context)
//                .load(channel.logo)
//                .into(card.mainImageView)
//        } else {
//            // If logo is a local drawable resource name
//            val resId = card.context.resources.getIdentifier(
//                channel.logo, "drawable", card.context.packageName
//            )
//            card.mainImage = card.context.getDrawable(resId)
//        }
//    }
//
//    override fun onUnbindViewHolder(viewHolder: ViewHolder) {
//        val card = viewHolder.view as ImageCardView
//        // Clear image to free resources
//        card.mainImage = null
//    }
//}

package com.bacbpl.iptv.jetfit.ui.presenters
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.leanback.widget.Presenter
import com.bacbpl.iptv.R
import com.bacbpl.iptv.jetfit.models.TvChannelItem
import com.bumptech.glide.Glide

class TvChannelPresenter : Presenter() {

    companion object {
        private const val CARD_WIDTH = 300
        private const val CARD_HEIGHT = 180
    }

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        // Create a simple FrameLayout instead of ImageCardView
        val container = FrameLayout(parent.context)
        container.layoutParams = ViewGroup.LayoutParams(CARD_WIDTH, CARD_HEIGHT)
        container.isFocusable = true
        container.isFocusableInTouchMode = true

        // Create ImageView
        val imageView = ImageView(parent.context)
        imageView.layoutParams = FrameLayout.LayoutParams(
            CARD_WIDTH,
            CARD_HEIGHT
        )
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.id = R.id.thumb
        container.addView(imageView)

        // Create title TextView
        val titleView = TextView(parent.context)
        titleView.layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = android.view.Gravity.BOTTOM
        }
        titleView.setPadding(8, 8, 8, 8)
        titleView.setTextColor(android.graphics.Color.WHITE)
        titleView.setBackgroundColor(android.graphics.Color.parseColor("#80000000"))
        titleView.id = R.id.title
        container.addView(titleView)

        return ViewHolder(container)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
        val channel = item as TvChannelItem
        val container = viewHolder.view as FrameLayout

        val imageView = container.findViewById<ImageView>(R.id.thumb)
        val titleView = container.findViewById<TextView>(R.id.title)

        titleView.text = channel.name

        // Load logo image
        if (channel.logo.startsWith("http")) {
            Glide.with(container.context)
                .load(channel.logo)
                .placeholder(android.R.color.darker_gray)
                .error(android.R.color.holo_red_dark)
                .into(imageView)
        } else {
            // If logo is a local drawable resource name
            val resId = container.context.resources.getIdentifier(
                channel.logo, "drawable", container.context.packageName
            )
            if (resId != 0) {
                imageView.setImageResource(resId)
            } else {
                imageView.setImageResource(android.R.drawable.ic_menu_gallery)
            }
        }

        // Handle focus change
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
        val imageView = container.findViewById<ImageView>(R.id.thumb)
        Glide.with(container.context).clear(imageView)
        container.setOnFocusChangeListener(null)
    }
}