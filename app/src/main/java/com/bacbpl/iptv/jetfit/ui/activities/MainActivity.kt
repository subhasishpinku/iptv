package com.bacbpl.iptv.jetfit.ui.activities

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.bacbpl.iptv.R
import com.bacbpl.iptv.jetfit.ui.fragments.MainFragment
import com.bacbpl.iptv.jetfit.utils.BackgroundSlideshow
import com.bacbpl.iptv.jetfit.utils.DpadController

class MainActivity : FragmentActivity() {

    var dpadController: DpadController? = null
    private lateinit var backgroundSlideshow: BackgroundSlideshow

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Background Slideshow
        val bgImage1 = findViewById<ImageView>(R.id.bg_slideshow_1)
        val bgImage2 = findViewById<ImageView>(R.id.bg_slideshow_2)

        backgroundSlideshow = BackgroundSlideshow(bgImage1, bgImage2)
        backgroundSlideshow.start()

        // Load MainFragment into the content frame
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.content, MainFragment(), "main")
            .addToBackStack(null)
            .commitAllowingStateLoss()

        // Initialize DpadController
        dpadController = DpadController(this)
        dpadController?.apply {
            enableDown(true)
            enableUp(true)
            enableRight(true)
            enableLeft(false)
        }
    }

    private fun getCurrentFragment(): Fragment? {
        return supportFragmentManager.findFragmentById(R.id.content)
    }

    // Handle key down events
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        dpadController?.dpadControl(event)

        val fragment = getCurrentFragment()
        return if (fragment is MainFragment) {
            fragment.dispatchKeyEvent(event) || super.onKeyDown(keyCode, event)
        } else {
            super.onKeyDown(keyCode, event)
        }
    }

    // Handle key up events
    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        val fragment = getCurrentFragment()
        return if (fragment is MainFragment) {
            fragment.dispatchKeyEvent(event) || super.onKeyUp(keyCode, event)
        } else {
            super.onKeyUp(keyCode, event)
        }
    }

    override fun onResume() {
        super.onResume()
        // Resume slideshow when activity resumes
        backgroundSlideshow.start()
    }

    override fun onPause() {
        super.onPause()
        // Stop slideshow when activity pauses to save resources
        backgroundSlideshow.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Clean up
        backgroundSlideshow.stop()
    }
}