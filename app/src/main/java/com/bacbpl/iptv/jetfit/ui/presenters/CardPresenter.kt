package com.bacbpl.iptv.jetfit.ui.presenters

import android.view.ViewGroup
import androidx.leanback.widget.Presenter
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bacbpl.iptv.R
import com.bacbpl.iptv.jetfit.models.tmdb.TmdbMovie
import com.bacbpl.iptv.jetfit.models.tmdb.TmdbTvSeries

class CardPresenter : Presenter() {

    companion object {
        private const val CARD_WIDTH = 264
        private const val CARD_HEIGHT = 396
    }

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        // Use custom ImageCardView
        val cardView = CustomImageCardView(parent.context)

        // Set layout params
        cardView.layoutParams = ViewGroup.LayoutParams(CARD_WIDTH, CARD_HEIGHT)

        return ViewHolder(cardView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
        val cardView = viewHolder.view as CustomImageCardView

        when (item) {
            is TmdbMovie -> {
                cardView.setTitle(item.title)
                val posterUrl = "https://image.tmdb.org/t/p/w342${item.posterPath}"
                cardView.setImageUrl(posterUrl)
            }
            is TmdbTvSeries -> {
                cardView.setTitle(item.name)
                val posterUrl = "https://image.tmdb.org/t/p/w342${item.posterPath}"
                cardView.setImageUrl(posterUrl)
            }
        }

        // Handle focus change
        cardView.setOnFocusChangeListener { v, hasFocus ->
            val color = if (hasFocus) {
                ContextCompat.getColor(cardView.context, R.color.default_accent)
            } else {
                ContextCompat.getColor(cardView.context, R.color.default_background)
            }
            cardView.setBackgroundColor(color)
        }
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder) {
        val cardView = viewHolder.view as CustomImageCardView
        Glide.with(cardView.context).clear(cardView.getImageView())
        cardView.setOnFocusChangeListener(null)
    }
}