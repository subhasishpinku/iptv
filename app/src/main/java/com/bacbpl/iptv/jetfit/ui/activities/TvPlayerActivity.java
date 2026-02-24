package com.bacbpl.iptv.jetfit.ui.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;


import com.bacbpl.iptv.R;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ext.rtmp.RtmpDataSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;

public class TvPlayerActivity extends Activity {

    public static final String EXTRA_URL = "channel_url";

    private SimpleExoPlayer player;
    private PlayerView playerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tv);

        playerView = findViewById(R.id.player_view);
        playerView.setFocusable(true);
        playerView.requestFocus();
        playerView.setUseController(false);

        player = new SimpleExoPlayer.Builder(this).build();
        playerView.setPlayer(player);

        String url = getIntent().getStringExtra(EXTRA_URL);
        if (url != null) {
            playChannel(url);
        }
    }

    private void playChannel(String url) {
        MediaSource mediaSource;

        if (url.startsWith("rtmp://")) {
            mediaSource = new ProgressiveMediaSource.Factory(
                    new RtmpDataSource.Factory()
            ).createMediaSource(MediaItem.fromUri(url));

        } else if (url.contains(".m3u8")) {
            mediaSource = new HlsMediaSource.Factory(
                    new DefaultHttpDataSource.Factory()
            ).createMediaSource(MediaItem.fromUri(url));

        } else {
            mediaSource = new ProgressiveMediaSource.Factory(
                    new DefaultHttpDataSource.Factory()
            ).createMediaSource(MediaItem.fromUri(url));
        }

        player.setMediaSource(mediaSource);
        player.prepare();
        player.play();
    }

    @SuppressLint("GestureBackNavigation")
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (player != null) {
            player.release();
            player = null;
        }
    }
}
