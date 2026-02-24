package com.bacbpl.iptv.jetfit.ui.presenters

import android.util.Log
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.bacbpl.iptv.jetfit.models.ottplay.OTTPlayItem
import com.bumptech.glide.Glide

private class OTTPlayItemPresenter : BaseImageCardPresenter<OTTPlayItem>(300, 450) {
    override fun bindData(container: FrameLayout, imageView: ImageView, data: OTTPlayItem) {
        // Glide দিয়ে ইমেজ লোড করুন
        Glide.with(container.context)
            .load(data.posterImage)
            .centerCrop()
            .placeholder(android.R.color.darker_gray)
            .error(android.R.color.holo_red_dark)
            .into(imageView)

        // ক্লিক লিসেনার
        container.setOnClickListener {
            // আইটেম ক্লিক করলে ডিটেইলস পেজ খুলতে পারেন
            Log.d("OTTPlayFragment", "Clicked: ${data.name}")

            // উদাহরণ: WebView বা Browser-এ URL খুলতে
            // val intent = Intent(Intent.ACTION_VIEW, Uri.parse(data.ottplayUrl))
            // startActivity(intent)
        }

        // টাইটেল দেখানোর জন্য TextView
        val titleView = container.findViewById<TextView>(android.R.id.title) ?:
        TextView(container.context).apply {
            id = android.R.id.title
            setTextColor(android.graphics.Color.WHITE)
            setBackgroundColor(android.graphics.Color.parseColor("#80000000"))
            setPadding(8, 8, 8, 8)
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = android.view.Gravity.BOTTOM
            }
            container.addView(this)
        }

        titleView.text = data.name
    }
}