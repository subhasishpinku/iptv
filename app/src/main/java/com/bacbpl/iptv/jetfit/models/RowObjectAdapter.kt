package com.bacbpl.iptv.jetfit.models

import androidx.leanback.widget.ArrayObjectAdapter
import com.bacbpl.iptv.jetfit.AppConstants

class RowObjectAdapter {
    var list: ArrayObjectAdapter = ArrayObjectAdapter()
    var page: Int = 0
    var type: Enum<AppConstants.TypeRows>? = null
    var header: String = ""
}