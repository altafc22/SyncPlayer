package com.sdpd.syncplayer;

import android.content.res.Configuration;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.io.File;

public class PlayerActivity extends AppCompatActivity {

    String TAG = "PlayerActivity";

    SimpleExoPlayer player;
    PlayerView pv_exoplayer;

    RecyclerView rvClientList;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        String path = getIntent().getStringExtra(getString(R.string.mediaSelectPathExtra));
        File file = (File)getIntent().getSerializableExtra(getString(R.string.mediaSelectFileExtra));
        Log.e(TAG, "onCreate");

        // Remove action bar and status bar for proper fullscreen
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        player = ExoPlayerFactory.newSimpleInstance(this);
        pv_exoplayer = findViewById(R.id.pv_exoplayer);
        pv_exoplayer.setPlayer(player);

        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, getString(R.string.app_name)));

        // This is the MediaSource representing the media to be played.
        MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.fromFile(file));

        // Prepare the player with the source.
        player.prepare(mediaSource);

//        if (savedInstanceState != null) {
//            player.setPlayWhenReady(savedInstanceState.getBoolean("PLAY_WHEN_READY"));
//            player.seekTo(savedInstanceState.getLong("SEEK_TIME", 0));
//        }

        rvClientList = findViewById(R.id.rv_clientList);
        layoutManager = new LinearLayoutManager(this);
        adapter = new ClientListAdapter();

        rvClientList.setHasFixedSize(true);
        rvClientList.setLayoutManager(layoutManager);
        rvClientList.setAdapter(adapter);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        Log.e(TAG, "Config Changed");

        int currentOrientation = getResources().getConfiguration().orientation;
        ViewGroup.LayoutParams params = pv_exoplayer.getLayoutParams();
        if(currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            rvClientList.setVisibility(View.GONE);
            params.height = params.MATCH_PARENT;
            pv_exoplayer.setLayoutParams(params);

            // remove status bar
            // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else if(currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            rvClientList.setVisibility(View.VISIBLE);
            params.height = 270 * getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT;
            pv_exoplayer.setLayoutParams(params);

            // show status bar
            // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

//        outState.putLong("SEEK_TIME", player.getCurrentPosition());
//        outState.putBoolean("PLAY_WHEN_READY", player.getPlayWhenReady());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.release();
    }
}
