package com.bacbpl.iptv.jetfit.ui.presenters

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.leanback.widget.BaseCardView
import com.bacbpl.iptv.R
import com.bumptech.glide.Glide

class CustomImageCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseCardView(context, attrs, defStyleAttr) {

    private val imageView: ImageView
    private val titleText: TextView
    private val contentText: TextView

    init {
        // Inflate custom layout
        LayoutInflater.from(context).inflate(R.layout.custom_image_card, this, true)

        imageView = findViewById(R.id.card_image)
        titleText = findViewById(R.id.card_title)
        contentText = findViewById(R.id.card_content)

        // Set focusable
        isFocusable = true
        isFocusableInTouchMode = true
    }

    fun setImageUrl(url: String?) {
        // Load image with Glide
        if (!url.isNullOrEmpty()) {
            Glide.with(context)
                .load(url)
                .centerCrop()
                .placeholder(android.R.color.darker_gray)
                .error(android.R.color.holo_red_dark)
                .into(imageView)
        }
    }

    fun setTitle(title: String?) {
        titleText.text = title ?: ""
    }

    fun setContent(content: String?) {
        contentText.text = content ?: ""
    }

    // এই method টি যোগ করুন
    fun getImageView(): ImageView = imageView
}