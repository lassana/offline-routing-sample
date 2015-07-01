package com.github.lassana.offlineroutingsample.map.downloader;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import com.github.lassana.offlineroutingsample.R;
import com.github.lassana.offlineroutingsample.map.MapsConfig;
import com.github.lassana.offlineroutingsample.map.marker.CustomMarker;
import com.github.lassana.offlineroutingsample.map.marker.CustomMarkerModel;
import org.osmdroid.ResourceProxy;
import org.osmdroid.bonuspack.clustering.GridMarkerClusterer;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * @author Nikolai Doronin {@literal <lassana.nd@gmail.com>}
 * @since 5/17/15.
 */
public abstract class AbstractMap {

    private static WeakReference<AbstractMap> mReference;

    public static AbstractMap instance() {
        if ( mReference == null || mReference.get() == null) {
            mReference = new WeakReference<AbstractMap>(new CyprusMap());
        }
        return mReference.get();
    }

    protected abstract String getDirectoryName();
    protected abstract long getMapSize();
    protected abstract String getMapFileUrl();
    protected abstract String getEdgesUrl();
    protected abstract String getGeometryUrl();
    protected abstract String getLocationIndexUrl();
    protected abstract String getNamesUrl();
    protected abstract String getNodesUrl();
    protected abstract String getPropertiesUrl();

    private File getFilesDir(@NonNull Context context) {
        File[] dirs = ContextCompat.getExternalFilesDirs(context, null);
        File dir = null;
        for (File directory : dirs) {
            if (directory != null) {
                dir = directory;
                break;
            }
        }
        if (dir != null && !dir.exists()) dir.mkdirs();
        return dir;
    }

    public boolean exist(final Context context) {
        return getMapsforgeFile(context).exists()
                && getRoutingEdgesFile(context).exists()
                && getRoutingGeometryFile(context).exists()
                && getRoutingLocationIndexFile(context).exists()
                && getRoutingNamesFile(context).exists()
                && getRoutingNodesFile(context).exists()
                && getRoutingPropertiesFile(context).exists();
    }

    public File getOfflineMapsDir(@NonNull Context context) {
        final File mapsRootDir = new File(getFilesDir(context), getDirectoryName());
        if (!mapsRootDir.exists()) mapsRootDir.mkdirs();
        return mapsRootDir;
    }

    public File getMapsforgeFile(@NonNull Context context) {
        return new File(getOfflineMapsDir(context), "map");
    }

    public File getRoutingEdgesFile(@NonNull Context context) {
        return new File(getOfflineMapsDir(context), "edges");
    }

    public File getRoutingGeometryFile(@NonNull Context context) {
        return new File(getOfflineMapsDir(context), "geometry");
    }

    public File getRoutingLocationIndexFile(@NonNull Context context) {
        return new File(getOfflineMapsDir(context), "locationIndex");
    }

    public File getRoutingNamesFile(@NonNull Context context) {
        return new File(getOfflineMapsDir(context), "names");
    }

    public File getRoutingNodesFile(@NonNull Context context) {
        return new File(getOfflineMapsDir(context), "nodes");
    }

    public File getRoutingPropertiesFile(@NonNull Context context) {
        return new File(getOfflineMapsDir(context), "properties");
    }

    public abstract GeoPoint getCenterGeoPoint();

    public GridMarkerClusterer createMarkersCluster(@NonNull MapView mapView,
                                                      @NonNull ResourceProxy resourceProxy,
                                                      @NonNull Marker.OnMarkerClickListener listener) {
        final GridMarkerClusterer gridMarkerClusterer = new GridMarkerClusterer(mapView.getContext());
        gridMarkerClusterer.setGridSize(MapsConfig.GRID_SIZE);

        final Resources resources = mapView.getContext().getResources();
        if (resources == null) throw new RuntimeException("Cannot get resources from context");
        final BitmapDrawable clusterDrawable = ((BitmapDrawable) resources.getDrawable(R.drawable.image_map_cluster));
        if (clusterDrawable == null) {
            throw new RuntimeException("Cannot get Drawable from resource");
        }
        gridMarkerClusterer.setIcon(clusterDrawable.getBitmap());

        for (CustomMarkerModel model : getCustomMarkerModels()) {
            gridMarkerClusterer.add(new CustomMarker(model, mapView, resources, resourceProxy, listener));
        }

        return gridMarkerClusterer;
    }

    protected abstract CustomMarkerModel[] getCustomMarkerModels();

    public abstract int getDefaultZoom();
}
