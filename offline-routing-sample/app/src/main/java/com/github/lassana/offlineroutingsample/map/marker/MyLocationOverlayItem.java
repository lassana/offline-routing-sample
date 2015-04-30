package com.github.lassana.offlineroutingsample.map.marker;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.OverlayItem;

/**
 * @author Nikolai Doronin {@literal <lassana.nd@gmail.com>}
 * @since 4/30/2015.
 */
public class MyLocationOverlayItem extends OverlayItem {

    public MyLocationOverlayItem(@NonNull GeoPoint aGeoPoint,
                                 @NonNull Resources resources,
                                 int titleResourceId,
                                 int drawableResourceId) {
        super(resources.getString(titleResourceId), null, aGeoPoint);
        setMarker(resources.getDrawable(drawableResourceId));
        setMarkerHotspot(HotspotPlace.BOTTOM_CENTER);
    }

}
