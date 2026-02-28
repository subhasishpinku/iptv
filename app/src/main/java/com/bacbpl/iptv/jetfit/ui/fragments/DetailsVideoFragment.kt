package com.bacbpl.iptv.jetfit.ui.fragments

import android.animation.Animator
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.bacbpl.iptv.R
import com.bacbpl.iptv.jetfit.ui.activities.DetailsActivity
import com.bacbpl.iptv.jetfit.utils.UtilsText
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class DetailsVideoFragment : Fragment() {

    private lateinit var playerView: StyledPlayerView
    private var exoPlayer: ExoPlayer? = null

    private lateinit var youTubePlayerView: YouTubePlayerView
    private var youTubePlayer: YouTubePlayer? = null

    private lateinit var iconView: ImageView
    private lateinit var textView: TextView
    private lateinit var curtimeView: TextView
    private lateinit var durationView: TextView
    private lateinit var seekContainerView: View
    private lateinit var seekBar: SeekBar
    private lateinit var progressBar: ProgressBar

    private var curtime = 0L
    private val handler = Handler(Looper.getMainLooper())
    private var currentYoutubeLink: String? = null
    private var isYouTubeMode = false
    private var isPlayerPrepared = false

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
            if (!isYouTubeMode && exoPlayer != null && isPlayerPrepared) {
                try {
                    val position = exoPlayer?.currentPosition ?: 0
                    val duration = exoPlayer?.duration ?: 0

                    if (duration > 0) {
                        seekBar.max = duration.toInt()
                        seekBar.progress = position.toInt()
                        curtimeView.text = UtilsText().msToStringTime(position.toInt())
                        durationView.text = UtilsText().msToStringTime(duration.toInt())
                    }

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
        return DetailsVideoFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.detail_video_fragment, container, false)

        // Initialize views
        playerView = view.findViewById(R.id.player_view1)
        youTubePlayerView = view.findViewById(R.id.youtube_player_view1)
        iconView = view.findViewById(R.id.video_action_icon)
        textView = view.findViewById(R.id.video_action_text)
        durationView = view.findViewById(R.id.video_duration)
        curtimeView = view.findViewById(R.id.video_curtime)
        seekContainerView = view.findViewById(R.id.video_seek_container)
        seekBar = view.findViewById(R.id.video_seekbar)
        progressBar = view.findViewById(R.id.progress_bar)

        // Set initial values
        curtimeView.text = "00:00"
        durationView.text = "00:00"
        seekContainerView.alpha = 0f
        progressBar.visibility = View.GONE

        // Hide players initially
        youTubePlayerView.visibility = View.GONE
        playerView.visibility = View.GONE

        // Setup YouTube Player
        setupYouTubePlayer()

        // Setup Video Player
        setupExoPlayer()

        return view
    }

    private fun setupYouTubePlayer() {
        try {
            lifecycle.addObserver(youTubePlayerView)

            youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    this@DetailsVideoFragment.youTubePlayer = youTubePlayer
                    progressBar.visibility = View.GONE
                    Log.d("YouTubePlayer", "YouTube player ready")

                    currentYoutubeLink?.let { link ->
                        val videoId = extractYoutubeId(link)
                        videoId?.let {
                            youTubePlayer.loadVideo(it, 0f)
                            isYouTubeMode = true
                            isPlayerPrepared = true
                        }
                    }
                }

                override fun onError(youTubePlayer: YouTubePlayer, error: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants.PlayerError) {
                    Log.e("YouTubePlayer", "Error: $error")
                    progressBar.visibility = View.GONE
                    Toast.makeText(context, "YouTube error: $error", Toast.LENGTH_SHORT).show()
                    fallbackToYouTubeApp(currentYoutubeLink)
                }
            })
        } catch (e: Exception) {
            Log.e("YouTubePlayer", "Setup error", e)
        }
    }

    private fun setupExoPlayer() {
        try {
            exoPlayer = ExoPlayer.Builder(requireContext()).build()
            playerView.player = exoPlayer
            playerView.useController = true
            playerView.setShowBuffering(StyledPlayerView.SHOW_BUFFERING_WHEN_PLAYING)

            exoPlayer?.addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    when (playbackState) {
                        Player.STATE_READY -> {
                            progressBar.visibility = View.GONE
                            isPlayerPrepared = true
                            Log.d("ExoPlayer", "Player ready")

                            val duration = exoPlayer?.duration ?: 0
                            if (duration > 0) {
                                seekBar.max = duration.toInt()
                                durationView.text = UtilsText().msToStringTime(duration.toInt())
                            }

                            seekBar.postDelayed(onEverySecond, 1000)

                            if (activity is DetailsActivity) {
                                (activity as DetailsActivity).bgContainerView.visibility = View.GONE
                                (activity as DetailsActivity).progressMain.visibility = View.GONE
                            }
                        }
                        Player.STATE_BUFFERING -> {
                            progressBar.visibility = View.VISIBLE
                            Log.d("ExoPlayer", "Buffering")
                        }
                        Player.STATE_ENDED -> {
                            Log.d("ExoPlayer", "Playback ended")
                        }
                        Player.STATE_IDLE -> {
                            Log.d("ExoPlayer", "Player idle")
                        }
                    }
                }

                override fun onPlayerError(error: PlaybackException) {
                    Log.e("ExoPlayer", "Error: ${error.message}")
                    progressBar.visibility = View.GONE
                    Toast.makeText(context, "Video error: ${error.message}", Toast.LENGTH_LONG).show()
                }

                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    Log.d("ExoPlayer", "Is playing: $isPlaying")
                }
            })
        } catch (e: Exception) {
            Log.e("ExoPlayer", "Setup error", e)
        }
    }

    fun setVideo(url: String) {
        try {
            Log.d("VideoPlayer", "Setting video URL: $url")
            hideYouTubePlayer()

            isYouTubeMode = false
            playerView.visibility = View.VISIBLE
            youTubePlayerView.visibility = View.GONE
            seekContainerView.visibility = View.VISIBLE
            progressBar.visibility = View.VISIBLE

            val mediaItem = MediaItem.fromUri(Uri.parse(url))
            exoPlayer?.setMediaItem(mediaItem)
            exoPlayer?.prepare()
            exoPlayer?.play()

            mute()

        } catch (e: Exception) {
            Log.e("VideoPlayer", "Error in setVideo", e)
            progressBar.visibility = View.GONE
            Toast.makeText(context, "Cannot play video: ${e.message}", Toast.LENGTH_LONG).show()
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
        Toast.makeText(context, videoId, Toast.LENGTH_SHORT).show()

        if (videoId.isNullOrEmpty()) {
            Toast.makeText(context, "Invalid YouTube link", Toast.LENGTH_SHORT).show()
            return
        }

        if (!isNetworkAvailable()) {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show()
            return
        }

        try {
            isYouTubeMode = true
            playerView.visibility = View.GONE
            seekContainerView.visibility = View.GONE
            youTubePlayerView.visibility = View.VISIBLE
            progressBar.visibility = View.VISIBLE

            if (activity is DetailsActivity) {
                (activity as DetailsActivity).bgContainerView.visibility = View.GONE
                (activity as DetailsActivity).progressMain.visibility = View.GONE
            }

            // If YouTube player is ready, load video directly
            if (youTubePlayer != null) {
                youTubePlayer?.loadVideo(videoId, 0f)
                isPlayerPrepared = true
                progressBar.visibility = View.GONE
            } else {
                // Otherwise, it will load when ready
                currentYoutubeLink = youtubeLink
                // Force YouTube player to initialize
                youTubePlayerView.visibility = View.VISIBLE
            }

        } catch (e: Exception) {
            Log.e("YouTubePlayer", "Error playing YouTube video", e)
            progressBar.visibility = View.GONE
            openInBrowser(videoId)
        }
    }

    private fun hideYouTubePlayer() {
        youTubePlayer?.pause()
        youTubePlayerView.visibility = View.GONE
    }

    private fun fallbackToYouTubeApp(youtubeLink: String?) {
        activity?.runOnUiThread {
            progressBar.visibility = View.GONE
            youTubePlayerView.visibility = View.GONE

            youtubeLink?.let {
                val videoId = extractYoutubeId(it)
                if (videoId != null) {
                    openInYouTubeApp(videoId)
                }
            }
        }
    }

    private fun openInYouTubeApp(videoId: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$videoId"))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        } catch (e: Exception) {
            openInBrowser(videoId)
        }
    }

    private fun openInBrowser(videoId: String) {
        try {
            val webIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://m.youtube.com/watch?v=$videoId")
            ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(webIntent)
        } catch (e: Exception) {
            Toast.makeText(context, "Cannot play YouTube video", Toast.LENGTH_SHORT).show()
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
            if (isYouTubeMode) {
                // YouTube player play state check is complex, assume true if player exists
                youTubePlayer != null
            } else {
                exoPlayer?.isPlaying ?: false
            }
        } catch (e: Exception) {
            false
        }
    }

    fun pause() {
        pause(true)
    }

    fun pause(b: Boolean) {
        try {
            if (isYouTubeMode) {
                youTubePlayer?.pause()
            } else if (exoPlayer?.isPlaying == true) {
                showSeekContainer()
                exoPlayer?.pause()
                curtime = exoPlayer?.currentPosition ?: 0
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
            if (isYouTubeMode) {
                youTubePlayer?.play()
            } else if (exoPlayer != null) {
                exoPlayer?.play()
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
            if (!isYouTubeMode) {
                exoPlayer?.volume = 0f
            }
        } catch (e: Exception) {
            Log.e("VideoPlayer", "Error in mute", e)
        }
    }

    fun unmute() {
        try {
            if (!isYouTubeMode) {
                exoPlayer?.volume = 1f
            }
        } catch (e: Exception) {
            Log.e("VideoPlayer", "Error in unmute", e)
        }
    }

    fun seek() {
        if (!isYouTubeMode) {
            try {
                seekBar.progress = curtime.toInt()
                exoPlayer?.seekTo(curtime)
                curtimeView.text = UtilsText().msToStringTime(curtime.toInt())
            } catch (e: Exception) {
                Log.e("VideoPlayer", "Error in seek", e)
            }
        }
    }

    fun seekPlus(ms: Int) {
        if (!isYouTubeMode && exoPlayer != null) {
            try {
                val duration = exoPlayer?.duration ?: 0
                val position = exoPlayer?.currentPosition ?: 0

                if (duration > position + ms) {
                    showSeekContainer()
                    curtime = position + ms
                    showCenterText("+${ms/1000}s")
                    seek()
                }
            } catch (e: Exception) {
                Log.e("VideoPlayer", "Error in seekPlus", e)
            }
        }
    }

    fun seekMinus(ms: Int) {
        if (!isYouTubeMode && exoPlayer != null) {
            try {
                val position = exoPlayer?.currentPosition ?: 0

                showSeekContainer()
                if (position > ms) {
                    curtime = position - ms
                    showCenterText("-${ms/1000}s")
                } else {
                    curtime = 0
                    showCenterText("-${(ms-position)/1000}s")
                }
                seek()
            } catch (e: Exception) {
                Log.e("VideoPlayer", "Error in seekMinus", e)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        exoPlayer?.pause()
        youTubePlayer?.pause()
    }

    override fun onResume() {
        super.onResume()
        if (!isYouTubeMode && exoPlayer != null && isPlayerPrepared) {
            exoPlayer?.play()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        exoPlayer?.release()
        exoPlayer = null
        youTubePlayer = null
        handler.removeCallbacksAndMessages(null)
    }
}