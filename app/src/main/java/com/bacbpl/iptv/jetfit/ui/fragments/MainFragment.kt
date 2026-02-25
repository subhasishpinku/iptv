package com.bacbpl.iptv.jetfit.ui.fragments

import android.animation.Animator
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
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

class MainFragment : BrowseSupportFragment() {
    private var mRowsAdapter: ArrayObjectAdapter? = null
    private var fragmentFactory: PageRowFragmentFactory? = null

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

    private fun setupUi() {
        brandColor = ContextCompat.getColor(requireActivity(),
            R.color.default_background
        )

        // সার্চ অ্যাফোর্ডেন্স সম্পূর্ণ সরানো হয়েছে
        // searchAffordanceColors লাইনটি ডিলিট করা হয়েছে

        setHeaderPresenterSelector(object : PresenterSelector() {
            override fun getPresenter(o: Any): Presenter {
                return HeaderIconItemPresenter()
            }
        })

        // সার্চ লিসেনার সরানো হয়েছে (কমেন্ট আউট)
        // setOnSearchClickedListener { }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // সার্চ বাটন visibility GONE করার কোড
        view.post {
            try {
                val searchOrb = view.findViewById<View>(androidx.leanback.R.id.title_orb)
                searchOrb?.visibility = View.GONE

                // যদি title_orb না পাওয়া যায়, তাহলে alternative আইডি চেষ্টা করুন
                val searchAffordance = view.findViewById<View>(androidx.leanback.R.id.search_orb)
                searchAffordance?.visibility = View.GONE
            } catch (e: Exception) {
                Log.e("MainFragment", "Error hiding search button", e)
            }
        }
    }

    // বাকি কোড unchanged...
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
                    mCashedFragments[cachedId] = NewsFragment(row.headerItem.name)
                    mCashedFragments[cachedId]
                }

                row.headerItem.name.equals(
                    context?.getString(R.string.main_sports), true
                ) -> {
                    mCashedFragments[cachedId] = SportsFragment(row.headerItem.name)
                    mCashedFragments[cachedId]
                }

                row.headerItem.name.equals(
                    context?.getString(R.string.main_ott), true
                ) -> {
                    mCashedFragments[cachedId] = DeepLinkFragment(row.headerItem.name)
                    mCashedFragments[cachedId]
                }
                row.headerItem.name.equals(
                    context?.getString(R.string.ott_ply), true
                ) -> {
                    mCashedFragments[cachedId] = OTTPlayFragment(row.headerItem.name)
                    mCashedFragments[cachedId]
                }

                row.headerItem.name.equals(
                    context?.getString(R.string.main_tv_channel), true
                ) -> {
                    mCashedFragments[cachedId] =
                        TvChannelGridFragment()
                    mCashedFragments[cachedId]
                }

                row.headerItem.name.equals(
                    context?.getString(R.string.main_devotional), true
                ) -> {
                    mCashedFragments[cachedId] = DevotionalFragment(row.headerItem.name)
                    mCashedFragments[cachedId]
                }


                row.headerItem.name.equals(
                    context?.getString(R.string.main_kids), true
                ) -> {
                    mCashedFragments[cachedId] = KidsFragment(row.headerItem.name)
                    mCashedFragments[cachedId]
                }

                row.headerItem.name.equals(
                    context?.getString(R.string.main_person), true
                ) -> {
                    mCashedFragments[cachedId] =
                        MainGridFragment(row.headerItem.name)
                    mCashedFragments[cachedId]
                }

                else -> {
                    mCashedFragments[cachedId] =
                        MainRowFragment(context!!, row.headerItem.name)
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
        val headerGridView = headersSupportFragment.verticalGridView
        if (event?.action == KeyEvent.ACTION_UP) {
            if (activity is MainActivity) {
                (activity as MainActivity).dpadController?.enableLeft(!isShowingHeaders)
                (activity as MainActivity).dpadController?.enableUp(!titleView.hasFocus())
                if (fragmentFactory?.fragment is MainRowFragment && !isShowingHeaders) {
                    val fr: MainRowFragment = fragmentFactory?.fragment as MainRowFragment
                    (activity as MainActivity).dpadController?.enableDown(fr.selectedPosition != fr.mRowsAdapter.size() - 1)
                } else if (isShowingHeaders && headerGridView?.selectedPosition == mRowsAdapter!!.size()-1){
                    (activity as MainActivity).dpadController?.enableDown(false)
                } else{
                    (activity as MainActivity).dpadController?.enableDown(true)
                }
            }
        } else if (event?.action == KeyEvent.ACTION_DOWN) {
            when(event.keyCode) {
                KeyEvent.KEYCODE_DPAD_DOWN -> {
                    if (!isShowingHeaders && titleView.hasFocus() && fragmentFactory?.fragment is MainRowFragment) {
                        (fragmentFactory?.fragment as MainRowFragment).verticalGridView.requestFocus()
                        (fragmentFactory?.fragment as MainRowFragment).setSelectedPosition(0, true)
                        return true
                    } else if (isShowingHeaders && titleView.hasFocus()) {
                        headerGridView?.requestFocus()
                        headerGridView?.setSelectedPositionSmooth(0)
                        return true
                    }
                }
                KeyEvent.KEYCODE_DPAD_RIGHT -> {
                    if (fragmentFactory?.fragment is MainGridFragment) {
                        if ((fragmentFactory?.fragment as MainGridFragment).canNextPosition() &&
                            (fragmentFactory?.fragment as MainGridFragment).mSelectedPosition >= MainGridFragment.COLUMNS - 1)
                            titleView.animate().translationYBy((-titleView.height).toFloat())
                                .setDuration(300)
                                .setListener(object : Animator.AnimatorListener {
                                    override fun onAnimationStart(animator: Animator) {}
                                    override fun onAnimationEnd(animator: Animator) {
                                        titleView.visibility = View.GONE
                                        titleView.translationY = 0f
                                    }
                                    override fun onAnimationCancel(animator: Animator) {}
                                    override fun onAnimationRepeat(animator: Animator) {}
                                }).start()
                        return (fragmentFactory?.fragment as MainGridFragment).nextPosition()
                    }
                }
                KeyEvent.KEYCODE_DPAD_UP -> {
                    if (fragmentFactory?.fragment is MainGridFragment) {
                        if ((fragmentFactory?.fragment as MainGridFragment).mSelectedPosition == MainGridFragment.COLUMNS) {
                            titleView.translationY = (-titleView.height).toFloat()
                            titleView.visibility = View.VISIBLE
                            titleView.animate().translationYBy((titleView.height).toFloat())
                                .setDuration(300)
                                .setListener(object : Animator.AnimatorListener {
                                    override fun onAnimationStart(animator: Animator) {}
                                    override fun onAnimationEnd(animator: Animator) {
                                        titleView.visibility = View.VISIBLE
                                        titleView.translationY = 0f
                                    }
                                    override fun onAnimationCancel(animator: Animator) {}
                                    override fun onAnimationRepeat(animator: Animator) {}
                                }).start()
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