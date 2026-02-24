package com.bacbpl.iptv.jetfit.utils.deeplink

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DeepLinkParams(
    val token: String,
    val backUrl: String,
    val source: String,
    val appName: String,
    val contentUrl: String,
    val contentId: String,
    val contentType: String
) : Parcelable