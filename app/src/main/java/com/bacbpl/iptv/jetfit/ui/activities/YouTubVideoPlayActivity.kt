package  com.bacbpl.iptv.jetfit.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bacbpl.iptv.R
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class YouTubVideoPlayActivity : AppCompatActivity() {

    private var youtubeLink: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        youtubeLink = intent.getStringExtra("youtubeLink")

        val youTubePlayerView =
            findViewById<YouTubePlayerView>(R.id.youtube_player_view)
        lifecycle.addObserver(youTubePlayerView)

        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youtubeLink?.let {
                    youTubePlayer.loadVideo(it, 0f)
                }
            }
        })
    }
}
