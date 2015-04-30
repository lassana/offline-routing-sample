package com.github.lassana.offlineroutingsample.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import com.github.lassana.offlineroutingsample.App;
import com.github.lassana.offlineroutingsample.R;
import com.github.lassana.offlineroutingsample.map.downloader.BelarusMap;
import com.github.lassana.offlineroutingsample.util.event.MapSuccessfulDownloadedEvent;
import com.squareup.otto.Subscribe;

public class MainActivity extends ActionBarActivity {

    private Fragment mCurrentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        App.getApplication(this).registerOttoBus(this);
        updateContent(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        App.getApplication(this).unregisterOttoBus(this);
    }

    @Subscribe
    public void onMapDownloaded(MapSuccessfulDownloadedEvent event) {
        updateContent(true);
    }

    private void updateContent(boolean force) {
        if ( mCurrentFragment == null || force) {
            mCurrentFragment = BelarusMap.exist(this) ? new MapFragment() : new MapDownloaderFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, mCurrentFragment).commit();
        }
    }

}
