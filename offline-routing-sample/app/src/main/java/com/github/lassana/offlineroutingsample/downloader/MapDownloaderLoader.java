package com.github.lassana.offlineroutingsample.downloader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import com.github.lassana.offlineroutingsample.App;

import static com.github.lassana.offlineroutingsample.util.LogUtils.LOGD;
import static com.github.lassana.offlineroutingsample.util.LogUtils.makeLogTag;

/**
 * @author Nikolai Doronin {@literal <lassana.nd@gmail.com>}
 * @since 4/28/2015.
 */
public class MapDownloaderLoader extends AsyncTaskLoader<Void> {

    private static final String TAG = makeLogTag(MapDownloaderLoader.class);

    public static class Progress {
        public final int progress;

        public Progress(int progress) {
            this.progress = progress;
        }
    }

    public MapDownloaderLoader(Context context) {
        super(context);
    }

    protected void publishProgress(int value) {
        LOGD(TAG, "publishProgress: " + value);
        App.getApplication(getContext()).sendOttoEvent(new Progress(value));
    }

    @Override
    public Void loadInBackground() {
        LOGD(TAG, "loadInBackground");
        for (int i = 0; i < 100; ++i) {
            if (isReset() || isAbandoned()) return null;
            publishProgress(i);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
        publishProgress(100);
        return null;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        //App.getApplication(getContext()).registerOttoBus(this);
        LOGD(TAG, "onStartLoading");
        // todo check if loading stated
        forceLoad();
    }

    @Override
    protected void onReset() {
        super.onReset();
        //App.getApplication(getContext()).unregisterOttoBus(this);
    }
}
