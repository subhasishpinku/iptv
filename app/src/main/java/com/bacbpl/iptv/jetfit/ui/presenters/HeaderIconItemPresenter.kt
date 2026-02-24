//package  com.bacbpl.iptv.jetfit.ui.presenters
//
//import android.annotation.SuppressLint
//import android.content.Context
//import androidx.leanback.widget.Presenter
//import android.view.ViewGroup
//
//import androidx.leanback.widget.RowHeaderPresenter
//import android.widget.TextView
//import android.view.LayoutInflater
//import android.widget.ImageView
//import androidx.leanback.widget.PageRow
//import  com.bacbpl.iptv.jetfit.models.HeaderIconItem
//import android.text.TextUtils
//import android.view.View
//import androidx.core.content.ContextCompat
//import com.bacbpl.iptv.R
//import  com.bacbpl.iptv.jetfit.AppConstants
//
//
///**
// * A CardPresenter is used to generate Views and bind Objects to them on demand.
// * It contains an ImageCardView.
// */
//class HeaderIconItemPresenter : RowHeaderPresenter() {
//
//    override fun onCreateViewHolder(viewGroup: ViewGroup): ViewHolder {
//        val inflater = viewGroup.context
//            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//
//        val view = inflater.inflate(R.layout.header_icon_item, null)
//        val label = view.findViewById<TextView>(R.id.header_label)
//        val icon = view.findViewById<ImageView>(R.id.header_icon)
//
//        icon.setColorFilter(ContextCompat.getColor(view.context,
//            R.color.card_secondary_text
//        ), android.graphics.PorterDuff.Mode.MULTIPLY);
//        label.setTextColor(ContextCompat.getColor(view.context,
//            R.color.card_secondary_text
//        ))
//        label.ellipsize = TextUtils.TruncateAt.END
//
//        view.setOnFocusChangeListener { v, b ->
//            if (b) {
//                icon.setColorFilter(ContextCompat.getColor(v.context,
//                    R.color.card_primary_text
//                ), android.graphics.PorterDuff.Mode.MULTIPLY);
//                label.setTextColor(ContextCompat.getColor(v.context,
//                    R.color.card_primary_text
//                ))
//                label.ellipsize = TextUtils.TruncateAt.MARQUEE
//            } else {
//                icon.setColorFilter(ContextCompat.getColor(v.context,
//                    R.color.card_secondary_text
//                ), android.graphics.PorterDuff.Mode.MULTIPLY);
//                label.setTextColor(ContextCompat.getColor(v.context,
//                    R.color.card_secondary_text
//                ))
//                label.ellipsize = TextUtils.TruncateAt.END
//            }
//        }
//
//        return ViewHolder(view)
//    }
//
//    @SuppressLint("UseCompatLoadingForDrawables")
//    override fun onBindViewHolder(viewHolder: Presenter.ViewHolder, o: Any?) {
//        val iconHeaderItem = (o as PageRow).headerItem as HeaderIconItem
//        val rootView = viewHolder.view
//        rootView.alpha = .7f
//
//        val iconView: ImageView = rootView.findViewById(R.id.header_icon)
//        val iconResId = iconHeaderItem.iconResId
//        if (iconResId != HeaderIconItem.ICON_NONE) { // Show icon only when it is set.
//            val icon = rootView.resources.getDrawable(iconResId, null)
//            iconView.setImageDrawable(icon)
//        }
//
//        val label: TextView = rootView.findViewById(R.id.header_label)
//        val space = rootView.findViewById<View>(R.id.space)
//        if (iconHeaderItem.name == AppConstants.SPACE_TEXT) {
//            iconView.visibility = View.GONE
//            label.visibility = View.GONE
//            space.visibility = View.VISIBLE
//            rootView.isFocusable = false
//        } else {
//            space.visibility = View.GONE
//            iconView.visibility = View.VISIBLE
//            label.visibility = View.VISIBLE
//            rootView.isFocusable = true
//            label.text = iconHeaderItem.name
//        }
//    }
//
//    override fun onUnbindViewHolder(viewHolder: Presenter.ViewHolder) {
//    }
//
//    override fun onSelectLevelChanged(holder: ViewHolder) {
//        holder.view.alpha = .7f
//    }
//
//}

package com.bacbpl.iptv.jetfit.ui.presenters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.leanback.widget.PageRow
import androidx.leanback.widget.Presenter
import androidx.leanback.widget.RowHeaderPresenter
import com.bacbpl.iptv.R
import com.bacbpl.iptv.jetfit.AppConstants
import com.bacbpl.iptv.jetfit.models.HeaderIconItem

class HeaderIconItemPresenter : RowHeaderPresenter() {

    override fun onCreateViewHolder(viewGroup: ViewGroup): ViewHolder {
        val inflater = viewGroup.context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val view = inflater.inflate(R.layout.header_icon_item, viewGroup, false)
        val label = view.findViewById<TextView>(R.id.header_label)
        val icon = view.findViewById<ImageView>(R.id.header_icon)

        icon.setColorFilter(ContextCompat.getColor(view.context,
            R.color.card_secondary_text
        ), android.graphics.PorterDuff.Mode.MULTIPLY)

        label.setTextColor(ContextCompat.getColor(view.context,
            R.color.card_secondary_text
        ))
        label.ellipsize = android.text.TextUtils.TruncateAt.END

        view.setOnFocusChangeListener { v, b ->
            if (b) {
                icon.setColorFilter(ContextCompat.getColor(v.context,
                    R.color.card_primary_text
                ), android.graphics.PorterDuff.Mode.MULTIPLY)
                label.setTextColor(ContextCompat.getColor(v.context,
                    R.color.card_primary_text
                ))
                label.ellipsize = android.text.TextUtils.TruncateAt.MARQUEE
            } else {
                icon.setColorFilter(ContextCompat.getColor(v.context,
                    R.color.card_secondary_text
                ), android.graphics.PorterDuff.Mode.MULTIPLY)
                label.setTextColor(ContextCompat.getColor(v.context,
                    R.color.card_secondary_text
                ))
                label.ellipsize = android.text.TextUtils.TruncateAt.END
            }
        }

        return ViewHolder(view)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(viewHolder: Presenter.ViewHolder, o: Any?) {
        try {
            val iconHeaderItem = (o as PageRow).headerItem as HeaderIconItem
            val rootView = viewHolder.view
            rootView.alpha = .7f

            val iconView: ImageView = rootView.findViewById(R.id.header_icon)
            val iconResId = iconHeaderItem.iconResId

            if (iconResId != HeaderIconItem.ICON_NONE) {
                // Load image with size optimization
                loadOptimizedIcon(iconView, iconResId)
            }

            val label: TextView = rootView.findViewById(R.id.header_label)
            val space = rootView.findViewById<View>(R.id.space)

            if (iconHeaderItem.name == AppConstants.SPACE_TEXT) {
                iconView.visibility = View.GONE
                label.visibility = View.GONE
                space.visibility = View.VISIBLE
                rootView.isFocusable = false
            } else {
                space.visibility = View.GONE
                iconView.visibility = View.VISIBLE
                label.visibility = View.VISIBLE
                rootView.isFocusable = true
                label.text = iconHeaderItem.name
            }
        } catch (e: Exception) {
            Log.e("HeaderIconItemPresenter", "Error binding view", e)
        }
    }

    private fun loadOptimizedIcon(imageView: ImageView, resId: Int) {
        try {
            // First try to load as vector drawable (smaller memory footprint)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                imageView.setImageResource(resId)
            } else {
                // For older APIs, decode bitmap with inSampleSize
                val options = BitmapFactory.Options().apply {
                    inJustDecodeBounds = true
                }
                BitmapFactory.decodeResource(imageView.context.resources, resId, options)

                // Calculate inSampleSize (target size 48dp)
                val targetWidth = 48
                val targetHeight = 48

                options.inSampleSize = calculateInSampleSize(options, targetWidth, targetHeight)
                options.inJustDecodeBounds = false
                options.inPreferredConfig = Bitmap.Config.RGB_565 // Use less memory

                try {
                    val bitmap = BitmapFactory.decodeResource(imageView.context.resources, resId, options)
                    imageView.setImageBitmap(bitmap)
                } catch (e: OutOfMemoryError) {
                    Log.e("HeaderIconItemPresenter", "Out of memory loading icon", e)
                    // Fallback to default icon
                    imageView.setImageResource(android.R.drawable.ic_menu_gallery)
                }
            }
        } catch (e: Exception) {
            Log.e("HeaderIconItemPresenter", "Error loading icon", e)
            imageView.setImageResource(android.R.drawable.ic_menu_gallery)
        }
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    override fun onUnbindViewHolder(viewHolder: Presenter.ViewHolder) {
        // Clear image to free memory
        val rootView = viewHolder.view
        val iconView = rootView.findViewById<ImageView>(R.id.header_icon)
        iconView.setImageDrawable(null)
    }

    override fun onSelectLevelChanged(holder: ViewHolder) {
        holder.view.alpha = .7f
    }
}