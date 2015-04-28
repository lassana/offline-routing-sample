package com.github.lassana.offlineroutingsample.ui;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.github.lassana.offlineroutingsample.R;


public class MainActivity extends ActionBarActivity implements MainActivityCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onMapDownloaded() {

    }
}
