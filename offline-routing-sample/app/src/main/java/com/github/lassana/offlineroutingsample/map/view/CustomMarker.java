package com.github.lassana.offlineroutingsample.map.view;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import org.osmdroid.ResourceProxy;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

/**
 * @author Nikolai Doronin {@literal <lassana.nd@gmail.com>}
 * @since 4/29/2015.
 */
public class CustomMarker extends Marker {

    public final String title;

    public CustomMarker(MapView mapView,
                        String title,
                        GeoPoint aGeoPoint,
                        Drawable icon,
                        ResourceProxy resourceProxy,
                        OnMarkerClickListener listener) {
        super(mapView, resourceProxy);
        this.title = title;
        setIcon(icon);
        setPosition(aGeoPoint);
        setOnMarkerClickListener(listener);
        setAnchor(ANCHOR_CENTER, ANCHOR_BOTTOM);
    }
}
