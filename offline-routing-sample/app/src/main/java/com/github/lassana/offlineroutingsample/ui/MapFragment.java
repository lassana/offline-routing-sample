package com.github.lassana.offlineroutingsample.ui;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.github.lassana.offlineroutingsample.R;
import com.github.lassana.offlineroutingsample.map.MapsConfig;
import com.github.lassana.offlineroutingsample.map.downloader.AbstractMap;
import com.github.lassana.offlineroutingsample.map.marker.CustomMarker;
import com.github.lassana.offlineroutingsample.map.marker.CustomMarkerModel;
import com.github.lassana.offlineroutingsample.map.marker.MyLocationOverlayItem;
import com.github.lassana.offlineroutingsample.map.routing.RouteLoader;
import com.github.lassana.offlineroutingsample.map.view.MapsforgeMapView;
import com.github.lassana.offlineroutingsample.util.LogUtils;
import org.mapsforge.map.android.util.AndroidUtil;
import org.mapsforge.map.layer.cache.TileCache;
import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.github.lassana.offlineroutingsample.util.LogUtils.LOGD;

/**
 * @author Nikolai Doronin {@literal <lassana.nd@gmail.com>}
 * @since 4/28/2015.
 */
public class MapFragment extends Fragment {

    private static final String TAG = LogUtils.makeLogTag(MapFragment.class);

    private TileCache tileCache;
    private MapsforgeMapView mMapView;
    private DefaultResourceProxyImpl mDefaultResourceProxy;
    private ItemizedIconOverlay<MyLocationOverlayItem> mMyLocationOverlayItem;

    private Location mLastUserPosition;
    private CustomMarkerModel mTarget;

    private LocationManager mLocationManager;
    private MyLocationListener mLocationListener;

    private View mOverviewLayout;
    private Button mFindRouteButton;
    private ImageView mMarkerImageView;
    private TextView mMarkerTextView;
    private TextView mMarkerDescriptionTextView;

    private Marker.OnMarkerClickListener mOnMarkerClickListener = new Marker.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker, MapView mapView) {
            if (marker instanceof CustomMarker) {
                final CustomMarker cMarker = (CustomMarker) marker;
                mTarget = cMarker.model;
                updateSelectedMarker(true);
                return true;
            } else {
                return false;
            }
        }
    };

    private ItemizedIconOverlay.OnItemGestureListener<MyLocationOverlayItem> onUserItemGestureListener =
            new ItemizedIconOverlay.OnItemGestureListener<MyLocationOverlayItem>() {
                @Override
                public boolean onItemSingleTapUp(int i, MyLocationOverlayItem overlayItem) {
                    Toast.makeText(getActivity(), overlayItem.getTitle(), Toast.LENGTH_SHORT).show();
                    return true;
                }

                @Override
                public boolean onItemLongPress(int i, MyLocationOverlayItem overlayItem) {
                    return false;
                }
            };


    private class MyLocationListener implements LocationListener {

        public void onLocationChanged(Location location) {
            LOGD(TAG, "onLocationChanged: " + location);
            mLastUserPosition = location;
            updateUserPosition(true);
            destroyLocationManager();
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            LOGD(TAG, "onStatusChanged: " + provider + "; status: " + status);
        }

        public void onProviderEnabled(String provider) {
            LOGD(TAG, "onProviderEnabled: " + provider);
        }

        public void onProviderDisabled(String provider) {
            LOGD(TAG, "onProviderDisabled: " + provider);
        }

    }

    private final LoaderManager.LoaderCallbacks<RouteLoader.Result> mRouteLoadManager = new LoaderManager.LoaderCallbacks<RouteLoader.Result>() {
        @Override
        public Loader<RouteLoader.Result> onCreateLoader(int id, Bundle args) {
            return new RouteLoader(getActivity(), mLastUserPosition, mTarget.getLocation());
        }

        @Override
        public void onLoadFinished(Loader<RouteLoader.Result> loader, RouteLoader.Result data) {
            getLoaderManager().destroyLoader(R.id.loader_find_route);
        }

        @Override
        public void onLoaderReset(Loader<RouteLoader.Result> loader) {

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rvalue = inflater.inflate(R.layout.fragment_map, container, false);

        tileCache = AndroidUtil.createTileCache(getActivity(), "mapcache", MapsConfig.TILE_SIZE, MapsConfig.SCREEN_RATION, MapsConfig.OVERDRAW);
        final File mapFile = AbstractMap.instance().getMapsforgeFile(getActivity());
        mMapView = new MapsforgeMapView(getActivity(), tileCache, mapFile.getAbsolutePath());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        RelativeLayout forMap = (RelativeLayout) rvalue.findViewById(R.id.layout_map);
        forMap.addView(mMapView, 0, params);

        return rvalue;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView.setMultiTouchControls(true);
        mMapView.setClickable(true);
        mMapView.setBuiltInZoomControls(false);
        mMapView.setUseDataConnection(false);

        view.findViewById(R.id.img_btn_zoom_in).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mMapView.getController().zoomIn();
            }
        });
        view.findViewById(R.id.img_btn_zoom_out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mMapView.getController().zoomOut();
            }
        });
        view.findViewById(R.id.btn_get_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LOGD(TAG, "My location button clicked; mLocationManager is" + (mLocationManager == null ? " " : " NOT ") + "null");
                if (mLocationManager == null) initLocationManager();
            }
        });

        mOverviewLayout = view.findViewById(R.id.layout_route);
        mFindRouteButton = (Button) view.findViewById(R.id.button_find_route);
        mMarkerImageView = (ImageView) view.findViewById(R.id.image_view_marker_overview);
        mMarkerTextView = (TextView) view.findViewById(R.id.text_view_marker_title);
        mMarkerDescriptionTextView = (TextView) view.findViewById(R.id.text_view_marker_description);

        mFindRouteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( getLoaderManager().getLoader(R.id.loader_find_route) == null
                        && mTarget != null
                        && mTarget.getLocation() != null
                        && mLastUserPosition != null ) {
                    getLoaderManager().initLoader(R.id.loader_find_route, null, mRouteLoadManager);
                }
            }
        });

        final GeoPoint initialCenter;
        if (savedInstanceState != null) {
            mMapView.getController().setZoom(savedInstanceState.getInt("zoom_lvl"));
            initialCenter = new GeoPoint(savedInstanceState.getDouble("lati"), savedInstanceState.getDouble("longi"));
            mLastUserPosition = savedInstanceState.getParcelable("location");
            mTarget = savedInstanceState.getParcelable("target");
            updateUserPosition(false);
        } else {
            mMapView.getController().setZoom(AbstractMap.instance().getDefaultZoom());
            initialCenter = AbstractMap.instance().getCenterGeoPoint();
        }
        updateSelectedMarker(false);
        updateFindRouteButtonState();

        mMapView.getController().setCenter(initialCenter);
        mMapView.setCenter(initialCenter);

        mMapView.getOverlays().add(AbstractMap.instance().createMarkersCluster(mMapView, getDefaultResourceProxyImpl(), mOnMarkerClickListener));
        mMapView.invalidate();
    }

    private void updateFindRouteButtonState() {
        if (getLoaderManager().getLoader(R.id.loader_find_route) != null) {
            mFindRouteButton.setEnabled(false);
        } else {
            if ( mLastUserPosition != null && mTarget != null ) {
                mFindRouteButton.setEnabled(true);
            } else {
                mFindRouteButton.setEnabled(false);
            }
        }
    }

    private DefaultResourceProxyImpl getDefaultResourceProxyImpl() {
        return mDefaultResourceProxy == null
                ? mDefaultResourceProxy = new DefaultResourceProxyImpl(getActivity())
                : mDefaultResourceProxy;
    }

    @Override
    public void onPause() {
        super.onPause();
        destroyLocationManager();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        LOGD(TAG, "onSaveInstanceState; mMapView is" + (mMapView == null ? " " : " NOT ") + "null");
        if (mMapView != null) {
            final IGeoPoint mapCenter = mMapView.getMapCenter();
            outState.putDouble("lati", mapCenter.getLatitude());
            outState.putDouble("longi", mapCenter.getLongitude());
            outState.putInt("zoom_lvl", mMapView.getZoomLevel());
        }
        outState.putParcelable("location", mLastUserPosition);
        outState.putParcelable("target", mTarget);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (tileCache != null) tileCache.destroy();
    }

    private void initLocationManager() {
        LOGD(TAG, "initLocationManager");
        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new MyLocationListener();
        /*if (mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            LOGD(TAG, "NETWORK_PROVIDER is enabled");
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocationListener);
        }
        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            LOGD(TAG, "GPS_PROVIDER is enabled");
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
        }*/
        final Criteria crit = new Criteria();
        crit.setAccuracy(Criteria.NO_REQUIREMENT);
        final String usedProvider = mLocationManager.getBestProvider(crit, true);
        mLocationManager.requestLocationUpdates(usedProvider, 0, 1, mLocationListener);
        LOGD(TAG, "Location manager will be use " + usedProvider + " as location provider");
    }


    private void destroyLocationManager() {
        LOGD(TAG, "destroyLocationManager");
        if (mLocationManager != null) {
            mLocationManager.removeUpdates(mLocationListener);
            mLocationListener = null;
            mLocationManager = null;
        }
    }

    private void updateUserPosition(boolean moveToCenter) {
        if (isDetached()) return;

        if (mMyLocationOverlayItem != null) {
            mMapView.getOverlays().remove(mMyLocationOverlayItem);
        }
        if ( mLastUserPosition != null ) {
            final List<MyLocationOverlayItem> mMyLocationOverlayItemArray = new ArrayList<>();
            MyLocationOverlayItem object =
                    new MyLocationOverlayItem(new GeoPoint(mLastUserPosition.getLatitude(), mLastUserPosition.getLongitude()),
                            getResources(),
                            R.string.title_map_my_location,
                            R.drawable.image_my_location);
            mMyLocationOverlayItemArray.add(object);
            mMyLocationOverlayItem =
                    new ItemizedIconOverlay<>(mMyLocationOverlayItemArray,
                            onUserItemGestureListener,
                            getDefaultResourceProxyImpl());
            mMapView.getOverlays().add(mMyLocationOverlayItem);
            if (moveToCenter) mMapView.setCenter(mLastUserPosition);
            updateDistanceToTarget();
            updateFindRouteButtonState();
        }
    }

    private void updateSelectedMarker(boolean moveToCenter) {
        if ( mTarget != null ) {
            mOverviewLayout.setVisibility(View.VISIBLE);
            mMarkerImageView.setImageDrawable(mTarget.getDrawable(getResources()));
            mMarkerTextView.setText(mTarget.getTitle());
            updateDistanceToTarget();

            if (moveToCenter) mMapView.setCenter(mTarget.getLocation());
            updateFindRouteButtonState();
        } else {
            mOverviewLayout.setVisibility(View.GONE);
        }
    }

    private void updateDistanceToTarget() {
        if ( mLastUserPosition != null && mTarget != null && mTarget.getLocation() != null) {
            float distance = mLastUserPosition.distanceTo(mTarget.getLocation());
            mMarkerDescriptionTextView.setText(String.format("Distance: %.2f km", distance/1000));
        }
    }

}
