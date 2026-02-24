/*
 * Copyright 2023 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
//
//package  com.bacbpl.iptv.jetStram.presentation.screens.videoPlayer
//
//import android.net.Uri
//import androidx.activity.compose.BackHandler
//import androidx.compose.foundation.focusable
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.focus.FocusRequester
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.platform.LocalContext
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.lifecycle.compose.collectAsStateWithLifecycle
//import androidx.media3.common.C
//import androidx.media3.common.MediaItem
//import androidx.media3.common.util.UnstableApi
//import androidx.media3.exoplayer.ExoPlayer
//import androidx.media3.ui.compose.PlayerSurface
//import androidx.media3.ui.compose.SURFACE_TYPE_TEXTURE_VIEW
//import androidx.media3.ui.compose.modifiers.resizeWithContentScale
//import com.bacbpl.iptv.jetStram.data.entities.Movie
//import com.bacbpl.iptv.jetStram.data.entities.MovieDetails
//import com.bacbpl.iptv.jetStram.presentation.common.Error
//import com.bacbpl.iptv.jetStram.presentation.common.Loading
//import  com.bacbpl.iptv.jetStram.presentation.screens.videoPlayer.components.VideoPlayerControls
//import  com.bacbpl.iptv.jetStram.presentation.screens.videoPlayer.components.VideoPlayerOverlay
//import  com.bacbpl.iptv.jetStram.presentation.screens.videoPlayer.components.VideoPlayerPulse
//import  com.bacbpl.iptv.jetStram.presentation.screens.videoPlayer.components.VideoPlayerPulse.Type.BACK
//import  com.bacbpl.iptv.jetStram.presentation.screens.videoPlayer.components.VideoPlayerPulse.Type.FORWARD
//import  com.bacbpl.iptv.jetStram.presentation.screens.videoPlayer.components.VideoPlayerPulseState
//import  com.bacbpl.iptv.jetStram.presentation.screens.videoPlayer.components.VideoPlayerState
//import  com.bacbpl.iptv.jetStram.presentation.screens.videoPlayer.components.rememberPlayer
//import  com.bacbpl.iptv.jetStram.presentation.screens.videoPlayer.components.rememberVideoPlayerPulseState
//import  com.bacbpl.iptv.jetStram.presentation.screens.videoPlayer.components.rememberVideoPlayerState
//import com.bacbpl.iptv.jetStram.presentation.utils.handleDPadKeyEvents
//
//object VideoPlayerScreen {
//    const val MovieIdBundleKey = "movieId"
//}
//
///**
// * [Work in progress] A composable screen for playing a video.
// *
// * @param onBackPressed The callback to invoke when the user presses the back button.
// * @param videoPlayerScreenViewModel The view model for the video player screen.
// */
//@Composable
//fun VideoPlayerScreen(
//    onBackPressed: () -> Unit,
//    videoPlayerScreenViewModel: VideoPlayerScreenViewModel = hiltViewModel()
//) {
//    val uiState by videoPlayerScreenViewModel.uiState.collectAsStateWithLifecycle()
//
//    // TODO: Handle Loading & Error states
//    when (val s = uiState) {
//        is VideoPlayerScreenUiState.Loading -> {
//            Loading(modifier = Modifier.fillMaxSize())
//        }
//
//        is VideoPlayerScreenUiState.Error -> {
//            Error(modifier = Modifier.fillMaxSize())
//        }
//
//        is VideoPlayerScreenUiState.Done -> {
//            VideoPlayerScreenContent(
//                movieDetails = s.movieDetails,
//                onBackPressed = onBackPressed
//            )
//        }
//    }
//}
//
//@androidx.annotation.OptIn(UnstableApi::class)
//@Composable
//fun VideoPlayerScreenContent(movieDetails: MovieDetails, onBackPressed: () -> Unit) {
//    val context = LocalContext.current
//    val exoPlayer = rememberPlayer(context)
//
//    val videoPlayerState = rememberVideoPlayerState(
//        hideSeconds = 4,
//    )
//
//    LaunchedEffect(exoPlayer, movieDetails) {
//        exoPlayer.addMediaItem(movieDetails.intoMediaItem())
//        movieDetails.similarMovies.forEach {
//            exoPlayer.addMediaItem(it.intoMediaItem())
//        }
//        exoPlayer.prepare()
//    }
//
//    BackHandler(onBack = onBackPressed)
//
//    val pulseState = rememberVideoPlayerPulseState()
//
//    Box(
//        Modifier
//            .dPadEvents(
//                exoPlayer,
//                videoPlayerState,
//                pulseState
//            )
//            .focusable()
//    ) {
//        PlayerSurface(
//            player = exoPlayer,
//            surfaceType = SURFACE_TYPE_TEXTURE_VIEW,
//            modifier = Modifier.resizeWithContentScale(
//                contentScale = ContentScale.Fit,
//                sourceSizeDp = null
//            )
//        )
//
//        val focusRequester = remember { FocusRequester() }
//        VideoPlayerOverlay(
//            modifier = Modifier.align(Alignment.BottomCenter),
//            focusRequester = focusRequester,
//            isPlaying = exoPlayer.isPlaying,
//            isControlsVisible = videoPlayerState.isControlsVisible,
//            centerButton = { VideoPlayerPulse(pulseState) },
//            subtitles = { /* TODO Implement subtitles */ },
//            showControls = videoPlayerState::showControls,
//            controls = {
//                VideoPlayerControls(
//                    player = exoPlayer,
//                    movieDetails = movieDetails,
//                    focusRequester = focusRequester,
//                    onShowControls = { videoPlayerState.showControls(exoPlayer.isPlaying) },
//                )
//            }
//        )
//    }
//}
//
//private fun Modifier.dPadEvents(
//    exoPlayer: ExoPlayer,
//    videoPlayerState: VideoPlayerState,
//    pulseState: VideoPlayerPulseState
//): Modifier = this.handleDPadKeyEvents(
//    onLeft = {
//        if (!videoPlayerState.isControlsVisible) {
//            exoPlayer.seekBack()
//            pulseState.setType(BACK)
//        }
//    },
//    onRight = {
//        if (!videoPlayerState.isControlsVisible) {
//            exoPlayer.seekForward()
//            pulseState.setType(FORWARD)
//        }
//    },
//    onUp = { videoPlayerState.showControls() },
//    onDown = { videoPlayerState.showControls() },
//    onEnter = {
//        exoPlayer.pause()
//        videoPlayerState.showControls()
//    }
//)
//
//private fun MovieDetails.intoMediaItem(): MediaItem {
//    return MediaItem.Builder()
//        .setUri(videoUri)
//        .setSubtitleConfigurations(
//            if (subtitleUri == null) {
//                emptyList()
//            } else {
//                listOf(
//                    MediaItem.SubtitleConfiguration
//                        .Builder(Uri.parse(subtitleUri))
//                        .setMimeType("application/vtt")
//                        .setLanguage("en")
//                        .setSelectionFlags(C.SELECTION_FLAG_DEFAULT)
//                        .build()
//                )
//            }
//        ).build()
//}
//
//private fun Movie.intoMediaItem(): MediaItem {
//    return MediaItem.Builder()
//        .setUri(videoUri)
//        .setSubtitleConfigurations(
//            if (subtitleUri == null) {
//                emptyList()
//            } else {
//                listOf(
//                    MediaItem.SubtitleConfiguration
//                        .Builder(Uri.parse(subtitleUri))
//                        .setMimeType("application/vtt")
//                        .setLanguage("en")
//                        .setSelectionFlags(C.SELECTION_FLAG_DEFAULT)
//                        .build()
//                )
//            }
//        )
//        .build()
//}
//package com.bacbpl.iptv.jetStram.presentation.screens.videoPlayer
//
//import android.content.Context
//import android.net.Uri
//import android.widget.Toast
//import androidx.activity.compose.BackHandler
//import androidx.annotation.OptIn
//import androidx.compose.foundation.focusable
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Button
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.focus.FocusRequester
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.unit.dp
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.lifecycle.compose.collectAsStateWithLifecycle
//import androidx.media3.common.C
//import androidx.media3.common.MediaItem
//import androidx.media3.common.MimeTypes
//import androidx.media3.common.PlaybackException
//import androidx.media3.common.Player
//import androidx.media3.common.util.UnstableApi
//import androidx.media3.datasource.DefaultHttpDataSource
//import androidx.media3.exoplayer.ExoPlayer
//import androidx.media3.exoplayer.hls.HlsMediaSource
//import androidx.media3.exoplayer.source.ProgressiveMediaSource
//import androidx.media3.ui.compose.PlayerSurface
//import androidx.tv.material3.MaterialTheme
//import com.bacbpl.iptv.R
//import com.bacbpl.iptv.jetStram.data.entities.MovieDetails
//import com.bacbpl.iptv.jetStram.presentation.common.Error
//import com.bacbpl.iptv.jetStram.presentation.common.Loading
//import com.bacbpl.iptv.jetStram.presentation.screens.videoPlayer.components.VideoPlayerControls
//import com.bacbpl.iptv.jetStram.presentation.screens.videoPlayer.components.VideoPlayerOverlay
//import com.bacbpl.iptv.jetStram.presentation.screens.videoPlayer.components.VideoPlayerPulse
//import com.bacbpl.iptv.jetStram.presentation.screens.videoPlayer.components.VideoPlayerPulse.Type.BACK
//import com.bacbpl.iptv.jetStram.presentation.screens.videoPlayer.components.VideoPlayerPulse.Type.FORWARD
//import com.bacbpl.iptv.jetStram.presentation.screens.videoPlayer.components.VideoPlayerPulseState
//import com.bacbpl.iptv.jetStram.presentation.screens.videoPlayer.components.VideoPlayerState
//import com.bacbpl.iptv.jetStram.presentation.screens.videoPlayer.components.rememberVideoPlayerPulseState
//import com.bacbpl.iptv.jetStram.presentation.screens.videoPlayer.components.rememberVideoPlayerState
//import com.bacbpl.iptv.jetStram.presentation.utils.handleDPadKeyEvents
//
//object VideoPlayerScreen {
//    const val MovieIdBundleKey = "movieId"
//}
//
//// Surface type constants
//private const val SURFACE_TYPE_SURFACE_VIEW = 0
//private const val SURFACE_TYPE_TEXTURE_VIEW = 1
//
//@OptIn(UnstableApi::class)
//@Composable
//fun rememberPlayer(context: Context): ExoPlayer {
//    return remember {
//        // Create data source factory
//        val dataSourceFactory = DefaultHttpDataSource.Factory()
//            .setUserAgent("JetStream/1.0")
//            .setConnectTimeoutMs(30000)
//            .setReadTimeoutMs(30000)
//            .setAllowCrossProtocolRedirects(true)
//
//        ExoPlayer.Builder(context)
//            .setSeekForwardIncrementMs(10000)
//            .setSeekBackIncrementMs(10000)
//            .build()
//            .apply {
//                playWhenReady = true
//                repeatMode = Player.REPEAT_MODE_OFF
//            }
//    }
//}
//
//@Composable
//fun VideoPlayerScreen(
//    onBackPressed: () -> Unit,
//    videoPlayerScreenViewModel: VideoPlayerScreenViewModel = hiltViewModel()
//) {
//    val uiState by videoPlayerScreenViewModel.uiState.collectAsStateWithLifecycle()
//
//    when (val s = uiState) {
//        is VideoPlayerScreenUiState.Loading -> {
//            Loading(modifier = Modifier.fillMaxSize())
//        }
//        is VideoPlayerScreenUiState.Error -> {
//            Error(modifier = Modifier.fillMaxSize())
//        }
//        is VideoPlayerScreenUiState.Done -> {
//            VideoPlayerScreenContent(
//                movieDetails = s.movieDetails,
//                onBackPressed = onBackPressed
//            )
//        }
//    }
//}
//
//@OptIn(UnstableApi::class)
//@Composable
//fun VideoPlayerScreenContent(movieDetails: MovieDetails, onBackPressed: () -> Unit) {
//    val context = LocalContext.current
//    val exoPlayer = rememberPlayer(context)
//
//    var isPlaybackError by remember { mutableStateOf(false) }
//    var errorMessage by remember { mutableStateOf<String?>(null) }
//
//    val videoPlayerState = rememberVideoPlayerState(hideSeconds = 4)
//
//    // Error listener
//    LaunchedEffect(exoPlayer) {
//        exoPlayer.addListener(object : Player.Listener {
//            override fun onPlayerError(error: PlaybackException) {
//                super.onPlayerError(error)
//                isPlaybackError = true
//                errorMessage = error.message
//
//                Toast.makeText(
//                    context,
//                    "Playback Error: ${error.message}",
//                    Toast.LENGTH_LONG
//                ).show()
//            }
//        })
//    }
//
//    LaunchedEffect(exoPlayer, movieDetails) {
//        try {
//            val mediaItem = createMediaItem(movieDetails.videoUri)
//            exoPlayer.setMediaItem(mediaItem)
//            exoPlayer.prepare()
//        } catch (e: Exception) {
//            isPlaybackError = true
//            errorMessage = e.message
//        }
//    }
//
//    BackHandler(onBack = onBackPressed)
//
//    val pulseState = rememberVideoPlayerPulseState()
//
//    Box(
//        Modifier
//            .dPadEvents(
//                exoPlayer,
//                videoPlayerState,
//                pulseState
//            )
//            .focusable()
//    ) {
//        PlayerSurface(
//            player = exoPlayer,
//            modifier = Modifier.fillMaxSize(),
//            surfaceType = SURFACE_TYPE_TEXTURE_VIEW  // Use TextureView for better composability
//        )
//
//        if (isPlaybackError) {
//            PlaybackErrorOverlay(
//                errorMessage = errorMessage,
//                onRetry = {
//                    isPlaybackError = false
//                    exoPlayer.prepare()
//                },
//                onBackPressed = onBackPressed
//            )
//        } else {
//            val focusRequester = remember { FocusRequester() }
//            VideoPlayerOverlay(
//                modifier = Modifier.align(Alignment.BottomCenter),
//                focusRequester = focusRequester,
//                isPlaying = exoPlayer.isPlaying,
//                isControlsVisible = videoPlayerState.isControlsVisible,
//                centerButton = { VideoPlayerPulse(pulseState) },
//                subtitles = { /* TODO Implement subtitles */ },
//                showControls = videoPlayerState::showControls,
//                controls = {
//                    VideoPlayerControls(
//                        player = exoPlayer,
//                        movieDetails = movieDetails,
//                        focusRequester = focusRequester,
//                        onShowControls = { videoPlayerState.showControls(exoPlayer.isPlaying) },
//                    )
//                }
//            )
//        }
//    }
//}
//
//@OptIn(UnstableApi::class)
//private fun createMediaItem(url: String): MediaItem {
//    return when {
//        url.contains(".m3u8") -> {
//            // HLS stream
//            MediaItem.Builder()
//                .setUri(url)
//                .setMimeType(MimeTypes.APPLICATION_M3U8)
//                .build()
//        }
//        url.contains(".mpd") -> {
//            // DASH stream
//            MediaItem.Builder()
//                .setUri(url)
//                .setMimeType(MimeTypes.APPLICATION_MPD)
//                .build()
//        }
//        url.contains(".mp4") -> {
//            // MP4 video
//            MediaItem.Builder()
//                .setUri(url)
//                .setMimeType(MimeTypes.VIDEO_MP4)
//                .build()
//        }
//        else -> {
//            // Progressive (default)
//            MediaItem.fromUri(url)
//        }
//    }
//}
//@Composable
//private fun PlaybackErrorOverlay(
//    errorMessage: String?,
//    onRetry: () -> Unit,
//    onBackPressed: () -> Unit
//) {
//    Box(
//        modifier = Modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text(
//                text = "Playback Error",
//                color = MaterialTheme.colorScheme.error
//            )
//            Spacer(modifier = Modifier.height(8.dp))
//            Text(
//                text = errorMessage ?: "Unknown error",
//                color = MaterialTheme.colorScheme.onSurface
//            )
//            Spacer(modifier = Modifier.height(16.dp))
//            Button(onClick = onRetry) {
//                Text(text = "Retry")
//            }
//            Spacer(modifier = Modifier.height(8.dp))
//            Button(onClick = onBackPressed) {
//                Text(text = "Go Back")
//            }
//        }
//    }
//}
//
//private fun Modifier.dPadEvents(
//    exoPlayer: ExoPlayer,
//    videoPlayerState: VideoPlayerState,
//    pulseState: VideoPlayerPulseState
//): Modifier = this.handleDPadKeyEvents(
//    onLeft = {
//        if (!videoPlayerState.isControlsVisible) {
//            exoPlayer.seekBack()
//            pulseState.setType(BACK)
//        }
//    },
//    onRight = {
//        if (!videoPlayerState.isControlsVisible) {
//            exoPlayer.seekForward()
//            pulseState.setType(FORWARD)
//        }
//    },
//    onUp = { videoPlayerState.showControls() },
//    onDown = { videoPlayerState.showControls() },
//    onEnter = {
//        if (exoPlayer.isPlaying) {
//            exoPlayer.pause()
//        } else {
//            exoPlayer.play()
//        }
//        videoPlayerState.showControls()
//    }
//)
package com.bacbpl.iptv.jetStram.presentation.screens.videoPlayer

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.annotation.OptIn
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.compose.PlayerSurface
import androidx.tv.material3.MaterialTheme
import com.bacbpl.iptv.jetStram.data.entities.MovieDetails
import com.bacbpl.iptv.jetStram.presentation.common.Error
import com.bacbpl.iptv.jetStram.presentation.common.Loading
import com.bacbpl.iptv.jetStram.presentation.screens.videoPlayer.components.VideoPlayerControls
import com.bacbpl.iptv.jetStram.presentation.screens.videoPlayer.components.VideoPlayerOverlay
import com.bacbpl.iptv.jetStram.presentation.screens.videoPlayer.components.VideoPlayerPulse
import com.bacbpl.iptv.jetStram.presentation.screens.videoPlayer.components.VideoPlayerPulse.Type.BACK
import com.bacbpl.iptv.jetStram.presentation.screens.videoPlayer.components.VideoPlayerPulse.Type.FORWARD
import com.bacbpl.iptv.jetStram.presentation.screens.videoPlayer.components.VideoPlayerPulseState
import com.bacbpl.iptv.jetStram.presentation.screens.videoPlayer.components.VideoPlayerState
import com.bacbpl.iptv.jetStram.presentation.screens.videoPlayer.components.rememberVideoPlayerPulseState
import com.bacbpl.iptv.jetStram.presentation.screens.videoPlayer.components.rememberVideoPlayerState
import com.bacbpl.iptv.jetStram.presentation.utils.handleDPadKeyEvents

object VideoPlayerScreen {
    const val MovieIdBundleKey = "movieId"
}

private const val SURFACE_TYPE_TEXTURE_VIEW = 1

@OptIn(UnstableApi::class)
@Composable
fun rememberPlayer(context: android.content.Context): ExoPlayer {
    return remember {
        val dataSourceFactory = DefaultHttpDataSource.Factory()
            .setUserAgent("JetStream/1.0")
            .setConnectTimeoutMs(30000)
            .setReadTimeoutMs(30000)
            .setAllowCrossProtocolRedirects(true)

        ExoPlayer.Builder(context)
            .setSeekForwardIncrementMs(10000)
            .setSeekBackIncrementMs(10000)
            .build()
            .apply {
                playWhenReady = true
                repeatMode = Player.REPEAT_MODE_OFF
            }
    }
}

@Composable
fun VideoPlayerScreen(
    onBackPressed: () -> Unit,
    videoPlayerScreenViewModel: VideoPlayerScreenViewModel = hiltViewModel()
) {
    val uiState by videoPlayerScreenViewModel.uiState.collectAsStateWithLifecycle()

    when (val s = uiState) {
        is VideoPlayerScreenUiState.Loading -> {
            Loading(modifier = Modifier.fillMaxSize())
        }
        is VideoPlayerScreenUiState.Error -> {
            Error(modifier = Modifier.fillMaxSize())
        }
        is VideoPlayerScreenUiState.Done -> {
            VideoPlayerScreenContent(
                movieDetails = s.movieDetails,
                onBackPressed = onBackPressed
            )
        }
    }
}

@OptIn(UnstableApi::class)
@Composable
fun VideoPlayerScreenContent(movieDetails: MovieDetails, onBackPressed: () -> Unit) {
    val context = LocalContext.current
    val exoPlayer = rememberPlayer(context)

    var isPlaybackError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isPlayerReady by remember { mutableStateOf(false) }

    val videoPlayerState = rememberVideoPlayerState(hideSeconds = 4)

    // Error listener
    LaunchedEffect(exoPlayer) {
        exoPlayer.addListener(object : Player.Listener {
            override fun onPlayerError(error: PlaybackException) {
                super.onPlayerError(error)
                isPlaybackError = true
                errorMessage = error.message
                isPlayerReady = false

                Toast.makeText(
                    context,
                    "Playback Error: ${error.message}",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                when (playbackState) {
                    Player.STATE_READY -> {
                        isPlayerReady = true
                        isPlaybackError = false
                    }
                    Player.STATE_BUFFERING -> {
                        // Show buffering if needed
                    }
                    Player.STATE_ENDED -> {
                        // Handle playback ended
                    }
                }
            }
        })
    }

    LaunchedEffect(exoPlayer, movieDetails) {
        try {
            // Clear any existing media
            exoPlayer.clearMediaItems()

            // Create and set the media item
            val mediaItem = createMediaItem(movieDetails.videoUri)
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
        } catch (e: Exception) {
            isPlaybackError = true
            errorMessage = e.message
            isPlayerReady = false
            e.printStackTrace()
        }
    }

    BackHandler(onBack = {
        exoPlayer.release()
        onBackPressed()
    })

    val pulseState = rememberVideoPlayerPulseState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .dPadEvents(
                exoPlayer,
                videoPlayerState,
                pulseState
            )
            .focusable()
    ) {
        // Show loading while player is not ready
        if (!isPlayerReady && !isPlaybackError) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Loading video...", color = MaterialTheme.colorScheme.onSurface)
            }
        } else {
            PlayerSurface(
                player = exoPlayer,
                modifier = Modifier.fillMaxSize(),
                surfaceType = SURFACE_TYPE_TEXTURE_VIEW
                // Removed onSizeChanged parameter
            )
        }

        if (isPlaybackError) {
            PlaybackErrorOverlay(
                errorMessage = errorMessage,
                onRetry = {
                    isPlaybackError = false
                    exoPlayer.prepare()
                },
                onBackPressed = onBackPressed
            )
        } else {
            val focusRequester = remember { FocusRequester() }
            VideoPlayerOverlay(
                modifier = Modifier.align(Alignment.BottomCenter),
                focusRequester = focusRequester,
                isPlaying = exoPlayer.isPlaying,
                isControlsVisible = videoPlayerState.isControlsVisible,
                centerButton = { VideoPlayerPulse(pulseState) },
                subtitles = { /* TODO Implement subtitles */ },
                showControls = videoPlayerState::showControls,
                controls = {
                    VideoPlayerControls(
                        player = exoPlayer,
                        movieDetails = movieDetails,
                        focusRequester = focusRequester,
                        onShowControls = { videoPlayerState.showControls(exoPlayer.isPlaying) },
                    )
                }
            )
        }
    }
}

@OptIn(UnstableApi::class)
private fun createMediaItem(url: String): MediaItem {
    return MediaItem.Builder()
        .setUri(Uri.parse(url))
        .build()
}

@Composable
private fun PlaybackErrorOverlay(
    errorMessage: String?,
    onRetry: () -> Unit,
    onBackPressed: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Playback Error",
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = errorMessage ?: "Unknown error",
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRetry) {
                Text(text = "Retry")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onBackPressed) {
                Text(text = "Go Back")
            }
        }
    }
}

private fun Modifier.dPadEvents(
    exoPlayer: ExoPlayer,
    videoPlayerState: VideoPlayerState,
    pulseState: VideoPlayerPulseState
): Modifier = this.handleDPadKeyEvents(
    onLeft = {
        if (!videoPlayerState.isControlsVisible) {
            exoPlayer.seekBack()
            pulseState.setType(BACK)
        }
    },
    onRight = {
        if (!videoPlayerState.isControlsVisible) {
            exoPlayer.seekForward()
            pulseState.setType(FORWARD)
        }
    },
    onUp = { videoPlayerState.showControls() },
    onDown = { videoPlayerState.showControls() },
    onEnter = {
        if (exoPlayer.isPlaying) {
            exoPlayer.pause()
        } else {
            exoPlayer.play()
        }
        videoPlayerState.showControls()
    }
)