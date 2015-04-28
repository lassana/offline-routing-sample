package com.github.lassana.offlineroutingsample.downloader;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import org.osmdroid.util.GeoPoint;

import java.io.File;

/**
 * @author Nikolai Doronin {@literal <lassana.nd@gmail.com>}
 * @since 4/28/2015.
 */
public abstract class BelarusMap {

    public static final GeoPoint GEOPOINT_CENTER = new GeoPoint(52.0801268, 23.7033696);;

    private BelarusMap() {
    }

    private static final String DIR_OFFLINE_MAPS_DATA = "offline_maps";

    public static final long MAP_SIZE = 132837770;
    public static final String MAP_FILE_URL = "https://github.com/lassana/offline-routing-sample/blob/map/raw/belarus/belarus.map?raw=true";
    public static final String EDGES_URL = "https://github.com/lassana/offline-routing-sample/blob/map/raw/belarus/edges?raw=true";
    public static final String GEOMETRY_URL = "https://github.com/lassana/offline-routing-sample/blob/map/raw/belarus/geometry?raw=true";
    public static final String LOCATION_INDEX_URL = "https://github.com/lassana/offline-routing-sample/blob/map/raw/belarus/locationIndex?raw=true";
    public static final String NAMES_URL = "https://github.com/lassana/offline-routing-sample/blob/map/raw/belarus/names?raw=true";
    public static final String NODES_URL = "https://github.com/lassana/offline-routing-sample/blob/map/raw/belarus/nodes?raw=true";
    public static final String PROPERTIES_URL = "https://github.com/lassana/offline-routing-sample/blob/map/raw/belarus/properties?raw=true";


    private static File getFilesDir(final Context context) {
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

    public static File getOfflineMapsDir(final Context context) {
        final File mapsRootDir = new File(getFilesDir(context), DIR_OFFLINE_MAPS_DATA);
        if (!mapsRootDir.exists()) mapsRootDir.mkdirs();
        return mapsRootDir;
    }

    public static boolean exist(final Context context) {
        return getMapsforgeFile(context).exists()
                && getRoutingEdgesFile(context).exists()
                && getRoutingGeometryFile(context).exists()
                && getRoutingLocationIndexFile(context).exists()
                && getRoutingNamesFile(context).exists()
                && getRoutingNodesFile(context).exists()
                && getRoutingPropertiesFile(context).exists();
    }

    public static File getMapsforgeFile(final Context context) {
        return new File(getOfflineMapsDir(context), "map");
    }

    public static File getRoutingEdgesFile(final Context context) {
        return new File(getOfflineMapsDir(context), "edges");
    }

    public static File getRoutingGeometryFile(final Context context) {
        return new File(getOfflineMapsDir(context), "geometry");
    }

    public static File getRoutingLocationIndexFile(final Context context) {
        return new File(getOfflineMapsDir(context), "locationIndex");
    }

    public static File getRoutingNamesFile(final Context context) {
        return new File(getOfflineMapsDir(context), "names");
    }

    public static File getRoutingNodesFile(final Context context) {
        return new File(getOfflineMapsDir(context), "nodes");
    }

    public static File getRoutingPropertiesFile(final Context context) {
        return new File(getOfflineMapsDir(context), "properties");
    }


}
