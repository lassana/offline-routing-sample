package com.github.lassana.offlineroutingsample.map.downloader;

import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import com.github.lassana.offlineroutingsample.R;
import com.github.lassana.offlineroutingsample.map.MapsConfig;
import com.github.lassana.offlineroutingsample.map.marker.CustomMarker;
import org.osmdroid.ResourceProxy;
import org.osmdroid.bonuspack.clustering.GridMarkerClusterer;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

/**
 * @author Nikolai Doronin {@literal <lassana.nd@gmail.com>}
 * @since 5/17/15.
 */
public class CyprusMap extends AbstractMap {

    @Override
    protected String getDirectoryName() {
        return "cyprus_map";
    }

    @Override
    protected long getMapSize() {
        return 0;
    }

    @Override
    protected String getMapFileUrl() {
        return null;
    }

    @Override
    protected String getEdgesUrl() {
        return null;
    }

    @Override
    protected String getGeometryUrl() {
        return null;
    }

    @Override
    protected String getLocationIndexUrl() {
        return null;
    }

    @Override
    protected String getNamesUrl() {
        return null;
    }

    @Override
    protected String getNodesUrl() {
        return null;
    }

    @Override
    protected String getPropertiesUrl() {
        return null;
    }

    @Override
    public GeoPoint getCenterGeoPoint() {
        return new GeoPoint(35.167, 33.367);
    }

    @Override
    public GridMarkerClusterer createMarkersCluster(@NonNull MapView mapView, @NonNull ResourceProxy resourceProxy, @NonNull Marker.OnMarkerClickListener listener) {
        final GridMarkerClusterer gridMarkerClusterer = new GridMarkerClusterer(mapView.getContext());
        gridMarkerClusterer.setGridSize(MapsConfig.GRID_SIZE);

        final Resources resources = mapView.getContext().getResources();
        if (resources == null) throw new RuntimeException("Cannot get resources from context");
        final BitmapDrawable clusterDrawable = ((BitmapDrawable) resources.getDrawable(R.drawable.image_map_cluster));
        final Drawable bluePinDrawable = resources.getDrawable(R.drawable.pin_a_blue);
        final Drawable greenPinDrawable = resources.getDrawable(R.drawable.pin_a_green);
        final Drawable orangePinDrawable = resources.getDrawable(R.drawable.pin_a_orange);
        final Drawable redPinDrawable = resources.getDrawable(R.drawable.pin_a_red);
        final Drawable violaPinDrawable = resources.getDrawable(R.drawable.pin_a_viola);
        if (clusterDrawable == null
                || bluePinDrawable == null
                || greenPinDrawable == null
                || orangePinDrawable == null
                || redPinDrawable == null
                || violaPinDrawable == null) {
            throw new RuntimeException("Cannot get Drawable from resource");
        }

        gridMarkerClusterer.setIcon(clusterDrawable.getBitmap());
        gridMarkerClusterer.add(new CustomMarker(mapView, "Selimiye Mosque", new GeoPoint(35.176496, 33.364478), bluePinDrawable, resourceProxy, listener));
        gridMarkerClusterer.add(new CustomMarker(mapView, "Kyrenia Gate", new GeoPoint(35.181389, 33.361667), greenPinDrawable, resourceProxy, listener));
        gridMarkerClusterer.add(new CustomMarker(mapView, "Hadjigeorgakis Kornesios Mansion", new GeoPoint(35.171938, 33.3669), orangePinDrawable, resourceProxy, listener));
        gridMarkerClusterer.add(new CustomMarker(mapView, "University of Cyprus", new GeoPoint(35.159722, 33.377778), redPinDrawable, resourceProxy, listener));
        gridMarkerClusterer.add(new CustomMarker(mapView, "Shacolas Tower", new GeoPoint(35.1719, 33.3615), violaPinDrawable, resourceProxy, listener));

        return gridMarkerClusterer;

    }
}
