package com.github.lassana.offlineroutingsample.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import com.github.lassana.offlineroutingsample.R;
import com.github.lassana.offlineroutingsample.downloader.BelarusMap;


public class MainActivity extends ActionBarActivity implements MainActivityCallback {

    private Fragment mCurrentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateContent(false);
    }

    @Override
    public void onMapDownloaded() {
        updateContent(true);
    }

    private void updateContent(boolean force) {
        if ( mCurrentFragment == null || force) {
            mCurrentFragment = BelarusMap.exist(this) ? new MapFragment() : new MapDownloaderFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, mCurrentFragment).commit();
        }
    }
}
