package com.bacbpl.iptv.jetfit.ui.fragments

import android.animation.Animator
import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.media.MediaPlayer
import android.net.Uri
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Handler
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import android.webkit.WebSettings
import android.webkit.WebChromeClient
import android.webkit.JavascriptInterface
import android.widget.*
import com.bacbpl.iptv.R
import com.bacbpl.iptv.jetfit.ui.activities.DetailsActivity
import com.bacbpl.iptv.jetfit.utils.UtilsText

class DetailsVideoFragment: Fragment() {
    private lateinit var videoView: VideoView
    private lateinit var iconView: ImageView
    private lateinit var textView: TextView
    private lateinit var curtimeView: TextView
    private lateinit var durationView: TextView
    private lateinit var seekContainerView: View
    private lateinit var seekBar: SeekBar
    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar
    private var curtime = 0
    private var mp: MediaPlayer? = null
    private val handler = Handler()
    private var currentYoutubeLink: String? = null
    private var isYouTubeMode = false

    private val runnableIcon = Runnable {
        iconView.animate()
            .alpha(0f)
            .setDuration(200)
            .start()
    }

    private val runnableText = Runnable {
        textView.animate()
            .alpha(0f)
            .setDuration(200)
            .start()
    }

    private val runnableSeek = Runnable {
        seekContainerView.animate()
            .alpha(0f)
            .setDuration(400)
            .start()
    }

    private val onEverySecond = object : Runnable {
        override fun run() {
            if (!isYouTubeMode && ::videoView.isInitialized) {
                try {
                    seekBar.progress = videoView.currentPosition
                    curtimeView.text = UtilsText().msToStringTime(videoView.currentPosition)
                    checkMute()
                    if (isPlaying()) {
                        seekBar.postDelayed(this, 1000)
                    }
                } catch (e: Exception) {
                    Log.e("VideoPlayer", "Error in onEverySecond", e)
                }
            }
        }
    }

    private fun checkMute() {
        try {
            if (activity is DetailsActivity) {
                if ((activity as DetailsActivity).detailsHiden)
                    unmute()
                else mute()
            }
        } catch (e: Exception) {
            Log.e("VideoPlayer", "Error in checkMute", e)
        }
    }

    fun newInstance(): DetailsVideoFragment {
        val fragment = DetailsVideoFragment()
        val args = Bundle()
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.detail_video_fragment, container, false)

        // Initialize views
        videoView = view.findViewById(R.id.video_view)
        iconView = view.findViewById(R.id.video_action_icon)
        textView = view.findViewById(R.id.video_action_text)
        durationView = view.findViewById(R.id.video_duration)
        curtimeView = view.findViewById(R.id.video_curtime)
        seekContainerView = view.findViewById(R.id.video_seek_container)
        seekBar = view.findViewById(R.id.video_seekbar)
        webView = view.findViewById(R.id.youtube_webview)
        progressBar = view.findViewById(R.id.progress_bar)

        // Set initial values
        curtimeView.text = "00:00"
        durationView.text = "00:00"
        seekContainerView.alpha = 0f
        webView.visibility = View.GONE
        progressBar.visibility = View.GONE
        webView.clearCache(true)
        webView.clearHistory()
        // Setup WebView for YouTube
        setupWebView()

        // Setup VideoView listeners
        setupVideoViewListeners()

        return view
    }

    private fun setupWebView() {
        try {
            val webSettings = webView.settings
            webSettings.javaScriptEnabled = true
            webSettings.loadWithOverviewMode = true
            webSettings.useWideViewPort = true
            webSettings.domStorageEnabled = true
            webSettings.cacheMode = WebSettings.LOAD_DEFAULT
            webSettings.mediaPlaybackRequiresUserGesture = false
            webSettings.allowContentAccess = true
            webSettings.allowFileAccess = true
            webSettings.allowUniversalAccessFromFileURLs = true
            webSettings.allowFileAccessFromFileURLs = true
            webSettings.javaScriptCanOpenWindowsAutomatically = true
            webSettings.setSupportMultipleWindows(true)
            webSettings.loadWithOverviewMode = true
            webSettings.useWideViewPort = true
            webSettings.builtInZoomControls = true
            webSettings.displayZoomControls = false
            webSettings.setSupportZoom(true)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }

            // WebChromeClient for better video support
            webView.webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    if (newProgress < 100) {
                        progressBar.visibility = View.VISIBLE
                        progressBar.progress = newProgress
                    } else {
                        progressBar.visibility = View.GONE
                    }
                }

                override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
                    super.onShowCustomView(view, callback)
                    // Handle fullscreen video if needed
                }

                override fun onHideCustomView() {
                    super.onHideCustomView()
                }
            }

            webView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    progressBar.visibility = View.GONE
                    Log.d("WebView", "Page finished: $url")
                }

                override fun onReceivedError(view: WebView?, errorCode: Int, description: String?, failingUrl: String?) {
                    super.onReceivedError(view, errorCode, description, failingUrl)
                    Log.e("WebView", "Error: $errorCode - $description")
                    progressBar.visibility = View.GONE
                    Toast.makeText(context, "Error loading video: $description", Toast.LENGTH_SHORT).show()
                }

                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    return false
                }
            }

            webView.addJavascriptInterface(WebAppInterface(), "Android")

        } catch (e: Exception) {
            Log.e("WebView", "Error setting up WebView", e)
        }
    }
    // JavaScript ইন্টারফেস আপডেট করুন
    inner class WebAppInterface {
        @JavascriptInterface
        fun onError(error: String) {
            activity?.runOnUiThread {
                Log.e("YouTube", "WebView YouTube Error: $error")
                Toast.makeText(context, "YouTube error: $error", Toast.LENGTH_SHORT).show()
                webView.visibility = View.GONE
                progressBar.visibility = View.GONE

                // Fallback to external player
                currentYoutubeLink?.let {
                    val videoId = extractYoutubeId(it)
                    if (videoId != null) {
                        playYoutubeExternal(videoId)
                    }
                }
            }
        }

        @JavascriptInterface
        fun onReady() {
            activity?.runOnUiThread {
                Log.d("YouTube", "WebView YouTube Ready")
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun setupVideoViewListeners() {
        videoView.setOnPreparedListener { mPlayer ->
            mp = mPlayer
            mute()

            seekBar.max = videoView.duration
            seekBar.postDelayed(onEverySecond, 1000)
            durationView.text = UtilsText().msToStringTime(videoView.duration)

            mp?.setOnBufferingUpdateListener { mp, percent ->
                val p = percent * seekBar.max / 100
                if (p < seekBar.max) {
                    seekBar.secondaryProgress = p
                } else
                    seekBar.secondaryProgress = seekBar.max
            }
        }

        videoView.setOnErrorListener { mp, what, extra ->
            Log.e("VideoView", "Error: what=$what, extra=$extra")
            if (activity is DetailsActivity) {
                (activity as DetailsActivity).progressMain.visibility = View.GONE
                (activity as DetailsActivity).bgContainerView.animate()
                    .alpha(1f)
                    .setDuration(200)
                    .start()
            }
            true
        }
    }

    fun setVideo(url: String) {
        try {
            isYouTubeMode = false
            videoView.visibility = View.VISIBLE
            webView.visibility = View.GONE
            seekContainerView.visibility = View.VISIBLE
            progressBar.visibility = View.GONE

            videoView.setVideoPath(url)

            videoView.setOnPreparedListener { mediaPlayer ->
                mp = mediaPlayer
                mediaPlayer.start()
                mute()

                seekBar.max = mediaPlayer.duration
                durationView.text = UtilsText().msToStringTime(mediaPlayer.duration)
                seekBar.postDelayed(onEverySecond, 1000)

                if (activity is DetailsActivity) {
                    (activity as DetailsActivity).bgContainerView.visibility = View.GONE
                    (activity as DetailsActivity).progressMain.visibility = View.GONE
                }
            }

            videoView.setOnErrorListener { _, _, _ ->
                Toast.makeText(context, "Video cannot be played", Toast.LENGTH_SHORT).show()
                true
            }
        } catch (e: Exception) {
            Log.e("VideoPlayer", "Error in setVideo", e)
        }
    }

    private fun extractYoutubeId(url: String): String? {
        val patterns = listOf(
            "v=([a-zA-Z0-9_-]{11})",
            "youtu\\.be/([a-zA-Z0-9_-]{11})",
            "embed/([a-zA-Z0-9_-]{11})",
            "youtube\\.com/watch\\?v=([a-zA-Z0-9_-]{11})"
        )

        for (pattern in patterns) {
            val regex = pattern.toRegex()
            val match = regex.find(url)
            if (match != null) {
                return match.groupValues[1]
            }
        }
        return null
    }
    fun playYoutube(youtubeLink: String) {
        currentYoutubeLink = youtubeLink
        val videoId = extractYoutubeId(youtubeLink)

        if (videoId.isNullOrEmpty()) {
            Toast.makeText(context, "Invalid YouTube link", Toast.LENGTH_SHORT).show()
            return
        }

        // নেটওয়ার্ক চেক করুন
        if (!isNetworkAvailable()) {
            Toast.makeText(context, "No internet connection. Please check your network.", Toast.LENGTH_LONG).show()
            return
        }

        try {
            isYouTubeMode = true
            videoView.visibility = View.GONE
            seekContainerView.visibility = View.GONE
            webView.visibility = View.VISIBLE
            progressBar.visibility = View.VISIBLE

            if (activity is DetailsActivity) {
                (activity as DetailsActivity).bgContainerView.visibility = View.GONE
                (activity as DetailsActivity).progressMain.visibility = View.GONE
            }

            // সরাসরি iframe ব্যবহার করে সহজ HTML
            val html = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
                <style>
                    body {
                        margin: 0;
                        padding: 0;
                        background-color: black;
                        overflow: hidden;
                    }
                    #player-container {
                        position: relative;
                        width: 100%;
                        height: 100vh;
                    }
                    iframe {
                        position: absolute;
                        top: 0;
                        left: 0;
                        width: 100%;
                        height: 100%;
                        border: none;
                    }
                </style>
            </head>
            <body>
                <div id="player-container">
                    <iframe 
                        src="https://www.youtube.com/embed/$videoId?autoplay=1&controls=1&rel=0&showinfo=0&modestbranding=1&iv_load_policy=3&playsinline=1"
                        allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture"
                        allowfullscreen>
                    </iframe>
                </div>
                
                <script>
                    window.onload = function() {
                        Android.onReady();
                    };
                    
                    window.onerror = function(message, source, lineno, colno, error) {
                        Android.onError(message);
                        return true;
                    };
                </script>
            </body>
            </html>
        """.trimIndent()

            webView.loadDataWithBaseURL("https://www.youtube.com", html, "text/html", "UTF-8", null)

        } catch (e: Exception) {
            Log.e("YouTubePlayer", "Error playing YouTube video", e)
            progressBar.visibility = View.GONE
            Toast.makeText(context, "Error playing video: ${e.message}", Toast.LENGTH_SHORT).show()
            playYoutubeExternal(videoId) // Fallback to external player
        }
    }

    private fun playYoutubeExternal(videoId: String) {
        try {
            progressBar.visibility = View.GONE

            // Try to open in YouTube app
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$videoId"))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        } catch (e: Exception) {
            try {
                // Fallback to browser
                val webIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://m.youtube.com/watch?v=$videoId")
                ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(webIntent)
            } catch (e2: Exception) {
                Toast.makeText(context, "Cannot play YouTube video", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork
            val capabilities = connectivityManager.getNetworkCapabilities(network)
            return capabilities != null && (
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                    )
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.activeNetworkInfo
            @Suppress("DEPRECATION")
            return networkInfo != null && networkInfo.isConnected
        }
    }

    private fun showCenterIcon(iconRes: Int) {
        try {
            iconView.setImageResource(iconRes)
            textView.alpha = 0f
            iconView.animate().cancel()
            handler.removeCallbacks(runnableIcon)
            handler.removeCallbacks(runnableText)
            iconView.animate()
                .alpha(1f)
                .setDuration(200)
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animator: Animator) {}
                    override fun onAnimationEnd(animator: Animator) {
                        handler.postDelayed(runnableIcon, 1500)
                    }
                    override fun onAnimationCancel(animator: Animator) {
                        iconView.alpha = 0f
                        handler.removeCallbacks(runnableIcon)
                        handler.removeCallbacks(runnableText)
                    }
                    override fun onAnimationRepeat(animator: Animator) {}
                })
                .start()
        } catch (e: Exception) {
            Log.e("VideoPlayer", "Error in showCenterIcon", e)
        }
    }

    private fun showCenterText(s: String) {
        try {
            textView.text = s
            textView.animate().cancel()
            iconView.alpha = 0f
            handler.removeCallbacks(runnableIcon)
            handler.removeCallbacks(runnableText)
            textView.animate()
                .alpha(1f)
                .setDuration(200)
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animator: Animator) {}
                    override fun onAnimationEnd(animator: Animator) {
                        handler.postDelayed(runnableText, 1500)
                    }
                    override fun onAnimationCancel(animator: Animator) {
                        textView.alpha = 0f
                        handler.removeCallbacks(runnableIcon)
                        handler.removeCallbacks(runnableText)
                    }
                    override fun onAnimationRepeat(animator: Animator) {}
                })
                .start()
        } catch (e: Exception) {
            Log.e("VideoPlayer", "Error in showCenterText", e)
        }
    }

    private fun showSeekContainer() {
        if (!isYouTubeMode) {
            try {
                handler.removeCallbacks(runnableSeek)
                seekContainerView.animate().cancel()
                seekContainerView.animate()
                    .alpha(1f)
                    .setDuration(400)
                    .setListener(object : Animator.AnimatorListener {
                        override fun onAnimationStart(animator: Animator) {}
                        override fun onAnimationEnd(animator: Animator) {
                            handler.postDelayed(runnableSeek, 5000)
                        }
                        override fun onAnimationCancel(animator: Animator) {
                            seekContainerView.alpha = 1f
                            handler.removeCallbacks(runnableSeek)
                        }
                        override fun onAnimationRepeat(animator: Animator) {}
                    })
                    .start()
            } catch (e: Exception) {
                Log.e("VideoPlayer", "Error in showSeekContainer", e)
            }
        }
    }

    fun isPlaying(): Boolean {
        return try {
            !isYouTubeMode && videoView.isPlaying
        } catch (e: Exception) {
            false
        }
    }

    fun pause() {
        pause(true)
    }

    fun pause(b: Boolean) {
        try {
            if (!isYouTubeMode && isPlaying()) {
                showSeekContainer()
                mp?.pause()
                curtime = mp?.currentPosition ?: 0
                checkMute()
                if (b) showCenterIcon(R.drawable.ic_player_action_pause)
            }
        } catch (e: Exception) {
            Log.e("VideoPlayer", "Error in pause", e)
        }
    }

    fun play() {
        play(true)
    }

    fun play(b: Boolean) {
        try {
            if (!isYouTubeMode) {
                mp?.start()
                checkMute()
                if (b) showCenterIcon(R.drawable.ic_player_action_play)
                seekBar.postDelayed(onEverySecond, 1000)
            }
        } catch (e: Exception) {
            Log.e("VideoPlayer", "Error in play", e)
        }
    }

    fun mute() {
        try {
            if (!isYouTubeMode && isPlaying()) {
                mp?.setVolume(0f, 0f)
            }
        } catch (e: Exception) {
            Log.e("VideoPlayer", "Error in mute", e)
        }
    }

    fun unmute() {
        try {
            if (!isYouTubeMode && isPlaying()) {
                mp?.setVolume(1f, 1f)
            }
        } catch (e: Exception) {
            Log.e("VideoPlayer", "Error in unmute", e)
        }
    }

    fun seek() {
        if (!isYouTubeMode) {
            try {
                seekBar.progress = curtime
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    mp?.seekTo(curtime.toLong(), MediaPlayer.SEEK_CLOSEST)
                } else mp?.seekTo(curtime)
                curtimeView.text = UtilsText().msToStringTime(curtime)
            } catch (e: Exception) {
                Log.e("VideoPlayer", "Error in seek", e)
            }
        }
    }

    fun seekPlus(ms: Int) {
        if (!isYouTubeMode && mp != null) {
            try {
                if (mp!!.duration > mp!!.currentPosition + ms) {
                    showSeekContainer()
                    curtime = mp!!.currentPosition + ms
                    showCenterText("+${ms/1000}s")
                    seek()
                }
            } catch (e: Exception) {
                Log.e("VideoPlayer", "Error in seekPlus", e)
            }
        }
    }

    fun seekMinus(ms: Int) {
        if (!isYouTubeMode && mp != null) {
            try {
                showSeekContainer()
                if (mp!!.currentPosition > ms) {
                    curtime = mp!!.currentPosition - ms
                    showCenterText("-${ms/1000}s")
                } else {
                    curtime = 0
                    showCenterText("-${(ms-mp!!.currentPosition)/1000}s")
                }
                seek()
            } catch (e: Exception) {
                Log.e("VideoPlayer", "Error in seekMinus", e)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        try {
            webView.onPause()
            webView.pauseTimers()
        } catch (e: Exception) {
            Log.e("WebView", "Error pausing WebView", e)
        }
    }

    override fun onResume() {
        super.onResume()
        try {
            webView.onResume()
            webView.resumeTimers()
        } catch (e: Exception) {
            Log.e("WebView", "Error resuming WebView", e)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        try {
            webView.stopLoading()
            webView.destroy()
        } catch (e: Exception) {
            Log.e("WebView", "Error destroying WebView", e)
        }
    }
}