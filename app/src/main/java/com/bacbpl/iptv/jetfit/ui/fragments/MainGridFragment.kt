//package  com.bacbpl.iptv.jetfit.ui.fragments
//
//import android.os.Bundle
//import android.view.View
//import android.widget.ProgressBar
//import androidx.leanback.widget.*
//import com.bacbpl.iptv.R
//import  com.bacbpl.iptv.jetfit.*
//import com.bacbpl.iptv.jetfit.models.RowObjectAdapter
//import info.movito.themoviedbapi.TmdbApi
//import  com.bacbpl.iptv.jetfit.ui.presenters.PersonPresenter
//import  com.bacbpl.iptv.jetfit.utils.CompletedListener
//import com.bacbpl.iptv.jetfit.utils.DoAsync
//import  com.bacbpl.iptv.jetfit.utils.ListListener
//import  com.bacbpl.iptv.jetfit.utils.LoadMoreItems
//
//class MainGridFragment(private val category: String) : CustomGridFragment() {
//    private val ZOOM_FACTOR = FocusHighlight.ZOOM_FACTOR_LARGE
//    private var page = 1
//    private var load = false
//    private lateinit var obj: RowObjectAdapter
//    private lateinit var tmdb: TmdbApi
//    private lateinit var presenterGrid: VerticalGridPresenter
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setupAdapter()
//        loadData()
//        mainFragmentAdapter.fragmentHost.notifyDataReady(mainFragmentAdapter)
//    }
//
//
//    private fun setupAdapter() {
//        presenterGrid = VerticalGridPresenter(ZOOM_FACTOR, false)
//        gridPresenter = presenterGrid
//
//        onItemViewClickedListener =
//            OnItemViewClickedListener { itemViewHolder, item, rowViewHolder, row ->
//
//            }
//        setOnItemViewSelectedListener(
//            OnItemViewSelectedListener { itemViewHolder, item, rowViewHolder, row ->
//                if (obj.list.indexOf(item) > obj.list.size() - (COLUMNS *2) && !load) {
//                    page++
//                    loadMoreItems(obj)
//                }
//            })
//    }
//
//    fun nextPosition(): Boolean {
//        if (canNextPosition()) {
//            setSelectedPosition(mSelectedPosition + 1)
//            return true
//        }
//        return false
//    }
//
//    fun canNextPosition(): Boolean {
//        val f = (mSelectedPosition.toFloat()+1)/COLUMNS.toFloat()
//        return (f.toString().endsWith(".0"))
//    }
//
//    private fun loadData() {
//        pb(true)
//        obj = RowObjectAdapter()
//        obj.page = 0
//        if (category.equals(context?.getString(R.string.main_person), true)) {
//            COLUMNS = 5
//            presenterGrid.numberOfColumns = COLUMNS
//            presenterGrid.shadowEnabled = false
//            obj.list = ArrayObjectAdapter(PersonPresenter())
//            obj.type = AppConstants.TypeRows.PEOPLE
//            obj.header = requireContext().getString(R.string.main_person)
//        }
//        adapter = obj.list
//        DoAsync(object : CompletedListener {
//            override fun onCompleted() {
//                loadMoreItems(obj)
//            }
//
//        }) {
//            tmdb = TmdbApi(AppConstants.API_TMDB)
//        }
//    }
//
//
//    private fun loadMoreItems(rowObject: RowObjectAdapter) {
//        pb(true)
//        LoadMoreItems(rowObject, tmdb, object : ListListener {
//            override fun onResult(list: ArrayList<Any>) {
//                pb(false)
//                rowObject.list.addAll(rowObject.list.size(), list)
//            }
//        })
//    }
//
//    private fun pb(b:Boolean) {
//        load = b
//        if (b) {
//            activity?.findViewById<ProgressBar>(R.id.pb)?.visibility = View.VISIBLE
//        } else {
//            activity?.findViewById<ProgressBar>(R.id.pb)?.visibility = View.GONE
//        }
//    }
//
//    companion object {
//        var COLUMNS = 5
//    }
//}

package com.bacbpl.iptv.jetfit.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.leanback.widget.*
import com.bacbpl.iptv.R
import com.bacbpl.iptv.jetfit.*
import com.bacbpl.iptv.jetfit.models.RowObjectAdapter
import com.bacbpl.iptv.jetfit.models.tmdb.TmdbPerson
import com.bacbpl.iptv.jetfit.repository.TmdbRepository
import com.bacbpl.iptv.jetfit.ui.presenters.PersonPresenter
import com.bacbpl.iptv.jetfit.utils.ListListener

class MainGridFragment(private val category: String) : CustomGridFragment() {
    private val ZOOM_FACTOR = FocusHighlight.ZOOM_FACTOR_LARGE
    private var page = 1
    private var load = false
    private lateinit var obj: RowObjectAdapter

    // Remove old tmdb
    // private lateinit var tmdb: TmdbApi

    // Add new repository
    private val tmdbRepository = TmdbRepository()

    private lateinit var presenterGrid: VerticalGridPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupAdapter()
        loadData()
        mainFragmentAdapter.fragmentHost.notifyDataReady(mainFragmentAdapter)
    }

    private fun setupAdapter() {
        presenterGrid = VerticalGridPresenter(ZOOM_FACTOR, false)
        gridPresenter = presenterGrid

        onItemViewClickedListener =
            OnItemViewClickedListener { itemViewHolder, item, rowViewHolder, row ->
                // Handle item click
            }

        setOnItemViewSelectedListener(
            OnItemViewSelectedListener { itemViewHolder, item, rowViewHolder, row ->
                if (obj.list.indexOf(item) > obj.list.size() - (COLUMNS * 2) && !load) {
                    page++
                    loadMoreItems(obj)
                }
            }
        )
    }

    fun nextPosition(): Boolean {
        if (canNextPosition()) {
            setSelectedPosition(mSelectedPosition + 1)
            return true
        }
        return false
    }

    fun canNextPosition(): Boolean {
        val f = (mSelectedPosition.toFloat() + 1) / COLUMNS.toFloat()
        return f.toString().endsWith(".0")
    }

    private fun loadData() {
        pb(true)
        obj = RowObjectAdapter()
        obj.page = 0

        if (category.equals(context?.getString(R.string.main_person), true)) {
            COLUMNS = 5
            presenterGrid.numberOfColumns = COLUMNS
            presenterGrid.shadowEnabled = false
            obj.list = ArrayObjectAdapter(PersonPresenter())
            obj.type = AppConstants.TypeRows.PEOPLE
            obj.header = requireContext().getString(R.string.main_person)
            loadPeople()
        } else {
            adapter = obj.list
            pb(false)
        }
    }

    private fun loadPeople() {
        tmdbRepository.getPopularPeople(
            page = 1,
            onSuccess = { people ->
                activity?.runOnUiThread {
                    people.forEach { person ->
                        obj.list.add(person)
                    }
                    adapter = obj.list
                    pb(false)
                }
            },
            onError = { error ->
                Log.e("MainGridFragment", "Error loading people: $error")
                activity?.runOnUiThread {
                    pb(false)
                }
            }
        )
    }

    private fun loadMoreItems(rowObject: RowObjectAdapter) {
        pb(true)

        when (rowObject.type) {
            AppConstants.TypeRows.PEOPLE -> {
                tmdbRepository.getPopularPeople(
                    page = page,
                    onSuccess = { people ->
                        activity?.runOnUiThread {
                            val list = ArrayList<Any>()
                            list.addAll(people)

                            rowObject.list.addAll(rowObject.list.size(), list)
                            pb(false)
                        }
                    },
                    onError = { error ->
                        Log.e("MainGridFragment", "Error loading more people: $error")
                        activity?.runOnUiThread {
                            pb(false)
                        }
                    }
                )
            }
            else -> {
                pb(false)
            }
        }
    }

    private fun pb(b: Boolean) {
        load = b
        activity?.runOnUiThread {
            if (b) {
                activity?.findViewById<ProgressBar>(R.id.pb)?.visibility = View.VISIBLE
            } else {
                activity?.findViewById<ProgressBar>(R.id.pb)?.visibility = View.GONE
            }
        }
    }

    companion object {
        var COLUMNS = 5
    }
}