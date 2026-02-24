package  com.bacbpl.iptv.jetfit.ui.activities

import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.bacbpl.iptv.R
import  com.bacbpl.iptv.jetfit.ui.fragments.MainFragment
import  com.bacbpl.iptv.jetfit.utils.DpadController

class MainActivity : FragmentActivity() {

    var dpadController: DpadController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
}
