package com.github.lassana.offlineroutingsample.map.downloader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.util.Pair;
import com.github.lassana.offlineroutingsample.App;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static com.github.lassana.offlineroutingsample.util.LogUtils.LOGD;
import static com.github.lassana.offlineroutingsample.util.LogUtils.LOGE;
import static com.github.lassana.offlineroutingsample.util.LogUtils.makeLogTag;

/**
 * @author Nikolai Doronin {@literal <lassana.nd@gmail.com>}
 * @since 4/28/2015.
 */
public class MapDownloaderLoader extends AsyncTaskLoader<Boolean> {

    private static final String TAG = makeLogTag(MapDownloaderLoader.class);
    public static final int BUFFER_SIZE = 4096;
    public static final String OKHTTP_TAG = "BELARUS_MAP_OKHTTP";

    private OkHttpClient mOkHttpClient;
    private long mDownloaded = 0;

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
        //LOGD(TAG, "publishProgress: " + value);
        App.getApplication(getContext()).sendOttoEvent(new Progress(value));
    }

    @Override
    public Boolean loadInBackground() {
        LOGD(TAG, "loadInBackground");

        final Context context = getContext();

        final List<Pair<String, File>> list = new ArrayList<>(7);
        list.add(new Pair<>(BelarusMap.MAP_FILE_URL, BelarusMap.getMapsforgeFile(context)));
        list.add(new Pair<>(BelarusMap.EDGES_URL, BelarusMap.getRoutingEdgesFile(context)));
        list.add(new Pair<>(BelarusMap.GEOMETRY_URL, BelarusMap.getRoutingGeometryFile(context)));
        list.add(new Pair<>(BelarusMap.LOCATION_INDEX_URL, BelarusMap.getRoutingLocationIndexFile(context)));
        list.add(new Pair<>(BelarusMap.NAMES_URL, BelarusMap.getRoutingNamesFile(context)));
        list.add(new Pair<>(BelarusMap.NODES_URL, BelarusMap.getRoutingNodesFile(context)));
        list.add(new Pair<>(BelarusMap.PROPERTIES_URL, BelarusMap.getRoutingPropertiesFile(context)));
        final File targetDir = BelarusMap.getOfflineMapsDir(context);

        mOkHttpClient = App.getApplication(context).getOkHttpClient();
        mDownloaded = 0;
        for (Pair<String, File> pair : list) {
            if ( !downloadFile(pair.first, pair.second)) {
                targetDir.delete();
                return false;
            }
        }

        mOkHttpClient = null;

        publishProgress(100);
        return true;
    }

    private boolean downloadFile(final String url, final File targetFile) {
        LOGD(TAG, "Downloading \"" + url + "\" into \"" + targetFile + "\"...");
        Request request = new Request.Builder().url(url).tag(OKHTTP_TAG).build();
        Response response;
        try {
            response = mOkHttpClient.newCall(request).execute();
            if (!response.isSuccessful()) {
                return false;
            }
            final ResponseBody body = response.body();
            final InputStream inputStream = body.byteStream();
            final OutputStream outputStream = new FileOutputStream(targetFile);
            byte[] buffer = new byte[BUFFER_SIZE];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
                mDownloaded += len;
                publishProgress((int) (mDownloaded * 100 / BelarusMap.MAP_SIZE));
            }
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            LOGE(TAG, "Cannot download map.", e);
            return false;
        }
        LOGD(TAG, "Downloaded.");
        return true;
    }



    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        LOGD(TAG, "onStartLoading");
        if ( mOkHttpClient != null ) mOkHttpClient.cancel(OKHTTP_TAG);
        forceLoad();
    }

    @Override
    protected void onReset() {
        super.onReset();
        mDownloaded = 0;
        if ( mOkHttpClient != null ) mOkHttpClient.cancel(OKHTTP_TAG);
    }
}
