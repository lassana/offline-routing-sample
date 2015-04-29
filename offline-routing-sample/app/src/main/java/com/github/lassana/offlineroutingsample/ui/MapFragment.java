package com.github.lassana.offlineroutingsample.ui;

import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.github.lassana.offlineroutingsample.R;
import com.github.lassana.offlineroutingsample.map.downloader.BelarusMap;
import com.github.lassana.offlineroutingsample.map.view.CustomMarker;
import com.github.lassana.offlineroutingsample.map.view.MapsforgeMapView;
import com.github.lassana.offlineroutingsample.util.MapsConfig;
import org.mapsforge.map.android.util.AndroidUtil;
import org.mapsforge.map.layer.cache.TileCache;
import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.bonuspack.clustering.GridMarkerClusterer;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.io.File;

/**
 * @author Nikolai Doronin {@literal <lassana.nd@gmail.com>}
 * @since 4/28/2015.
 */
public class MapFragment extends Fragment {

    private TileCache tileCache;
    private MapsforgeMapView mMapView;
    private DefaultResourceProxyImpl mDefaultResourceProxy;
    private Marker.OnMarkerClickListener mOnMarkerClickListener = new Marker.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker, MapView mapView) {
            if (marker instanceof CustomMarker) {
                CustomMarker theMarker = (CustomMarker) marker;
                mMapView.setCenter(theMarker.getPosition());
                return true;
            } else {
                return false;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rvalue = inflater.inflate(R.layout.fragment_map, container, false);

        tileCache = AndroidUtil.createTileCache(getActivity(), "mapcache", MapsConfig.TILE_SIZE, MapsConfig.SCREEN_RATION, MapsConfig.OVERDRAW);
        final File mapFile = BelarusMap.getMapsforgeFile(getActivity());
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

        View viewZoomIn = view.findViewById(R.id.img_btn_zoom_in);
        View viewZoomOut = view.findViewById(R.id.img_btn_zoom_out);
        viewZoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mMapView.getController().zoomIn();
            }
        });
        viewZoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mMapView.getController().zoomOut();
            }
        });

        final GeoPoint initialCenter;
        if (savedInstanceState != null) {
            mMapView.getController().setZoom(savedInstanceState.getInt("zoom_lvl"));
            initialCenter = new GeoPoint(savedInstanceState.getDouble("lati"), savedInstanceState.getDouble("longi"));
        } else {
            mMapView.getController().setZoom(6);
            initialCenter = BelarusMap.GEOPOINT_CENTER;
        }
        mMapView.getController().setCenter(initialCenter);
        mMapView.setCenter(initialCenter);

        final GridMarkerClusterer gridMarkerClusterer = new GridMarkerClusterer(getActivity());
        gridMarkerClusterer.setGridSize(MapsConfig.GRID_SIZE);
        final Resources resources = getResources();
        gridMarkerClusterer.setIcon(((BitmapDrawable) resources.getDrawable(R.drawable.image_map_cluster)).getBitmap());
        gridMarkerClusterer.add(new CustomMarker(mMapView, "Брест-Центральный", new GeoPoint(52.100472, 23.68056), resources.getDrawable(R.drawable.pin_a_blue), getDefaultResourceProxyImpl(), mOnMarkerClickListener));
        gridMarkerClusterer.add(new CustomMarker(mMapView, "Гродно", new GeoPoint(53.686791, 23.848546), resources.getDrawable(R.drawable.pin_a_green), getDefaultResourceProxyImpl(), mOnMarkerClickListener));
        gridMarkerClusterer.add(new CustomMarker(mMapView, "Витебск", new GeoPoint(55.19611,30.18500), resources.getDrawable(R.drawable.pin_a_orange), getDefaultResourceProxyImpl(), mOnMarkerClickListener));
        gridMarkerClusterer.add(new CustomMarker(mMapView, "Минский железнодорожный вокзал", new GeoPoint(53.890667,27.55111), resources.getDrawable(R.drawable.pin_a_red), getDefaultResourceProxyImpl(), mOnMarkerClickListener));
        gridMarkerClusterer.add(new CustomMarker(mMapView, "Могилёв 1-на-Днепре", new GeoPoint(53.92611,30.33833), resources.getDrawable(R.drawable.pin_a_viola), getDefaultResourceProxyImpl(), mOnMarkerClickListener));
        gridMarkerClusterer.add(new CustomMarker(mMapView, "Гомель-Пассажирский", new GeoPoint(52.43083,30.99111), resources.getDrawable(R.drawable.pin_a_blue), getDefaultResourceProxyImpl(), mOnMarkerClickListener));
        gridMarkerClusterer.add(new CustomMarker(mMapView, "Автовокзал г. Бреста", new GeoPoint(52.098363, 23.691269), resources.getDrawable(R.drawable.pin_a_green), getDefaultResourceProxyImpl(), mOnMarkerClickListener));
        gridMarkerClusterer.add(new CustomMarker(mMapView, "Автовокзал Гродно", new GeoPoint(53.677871, 23.843805), resources.getDrawable(R.drawable.pin_a_orange), getDefaultResourceProxyImpl(), mOnMarkerClickListener));
        gridMarkerClusterer.add(new CustomMarker(mMapView, "Автовокзал \"Витебск\"", new GeoPoint(55.196350, 30.187946), resources.getDrawable(R.drawable.pin_a_red), getDefaultResourceProxyImpl(), mOnMarkerClickListener));
        gridMarkerClusterer.add(new CustomMarker(mMapView, "Центральный автовокзал", new GeoPoint(53.890068, 27.554977), resources.getDrawable(R.drawable.pin_a_viola), getDefaultResourceProxyImpl(), mOnMarkerClickListener));
        gridMarkerClusterer.add(new CustomMarker(mMapView, "Восточный автовокзал", new GeoPoint(53.87722,27.59889), resources.getDrawable(R.drawable.pin_a_blue), getDefaultResourceProxyImpl(), mOnMarkerClickListener));
        gridMarkerClusterer.add(new CustomMarker(mMapView, "Московский автовокзал", new GeoPoint(53.928603,27.636870), resources.getDrawable(R.drawable.pin_a_green), getDefaultResourceProxyImpl(), mOnMarkerClickListener));
        gridMarkerClusterer.add(new CustomMarker(mMapView, "Автовокзал Могилев", new GeoPoint(53.913231, 30.347587), resources.getDrawable(R.drawable.pin_a_orange), getDefaultResourceProxyImpl(), mOnMarkerClickListener));
        gridMarkerClusterer.add(new CustomMarker(mMapView, "Гомельский объединённый автовокзал", new GeoPoint(52.434200,30.993193), resources.getDrawable(R.drawable.pin_a_red), getDefaultResourceProxyImpl(), mOnMarkerClickListener));

        mMapView.getOverlays().add(gridMarkerClusterer);
        mMapView.invalidate();
    }

    private DefaultResourceProxyImpl getDefaultResourceProxyImpl() {
        return mDefaultResourceProxy == null
                ? mDefaultResourceProxy = new DefaultResourceProxyImpl(getActivity())
                : mDefaultResourceProxy;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mMapView != null) {
            final IGeoPoint mapCenter = mMapView.getMapCenter();
            outState.putDouble("lati", mapCenter.getLatitude());
            outState.putDouble("longi", mapCenter.getLongitude());
            outState.putInt("zoom_lvl", mMapView.getZoomLevel());
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (tileCache != null) tileCache.destroy();
    }

}
