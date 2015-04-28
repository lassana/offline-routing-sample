package com.github.lassana.offlineroutingsample.ui;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.widget.ViewSwitcher;
import com.github.lassana.offlineroutingsample.R;


/**
 * A placeholder fragment containing a simple view.
 */
public class MapDownloaderFragment extends Fragment {

    private MainActivityCallback mActivityCallback;
    private ViewSwitcher mViewSwitcher;
    private TextView mDownloadingProgressTextView;
    private ProgressBar mDownloadingProgressBar;

    public MapDownloaderFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof MainActivityCallback) {
            mActivityCallback = (MainActivityCallback) activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivityCallback = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map_downloader, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewSwitcher = (ViewSwitcher) view.findViewById(R.id.view_flipper);
        mDownloadingProgressTextView = (TextView) view.findViewById(R.id.text_view_downloading_progress);
        mDownloadingProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar_downloading);
        view.findViewById(R.id.button_start_downloading).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDownloading();
            }
        });
        view.findViewById(R.id.button_cancel_downloading).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StopDownloading();
            }
        });
    }

    private void StopDownloading() {
        mViewSwitcher.setDisplayedChild(0);
    }

    private void startDownloading() {
        mViewSwitcher.setDisplayedChild(1);
    }
}
