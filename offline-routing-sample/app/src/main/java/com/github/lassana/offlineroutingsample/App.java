package com.github.lassana.offlineroutingsample;

import android.app.Application;
import android.content.Context;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;

import java.lang.ref.WeakReference;

/**
 * @author Nikolai Doronin {@literal <lassana.nd@gmail.com>}
 * @since 4/28/2015.
 */
public class App extends Application {

    private Bus mOttoBus;
    private WeakReference<OkHttpClient> mOkHttpClientWeakReference;

    public static App getApplication(Context context) {
        if (context instanceof App) {
            return (App) context;
        }
        return (App) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidGraphicFactory.createInstance(this);
        mOttoBus = new Bus(ThreadEnforcer.ANY);
    }

    public OkHttpClient getOkHttpClient() {
        if (mOkHttpClientWeakReference == null || mOkHttpClientWeakReference.get() == null) {
            mOkHttpClientWeakReference = new WeakReference<>(new OkHttpClient());
        }
        return mOkHttpClientWeakReference.get();
    }

    public void registerOttoBus(Object object) {
        mOttoBus.register(object);
    }

    public void unregisterOttoBus(Object object) {
        mOttoBus.unregister(object);
    }

    public void sendOttoEvent(Object event) {
        mOttoBus.post(event);
    }

}
