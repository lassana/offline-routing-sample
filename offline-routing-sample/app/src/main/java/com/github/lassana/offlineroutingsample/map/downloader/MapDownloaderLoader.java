package com.github.lassana.offlineroutingsample.map.downloader;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.util.Pair;
import com.github.lassana.offlineroutingsample.App;
import com.github.lassana.offlineroutingsample.util.event.MapDownloaderProgressEvent;
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

    private final AbstractMap mSelectedMap;

    private OkHttpClient mOkHttpClient;
    private long mDownloaded = 0;

    public MapDownloaderLoader(@NonNull Context context) {
        super(context);
        mSelectedMap = AbstractMap.getSelectedMap();
    }

    protected void publishProgress(int value) {
        //LOGD(TAG, "publishProgress: " + value);
        App.getApplication(getContext()).sendOttoEvent(new MapDownloaderProgressEvent(value));
    }

    @Override
    public Boolean loadInBackground() {
        LOGD(TAG, "loadInBackground");

        final Context context = getContext();

        final List<Pair<String, File>> list = new ArrayList<>(7);
        list.add(new Pair<>(mSelectedMap.getMapFileUrl(), mSelectedMap.getMapsforgeFile(context)));
        list.add(new Pair<>(mSelectedMap.getEdgesUrl(), mSelectedMap.getRoutingEdgesFile(context)));
        list.add(new Pair<>(mSelectedMap.getGeometryUrl(), mSelectedMap.getRoutingGeometryFile(context)));
        list.add(new Pair<>(mSelectedMap.getLocationIndexUrl(), mSelectedMap.getRoutingLocationIndexFile(context)));
        list.add(new Pair<>(mSelectedMap.getNamesUrl(), mSelectedMap.getRoutingNamesFile(context)));
        list.add(new Pair<>(mSelectedMap.getNodesUrl(), mSelectedMap.getRoutingNodesFile(context)));
        list.add(new Pair<>(mSelectedMap.getPropertiesUrl(), mSelectedMap.getRoutingPropertiesFile(context)));
        final File targetDir = mSelectedMap.getOfflineMapsDir(context);

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

    private boolean downloadFile(@NonNull String url, @NonNull File targetFile) {
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
                publishProgress((int) (mDownloaded * 100 / mSelectedMap.getMapSize()));
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
