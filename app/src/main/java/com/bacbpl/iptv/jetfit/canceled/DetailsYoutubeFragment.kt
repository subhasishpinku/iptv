package com.bacbpl.iptv.jetfit.canceled


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import com.bacbpl.iptv.R
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import java.io.Serializable

class DetailsYoutubeFragment : Fragment() {

    private lateinit var youtubeView: YouTubePlayerView
    private var youTubePlayer: YouTubePlayer? = null
    private var tracker: YouTubePlayerTracker? = null
    private var card: ObjectCard? = null

    companion object {
        private const val PARAM_CARD = "param_card"

        fun newInstance(card: ObjectCard): DetailsYoutubeFragment {
            val fragment = DetailsYoutubeFragment()
            val args = Bundle()
            args.putSerializable(PARAM_CARD, card)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        card = arguments?.getSerializable(PARAM_CARD) as? ObjectCard
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.detail_youtube_fragment, container, false)

        youtubeView = view.findViewById(R.id.youtube_view)
        lifecycle.addObserver(youtubeView)

        tracker = YouTubePlayerTracker()

        youtubeView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(@NonNull player: YouTubePlayer) {
                youTubePlayer = player
                tracker?.let { player.addListener(it) }

                // Load & play video
                card?.videoId?.let { videoId ->
                    player.loadVideo(videoId, 0f)
                }
            }
        })

        return view
    }

    fun play() {
        youTubePlayer?.play()
    }

    fun pause() {
        youTubePlayer?.pause()
    }
}

/* ---------------------------------- */
/* MODEL */
/* ---------------------------------- */

data class ObjectCard(
    val videoId: String
) : Serializable
