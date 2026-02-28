package com.bacbpl.iptv.jetfit.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.app.VerticalGridSupportFragment
import com.bacbpl.iptv.R

abstract class BaseVerticalGridFragment(private val category: String) :
    VerticalGridSupportFragment(),
    BrowseSupportFragment.MainFragmentAdapterProvider {

    private val mainFragmentAdapter = BrowseSupportFragment.MainFragmentAdapter(this)

    override fun getMainFragmentAdapter(): BrowseSupportFragment.MainFragmentAdapter<*> = mainFragmentAdapter

    // onStart() এ super.onStart() কল করবেন না
    override fun onStart() {
        super.onStart()
        try {
            // super.onStart() কল করবেন না - এটাই সমস্যার কারণ
            // super.onStart()

            Log.d("BaseVerticalGridFragment", "onStart: $category")

            // ব্যাকগ্রাউন্ড সেট করুন
            view?.let {
                it.setBackgroundColor(
                    ContextCompat.getColor(requireContext(), R.color.default_background)
                )
            }
        } catch (e: Exception) {
            Log.e("BaseVerticalGridFragment", "Error in onStart", e)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        try {
            super.onViewCreated(view, savedInstanceState)
            view.setBackgroundColor(
                ContextCompat.getColor(requireContext(), R.color.default_background)
            )
        } catch (e: Exception) {
            Log.e("BaseVerticalGridFragment", "Error in onViewCreated", e)
        }
    }

    override fun onResume() {
        try {
            super.onResume()
        } catch (e: Exception) {
            Log.e("BaseVerticalGridFragment", "Error in onResume", e)
        }
    }

    override fun onPause() {
        try {
            super.onPause()
        } catch (e: Exception) {
            Log.e("BaseVerticalGridFragment", "Error in onPause", e)
        }
    }
}