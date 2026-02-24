package com.bacbpl.iptv.jetfit.ui.fragments

import android.animation.Animator
import android.content.Intent
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.util.Log
import android.util.SparseArray
import android.widget.*
import androidx.annotation.NonNull
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YouTubeExtractor
import at.huber.youtubeExtractor.YtFile

import com.bacbpl.iptv.R
import com.bacbpl.iptv.jetfit.ui.activities.DetailsActivity
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.bacbpl.iptv.jetfit.utils.UtilsText
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants


class DetailsVideoFragment: Fragment() {
    private lateinit var videoView: VideoView
    private lateinit var iconView: ImageView
    private lateinit var textView: TextView
    private lateinit var curtimeView: TextView
    private lateinit var durationView: TextView
    private lateinit var seekContainerView: View
    private lateinit var seekBar: SeekBar
    private var curtime = 0
    private var mp: MediaPlayer? = null
    private val handler = Handler()
    private lateinit var ytPlayer: YouTubePlayerView
    private var currentYoutubeLink: String? = null

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
            seekBar.progress = videoView.currentPosition
            curtimeView.text = UtilsText().msToStringTime(videoView.currentPosition)
            checkMute()
            if (isPlaying()) {
                seekBar.postDelayed(this, 1000)
            }
        }
    }

    private fun checkMute() {
        if (activity is DetailsActivity) {
            if ((activity as DetailsActivity).detailsHiden)
                unmute()
            else mute()
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
        ytPlayer = view.findViewById(R.id.youtube_player)

        // Set initial values
        curtimeView.text = "00:00"
        durationView.text = "00:00"
        seekContainerView.alpha = 0f

        // Initialize YouTube Player with error handling
        setupYouTubePlayer()

        // Setup VideoView listeners
        setupVideoViewListeners()

        return view
    }

    private fun setupYouTubePlayer() {
        try {
            lifecycle.addObserver(ytPlayer)

            ytPlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(@NonNull youTubePlayer: YouTubePlayer) {
                    Log.d("YouTubePlayer", "Player is ready")
                }

                override fun onError(
                    youTubePlayer: YouTubePlayer,
                    error: PlayerConstants.PlayerError
                ) {
                    Log.e("YouTubePlayer", "Error: $error")

                    // Fallback to external player
                    currentYoutubeLink?.let { link ->
                        val videoId = extractYoutubeId(link)
                        if (videoId != null) {
                            playYoutubeExternal(videoId)
                        }
                    }
                }
            })
        } catch (e: Exception) {
            Log.e("YouTubePlayer", "Error setting up YouTube player", e)
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
    }

    private fun extractYoutubeId(url: String): String? {
        val regex = Regex("(?:v=|/)([0-9A-Za-z_-]{11}).*")
        return regex.find(url)?.groupValues?.get(1)
    }

    // ========== এই ফাংশনটি রাখুন ==========
// In DetailsVideoFragment.kt - Replace playYoutube function

// In DetailsVideoFragment.kt - Replace the playYoutube function

    fun playYoutube(youtubeLink: String) {
        currentYoutubeLink = youtubeLink
        val videoId = extractYoutubeId(youtubeLink)

        if (videoId.isNullOrEmpty()) {
            Toast.makeText(context, "Invalid YouTube link", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            // Hide VideoView, show YouTube player
            videoView.visibility = View.GONE
            ytPlayer.visibility = View.VISIBLE

            // Make sure YouTube player is added to lifecycle
            lifecycle.addObserver(ytPlayer)

            // The correct way to get YouTube player - add listener and wait for onReady
            ytPlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    super.onReady(youTubePlayer)
                    // Load video when player is ready
                    youTubePlayer.loadVideo(videoId, 0f)

                    if (activity is DetailsActivity) {
                        (activity as DetailsActivity).progressMain.visibility = View.GONE
                    }
                }

                override fun onError(youTubePlayer: YouTubePlayer, error: PlayerConstants.PlayerError) {
                    super.onError(youTubePlayer, error)
                    Log.e("YouTubePlayer", "Error: $error")
                    // Fallback to external player
                    playYoutubeExternal(videoId)
                }
            })
        } catch (e: Exception) {
            Log.e("YouTubePlayer", "Error playing YouTube video", e)
            // Fallback to external player
            playYoutubeExternal(videoId)
        }
    }
    private fun playYoutubeExternal(videoId: String) {
        try {
            // Try YouTube TV app
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("vnd.youtube:$videoId")
                setPackage("com.google.android.youtube.tv")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(intent)
        } catch (e: Exception) {
            try {
                // Fallback: normal YouTube app
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse("vnd.youtube:$videoId")
                    setPackage("com.google.android.youtube")
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                startActivity(intent)
            } catch (e2: Exception) {
                // Last fallback: browser
                val webIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/watch?v=$videoId")
                ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(webIntent)
            }
        }
    }

    // ========== বাকি ফাংশনগুলো অপরিবর্তিত ==========
    private fun showCenterIcon(icPlayerActionPause: Int) {
        iconView.setImageResource(icPlayerActionPause)
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
    }

    private fun showCenterText(s: String) {
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
    }

    private fun showSeekContainer() {
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
    }

    fun isPlaying(): Boolean {
        return videoView.isPlaying
    }

    fun pause() {
        pause(true)
    }

    fun pause(b: Boolean) {
        if (isPlaying()) {
            showSeekContainer()
            mp?.pause()
            curtime = mp!!.currentPosition
            checkMute()
            if (b) showCenterIcon(R.drawable.ic_player_action_pause)
        }
    }

    fun play() {
        play(true)
        seekBar.postDelayed(onEverySecond, 1000)
    }

    fun play(b: Boolean) {
        mp?.start()
        checkMute()
        if (b) showCenterIcon(R.drawable.ic_player_action_play)
    }

    fun mute() {
        if (isPlaying()) {
            mp?.setVolume(0f, 0f)
        }
    }

    fun unmute() {
        if (isPlaying()) {
            mp?.setVolume(1f, 1f)
        }
    }

    fun seek() {
        seekBar.progress = curtime
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mp?.seekTo(curtime.toLong(), MediaPlayer.SEEK_CLOSEST)
        } else mp?.seekTo(curtime)
        curtimeView.text = UtilsText().msToStringTime(curtime)
    }

    fun seekPlus(ms: Int) {
        if (mp != null) {
            if(mp!!.duration > mp!!.currentPosition + ms) {
                showSeekContainer()
                curtime = mp!!.currentPosition + ms
                showCenterText("+${ms/1000}s")
                seek()
            }
        }
    }

    fun seekMinus(ms: Int) {
        if (mp != null) {
            showSeekContainer()
            if (mp!!.currentPosition > ms) {
                curtime = mp!!.currentPosition - ms
                showCenterText("-${ms/1000}s")
            } else {
                curtime = 0
                showCenterText("-${(ms-mp!!.currentPosition)/1000}s")
            }
            seek()
        }
    }
}