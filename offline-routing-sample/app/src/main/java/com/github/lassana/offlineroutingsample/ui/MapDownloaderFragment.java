package com.github.lassana.offlineroutingsample.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import com.github.lassana.offlineroutingsample.App;
import com.github.lassana.offlineroutingsample.R;
import com.github.lassana.offlineroutingsample.map.downloader.MapDownloaderLoader;
import com.squareup.otto.Subscribe;


/**
 * A placeholder fragment containing a simple view.
 */
public class MapDownloaderFragment extends Fragment {

    private static final int LOADER_ID = 0x1;

    private MainActivityCallback mActivityCallback;
    private ViewSwitcher mViewSwitcher;
    private TextView mDownloadingProgressTextView;
    private ProgressBar mDownloadingProgressBar;

    private final LoaderManager.LoaderCallbacks<Boolean> mLoadManager = new LoaderManager.LoaderCallbacks<Boolean>() {

        @Override
        public Loader<Boolean> onCreateLoader(int id, Bundle args) {
            return new MapDownloaderLoader(getActivity());
        }

        @Override
        public void onLoadFinished(Loader<Boolean> loader, Boolean data) {
            getLoaderManager().destroyLoader(LOADER_ID);
        }

        @Override
        public void onLoaderReset(Loader<Boolean> loader) {
        }
    };

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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map_downloader, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getLoaderManager().destroyLoader(LOADER_ID);
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
        mViewSwitcher.setDisplayedChild(getLoaderManager().getLoader(LOADER_ID) == null ? 0 : 1);
    }

    @Override
    public void onPause() {
        super.onPause();
        App.getApplication(getActivity()).unregisterOttoBus(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        App.getApplication(getActivity()).registerOttoBus(this);
    }

    private void StopDownloading() {
        getLoaderManager().destroyLoader(LOADER_ID);
        mViewSwitcher.setDisplayedChild(0);
    }

    private void startDownloading() {
        getLoaderManager().initLoader(LOADER_ID, null, mLoadManager);
        mViewSwitcher.setDisplayedChild(1);
    }

    @Subscribe
    public void answerAvailable(final MapDownloaderLoader.Progress event) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDownloadingProgressTextView.setText(getString(R.string.text_view_downloading_progress, event.progress));
                mDownloadingProgressBar.setProgress(event.progress);
            }
        });

    }
}
