package com.github.lassana.offlineroutingsample.map.marker;

import android.content.res.Resources;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.OverlayItem;

/**
 * @author Nikolai Doronin {@literal <lassana.nd@gmail.com>}
 * @since 4/30/2015.
 */
public class MyLocationOverlayItem extends OverlayItem {

    public MyLocationOverlayItem(@NonNull GeoPoint aGeoPoint,
                                 @NonNull Resources resources,
                                 @StringRes int titleResourceId,
                                 @DrawableRes int drawableResourceId) {
        super(resources.getString(titleResourceId), null, aGeoPoint);
        setMarker(resources.getDrawable(drawableResourceId));
        setMarkerHotspot(HotspotPlace.BOTTOM_CENTER);
    }

}
