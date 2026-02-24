package  com.bacbpl.iptv.jetfit.ui.presenters

import android.graphics.Color
import android.text.SpannableString
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.leanback.widget.*
import com.bacbpl.iptv.R
import  com.bacbpl.iptv.jetfit.utils.CircleProgressBar
import  com.bacbpl.iptv.jetfit.utils.TextViewScroller

class CustomListRowPresenter : ListRowPresenter() {

    var curTitle: String = ""
    private var descView: View? = null
    private var titleScroller: TextViewScroller? = null
    private var expanded = false

    // -------------------------
    // Rating UI
    // -------------------------
    fun setRating(rating: Float) {
        val progressRating =
            descView?.findViewById<CircleProgressBar>(R.id.progress_rating)
        val ratingTextView =
            descView?.findViewById<TextView>(R.id.progress_rating_text)
        val ratingTextProcView =
            descView?.findViewById<TextView>(R.id.progress_rating_text_proc)

        when {
            rating < 4 ->
                progressRating?.setColor(Color.RED)

            rating < 7 ->
                progressRating?.setColor(
                    ContextCompat.getColor(
                        progressRating.context,
                        R.color.rate_middle
                    )
                )

            else ->
                progressRating?.setColor(
                    ContextCompat.getColor(
                        progressRating.context,
                        R.color.default_accent
                    )
                )
        }

        progressRating?.setStrokeWidth(10f)
        progressRating?.setProgressWithAnimation(rating * 10)

        ratingTextView?.text = (rating * 10).toInt().toString()
        ratingTextProcView?.text = "%"
    }

    // -------------------------
    // Description UI
    // -------------------------
    fun setDesc(title: String, subtitle: SpannableString) {
        val titleView = descView?.findViewById<TextView>(R.id.row_title)
        val subtitleView = descView?.findViewById<TextView>(R.id.row_subtitle)

        if (!TextUtils.isEmpty(title)) {
            if (title != curTitle || titleView?.text.isNullOrEmpty()) {
                curTitle = title

                titleScroller?.stop()
                titleView?.text = curTitle

                titleScroller = TextViewScroller(titleView)
                titleScroller?.start()

                titleView?.alpha = 0f
                titleView?.animate()?.alpha(1f)?.setDuration(400)?.start()

                subtitleView?.alpha = 0f
                subtitleView?.animate()?.alpha(1f)?.setDuration(400)?.start()
            }
        } else {
            titleView?.text = ""
        }

        subtitleView?.text = if (!TextUtils.isEmpty(subtitle)) subtitle else ""
    }

    // -------------------------
    // Row expanded / collapsed
    // -------------------------
    override fun onRowViewExpanded(
        holder: RowPresenter.ViewHolder,
        expanded: Boolean
    ) {
        super.onRowViewExpanded(holder, expanded)
        this.expanded = expanded

        if (expanded) {
            descView?.visibility = View.VISIBLE
        } else {
            titleScroller = null
            descView?.visibility = View.GONE
        }
    }

    // -------------------------
    // Selection level changed
    // -------------------------
    override fun onSelectLevelChanged(holder: RowPresenter.ViewHolder) {
        super.onSelectLevelChanged(holder)

        if (holder.row?.headerItem?.name != null) {
            descView = holder.view.findViewById(R.id.description)
        }
    }

    // -------------------------
    // Row selected
    // -------------------------
    override fun onRowViewSelected(
        holder: RowPresenter.ViewHolder,
        selected: Boolean
    ) {
        super.onRowViewSelected(holder, selected)

        if (selected && expanded) {
            descView?.visibility = View.VISIBLE
        } else {
            titleScroller = null
            descView?.visibility = View.GONE
        }
    }

    // -------------------------
    // Create Row ViewHolder
    // -------------------------
    override fun createRowViewHolder(parent: ViewGroup): ViewHolder {
        val viewHolder = super.createRowViewHolder(parent)

        if (descView == null) {
            descView = viewHolder.view.findViewById(R.id.description)
        }

        with((viewHolder.view as ListRowView).gridView) {
            windowAlignment = BaseGridView.WINDOW_ALIGN_NO_EDGE
            windowAlignmentOffsetPercent = 0f
            windowAlignmentOffset =
                parent.resources.getDimensionPixelSize(
                    R.dimen.lb_browse_padding_start_low
                )
            itemAlignmentOffsetPercent = 0f
        }

        return viewHolder as ViewHolder
    }
}
