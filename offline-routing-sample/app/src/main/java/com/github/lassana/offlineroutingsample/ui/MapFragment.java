package com.github.lassana.offlineroutingsample.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.github.lassana.offlineroutingsample.R;
import com.github.lassana.offlineroutingsample.downloader.BelarusMap;
import com.github.lassana.offlineroutingsample.ui.map.MapsforgeMapView;
import com.github.lassana.offlineroutingsample.util.MapsConfig;
import org.mapsforge.map.android.util.AndroidUtil;
import org.mapsforge.map.layer.cache.TileCache;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.util.GeoPoint;

import java.io.File;

/**
 * @author Nikolai Doronin {@literal <lassana.nd@gmail.com>}
 * @since 4/28/2015.
 */
public class MapFragment extends Fragment {

    private TileCache tileCache;
    private MapsforgeMapView mMapView;

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
