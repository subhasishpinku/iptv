package com.bacbpl.iptv.jetfit.ui.fragments

import android.animation.Animator
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.*
import com.bacbpl.iptv.R
import com.bacbpl.iptv.jetfit.ui.activities.MainActivity
import androidx.leanback.widget.Presenter
import androidx.leanback.widget.PresenterSelector
import com.bacbpl.iptv.jetfit.models.HeaderIconItem
import com.bacbpl.iptv.jetfit.ui.presenters.HeaderIconItemPresenter
import androidx.leanback.R as leanbackR
import java.text.SimpleDateFormat
import java.util.*

class MainFragment : BrowseSupportFragment() {
    private var mRowsAdapter: ArrayObjectAdapter? = null
    private var fragmentFactory: PageRowFragmentFactory? = null
    private var timeUpdateHandler: Handler? = null
    private var timeUpdateRunnable: Runnable? = null
    private var customTitleView: View? = null

    // একটি flag যোগ করুন যাতে বোঝা যায় এই ফ্র্যাগমেন্টে কাস্টম টাইটেল দেখানো হবে কিনা
    private var isMainFragment = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupUi()
        loadData()

        fragmentFactory = PageRowFragmentFactory(context)
        mainFragmentRegistry.registerFragment(
            PageRow::class.java,
            fragmentFactory
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // শুধু MainFragment-এ লোগো ও সময় দেখান
        if (isMainFragment) {
            Handler(Looper.getMainLooper()).postDelayed({
                setupCustomTitle()
            }, 300)
        } else {
            // অন্যান্য ফ্র্যাগমেন্টে ডিফল্ট টাইটেল দেখান
            resetToDefaultTitle()
        }

        // সার্চ বাটন visibility GONE
        hideSearchButtons(view)
    }

    private fun resetToDefaultTitle() {
        try {
            val titleView = titleView
            if (titleView != null && titleView is ViewGroup) {
                titleView.removeAllViews()
                // ডিফল্ট টাইটেল টেক্সট সেট করুন
                val defaultTitle = LayoutInflater.from(context).inflate(androidx.leanback.R.layout.lb_browse_title, titleView, false)
                titleView.addView(defaultTitle)
            }
        } catch (e: Exception) {
            Log.e("MainFragment", "Error resetting title", e)
        }
    }

    private fun setupCustomTitle() {
        try {
            val titleView = titleView
            if (titleView != null && titleView is ViewGroup) {
                Log.d("MainFragment", "Setting up custom title in MainFragment")

                titleView.removeAllViews()

                customTitleView = LayoutInflater.from(context).inflate(R.layout.title_main, titleView, false)

                // লোগো সেট করুন
                val logoImage = customTitleView?.findViewById<ImageView>(R.id.logo_image)
                logoImage?.setImageResource(R.drawable.logo1)

                // Time TextView সেট করুন
                val timeText = customTitleView?.findViewById<TextView>(R.id.time_text)
                if (timeText != null) {
                    val calendar = Calendar.getInstance()
                    val dateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
                    val timeString = dateFormat.format(calendar.time)

                    timeText.text = timeString
                    timeText.visibility = View.VISIBLE
                }

                titleView.addView(customTitleView)

                startTimeUpdates()
            }
        } catch (e: Exception) {
            Log.e("MainFragment", "Error setting up title", e)
        }
    }

    private fun startTimeUpdates() {
        timeUpdateHandler?.removeCallbacksAndMessages(null)

        timeUpdateHandler = Handler(Looper.getMainLooper())
        timeUpdateRunnable = object : Runnable {
            override fun run() {
                try {
                    if (customTitleView != null) {
                        val timeText = customTitleView?.findViewById<TextView>(R.id.time_text)
                        if (timeText != null) {
                            val calendar = Calendar.getInstance()
                            val dateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
                            val timeString = dateFormat.format(calendar.time)

                            timeText.text = timeString
                            Log.d("MainFragment", "Time updated: $timeString")
                        }
                    }
                } catch (e: Exception) {
                    Log.e("MainFragment", "Error updating time", e)
                }
                timeUpdateHandler?.postDelayed(this, 1000)
            }
        }
        timeUpdateRunnable?.run()
    }

    private fun hideSearchButtons(view: View) {
        try {
            Handler(Looper.getMainLooper()).postDelayed({
                val searchOrb = view.findViewById<View>(leanbackR.id.title_orb)
                searchOrb?.visibility = View.GONE

                val searchAffordance = view.findViewById<View>(leanbackR.id.search_orb)
                searchAffordance?.visibility = View.GONE

                val headersFragment = headersSupportFragment
                headersFragment?.view?.apply {
                    setPadding(0, 0, 0, 0)
                    val layoutParams = layoutParams as? ViewGroup.MarginLayoutParams
                    layoutParams?.topMargin = 0
                    layoutParams?.bottomMargin = 0
                    requestLayout()
                }
            }, 500)
        } catch (e: Exception) {
            Log.e("MainFragment", "Error hiding search buttons", e)
        }
    }

    private fun setupUi() {
        brandColor = ContextCompat.getColor(requireActivity(),
            R.color.default_background
        )

        setHeaderPresenterSelector(object : PresenterSelector() {
            override fun getPresenter(o: Any): Presenter {
                return HeaderIconItemPresenter()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        if (isMainFragment) {
            startTimeUpdates()
        }
    }

    override fun onPause() {
        super.onPause()
        timeUpdateHandler?.removeCallbacksAndMessages(null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timeUpdateHandler?.removeCallbacksAndMessages(null)
        timeUpdateHandler = null
        timeUpdateRunnable = null
        customTitleView = null
    }

    private fun loadData() {
        mRowsAdapter = ArrayObjectAdapter(ListRowPresenter())
        adapter = mRowsAdapter
        createRows()
    }

    private fun createRows() {
        for (title in resources.getStringArray(R.array.category_title)) {

            val headerItem = when (title) {

                getString(R.string.main_movie) ->
                    HeaderIconItem(title.uppercase(), R.drawable.movies)

                getString(R.string.main_serial) ->
                    HeaderIconItem(title.uppercase(), R.drawable.tvshow)


                getString(R.string.main_news) ->
                    HeaderIconItem(title.uppercase(), R.drawable.news)

                getString(R.string.main_sports) ->
                    HeaderIconItem(title.uppercase(), R.drawable.sportss)

                getString(R.string.main_ott) ->
                    HeaderIconItem(title.uppercase(), R.drawable.ottall)

                getString(R.string.ott_ply) ->
                    HeaderIconItem(title.uppercase(), R.drawable.ott_play)

                getString(R.string.main_person) ->
                    HeaderIconItem(title.uppercase(), R.drawable.ic_main_persons)

                getString(R.string.main_tv_channel) ->
                    HeaderIconItem(title.uppercase(), R.drawable.ic_main_tv_channel)

                getString(R.string.main_devotional) ->
                    HeaderIconItem(title.uppercase(), R.drawable.devotional)


                getString(R.string.main_kids) ->
                    HeaderIconItem(title.uppercase(), R.drawable.kids)

                getString(R.string.main_music) ->
                    HeaderIconItem(title.uppercase(), R.drawable.music)

                getString(R.string.main_game) ->
                    HeaderIconItem(title.uppercase(), R.drawable.game)

                getString(R.string.main_webWatch) ->
                    HeaderIconItem(title.uppercase(), R.drawable.webwatch)

                getString(R.string.main_settings) -> {
                    mRowsAdapter!!.add(PageRow(HeaderIconItem(" ")))
                    HeaderIconItem(title.uppercase(), R.drawable.ic_main_settings)
                }

                else -> HeaderIconItem(title.uppercase())
            }

            mRowsAdapter!!.add(PageRow(headerItem))
        }
    }

    private class PageRowFragmentFactory(private val context: Context?) :
        BrowseSupportFragment.FragmentFactory<Fragment>() {
        var fragment: Fragment? = null
        private val mCashedFragments: MutableMap<String, Fragment> = HashMap()

        override fun createFragment(rowObj: Any): Fragment {
            val row = rowObj as Row
            val cachedId = row.headerItem.name

            fragment = when {
                mCashedFragments.containsKey(cachedId) -> {
                    mCashedFragments[cachedId]
                }
                row.headerItem.name.equals(
                    context?.getString(R.string.main_news), true
                ) -> {
                    val newsFragment = NewsFragment(row.headerItem.name)
                    mCashedFragments[cachedId] = newsFragment
                    mCashedFragments[cachedId]
                }

                row.headerItem.name.equals(
                    context?.getString(R.string.main_sports), true
                ) -> {
                    val sportsFragment = SportsFragment(row.headerItem.name)
                    mCashedFragments[cachedId] = sportsFragment
                    mCashedFragments[cachedId]
                }

                row.headerItem.name.equals(
                    context?.getString(R.string.main_ott), true
                ) -> {
                    val deepLinkFragment = DeepLinkFragment(row.headerItem.name)
                    mCashedFragments[cachedId] = deepLinkFragment
                    mCashedFragments[cachedId]
                }

                row.headerItem.name.equals(
                    context?.getString(R.string.ott_ply), true
                ) -> {
                    val ottPlayFragment = OTTPlayFragment(row.headerItem.name)
                    mCashedFragments[cachedId] = ottPlayFragment
                    mCashedFragments[cachedId]
                }

                row.headerItem.name.equals(
                    context?.getString(R.string.main_tv_channel), true
                ) -> {
                    val tvChannelFragment = TvChannelGridFragment()
                    mCashedFragments[cachedId] = tvChannelFragment
                    mCashedFragments[cachedId]
                }

                row.headerItem.name.equals(
                    context?.getString(R.string.main_devotional), true
                ) -> {
                    val devotionalFragment = DevotionalFragment(row.headerItem.name)
                    mCashedFragments[cachedId] = devotionalFragment
                    mCashedFragments[cachedId]
                }

                row.headerItem.name.equals(
                    context?.getString(R.string.main_kids), true
                ) -> {
                    val kidsFragment = KidsFragment(row.headerItem.name)
                    mCashedFragments[cachedId] = kidsFragment
                    mCashedFragments[cachedId]
                }

                row.headerItem.name.equals(
                    context?.getString(R.string.main_person), true
                ) -> {
                    val mainGridFragment = MainGridFragment(row.headerItem.name)
                    mCashedFragments[cachedId] = mainGridFragment
                    mCashedFragments[cachedId]
                }

                else -> {
                    val mainRowFragment = MainRowFragment(context!!, row.headerItem.name)
                    mCashedFragments[cachedId] = mainRowFragment
                    mCashedFragments[cachedId]
                }
            }

            when (fragment) {
                is MainRowFragment -> {
                    Log.e("test", "fragment is MainRowFragment")
                    (fragment as MainRowFragment).selectedPosition = 0
                }
            }

            return fragment!!
        }
    }

    fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        val headerGridView = headersSupportFragment?.verticalGridView
        val currentTitleView = titleView

        if (event?.action == KeyEvent.ACTION_UP) {
            if (activity is MainActivity) {
                val mainActivity = activity as MainActivity

                mainActivity.dpadController?.enableLeft(!isShowingHeaders)

                if (currentTitleView != null) {
                    mainActivity.dpadController?.enableUp(!currentTitleView.hasFocus())
                } else {
                    mainActivity.dpadController?.enableUp(true)
                }

                if (fragmentFactory?.fragment is MainRowFragment && !isShowingHeaders) {
                    val fr: MainRowFragment = fragmentFactory?.fragment as MainRowFragment
                    mainActivity.dpadController?.enableDown(fr.selectedPosition != fr.mRowsAdapter.size() - 1)
                } else if (isShowingHeaders && headerGridView?.selectedPosition == mRowsAdapter?.size()?.minus(1)) {
                    mainActivity.dpadController?.enableDown(false)
                } else {
                    mainActivity.dpadController?.enableDown(true)
                }
            }
        } else if (event?.action == KeyEvent.ACTION_DOWN) {
            when(event.keyCode) {
                KeyEvent.KEYCODE_DPAD_DOWN -> {
                    if (!isShowingHeaders && currentTitleView?.hasFocus() == true && fragmentFactory?.fragment is MainRowFragment) {
                        (fragmentFactory?.fragment as MainRowFragment).verticalGridView.requestFocus()
                        (fragmentFactory?.fragment as MainRowFragment).setSelectedPosition(0, true)
                        return true
                    } else if (isShowingHeaders && currentTitleView?.hasFocus() == true) {
                        headerGridView?.requestFocus()
                        headerGridView?.setSelectedPositionSmooth(0)
                        return true
                    }
                }
                KeyEvent.KEYCODE_DPAD_RIGHT -> {
                    if (fragmentFactory?.fragment is MainGridFragment) {
                        val gridFragment = fragmentFactory?.fragment as MainGridFragment
                        if (gridFragment.canNextPosition() &&
                            gridFragment.mSelectedPosition >= MainGridFragment.COLUMNS - 1) {
                            currentTitleView?.animate()?.translationYBy((-currentTitleView.height).toFloat())
                                ?.setDuration(300)
                                ?.setListener(object : Animator.AnimatorListener {
                                    override fun onAnimationStart(animator: Animator) {}
                                    override fun onAnimationEnd(animator: Animator) {
                                        currentTitleView?.visibility = View.GONE
                                        currentTitleView?.translationY = 0f
                                    }
                                    override fun onAnimationCancel(animator: Animator) {}
                                    override fun onAnimationRepeat(animator: Animator) {}
                                })?.start()
                        }
                        return gridFragment.nextPosition()
                    }
                }
                KeyEvent.KEYCODE_DPAD_UP -> {
                    if (fragmentFactory?.fragment is MainGridFragment) {
                        val gridFragment = fragmentFactory?.fragment as MainGridFragment
                        if (gridFragment.mSelectedPosition == MainGridFragment.COLUMNS) {
                            currentTitleView?.translationY = (-(currentTitleView?.height ?: 0)).toFloat()
                            currentTitleView?.visibility = View.VISIBLE
                            currentTitleView?.animate()?.translationYBy((currentTitleView?.height ?: 0).toFloat())
                                ?.setDuration(300)
                                ?.setListener(object : Animator.AnimatorListener {
                                    override fun onAnimationStart(animator: Animator) {}
                                    override fun onAnimationEnd(animator: Animator) {
                                        currentTitleView?.visibility = View.VISIBLE
                                        currentTitleView?.translationY = 0f
                                    }
                                    override fun onAnimationCancel(animator: Animator) {}
                                    override fun onAnimationRepeat(animator: Animator) {}
                                })?.start()
                        }
                    }
                }
                KeyEvent.KEYCODE_BACK, KeyEvent.KEYCODE_ESCAPE, KeyEvent.KEYCODE_B -> {
                    if (isShowingHeaders) {
                        activity?.finish()
                        return true
                    }
                }
            }
        }
        return false
    }
}