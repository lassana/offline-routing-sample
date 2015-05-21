package com.github.lassana.offlineroutingsample.map.routing;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;
import com.github.lassana.offlineroutingsample.map.downloader.AbstractMap;
import com.github.lassana.offlineroutingsample.util.LogUtils;
import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.routing.util.EncodingManager;
import com.graphhopper.util.PointList;
import org.osmdroid.util.GeoPoint;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import static com.github.lassana.offlineroutingsample.util.LogUtils.LOGE;
import static com.github.lassana.offlineroutingsample.util.LogUtils.LOGI;
import static com.github.lassana.offlineroutingsample.util.LogUtils.LOGW;

/**
 * @author Nikolai Doronin {@literal <lassana.nd@gmail.com>}
 * @since 5/21/2015.
 */
public class RouteLoader extends AsyncTaskLoader<RouteLoader.Result> {

    public static class Result {
        public static final Result INTERNAL_ERROR = new Result(false, true);
        public static final Result NO_ROUTE = new Result(true, false);

        private final List<GeoPoint> mGeoPoints;
        private final boolean mIsError;
        private final boolean mIsNoRoute;

        public Result(@NonNull List<GeoPoint> geoPoints) {
            mGeoPoints = geoPoints;
            mIsError = mIsNoRoute = false;
        }

        private Result(boolean isNoRoute, boolean isError) {
            mGeoPoints = null;
            mIsNoRoute = isNoRoute;
            mIsError = isError;
        }

        public List<GeoPoint> getGeoPoints() {
            return mGeoPoints;
        }

        public boolean isError() {
            return mIsError;
        }

        public boolean isNoRoute() {
            return mIsNoRoute;
        }
    }

    private static final String TAG = LogUtils.makeLogTag(RouteLoader.class);

    private final Location mStartLocation;
    private final Location mEndLocation;

    public RouteLoader(@NonNull Context context, @NonNull Location startLocation, @NonNull Location endLocation) {
        super(context);
        mStartLocation = startLocation;
        mEndLocation = endLocation;
    }

    @Override
    public Result loadInBackground() {
        LOGI(TAG, "#loadInBackground; mStartLocation = " + mStartLocation + "; mEndLocation = " + mEndLocation);
        try {
            final GraphHopper hopper = new GraphHopper().forMobile();
            hopper.setInMemory(true);
            final File mapsforgeFile = AbstractMap.instance().getMapsforgeFile(getContext());
            hopper.setOSMFile(mapsforgeFile.getAbsolutePath());
            hopper.setGraphHopperLocation(mapsforgeFile.getParent());
            hopper.setEncodingManager(new EncodingManager("car"));
            hopper.importOrLoad();
            final GHRequest req =
                    new GHRequest(
                            mStartLocation.getLatitude(),
                            mStartLocation.getLongitude(),
                            mEndLocation.getLatitude(),
                            mEndLocation.getLongitude())
                            .setVehicle("car");
            final GHResponse rsp = hopper.route(req);
            if (rsp.hasErrors()) {
                LOGW(TAG, "GHResponse contains errors!");
                List<Throwable> errors = rsp.getErrors();
                for (int i = 0; i < errors.size(); i++) {
                    LOGE(TAG, "Graphhopper error #" + i, errors.get(i));
                }
                return Result.INTERNAL_ERROR;
            }
            if (!rsp.isFound()) {
                LOGW(TAG, "Graphhopper cannot find route!");
                return Result.NO_ROUTE;
            } else {
                final List<GeoPoint> geoPoints = new LinkedList<>();
                final PointList points = rsp.getPoints();
                double lati, longi, alti;
                for (int i = 0; i < points.getSize(); i++) {
                    lati = points.getLatitude(i);
                    longi = points.getLongitude(i);
                    alti = points.getElevation(i);
                    geoPoints.add(new GeoPoint(lati, longi, alti));
                }
                return new Result(geoPoints);
            }
        } catch (OutOfMemoryError e) {
            LOGE(TAG, "Graphhoper OOM", e);
            return Result.INTERNAL_ERROR;
        }

    }

}
