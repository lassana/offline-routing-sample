package com.github.lassana.offlineroutingsample.map.downloader;

import com.github.lassana.offlineroutingsample.R;
import com.github.lassana.offlineroutingsample.map.marker.CustomMarkerModel;
import org.osmdroid.util.GeoPoint;

import java.lang.ref.WeakReference;

/**
 * @author Nikolai Doronin {@literal <lassana.nd@gmail.com>}
 * @since 5/17/15.
 */
public class CyprusMap extends AbstractMap {

    protected CyprusMap() {
    }

    @Override
    protected String getDirectoryName() {
        return "cyprus_map";
    }

    @Override
    protected long getMapSize() {
        return 22937815;
    }

    @Override
    protected String getMapFileUrl() {
        return "https://github.com/lassana/offline-routing-sample/blob/map/raw/cyprus/cyprus.map?raw=true";
    }

    @Override
    protected String getEdgesUrl() {
        return "https://github.com/lassana/offline-routing-sample/blob/map/raw/cyprus/edges?raw=true";
    }

    @Override
    protected String getGeometryUrl() {
        return "https://github.com/lassana/offline-routing-sample/blob/map/raw/cyprus/geometry?raw=true";
    }

    @Override
    protected String getLocationIndexUrl() {
        return "https://github.com/lassana/offline-routing-sample/blob/map/raw/cyprus/locationIndex?raw=true";
    }

    @Override
    protected String getNamesUrl() {
        return "https://github.com/lassana/offline-routing-sample/blob/map/raw/cyprus/names?raw=true";
    }

    @Override
    protected String getNodesUrl() {
        return "https://github.com/lassana/offline-routing-sample/blob/map/raw/cyprus/nodes?raw=true";
    }

    @Override
    protected String getPropertiesUrl() {
        return "https://github.com/lassana/offline-routing-sample/blob/map/raw/cyprus/properties?raw=true";
    }


    @Override
    public GeoPoint getCenterGeoPoint() {
        return new GeoPoint(35.167, 33.367);
    }


    @Override
    protected CustomMarkerModel[] getCustomMarkerModels() {
        return new CustomMarkerModel[]{
                new CustomMarkerModel("Selimiye Mosque", R.drawable.pin_a_blue, 35.176496, 33.364478),
                new CustomMarkerModel("Kyrenia Gate", R.drawable.pin_a_green, 35.181389, 33.361667),
                new CustomMarkerModel("Hadjigeorgakis Kornesios Mansion", R.drawable.pin_a_orange, 35.171938, 33.3669),
                new CustomMarkerModel("University of Cyprus", R.drawable.pin_a_red, 35.159722, 33.377778),
                new CustomMarkerModel("Shacolas Tower", R.drawable.pin_a_viola, 35.1719, 33.3615)
        };
    }

    @Override
    public int getDefaultZoom() {
        return 10;
    }
}
