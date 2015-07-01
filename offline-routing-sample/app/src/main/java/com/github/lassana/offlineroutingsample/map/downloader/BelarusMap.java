package com.github.lassana.offlineroutingsample.map.downloader;

import com.github.lassana.offlineroutingsample.R;
import com.github.lassana.offlineroutingsample.map.marker.CustomMarkerModel;
import org.osmdroid.util.GeoPoint;

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
    protected CustomMarkerModel[] getCustomMarkerModels() {
        return new CustomMarkerModel[]{
                new CustomMarkerModel("Брест-Центральный", R.drawable.pin_a_blue, 52.100472, 23.68056),
                new CustomMarkerModel("Гродно", R.drawable.pin_a_green, 53.686791, 23.848546),
                new CustomMarkerModel("Витебск", R.drawable.pin_a_orange, 55.19611, 30.18500),
                new CustomMarkerModel("Минский железнодорожный вокзал", R.drawable.pin_a_red, 53.890667, 27.55111),
                new CustomMarkerModel("Могилёв 1-на-Днепре", R.drawable.pin_a_viola, 53.92611, 30.33833),
                new CustomMarkerModel("Гомель-Пассажирский", R.drawable.pin_a_blue, 52.43083, 30.99111),
                new CustomMarkerModel("Автовокзал г. Бреста", R.drawable.pin_a_green, 52.098363, 23.691269),
                new CustomMarkerModel("Автовокзал Гродно", R.drawable.pin_a_orange, 53.677871, 23.843805),
                new CustomMarkerModel("Автовокзал \"Витебск\"", R.drawable.pin_a_red, 55.196350, 30.187946),
                new CustomMarkerModel("Центральный автовокзал", R.drawable.pin_a_viola, 53.890068, 27.554977),
                new CustomMarkerModel("Восточный автовокзал", R.drawable.pin_a_blue, 53.87722, 27.59889),
                new CustomMarkerModel("Московский автовокзал", R.drawable.pin_a_green ,53.928603, 27.636870),
                new CustomMarkerModel("Автовокзал Могилев", R.drawable.pin_a_orange, 53.913231, 30.347587),
                new CustomMarkerModel("Гомельский объединённый автовокзал", R.drawable.pin_a_red, 52.434200, 30.993193),
        };
    }

    @Override
    public int getDefaultZoom() {
        return 6;
    }
}
