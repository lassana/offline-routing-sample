package com.github.lassana.offlineroutingsample.map.marker;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
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
    public final Drawable icon;

    public CustomMarker(@NonNull MapView mapView,
                        @NonNull String title,
                        @NonNull GeoPoint aGeoPoint,
                        @NonNull Drawable icon,
                        @NonNull ResourceProxy resourceProxy,
                        @NonNull OnMarkerClickListener listener) {
        super(mapView, resourceProxy);
        this.title = title;
        this.icon = icon;
        setIcon(icon);
        setPosition(aGeoPoint);
        setOnMarkerClickListener(listener);
        setAnchor(ANCHOR_CENTER, ANCHOR_BOTTOM);
    }
}
