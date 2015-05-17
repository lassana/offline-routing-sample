package com.github.lassana.offlineroutingsample.map.downloader;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
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
 * @since 4/28/2015.
 */
public class BelarusMap extends AbstractMap {

    @Override
    protected String getDirectoryName() {
        return "belarus_map";
    }

    @Override
    protected long getMapSize() {
        return 132837770;
    }

    @Override
    protected String getMapFileUrl() {
        return "https://github.com/lassana/offline-routing-sample/blob/map/raw/belarus/belarus.map?raw=true";
    }

    @Override
    protected String getEdgesUrl() {
        return "https://github.com/lassana/offline-routing-sample/blob/map/raw/belarus/edges?raw=true";
    }

    @Override
    protected String getGeometryUrl() {
        return "https://github.com/lassana/offline-routing-sample/blob/map/raw/belarus/geometry?raw=true";
    }

    @Override
    protected String getLocationIndexUrl() {
        return "https://github.com/lassana/offline-routing-sample/blob/map/raw/belarus/locationIndex?raw=true";
    }

    @Override
    protected String getNamesUrl() {
        return "https://github.com/lassana/offline-routing-sample/blob/map/raw/belarus/names?raw=true";
    }

    @Override
    protected String getNodesUrl() {
        return "https://github.com/lassana/offline-routing-sample/blob/map/raw/belarus/nodes?raw=true";
    }

    @Override
    protected String getPropertiesUrl() {
        return "https://github.com/lassana/offline-routing-sample/blob/map/raw/belarus/properties?raw=true";
    }

    @Override
    public GeoPoint getCenterGeoPoint() {
        return new GeoPoint(52.0801268, 23.7033696);
    }

    @Override
    public GridMarkerClusterer createMarkersCluster(@NonNull MapView mapView,
                                                    @NonNull ResourceProxy resourceProxy,
                                                    @NonNull Marker.OnMarkerClickListener listener) {
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
        gridMarkerClusterer.add(new CustomMarker(mapView, "Брест-Центральный", new GeoPoint(52.100472, 23.68056), bluePinDrawable, resourceProxy, listener));
        gridMarkerClusterer.add(new CustomMarker(mapView, "Гродно", new GeoPoint(53.686791, 23.848546), greenPinDrawable, resourceProxy, listener));
        gridMarkerClusterer.add(new CustomMarker(mapView, "Витебск", new GeoPoint(55.19611, 30.18500), orangePinDrawable, resourceProxy, listener));
        gridMarkerClusterer.add(new CustomMarker(mapView, "Минский железнодорожный вокзал", new GeoPoint(53.890667, 27.55111), redPinDrawable, resourceProxy, listener));
        gridMarkerClusterer.add(new CustomMarker(mapView, "Могилёв 1-на-Днепре", new GeoPoint(53.92611, 30.33833), violaPinDrawable, resourceProxy, listener));
        gridMarkerClusterer.add(new CustomMarker(mapView, "Гомель-Пассажирский", new GeoPoint(52.43083, 30.99111), bluePinDrawable, resourceProxy, listener));
        gridMarkerClusterer.add(new CustomMarker(mapView, "Автовокзал г. Бреста", new GeoPoint(52.098363, 23.691269), greenPinDrawable, resourceProxy, listener));
        gridMarkerClusterer.add(new CustomMarker(mapView, "Автовокзал Гродно", new GeoPoint(53.677871, 23.843805), orangePinDrawable, resourceProxy, listener));
        gridMarkerClusterer.add(new CustomMarker(mapView, "Автовокзал \"Витебск\"", new GeoPoint(55.196350, 30.187946), redPinDrawable, resourceProxy, listener));
        gridMarkerClusterer.add(new CustomMarker(mapView, "Центральный автовокзал", new GeoPoint(53.890068, 27.554977), violaPinDrawable, resourceProxy, listener));
        gridMarkerClusterer.add(new CustomMarker(mapView, "Восточный автовокзал", new GeoPoint(53.87722, 27.59889), bluePinDrawable, resourceProxy, listener));
        gridMarkerClusterer.add(new CustomMarker(mapView, "Московский автовокзал", new GeoPoint(53.928603, 27.636870), greenPinDrawable, resourceProxy, listener));
        gridMarkerClusterer.add(new CustomMarker(mapView, "Автовокзал Могилев", new GeoPoint(53.913231, 30.347587), orangePinDrawable, resourceProxy, listener));
        gridMarkerClusterer.add(new CustomMarker(mapView, "Гомельский объединённый автовокзал", new GeoPoint(52.434200, 30.993193), redPinDrawable, resourceProxy, listener));

        return gridMarkerClusterer;
    }
}
