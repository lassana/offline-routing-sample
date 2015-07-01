package com.github.lassana.offlineroutingsample.map.marker;

import android.content.res.Resources;
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

    public final CustomMarkerModel model;

    public CustomMarker(@NonNull CustomMarkerModel model,
                        @NonNull MapView mapView,
                        @NonNull Resources resources,
                        @NonNull ResourceProxy resourceProxy,
                        @NonNull OnMarkerClickListener listener) {
        super(mapView, resourceProxy);
        this.model = model;
        setIcon(resources.getDrawable(model.getDrawableRes()));
        setPosition(new GeoPoint(model.getLocation()));
        setOnMarkerClickListener(listener);
        setAnchor(ANCHOR_CENTER, ANCHOR_BOTTOM);
    }

}
