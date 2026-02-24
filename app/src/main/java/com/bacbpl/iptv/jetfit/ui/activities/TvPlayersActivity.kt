package com.bacbpl.iptv.jetfit.ui.activities

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bacbpl.iptv.R
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerView

class TvPlayersActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_URL = "channel_url"
        private const val TAG = "TvPlayerActivity"
    }

    private var player: ExoPlayer? = null
    private lateinit var playerView: PlayerView
    private lateinit var progressBar: ProgressBar

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activitys_main_tv)

        playerView = findViewById(R.id.player_view)
        progressBar = findViewById(R.id.progress_bar)

        playerView.setFocusable(true)
        playerView.requestFocus()
        playerView.useController = true

        val url = intent.getStringExtra(EXTRA_URL)
        if (!url.isNullOrEmpty()) {
            initializePlayer(url)
        } else {
            Toast.makeText(this, "No URL provided", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun initializePlayer(url: String) {
        progressBar.visibility = View.VISIBLE
        Log.d(TAG, "Playing URL: $url")

        player = ExoPlayer.Builder(this).build().also { exoPlayer ->
            playerView.player = exoPlayer

            // MediaItem তৈরি করুন
            val mediaItem = MediaItem.fromUri(Uri.parse(url))
            exoPlayer.setMediaItem(mediaItem)

            // Listener যোগ করুন
            exoPlayer.addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    when (playbackState) {
                        ExoPlayer.STATE_BUFFERING -> {
                            progressBar.visibility = View.VISIBLE
                            Log.d(TAG, "Buffering...")
                        }
                        ExoPlayer.STATE_READY -> {
                            progressBar.visibility = View.GONE
                            Log.d(TAG, "Ready to play")
                        }
                    }
                }

                override fun onPlayerError(error: PlaybackException) {
                    progressBar.visibility = View.GONE
                    Log.e(TAG, "Playback error: ${error.message}")
                    Toast.makeText(this@TvPlayersActivity,
                        "Error: ${error.message}", Toast.LENGTH_LONG).show()
                }
            })

            exoPlayer.prepare()
            exoPlayer.play()
        }
    }

    @SuppressLint("GestureBackNavigation")
    override fun onKeyDown(keyCode: Int, event: android.view.KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    private fun releasePlayer() {
        player?.let {
            it.stop()
            it.release()
        }
        player = null
    }
}